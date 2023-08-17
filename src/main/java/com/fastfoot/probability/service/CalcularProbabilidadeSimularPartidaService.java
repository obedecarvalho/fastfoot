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

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.service.CalcularEstatisticasFinalizacaoDefesaService;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.probability.model.CampeonatoClubeProbabilidadePosicao;
import com.fastfoot.probability.model.ClubeRankingPosicaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.probability.model.TipoCampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.probability.model.repository.CampeonatoClubeProbabilidadeRepository;
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
public class CalcularProbabilidadeSimularPartidaService implements ICalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES_SEM_17 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_19 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_21 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 10000;
	
	private static final Random R = new Random();
	
	private static boolean imprimir = false;
	
	//###	REPOSITORY	###
	
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
	
	@Autowired
	private CampeonatoClubeProbabilidadeRepository campeonatoClubeProbabilidadeRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private CarregarParametroService carregarParametroService;
	
	/*@Autowired
	private ClubeRepository clubeRepository;*/
	
	@Autowired
	private CalcularEstatisticasFinalizacaoDefesaService calcularEstatisticasFinalizacaoDefesaService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoService calcularPartidaProbabilidadeResultadoService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato nacional, Campeonato nacionalII,
			TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {

		nacional.setClassificacao(classificacaoRepository.findByCampeonato(nacional));
		nacional.setRodadas(rodadaRepository.findByCampeonato(nacional));

		/*for (Rodada r : nacional.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}*/
		
		nacionalII.setClassificacao(classificacaoRepository.findByCampeonato(nacionalII));
		nacionalII.setRodadas(rodadaRepository.findByCampeonato(nacionalII));

		/*for (Rodada r : nacionalII.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}*/

		List<Rodada> rodadas = new ArrayList<Rodada>();
		rodadas.addAll(nacional.getRodadas());
		rodadas.addAll(nacionalII.getRodadas());
		carregarPartidas(rodadas, tipoClubeProbabilidade);
		
		//
		Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade = new HashMap<PartidaResultado, PartidaProbabilidadeResultado>();
		
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(semana.getTemporada());
		Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasDefesaClube(semana.getTemporada());

		for (int r = nacional.getRodadaAtual(); r < nacional.getRodadas().size(); r++) {			
			for (PartidaResultado p : nacional.getRodadas().get(r).getPartidas()) {
				partidaProbabilidade.put(p, calcularPartidaProbabilidadeResultadoService.calcularPartidaProbabilidadeResultado(p,
						clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa, p.getEscalacaoMandante(),
						p.getEscalacaoVisitante(), tipoClubeProbabilidade));
			}
		}
		
		/*clubeProbabilidadeFinalizacoes = calcularEstatisticasFinalizacaoDefesaService
				.getEstatisticasFinalizacaoClube(nacionalII);
		clubesProbabilidadeDefesa = calcularEstatisticasFinalizacaoDefesaService.getEstatisticasDefesaClube(nacionalII);*/
		
		for (int r = nacionalII.getRodadaAtual(); r < nacionalII.getRodadas().size(); r++) {			
			for (PartidaResultado p : nacionalII.getRodadas().get(r).getPartidas()) {
				partidaProbabilidade.put(p, calcularPartidaProbabilidadeResultadoService.calcularPartidaProbabilidadeResultado(p,
						clubeProbabilidadeFinalizacoes, clubesProbabilidadeDefesa, p.getEscalacaoMandante(),
						p.getEscalacaoVisitante(), tipoClubeProbabilidade));
			}
		}
		//
		
		Collection<CampeonatoClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, nacional, nacionalII,
				partidaProbabilidade, tipoClubeProbabilidade);
		
		salvarProbabilidades(probabilidades);
		
		return CompletableFuture.completedFuture(true);
	}

	private void carregarPartidas(List<Rodada> rodadas, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		List<PartidaResultado> partidas = partidaRepository.findByRodadas(rodadas);
		if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA.equals(tipoClubeProbabilidade)) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacao(partidas);
		} else if (TipoCampeonatoClubeProbabilidade.SIMULAR_PARTIDA_HABILIDADE_GRUPO.equals(tipoClubeProbabilidade)) {
			carregarEscalacaoJogadoresPartidaService.carregarEscalacaoHabilidadeGrupo(partidas);
		}
		Map<Rodada, List<PartidaResultado>> partidasRodada = partidas.stream()
				.collect(Collectors.groupingBy(PartidaResultado::getRodada));
		rodadas.stream().forEach(r -> r.setPartidas(partidasRodada.get(r)));
	}
	
	private Collection<CampeonatoClubeProbabilidade> calcularClubeProbabilidade(Semana semana, Campeonato nacional,
			Campeonato nacionalII, Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		
		Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, CampeonatoClubeProbabilidade>();
		
		List<Clube> clubesLiga = nacional.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		List<Clube> clubesLigaII = nacionalII.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList());
		
		inicializarClubeProbabilidade(clubeProbabilidades, clubesLiga, semana, nacional, tipoClubeProbabilidade);

		inicializarClubeProbabilidade(clubeProbabilidades, clubesLigaII, semana, nacionalII, tipoClubeProbabilidade);
		
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
				jogarRodada(nacional.getRodadas().get(r), classificacaoProbabilidadesI, partidaProbabilidade);
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
				jogarRodada(nacionalII.getRodadas().get(r), classificacaoProbabilidadesII, partidaProbabilidade);
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
	
	private void calcularProbabilidadesEspecificas(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades, Semana semana/*, NivelCampeonato nivelCampeonato*/) {
		
		Integer numeroRebaixados = carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		CampeonatoClubeProbabilidadePosicao cpp = null;

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (CampeonatoClubeProbabilidade cp : clubeProbabilidades.values()) {
			
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
	
	private void agruparClubeProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades,
			List<ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (ClassificacaoProbabilidade clasp : classificacaoProbabilidades) {
			CampeonatoClubeProbabilidade clup = clubeProbabilidades.get(clasp.getClube());
			
			
			CampeonatoClubeProbabilidadePosicao cpp = clup.getClubeProbabilidadePosicao().get(clasp.getPosicao());
			
			if (cpp == null) {
				cpp = new CampeonatoClubeProbabilidadePosicao();
				
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
	
	private void agruparClubeRankingProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades, List<ClubeRankingProbabilidade> ranking) {

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (ClubeRankingProbabilidade crp : ranking) {
			if (clubeProbabilidades.get(crp.getClube()) != null) {
				CampeonatoClubeProbabilidade clubeProb = clubeProbabilidades.get(crp.getClube());
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
	
	private void inicializarClubeProbabilidade(Map<Clube, CampeonatoClubeProbabilidade> clubeProbabilidades,
			List<Clube> clubes, Semana semana, Campeonato campeonato, TipoCampeonatoClubeProbabilidade tipoClubeProbabilidade) {
		//if (clubeProbabilidades.isEmpty()) {
		for (Clube clube : clubes) {
			CampeonatoClubeProbabilidade clup = new CampeonatoClubeProbabilidade();
			
			clup.setClube(clube);
			clup.setCampeonato(campeonato);
			clup.setSemana(semana);
			//clup.setCompleto(true);
			clup.setTipoClubeProbabilidade(tipoClubeProbabilidade);

			clup.setClubeProbabilidadePosicao(new HashMap<Integer, CampeonatoClubeProbabilidadePosicao>());
			
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
		
		int numeroRodadas = carregarParametroService.getNumeroRodadasCopaNacional();

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
	
	private void salvarProbabilidades(Collection<CampeonatoClubeProbabilidade> probabilidades) {
		campeonatoClubeProbabilidadeRepository.saveAll(probabilidades);
	}

	private void jogarRodada(Rodada r, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			Map<PartidaResultado, PartidaProbabilidadeResultado> partidaProbabilidade) {

		for (PartidaResultado p : r.getPartidas()) {
			jogarPartida(p, classificacaoProbabilidades, partidaProbabilidade.get(p));
		}

	}

	private void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades,
			PartidaProbabilidadeResultado partidaProbabilidade) {

		ClassificacaoProbabilidade clasMandante = classificacaoProbabilidades.get(p.getClubeMandante());

		ClassificacaoProbabilidade clasVisitante = classificacaoProbabilidades.get(p.getClubeVisitante());

		double vitoriaMandante = partidaProbabilidade.getProbabilidadeVitoriaMandante();

		double empate = partidaProbabilidade.getProbabilidadeEmpate();

		//double vitoriaVisitante = clasMandante.getProbabilidadeDerrota() + clasVisitante.getProbabilidadeVitoria();

		double resultado = R.nextDouble();

		if (resultado <= vitoriaMandante) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_VITORIA);
			clasMandante.setVitorias(clasMandante.getVitorias() + 1);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + 1);
			clasMandante.setSaldoGols(clasMandante.getSaldoGols() + 1);

			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() - 1);
		} else if (resultado <= empate) {
			clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_EMPATE);
			clasMandante.setGolsPro(clasMandante.getGolsPro() + 0);

			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_EMPATE);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + 0);
		} else {
			clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_VITORIA);
			clasVisitante.setVitorias(clasVisitante.getVitorias() + 1);
			clasVisitante.setGolsPro(clasVisitante.getGolsPro() + 1);
			clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + 1);

			clasMandante.setSaldoGols(clasMandante.getSaldoGols() - 1);
		}

		//clasMandante.calcularProbabilidades();
		//clasVisitante.calcularProbabilidades();
	}

	private Integer getPosicoesClassificamCIMin() { return 1; }
	
	private Integer getPosicoesClassificamCIMax() { return 4; }
	
	private Integer getPosicoesClassificamCIIMin() { return 5; }
	
	private Integer getPosicoesClassificamCIIMax() { return 8; }
	
	private Integer getPosicoesClassificamCIIIMin() {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		return 9;
	}

	private Integer getPosicoesClassificamCIIIMax() {

		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);

		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);

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
		
		return carregarParametroService.getNumeroTimesParticipantesCopaNacional();
	}
	
	//########	TESTE	##############

	private synchronized void printClubeProbabilidade(Collection<CampeonatoClubeProbabilidade> clubeProbabilidades) {
		for (CampeonatoClubeProbabilidade cp : clubeProbabilidades) {
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
