package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.match.model.PremiacaoClassificacao;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.service.util.SemanaUtil;
import com.fastfoot.service.ParametroService;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class DistribuirPremiacaoCompeticoesService {//TODO: mover para scheduler.service
	
	//########	REPOSITORY	##########
	
	/*@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;*/
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;

	/*@Autowired
	private RodadaRepository rodadaRepository;*/
	
	/*@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;*/
	
	/*@Autowired
	private MovimentacaoFinanceiraEntradaRepository movimentacaoFinanceiraEntradaRepository;*/
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;

	//#######	SERVICE	#############
	
	@Autowired
	private ParametroService parametroService;

	public void distribuirPremiacaoCompeticoes(Semana semana) {
		/*
		 * Premiações:
		 * 	* Classificar para continental
		 * 	* Avançar fase continental
		 * 	* Classificação final Nacional
		 * 	* Avançar fase Copa Nacional
		 */
		
		List<MovimentacaoFinanceira> entradas = new ArrayList<MovimentacaoFinanceira>();

		List<PartidaEliminatoriaResultado> partidaEliminatoriasSemana = partidaEliminatoriaResultadoRepository
				.findBySemana(semana);

		Map<NivelCampeonato, List<PartidaEliminatoriaResultado>> partidasNivel = partidaEliminatoriasSemana.stream()
				.collect(Collectors.groupingBy(PartidaEliminatoriaResultado::getNivelCampeonato));
		
		if (SemanaUtil.isSemanaContinental(semana.getNumero())) {
			// r: 0, 3, 4, 5, 6
			int rodada = SemanaUtil.getRodadaContinental(semana.getNumero());
			if (rodada >= 4) {
				distribuirPremiacaoCompeticoesContinentalFaseEliminatoria(semana, rodada, partidasNivel, entradas);
			} else if (rodada == 3) {
				List<Classificacao> classificacao = classificacaoRepository
						.findByTemporadaGrupoCampeonato(semana.getTemporada());
				
				for (Classificacao c : classificacao) {
					Double premiacao = getPremiacao(c.getGrupoCampeonato().getCampeonato().getNivelCampeonato(), c.getPosicao());
					if (premiacao != null) {
						entradas.add(criarMovimentacaoFinanceira(c.getClube(), semana, premiacao,
								String.format("Classificação à Quartas Final (%s)",
										c.getGrupoCampeonato().getCampeonato().getNivelCampeonato().name())));//TODO: corrigir NivelCampeonato na descricao
					}
				}
				
			}
		}
		
		Integer numRodadasCN = parametroService.getNumeroRodadasCopaNacional();
		
		if (SemanaUtil.isSemanaCopaNacional(numRodadasCN, semana.getNumero())) {
			distribuirPremiacaoCompeticoesCopaNacional(semana, numRodadasCN, partidasNivel, entradas);
		}
		
		if (SemanaUtil.isSemanaCampeonatoNacional(semana.getNumero())) {
			//r: 15
			int rodada = SemanaUtil.getRodadaCampeonatoNacional(semana.getNumero());

			if (rodada == 15) {
				List<Classificacao> classificacao = classificacaoRepository
						.findByTemporadaCampeonato(semana.getTemporada());

				entradas.addAll(classificacao.stream().map(c -> criarMovimentacaoFinanceira(c.getClube(), semana,
						PremiacaoClassificacao.getPremiacaoClassificacaoNacional(c.getCampeonato().getNivelCampeonato(),
								c.getPosicao()),
						String.format("Classificação Final (%s)", c.getCampeonato().getNivelCampeonato().name())))
						.collect(Collectors.toList()));
			}
			
		}
		
		movimentacaoFinanceiraRepository.saveAll(entradas);
	}
	
	public void distribuirPremiacaoCompeticoes(Temporada temporada) {

		List<MovimentacaoFinanceira> entradas = new ArrayList<MovimentacaoFinanceira>();

		List<Classificacao> classificacao = classificacaoRepository.findByTemporadaGrupoCampeonato(temporada);

		entradas.addAll(classificacao.stream()
				.map(c -> criarMovimentacaoFinanceira(c.getClube(),
						temporada.getSemanas().stream().filter(s -> s.getNumero() == 1).findFirst().get(),
						PremiacaoClassificacao.getPremiacao(c.getGrupoCampeonato().getCampeonato().getNivelCampeonato(),
								PremiacaoClassificacao.CLASSIFICACAO_FASE_GRUPOS),
						String.format("Classificação à Fase de Grupos (%s)",
								c.getGrupoCampeonato().getCampeonato().getNivelCampeonato().name())))
				.collect(Collectors.toList()));

		movimentacaoFinanceiraRepository.saveAll(entradas);
	}

	private void distribuirPremiacaoCompeticoesCopaNacional(Semana semana, Integer numRodadasCN,
			Map<NivelCampeonato, List<PartidaEliminatoriaResultado>> partidasNivel,
			List<MovimentacaoFinanceira> entradas) {

		List<PartidaEliminatoriaResultado> partidaEliminatorias = null;

		for (NivelCampeonato nc : Arrays.asList(NivelCampeonato.COPA_NACIONAL, NivelCampeonato.COPA_NACIONAL_II)) {

			// r: todas
			int rodada = SemanaUtil.getRodadaCopaNacional(numRodadasCN, semana.getNumero());

			partidaEliminatorias = partidasNivel.get(nc);

			if (!ValidatorUtil.isEmpty(partidaEliminatorias)) {
				if (rodada == numRodadasCN) {// Final
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc, PremiacaoClassificacao.CAMPEAO),
									String.format("Campeão (%s)", nc.name())))
							.collect(Collectors.toList()));

				}
	
				if (rodada == (numRodadasCN - 1)) {// SemiFinal
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc, PremiacaoClassificacao.CLASSIFICACAO_FINAL),
									String.format("Classificação à Final (%s)", nc.name())))
							.collect(Collectors.toList()));
	
				}
	
				if (rodada == (numRodadasCN - 2)) {// Quartas Final
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc,
											PremiacaoClassificacao.CLASSIFICACAO_SEMI_FINAL),
									String.format("Classificação à Semi Final (%s)", nc.name())))
							.collect(Collectors.toList()));
	
				}
	
				if (rodada == (numRodadasCN - 3)) {// Oitavas Final
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc,
											PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL),
									String.format("Classificação à Quartas Final (%s)", nc.name())))
							.collect(Collectors.toList()));
	
				}
	
				if (rodada == (numRodadasCN - 4)) {// Fase Preliminar II
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc,
											PremiacaoClassificacao.CLASSIFICACAO_OITAVAS_FINAL),
									String.format("Classificação à Oitavas Final (%s)", nc.name())))
							.collect(Collectors.toList()));
	
				}
	
				if (rodada == (numRodadasCN - 5)) {// Fase Preliminar I
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc,
											PremiacaoClassificacao.CLASSIFICACAO_FASE_PRELIMINAR),
									String.format("Classificação à Fase Preliminar II (%s)", nc.name())))
							.collect(Collectors.toList()));
	
				}
			}
		}
	}
	
	private void distribuirPremiacaoCompeticoesContinentalFaseEliminatoria(Semana semana, int rodada,
			Map<NivelCampeonato, List<PartidaEliminatoriaResultado>> partidasNivel,
			List<MovimentacaoFinanceira> entradas) {

		List<PartidaEliminatoriaResultado> partidaEliminatorias = null;

		for (NivelCampeonato nc : Arrays.asList(NivelCampeonato.CONTINENTAL, NivelCampeonato.CONTINENTAL_II,
				NivelCampeonato.CONTINENTAL_III)) {

			partidaEliminatorias = partidasNivel.get(nc);

			if (!ValidatorUtil.isEmpty(partidaEliminatorias)) {
				if (rodada == 6) {// Final
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc, PremiacaoClassificacao.CAMPEAO),
									String.format("Campeão (%s)", nc.name())))
							.collect(Collectors.toList()));

				}
	
				if (rodada == 5) {// SemiFinal
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc, PremiacaoClassificacao.CLASSIFICACAO_FINAL),
									String.format("Classificação à Final (%s)", nc.name())))
							.collect(Collectors.toList()));

				}
	
				if (rodada == 4) {// Quartas Final
					entradas.addAll(partidaEliminatorias.stream()
							.map(p -> criarMovimentacaoFinanceira(p.getClubeVencedor(), semana,
									PremiacaoClassificacao.getPremiacao(nc,
											PremiacaoClassificacao.CLASSIFICACAO_SEMI_FINAL),
									String.format("Classificação à Semi Final (%s)", nc.name())))
							.collect(Collectors.toList()));

				}
			}
		}
	}
	
	private Double getPremiacao(NivelCampeonato nivelCampeonato, int posicao) {
		Boolean melhorEliminado = parametroService.isEstrategiaPromotorContinentalMelhorEliminado();
		Boolean cIIIReduzido = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		Boolean jogarCIII = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS) == 3;
		
		if (!melhorEliminado && (posicao == 1 || posicao == 2)) {
			return PremiacaoClassificacao.getPremiacao(nivelCampeonato,
					PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
		}

		if (melhorEliminado) {
			if (nivelCampeonato.isContinental() && (posicao == 1 || posicao == 2)) {
				return PremiacaoClassificacao.getPremiacao(nivelCampeonato,
						PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
			}
			
			if (nivelCampeonato.isContinental() && posicao == 3) {
				return PremiacaoClassificacao.getPremiacao(NivelCampeonato.CONTINENTAL_II,
						PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
			}
			
			if (nivelCampeonato.isContinentalII() && posicao == 1) {
				return PremiacaoClassificacao.getPremiacao(nivelCampeonato,
						PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
			}
			
			if (jogarCIII) {
				
				if (nivelCampeonato.isContinentalII() && posicao == 2) {
					return PremiacaoClassificacao.getPremiacao(NivelCampeonato.CONTINENTAL_III,
							PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
				}
				
				if (nivelCampeonato.isContinentalIII() && posicao == 1) {
					return PremiacaoClassificacao.getPremiacao(nivelCampeonato,
							PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
				}
				
				if (cIIIReduzido && nivelCampeonato.isContinentalIII() && posicao == 2) {
					return PremiacaoClassificacao.getPremiacao(nivelCampeonato,
							PremiacaoClassificacao.CLASSIFICACAO_QUARTAS_FINAL);
				}
			}
		}
		
		return null;
	}
	
	private MovimentacaoFinanceira criarMovimentacaoFinanceira(Clube clube, Semana semana,
			Double valorMovimentacao, String descricao) {
		
		MovimentacaoFinanceira entrada = new MovimentacaoFinanceira();
		
		entrada.setClube(clube);
		entrada.setSemana(semana);
		entrada.setTipoMovimentacao(TipoMovimentacaoFinanceira.ENTRADA_PREMIACAO);
		entrada.setValorMovimentacao(valorMovimentacao);
		entrada.setDescricao(descricao);

		return entrada;
	}
}
