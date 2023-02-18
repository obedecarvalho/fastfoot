package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.probability.model.ClubeProbabilidadePosicao;
import com.fastfoot.probability.model.ClubeRankingPosicaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.probability.model.TipoClubeProbabilidade;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.model.repository.ClubeProbabilidadeRepository;
import com.fastfoot.probability.service.util.ClassificacaoProbabilidadeUtil;
import com.fastfoot.probability.service.util.ClubeRankingProbabilidadeUtil;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.service.CarregarParametroService;

@Service
@Deprecated /* Substituir por TipoClubeProbabilidade.SIMULAR_PARTIDA_EST_FINALIZACAO */
public class CalcularProbabilidadeEstatisticasFinalizacoesService {

	private static final Integer NUM_SIMULACOES_SEM_17 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_19 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_21 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 10000;
	
	private static final Random R = new Random();
	
	private static boolean imprimir = false;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;

	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;

	/*@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;*/
	
	@Autowired
	private CarregarParametroService parametroService;
	
	/*@Autowired
	private ClubeRepository clubeRepository;*/
	
	@Autowired
	private ClubeProbabilidadeRepository clubeProbabilidadeRepository;
	
	@Autowired
	private CalcularEstatisticasFinalizacaoDefesaService calcularEstatisticasFinalizacaoDefesaService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato nacional,
			Campeonato nacionalII) {

		nacional.setClassificacao(classificacaoRepository.findByCampeonato(nacional));
		nacional.setRodadas(rodadaRepository.findByCampeonato(nacional));

		for (Rodada r : nacional.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}
		
		nacionalII.setClassificacao(classificacaoRepository.findByCampeonato(nacionalII));
		nacionalII.setRodadas(rodadaRepository.findByCampeonato(nacionalII));

		for (Rodada r : nacionalII.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}
		
		/*List<Map<String, Object>> estatisticasFinalizacoes = jogadorEstatisticasTemporadaRepository
				.findEstatisticasFinalizacoesPorClube(semana.getTemporada().getId());
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = new HashMap<Clube, ClubeProbabilidadeFinalizacao>();
		ClubeProbabilidadeFinalizacao clubeProbabilidadeFinalizacao = null;
		
		for (Map<String, Object> e : estatisticasFinalizacoes) {
			clubeProbabilidadeFinalizacao = new ClubeProbabilidadeFinalizacao();
			
			clubeProbabilidadeFinalizacao.setClube(new Clube((Integer) e.get("id_clube")));
			clubeProbabilidadeFinalizacao
					.setFinalizacoesPartidas(((BigDecimal) e.get("finalizacoes_partidas")).doubleValue());
			clubeProbabilidadeFinalizacao.setGolsPartida(((BigDecimal) e.get("gols_partida")).doubleValue());
			clubeProbabilidadeFinalizacao
					.setProbabilidadeGolFinalizacao(((BigDecimal) e.get("probilidade_gols")).doubleValue());
			
			clubeProbabilidadeFinalizacoes.put(clubeProbabilidadeFinalizacao.getClube(), clubeProbabilidadeFinalizacao);
		}*/
		
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(semana.getTemporada());
		
		Collection<ClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, nacional, nacionalII,
				clubeProbabilidadeFinalizacoes);
		
		salvarProbabilidades(probabilidades);
		
		return CompletableFuture.completedFuture(true);
	}
	
	private Collection<ClubeProbabilidade> calcularClubeProbabilidade(Semana semana, Campeonato nacional,
			Campeonato nacionalII, Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes) {
		
		Map<Clube, ClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, ClubeProbabilidade>();
		
		List<Clube> clubesLiga = nacional.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		List<Clube> clubesLigaII = nacionalII.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		inicializarClubeProbabilidade(clubeProbabilidades, clubesLiga, semana, nacional);

		inicializarClubeProbabilidade(clubeProbabilidades, clubesLigaII, semana, nacionalII);
		
		clubesLiga.addAll(clubesLigaII);
		
		Map<Integer, Clube> clubesCampeoes = new HashMap<Integer, Clube>();
		
		getCampeoesContinentais(semana.getTemporada(), clubesCampeoes);
		
		getCampeoesCopaNacional(semana.getTemporada(), clubesCampeoes, nacional.getLiga());
		
		//List<Clube> clubesLiga = clubeRepository.findByLiga(nacional.getLiga());

		for (int i = 0; i < getNumeroSimulacoes(semana); i++) {

			//NI
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidadesI = nacional.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = nacional.getRodadaAtual(); r < nacional.getRodadas().size(); r++) {
				jogarRodada(nacional.getRodadas().get(r), classificacaoProbabilidadesI, clubeProbabilidadeFinalizacoes);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesListI = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidadesI.values());
			
			ClassificacaoProbabilidadeUtil.ordernarClassificacao(classificacaoProbabilidadesListI, true);
			
			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesListI);
			
			//NII
			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidadesII = nacionalII.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = nacionalII.getRodadaAtual(); r < nacionalII.getRodadas().size(); r++) {
				jogarRodada(nacionalII.getRodadas().get(r), classificacaoProbabilidadesII,
						clubeProbabilidadeFinalizacoes);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesListII = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidadesII.values());
			
			ClassificacaoProbabilidadeUtil.ordernarClassificacao(classificacaoProbabilidadesListII, true);

			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesListII);
			
			//Rankear
			List<ClubeRankingProbabilidade> ranking = ClubeRankingProbabilidadeUtil.rankearClubesTemporada(
					clubesLiga, classificacaoProbabilidadesListI, classificacaoProbabilidadesListII, clubesCampeoes);
			
			agruparClubeRankingProbabilidade(clubeProbabilidades, ranking);
			
			//System.err.println("==>" + ranking);

		}

		calcularProbabilidadesEspecificas(clubeProbabilidades, semana);
		
		if (imprimir) printClubeProbabilidade(clubeProbabilidades.values());

		return clubeProbabilidades.values();

	}
	
	private void calcularProbabilidadesEspecificas(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			Semana semana/* , NivelCampeonato nivelCampeonato */) {
		
		Integer numeroRebaixados = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		ClubeProbabilidadePosicao cpp = null;

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (ClubeProbabilidade cp : clubeProbabilidades.values()) {
			
			//Campeao
			cpp = cp.getClubeProbabilidadePosicao().get(1);
			if (cpp != null) {
				cp.setProbabilidadeCampeao(cpp.getProbabilidade().doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeCampeao(cpp.getProbabilidade());
			}
			
			//Rebaixamento
			if (cp.getCampeonato().getNivelCampeonato().isNacional()) {
				Integer probabilidadeRebaixamento = 0;
				for (int i = Constantes.NRO_CLUBE_CAMP_NACIONAL; i > (Constantes.NRO_CLUBE_CAMP_NACIONAL - numeroRebaixados); i-- ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeRebaixamento += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeRebaixamento(probabilidadeRebaixamento.doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeRebaixamento(probabilidadeRebaixamento);
			}
			
			//Acesso
			if (cp.getCampeonato().getNivelCampeonato().isNacionalII()) {
				Integer probabilidadeAcesso = 0;
				for (int i = 1; i <= numeroRebaixados; i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeAcesso += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeAcesso(probabilidadeAcesso.doubleValue()/getNumeroSimulacoes(semana));
				//cp.setQtdeAcesso(probabilidadeAcesso);
			}
			
			if (semana.getNumero() >= 22) {
				//CI
				Integer probabilidadeClassificacaoCI = 0;
				for (int i = getPosicoesClassificamCIMin(); i <= getPosicoesClassificamCIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCI += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCI(probabilidadeClassificacaoCI);
				cp.setProbabilidadeClassificacaoCI(probabilidadeClassificacaoCI.doubleValue()/getNumeroSimulacoes(semana));
	
				//CII
				Integer probabilidadeClassificacaoCII = 0;
				for (int i = getPosicoesClassificamCIIMin(); i <= getPosicoesClassificamCIIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCII += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCII(probabilidadeClassificacaoCII);
				cp.setProbabilidadeClassificacaoCII(probabilidadeClassificacaoCII.doubleValue()/getNumeroSimulacoes(semana));
	
				//CIII
				Integer probabilidadeClassificacaoCIII = 0;
				for (int i = getPosicoesClassificamCIIIMin(); i <= getPosicoesClassificamCIIIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCIII += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCIII(probabilidadeClassificacaoCIII);
				cp.setProbabilidadeClassificacaoCIII(probabilidadeClassificacaoCIII.doubleValue()/getNumeroSimulacoes(semana));
	
				//CNI
				Integer probabilidadeClassificacaoCNI = 0;
				for (int i = getPosicoesClassificamCNIMin(); i <= getPosicoesClassificamCNIMax(); i++ ) {
					crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
					if (crpp != null) {
						probabilidadeClassificacaoCNI += crpp.getProbabilidade();
					}
				}
				//cp.setQtdeClassificacaoCNI(probabilidadeClassificacaoCNI);
				cp.setProbabilidadeClassificacaoCNI(probabilidadeClassificacaoCNI.doubleValue()/getNumeroSimulacoes(semana));
			}

		}

	}
	
	private void agruparClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
			ClubeProbabilidade clup = clubeProbabilidades.get(clasp.getClube());
			
			
			ClubeProbabilidadePosicao cpp = clup.getClubeProbabilidadePosicao().get(clasp.getPosicao());
			
			if (cpp == null) {
				cpp = new ClubeProbabilidadePosicao();
				
				cpp.setPosicao(clasp.getPosicao());
				cpp.setProbabilidade(1);
				cpp.setClubeProbabilidade(clup);
				
				/*cpp.setPontuacaoMaxima(clasp.getPontos());
				cpp.setPontuacaoMedia(clasp.getPontos().doubleValue());
				cpp.setPontuacaoMinima(clasp.getPontos());*/
				
				clup.getClubeProbabilidadePosicao().put(clasp.getPosicao(), cpp);
			} else {
				
				/*if (clasp.getPontos() > cpp.getPontuacaoMaxima()) {
					cpp.setPontuacaoMaxima(clasp.getPontos());
				}
				
				if (clasp.getPontos() < cpp.getPontuacaoMinima()) {
					cpp.setPontuacaoMinima(clasp.getPontos());
				}
				
				double media = (cpp.getPontuacaoMedia() * cpp.getProbabilidade() + clasp.getPontos().byteValue())/(cpp.getProbabilidade() + 1);
				cpp.setPontuacaoMedia(media);*/
				
				cpp.setProbabilidade(cpp.getProbabilidade() + 1);
			}

		}
		
	}
	
	private void agruparClubeRankingProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades, List<ClubeRankingProbabilidade> ranking) {

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (ClubeRankingProbabilidade crp : ranking) {
			if (clubeProbabilidades.get(crp.getClube()) != null) {
				ClubeProbabilidade clubeProb = clubeProbabilidades.get(crp.getClube());
				crpp = clubeProb.getClubeProbabilidadePosicaoGeral().get(crp.getPosicaoGeral());
					
				if (crpp == null) {
					crpp = new ClubeRankingPosicaoProbabilidade();
					
					crpp.setPosicaoGeral(crp.getPosicaoGeral());
					crpp.setProbabilidade(1);
					crpp.setClubeProbabilidade(clubeProb);
	
					clubeProb.getClubeProbabilidadePosicaoGeral().put(crp.getPosicaoGeral(), crpp);
					
				} else {
					
					crpp.setProbabilidade(crpp.getProbabilidade() + 1);
				}
			}
		}		
	}
	
	private void inicializarClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<Clube> clubes, Semana semana, Campeonato campeonato) {
		//if (clubeProbabilidades.isEmpty()) {
		for (Clube clube : clubes) {
			ClubeProbabilidade clup = new ClubeProbabilidade();
			
			clup.setClube(clube);
			clup.setCampeonato(campeonato);
			clup.setSemana(semana);
			//clup.setCompleto(true);
			clup.setTipoClubeProbabilidade(TipoClubeProbabilidade.ESTATISTICAS_FINALIZACAO);

			clup.setClubeProbabilidadePosicao(new HashMap<Integer, ClubeProbabilidadePosicao>());
			
			clup.setClubeProbabilidadePosicaoGeral(new HashMap<Integer, ClubeRankingPosicaoProbabilidade>());
			
			clubeProbabilidades.put(clube, clup);
		}
		//}
	}

	private void getCampeoesContinentais(Temporada t, Map<Integer, Clube> clubesCampeoes) {
		//Campeoes
		List<CampeonatoMisto> continentais = campeonatoMistoRepository.findByTemporada(t);
		
		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;

		for (CampeonatoMisto c : continentais) {
			r = rodadaEliminatoriaRepository.findFirstByCampeonatoMistoAndNumero(c, 6).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);
			if (c.getNivelCampeonato().isContinental()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT, p.get(0).getClubeVencedor());
			} else if (c.getNivelCampeonato().isContinentalII()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT_II, p.get(0).getClubeVencedor());
			} else if (c.getNivelCampeonato().isContinentalIII()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_CONT_III, p.get(0).getClubeVencedor());
			}
		}
	}
	
	private void getCampeoesCopaNacional(Temporada t, Map<Integer, Clube> clubesCampeoes, Liga liga) {
		//Campeoes
		List<CampeonatoEliminatorio> copasNacionais = campeonatoEliminatorioRepository.findByTemporadaAndLiga(t, liga);
		
		RodadaEliminatoria r = null;
		List<PartidaEliminatoriaResultado> p = null;
		
		int numeroRodadas = parametroService.getNumeroRodadasCopaNacional();

		for (CampeonatoEliminatorio c : copasNacionais) {
			
			r = rodadaEliminatoriaRepository.findFirstByCampeonatoEliminatorioAndNumero(c,
					c.getNivelCampeonato().isCopaNacional() ? numeroRodadas : 4).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);
			if (c.getNivelCampeonato().isCopaNacional()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC, p.get(0).getClubeVencedor());
			} else if (c.getNivelCampeonato().isCopaNacionalII()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC_II, p.get(0).getClubeVencedor());
			}
		}
	}

	private Integer getNumeroSimulacoes(Semana semana) {
		//return 100;

		if (semana.getNumero() == 17)
			return NUM_SIMULACOES_SEM_17;
		if (semana.getNumero() == 19)
			return NUM_SIMULACOES_SEM_19;
		if (semana.getNumero() == 21)
			return NUM_SIMULACOES_SEM_21;
		if (semana.getNumero() == 22)
			return NUM_SIMULACOES_SEM_22;
		if (semana.getNumero() == 23)
			return NUM_SIMULACOES_SEM_23;
		if (semana.getNumero() == 24)
			return NUM_SIMULACOES_SEM_24;
		return -1;
	}
	
	private void salvarProbabilidades(Collection<ClubeProbabilidade> probabilidades) {
		clubeProbabilidadeRepository.saveAll(probabilidades);
	}

	private void jogarRodada(Rodada r, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes) {

		for (PartidaResultado p : r.getPartidas()) {
			jogarPartida(p, classificacaoProbabilidades, clubeProbabilidadeFinalizacoes);
		}

	}

	private void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes) {
		
		ClassificacaoProbabilidade clasMandante = classificacaoProbabilidades.get(p.getClubeMandante());

		ClassificacaoProbabilidade clasVisitante = classificacaoProbabilidades.get(p.getClubeVisitante());
		
		ClubeProbabilidadeFinalizacao probFinalizacaoMandante = clubeProbabilidadeFinalizacoes
				.get(p.getClubeMandante());
		
		ClubeProbabilidadeFinalizacao probFinalizacaoVisitante = clubeProbabilidadeFinalizacoes
				.get(p.getClubeVisitante());
		
		int golsMandante = 0, golsVisitante = 0;
		
		double resultado = 0d;
		
		for (int i = 0; i < probFinalizacaoMandante.getFinalizacoesPartidas(); i++) {
			resultado = R.nextDouble();
			
			if (resultado <= probFinalizacaoMandante.getProbabilidadeGolFinalizacao()) {
				golsMandante++;
			}
		}
		
		for (int i = 0; i < probFinalizacaoVisitante.getFinalizacoesPartidas(); i++) {
			resultado = R.nextDouble();
			
			if (resultado <= probFinalizacaoVisitante.getProbabilidadeGolFinalizacao()) {
				golsVisitante++;
			}
		}
		
		if (golsMandante > golsVisitante) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_VITORIA);
			clasMandante.setVitorias(clasMandante.getVitorias() + 1);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + golsMandante);
			clasMandante.setSaldoGols(clasMandante.getSaldoGols() + (golsMandante - golsVisitante));

			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + (golsVisitante - golsMandante));
		} else if (golsMandante == golsVisitante) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_EMPATE);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + golsMandante);

			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_EMPATE);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + golsVisitante);
		} else {
			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_VITORIA);
			clasVisitante.setVitorias(clasVisitante.getVitorias() + 1);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + golsVisitante);
			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + (golsVisitante - golsMandante));

			clasMandante.setSaldoGols(clasMandante.getSaldoGols() + (golsMandante - golsVisitante));
		}

	}

	private Integer getPosicoesClassificamCIMin() { return 1; }
	
	private Integer getPosicoesClassificamCIMax() { return 4; }
	
	private Integer getPosicoesClassificamCIIMin() { return 5; }
	
	private Integer getPosicoesClassificamCIIMax() { return 8; }
	
	private Integer getPosicoesClassificamCIIIMin() {

		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		return 9;
	}

	private Integer getPosicoesClassificamCIIIMax() {

		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);

		Boolean cIIIReduzido = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		//TODO: parametroService.isEstrategiaPromotorContinentalMelhorEliminado()

		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		if (cIIIReduzido) {
			return 10;
		}

		return 12;
	}
	
	private Integer getPosicoesClassificamCNIMin() { return 1; }
	
	private Integer getPosicoesClassificamCNIMax() {
		
		return parametroService.getNumeroTimesParticipantesCopaNacional();
	}
	
	//########	TESTE	##############

	private synchronized void printClubeProbabilidade(Collection<ClubeProbabilidade> clubeProbabilidades) {
		for (ClubeProbabilidade cp : clubeProbabilidades) {
			/*System.err.println("\t" + cp.getClube().getNome());
			System.err.println("\t\tCampeao: " + cp.getProbabilidadeCampeao());
			System.err.println("\t\tRebaixamento: " + cp.getProbabilidadeRebaixamento());
			System.err.println("\t\tAcesso: " + cp.getProbabilidadeAcesso());
			System.err.println("\t\tC1: " + cp.getProbabilidadeClassificacaoCI());
			System.err.println("\t\tC2: " + cp.getProbabilidadeClassificacaoCII());
			System.err.println("\t\tC3: " + cp.getProbabilidadeClassificacaoCIII());*/
			
			System.err.println(cp);
			
			/*for (Integer pos : cp.getClubeProbabilidadePosicao().keySet()) {
				System.err.println(String.format("\t\t\t%d:%d", pos, cp.getClubeProbabilidadePosicao().get(pos).getProbabilidade()));
				//System.err.println(String.format("\t\t\tmax:%d, min:%d, avg:%f", cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMaxima(), cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMinima(), cp.getClubeProbabilidadePosicao().get(pos).getPontuacaoMedia()) );
			}*/
		}
	}
}
