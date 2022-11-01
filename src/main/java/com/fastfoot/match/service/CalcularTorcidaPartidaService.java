package com.fastfoot.match.service;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceiraEntrada;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraEntrada;
import com.fastfoot.match.model.PartidaTorcidaPorcentagem;
import com.fastfoot.match.model.dto.PartidaTorcidaSalvarDTO;
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.ParametroService;

@Service
public class CalcularTorcidaPartidaService {
	
	private static final Double PORCENTAGEM_MANDANTE = 0.65;
	
	private static final Double PORCENTAGEM_VISITANTE = 0.35;
	
	protected static final Random R = new Random();
	
	private static final Double PORC_STDEV = 0.05;
	
	@Autowired
	private ParametroService parametroService;

	public void calcularTorcidaPartida(RodadaJogavel rodada, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {

		if (rodada.getNivelCampeonato().isCIOuCIIOuCIII()
				|| rodada.getNivelCampeonato().isCNIOuCNII()) {
			for (PartidaResultadoJogavel p : rodada.getPartidas()) {
				calcularTorcidaPartida(p, rodada.getSemana(), partidaTorcidaSalvarDTO);
			}
		}

		if (rodada.getNivelCampeonato().isNIOuNII()) {
			
			Map<Clube, Classificacao> clubeClassificacao = ((Campeonato) rodada.getCampeonatoJogavel())
					.getClassificacao().stream().collect(Collectors.toMap(Classificacao::getClube, Function.identity()));
			
			
			for (PartidaResultadoJogavel p : rodada.getPartidas()) {
				calcularTorcidaPartidaNacional(p, clubeClassificacao, rodada.getSemana(), partidaTorcidaSalvarDTO);
			}
		}
	}
	
	public void calcularTorcidaPartidaNacional(PartidaResultadoJogavel partida, Map<Clube, Classificacao> clubeClassificacao, Semana semana, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		double renda = 0.0d;

		int tamanhoEstadio = partida.getClubeMandante().getClubeNivel().getTamanhoTorcida();

		Double porcPublicoAlvoMandante = getPorcentagemPublicoAlvoNacional(partida.getNivelCampeonato(),
				clubeClassificacao, partida.getClubeMandante());
		Double porcPublicoAlvoVisitante = getPorcentagemPublicoAlvoNacional(partida.getNivelCampeonato(),
				clubeClassificacao, partida.getClubeVisitante());

		//if (porcPublicoAlvo == null) return;

		Integer publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_MANDANTE,
				porcPublicoAlvoMandante).intValue();

		Integer publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_VISITANTE,
				porcPublicoAlvoVisitante).intValue();

		/*System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,* /
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));*/
		
		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoMandante,
				partida.getRodada().getNumero(), null);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceiraEntrada(criarMovimentacaoFinanceira(partida.getClubeMandante(),
				semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)", partida.getNivelCampeonato().name(),
						partida.getRodada().getNumero(), tamanhoEstadio)));

		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoVisitante,
				partida.getRodada().getNumero(), null);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceiraEntrada(criarMovimentacaoFinanceira(
				partida.getClubeVisitante(), semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)",
						partida.getNivelCampeonato().name(), partida.getRodada().getNumero(), tamanhoEstadio)));
		
		PartidaTorcida partidaTorcida = new PartidaTorcida();
		partidaTorcida.setPublico(publicoMandante + publicoVisitante);
		
		if (partida instanceof PartidaResultado) {
			partidaTorcida.setPartidaResultado((PartidaResultado) partida);
		} else {
			throw new RuntimeException("Erro inesperado");//TODO: amistosos
		}
		
		partidaTorcidaSalvarDTO.addPartidaTorcida(partidaTorcida);
	}
	
	private Double getPorcentagemPublicoAlvoNacional(NivelCampeonato nivelCampeonato, Map<Clube, Classificacao> clubeClassificacao, Clube clube) {
		return PartidaTorcidaPorcentagem.getPorcentagem(nivelCampeonato, clubeClassificacao.get(clube).getPosicao());
	}

	public void calcularTorcidaPartida(PartidaResultadoJogavel partida, Semana semana, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		double renda = 0.0d;
		
		Integer numRodadasCN = parametroService.getNumeroRodadasCopaNacional();

		int tamanhoEstadio = partida.getClubeMandante().getClubeNivel().getTamanhoTorcida();

		Double porcPublicoAlvo = getPorcentagemPublicoAlvo(partida);

		//if (porcPublicoAlvo == null) return;

		Integer publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_MANDANTE, porcPublicoAlvo).intValue();

		Integer publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getClubeNivel().getTamanhoTorcida(), PORCENTAGEM_VISITANTE,
				porcPublicoAlvo).intValue();

		/*System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,* /
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));*/
		
		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoMandante,
				partida.getRodada().getNumero(), numRodadasCN);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceiraEntrada(criarMovimentacaoFinanceira(partida.getClubeMandante(),
				semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)", partida.getNivelCampeonato().name(),
						partida.getRodada().getNumero(), tamanhoEstadio)));

		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoVisitante,
				partida.getRodada().getNumero(), numRodadasCN);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceiraEntrada(criarMovimentacaoFinanceira(
				partida.getClubeVisitante(), semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)",
						partida.getNivelCampeonato().name(), partida.getRodada().getNumero(), tamanhoEstadio)));
		
		PartidaTorcida partidaTorcida = new PartidaTorcida();
		partidaTorcida.setPublico(publicoMandante + publicoVisitante);
		
		if (partida instanceof PartidaEliminatoriaResultado) {
			partidaTorcida.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partida);
		} else if (partida instanceof PartidaResultado) {
			partidaTorcida.setPartidaResultado((PartidaResultado) partida);
		} else {
			throw new RuntimeException("Erro inesperado");//TODO: amistosos
		}
		
		partidaTorcidaSalvarDTO.addPartidaTorcida(partidaTorcida);
	}
	
	private MovimentacaoFinanceiraEntrada criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceiraEntrada entrada = new MovimentacaoFinanceiraEntrada();
		
		entrada.setClube(clube);
		entrada.setSemana(semana);
		entrada.setTipoMovimentacao(TipoMovimentacaoFinanceiraEntrada.INGRESSOS);
		entrada.setValorMovimentacao(valorMovimentacao);
		entrada.setDescricao(descricao);

		return entrada;
	}
	
	private Double getPorcentagemPublicoAlvo(PartidaResultadoJogavel partida) {

		if (partida.getNivelCampeonato().isCopaNacional()) {
			
			Integer numRodadasCN = parametroService.getNumeroRodadasCopaNacional();

			if (partida.getRodada().getNumero() == numRodadasCN) {// Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 1)) {// Semi Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 2)) {// Quartas Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 3)) {// Oitavas Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_OITAVAS_FINAL);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 4)) {// Preliminar II
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_PRELIMINAR);
			} else if (partida.getRodada().getNumero() == (numRodadasCN - 5)) {// Preliminar I
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_PRELIMINAR);
			}
		}

		if (partida.getNivelCampeonato().isCopaNacionalII()) {

			if (partida.getRodada().getNumero() == 4) {// Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			} else if (partida.getRodada().getNumero() == 3) {// Semi Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == 2) {// Quartas Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == 1) {// Oitavas Final
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_OITAVAS_FINAL);
			}
		}

		if (partida.getNivelCampeonato().isCIOuCIIOuCIII()) {

			if (partida.getRodada().getNumero() == 1) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 2) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 3) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FASE_GRUPOS);
			} else if (partida.getRodada().getNumero() == 4) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_QUARTAS_FINAL);
			} else if (partida.getRodada().getNumero() == 5) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_SEMI_FINAL);
			} else if (partida.getRodada().getNumero() == 6) {
				return PartidaTorcidaPorcentagem.getPorcentagem(partida.getNivelCampeonato(),
						PartidaTorcidaPorcentagem.PORC_PUBLICO_FINAL);
			}

		}

		return null;
	}

	private Long sortearTorcida(Integer tamanhoEstadio, Integer tamanhoTorcidaClube, Double ajuste,
			Double porcPublicoAlvo) {

		double porcPublicoAlvoFinal = porcPublicoAlvo + (R.nextGaussian() * PORC_STDEV);
		
		if (porcPublicoAlvoFinal > 1.0) porcPublicoAlvoFinal = 1.0;

		double torcidaMax = tamanhoEstadio * ajuste * porcPublicoAlvoFinal;
		double torcidaAlvo = tamanhoTorcidaClube * porcPublicoAlvoFinal;

		return Math.round(Math.min(torcidaMax, torcidaAlvo));

	}
}
