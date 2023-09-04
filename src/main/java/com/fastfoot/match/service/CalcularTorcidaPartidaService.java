package com.fastfoot.match.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.match.model.PartidaTorcidaPorcentagem;
import com.fastfoot.match.model.dto.PartidaTorcidaSalvarDTO;
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.CarregarParametroService;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class CalcularTorcidaPartidaService {
	
	private static final Double PORCENTAGEM_MANDANTE = 0.65;
	
	private static final Double PORCENTAGEM_VISITANTE = 0.35;
	
	private static final Double PORCENTAGEM_MANDANTE_AMISTOSO = 0.50;
	
	private static final Double PORCENTAGEM_VISITANTE_AMISTOSO = 0.50;
	
	protected static final Random R = new Random();
	
	private static final Double PORC_STDEV = 0.05;
	
	@Autowired
	private CarregarParametroService carregarParametroService;

	public void calcularTorcidaPartida(RodadaJogavel rodada, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		if (rodada.isAmistoso()) {
			for (PartidaResultadoJogavel p : rodada.getPartidas()) {
				calcularTorcidaPartidaAmistosa(p, rodada.getSemana(), partidaTorcidaSalvarDTO);
			}
		} else {

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

	}
	
	protected void calcularTorcidaPartidaNacional(PartidaResultadoJogavel partida,
			Map<Clube, Classificacao> clubeClassificacao, Semana semana,
			PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		double renda = 0.0d;

		int tamanhoEstadio = partida.getClubeMandante().getNivelNacional().getTamanhoTorcida();

		Double porcPublicoAlvoMandante = getPorcentagemPublicoAlvoNacional(partida.getNivelCampeonato(),
				clubeClassificacao, partida.getClubeMandante());
		Double porcPublicoAlvoVisitante = getPorcentagemPublicoAlvoNacional(partida.getNivelCampeonato(),
				clubeClassificacao, partida.getClubeVisitante());

		//if (porcPublicoAlvo == null) return;

		Integer publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_MANDANTE,
				porcPublicoAlvoMandante, true).intValue();

		Integer publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_VISITANTE,
				porcPublicoAlvoVisitante, true).intValue();

		/*System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,* /
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));*/
		
		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoMandante,
				partida.getRodada().getNumero(), null);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceira(partida.getClubeMandante(),
				semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)", partida.getNivelCampeonato().name(),
						partida.getRodada().getNumero(), tamanhoEstadio)));

		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoVisitante,
				partida.getRodada().getNumero(), null);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceira(
				partida.getClubeVisitante(), semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)",
						partida.getNivelCampeonato().name(), partida.getRodada().getNumero(), tamanhoEstadio)));
		
		PartidaTorcida partidaTorcida = new PartidaTorcida();
		partidaTorcida.setPublico(publicoMandante + publicoVisitante);
		
		if (partida instanceof PartidaResultado) {
			partidaTorcida.setPartidaResultado((PartidaResultado) partida);
		} else {
			throw new RuntimeException("Erro inesperado");
		}
		
		partidaTorcidaSalvarDTO.addPartidaTorcida(partidaTorcida);
	}
	
	private Double getPorcentagemPublicoAlvoNacional(NivelCampeonato nivelCampeonato, Map<Clube, Classificacao> clubeClassificacao, Clube clube) {
		return PartidaTorcidaPorcentagem.getPorcentagem(nivelCampeonato, clubeClassificacao.get(clube).getPosicao());
	}
	
	protected void calcularTorcidaPartidaAmistosa(PartidaResultadoJogavel partida, Semana semana, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		double renda = 0.0d;

		int tamanhoEstadio = partida.getClubeMandante().getNivelNacional().getTamanhoTorcida();

		Double porcPublicoAlvo = PartidaTorcidaPorcentagem.getPorcentagemAmistoso();

		//if (porcPublicoAlvo == null) return;

		Integer publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_MANDANTE_AMISTOSO, porcPublicoAlvo, true).intValue();

		Integer publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_VISITANTE_AMISTOSO,
				porcPublicoAlvo, true).intValue();

		/*System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,* /
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));*/
		
		renda = PartidaTorcidaPorcentagem.getRendaIngressosAmistosos(publicoMandante);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceiraAmistosos(partida.getClubeMandante(),
				semana, renda,
				String.format("Ingressos (R:%d, E:%d)", partida.getRodada().getNumero(), tamanhoEstadio)));

		renda = PartidaTorcidaPorcentagem.getRendaIngressosAmistosos(publicoVisitante);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceiraAmistosos(partida.getClubeVisitante(),
				semana, renda,
				String.format("Ingressos (R:%d, E:%d)", partida.getRodada().getNumero(), tamanhoEstadio)));
		
		PartidaTorcida partidaTorcida = new PartidaTorcida();
		partidaTorcida.setPublico(publicoMandante + publicoVisitante);
		
		if (partida instanceof PartidaAmistosaResultado) {
			partidaTorcida.setPartidaAmistosaResultado((PartidaAmistosaResultado) partida);
		} else {
			throw new RuntimeException("Erro inesperado");
		}
		
		partidaTorcidaSalvarDTO.addPartidaTorcida(partidaTorcida);
	}

	protected void calcularTorcidaPartida(PartidaResultadoJogavel partida, Semana semana, PartidaTorcidaSalvarDTO partidaTorcidaSalvarDTO) {
		
		double renda = 0.0d;
		
		Integer numRodadasCN = carregarParametroService.getNumeroRodadasCopaNacional(semana.getTemporada().getJogo());

		int tamanhoEstadio = partida.getClubeMandante().getNivelNacional().getTamanhoTorcida();

		Double porcPublicoAlvo = getPorcentagemPublicoAlvo(semana.getTemporada().getJogo(), partida);

		//if (porcPublicoAlvo == null) return;

		Integer publicoMandante = sortearTorcida(tamanhoEstadio,
				partida.getClubeMandante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_MANDANTE, porcPublicoAlvo, true).intValue();

		Integer publicoVisitante = sortearTorcida(tamanhoEstadio,
				partida.getClubeVisitante().getNivelNacional().getTamanhoTorcida(), PORCENTAGEM_VISITANTE,
				porcPublicoAlvo, true).intValue();

		/*System.err.println(String.format("%d, %d, %f, %s, %d", tamanhoEstadio, /*publicoMandante, publicoVisitante,* /
				publicoMandante + publicoVisitante, porcPublicoAlvo, partida.getNivelCampeonato(), partida.getRodada().getNumero()));*/
		
		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoMandante,
				partida.getRodada().getNumero(), numRodadasCN);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceira(partida.getClubeMandante(),
				semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)", partida.getNivelCampeonato().name(),
						partida.getRodada().getNumero(), tamanhoEstadio)));

		renda = PartidaTorcidaPorcentagem.getRendaIngressos(partida.getNivelCampeonato(), publicoVisitante,
				partida.getRodada().getNumero(), numRodadasCN);

		partidaTorcidaSalvarDTO.addMovimentacaoFinanceira(criarMovimentacaoFinanceira(
				partida.getClubeVisitante(), semana, renda, String.format("Ingressos (N:%s, R:%d, E:%d)",
						partida.getNivelCampeonato().name(), partida.getRodada().getNumero(), tamanhoEstadio)));
		
		PartidaTorcida partidaTorcida = new PartidaTorcida();
		partidaTorcida.setPublico(publicoMandante + publicoVisitante);
		
		if (partida instanceof PartidaEliminatoriaResultado) {
			partidaTorcida.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partida);
		} else if (partida instanceof PartidaResultado) {
			partidaTorcida.setPartidaResultado((PartidaResultado) partida);
		} else {
			throw new RuntimeException("Erro inesperado");
		}
		
		partidaTorcidaSalvarDTO.addPartidaTorcida(partidaTorcida);
	}
	
	private MovimentacaoFinanceira criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceira entrada = new MovimentacaoFinanceira();
		
		entrada.setClube(clube);
		entrada.setSemana(semana);
		entrada.setTipoMovimentacao(TipoMovimentacaoFinanceira.ENTRADA_INGRESSOS);
		entrada.setValorMovimentacao(valorMovimentacao);
		entrada.setDescricao(descricao);

		return entrada;
	}
	
	private MovimentacaoFinanceira criarMovimentacaoFinanceiraAmistosos(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceira entrada = new MovimentacaoFinanceira();
		
		entrada.setClube(clube);
		entrada.setSemana(semana);
		entrada.setTipoMovimentacao(TipoMovimentacaoFinanceira.ENTRADA_INGRESSOS_AMISTOSOS);
		entrada.setValorMovimentacao(valorMovimentacao);
		entrada.setDescricao(descricao);

		return entrada;
	}
	
	private Double getPorcentagemPublicoAlvo(Jogo jogo, PartidaResultadoJogavel partida) {

		if (partida.getNivelCampeonato().isCopaNacional()) {
			
			Integer numRodadasCN = carregarParametroService.getNumeroRodadasCopaNacional(jogo);

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
			Double porcPublicoAlvo, Boolean variavel) {

		double porcPublicoAlvoFinal = porcPublicoAlvo;
		
		if (variavel) {
			porcPublicoAlvoFinal = porcPublicoAlvo + (R.nextGaussian() * PORC_STDEV);
		
			if (porcPublicoAlvoFinal > 1.0) porcPublicoAlvoFinal = 1.0;
		}

		double torcidaMax = tamanhoEstadio * ajuste * porcPublicoAlvoFinal;
		double torcidaAlvo = tamanhoTorcidaClube * porcPublicoAlvoFinal;

		return Math.round(Math.min(torcidaMax, torcidaAlvo));

	}

	public Double calcularPrevisaoEntradaIngressos(Jogo jogo, List<? extends PartidaResultadoJogavel> partidas, boolean mandante) {
		if (ValidatorUtil.isEmpty(partidas)) return 0.0d;

		Double porcPublicoAlvo = null, renda = 0.0d;
		Integer publicoClube = null, tamanhoEstadio = null;
		
		Integer numRodadasCN = carregarParametroService.getNumeroRodadasCopaNacional(jogo);
		
		for (PartidaResultadoJogavel p : partidas) {
			
			if (p.getClubeMandante() != null) {
				tamanhoEstadio = p.getClubeMandante().getNivelNacional().getTamanhoTorcida();
			} else {
				tamanhoEstadio = ClubeNivel.NIVEL_G.getTamanhoTorcida();
			}
			
			if (p.isAmistoso()) {
				porcPublicoAlvo = PartidaTorcidaPorcentagem.getPorcentagemAmistoso();
			} else if (p.getNivelCampeonato().isCIOuCIIOuCIII() || p.getNivelCampeonato().isCNIOuCNII()) {
				porcPublicoAlvo = getPorcentagemPublicoAlvo(jogo, p);
			} else if (p.getNivelCampeonato().isNIOuNII()) {
				porcPublicoAlvo = PartidaTorcidaPorcentagem.getPorcentagem(p.getNivelCampeonato(), 9);
			}
			
			if (mandante) {
				publicoClube = sortearTorcida(tamanhoEstadio, p.getClubeMandante().getNivelNacional().getTamanhoTorcida(),
						p.isAmistoso() ? PORCENTAGEM_MANDANTE_AMISTOSO : PORCENTAGEM_MANDANTE, porcPublicoAlvo, false)
						.intValue();
			} else {
				publicoClube = sortearTorcida(tamanhoEstadio, p.getClubeVisitante().getNivelNacional().getTamanhoTorcida(),
						p.isAmistoso() ? PORCENTAGEM_VISITANTE_AMISTOSO : PORCENTAGEM_VISITANTE, porcPublicoAlvo, false)
						.intValue();
			}
			
			if (p.isAmistoso()) {
				renda += PartidaTorcidaPorcentagem.getRendaIngressosAmistosos(publicoClube);
			} else {
				renda += PartidaTorcidaPorcentagem.getRendaIngressos(p.getNivelCampeonato(), publicoClube,
						p.getRodada().getNumero(), numRodadasCN);
			}
		}

		return renda;
	}
}
