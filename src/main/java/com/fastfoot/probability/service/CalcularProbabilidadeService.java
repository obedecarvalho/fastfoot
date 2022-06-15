package com.fastfoot.probability.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.model.repository.ClubeRepository;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeProbabilidadePosicao;
import com.fastfoot.probability.model.ClubeRankingPosicaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.probability.model.repository.ClubeProbabilidadeRepository;
import com.fastfoot.probability.service.util.ClubeRankingProbabilidadeUtil;
import com.fastfoot.scheduler.model.NivelCampeonato;
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
import com.fastfoot.service.ParametroService;

@Service
public class CalcularProbabilidadeService {
	
	private static final Integer NUM_SIMULACOES_SEM_22 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_23 = 10000;
	
	private static final Integer NUM_SIMULACOES_SEM_24 = 10000;
	
	private static final Random R = new Random();
	
	private static boolean imprimir = false;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private ClubeProbabilidadeRepository clubeProbabilidadeRepository;

	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;

	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Async("probabilidadeExecutor")
	public CompletableFuture<Boolean> calcularProbabilidadesCampeonato(Semana semana, Campeonato c) {

		c.setClassificacao(classificacaoRepository.findByCampeonato(c));
		c.setRodadas(rodadaRepository.findByCampeonato(c));

		for (Rodada r : c.getRodadas()) {
			r.setPartidas(partidaRepository.findByRodada(r));
		}
		
		Collection<ClubeProbabilidade> probabilidades = calcularClubeProbabilidade(semana, c);
		
		salvarProbabilidades(probabilidades);
		
		return CompletableFuture.completedFuture(true);
	}

	private void salvarProbabilidades(Collection<ClubeProbabilidade> probabilidades) {
		clubeProbabilidadeRepository.saveAll(probabilidades);
	}

	private Integer getNumeroSimulacoes(Semana semana) {
		//return 100;

		if (semana.getNumero() == 22)
			return NUM_SIMULACOES_SEM_22;
		if (semana.getNumero() == 23)
			return NUM_SIMULACOES_SEM_23;
		if (semana.getNumero() == 24)
			return NUM_SIMULACOES_SEM_24;
		return -1;
	}
	
	public Collection<ClubeProbabilidade> calcularClubeProbabilidade(Semana semana, Campeonato campeonato) {
		
		Map<Clube, ClubeProbabilidade> clubeProbabilidades = new HashMap<Clube, ClubeProbabilidade>();
		
		inicializarClubeProbabilidade(clubeProbabilidades,
				campeonato.getClassificacao().stream().map(c -> c.getClube()).collect(Collectors.toList()), semana,
				campeonato);
		
		Map<Integer, Clube> clubesCampeoes = new HashMap<Integer, Clube>();
		
		getCampeoesContinentais(semana.getTemporada(), clubesCampeoes);
		
		getCampeoesCopaNacional(semana.getTemporada(), clubesCampeoes, campeonato.getLiga());
		
		List<Clube> clubesLiga = clubeRepository.findByLiga(campeonato.getLiga());

		for (int i = 0; i < getNumeroSimulacoes(semana); i++) {

			Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades = campeonato.getClassificacao().stream()
					.map(c -> ClassificacaoProbabilidade.criar(c))
					.collect(Collectors.toMap(ClassificacaoProbabilidade::getClube, Function.identity()));
		
		
			for (int r = campeonato.getRodadaAtual(); r < campeonato.getRodadas().size(); r++) {
				jogarRodada(campeonato.getRodadas().get(r), classificacaoProbabilidades);
			}
			
			List<ClassificacaoProbabilidade> classificacaoProbabilidadesList = new ArrayList<ClassificacaoProbabilidade>(
					classificacaoProbabilidades.values());
			
			ordernarClassificacao(classificacaoProbabilidadesList, true);

			List<ClubeRankingProbabilidade> ranking = ClubeRankingProbabilidadeUtil.rankearClubesTemporada(
					clubesLiga,
					classificacaoProbabilidadesList, campeonato.getNivelCampeonato(), clubesCampeoes);

			agruparClubeProbabilidade(clubeProbabilidades, classificacaoProbabilidadesList);
			
			agruparClubeRankingProbabilidade(clubeProbabilidades, ranking);

		}

		calcularProbabilidadesEspecificas(clubeProbabilidades, semana, campeonato.getNivelCampeonato());
		
		if (imprimir) printClubeProbabilidade(clubeProbabilidades.values());

		return clubeProbabilidades.values();

	}
	
	private void calcularProbabilidadesEspecificas(Map<Clube, ClubeProbabilidade> clubeProbabilidades, Semana semana, NivelCampeonato nivelCampeonato) {
		
		Integer numeroRebaixados = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS);

		ClubeProbabilidadePosicao cpp = null;

		ClubeRankingPosicaoProbabilidade crpp = null;
		
		for (ClubeProbabilidade cp : clubeProbabilidades.values()) {
			
			//Campeao
			cpp = cp.getClubeProbabilidadePosicao().get(1);
			if (cpp != null) {
				cp.setProbabilidadeCampeao(cpp.getProbabilidade().doubleValue()/getNumeroSimulacoes(semana));
				cp.setQtdeCampeao(cpp.getProbabilidade());
			}
			
			//Rebaixamento
			if (nivelCampeonato.isNacional()) {
				Integer probabilidadeRebaixamento = 0;
				for (int i = Constantes.NRO_CLUBE_CAMP_NACIONAL; i > (Constantes.NRO_CLUBE_CAMP_NACIONAL - numeroRebaixados); i-- ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeRebaixamento += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeRebaixamento(probabilidadeRebaixamento.doubleValue()/getNumeroSimulacoes(semana));
				cp.setQtdeRebaixamento(probabilidadeRebaixamento);
			}
			
			//Acesso
			if (nivelCampeonato.isNacionalII()) {
				Integer probabilidadeAcesso = 0;
				for (int i = 1; i <= numeroRebaixados; i++ ) {
					cpp = cp.getClubeProbabilidadePosicao().get(i);
					if (cpp != null) {
						probabilidadeAcesso += cpp.getProbabilidade();
					}
				}
				cp.setProbabilidadeAcesso(probabilidadeAcesso.doubleValue()/getNumeroSimulacoes(semana));
				cp.setQtdeAcesso(probabilidadeAcesso);
			}
			
			//CI
			Integer probabilidadeClassificacaoCI = 0;
			for (int i = getPosicoesClassificamCIMin(); i <= getPosicoesClassificamCIMax(); i++ ) {
				crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
				if (crpp != null) {
					probabilidadeClassificacaoCI += crpp.getProbabilidade();
				}
			}
			cp.setQtdeClassificacaoCI(probabilidadeClassificacaoCI);
			cp.setProbabilidadeClassificacaoCI(probabilidadeClassificacaoCI.doubleValue()/getNumeroSimulacoes(semana));

			//CII
			Integer probabilidadeClassificacaoCII = 0;
			for (int i = getPosicoesClassificamCIIMin(); i <= getPosicoesClassificamCIIMax(); i++ ) {
				crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
				if (crpp != null) {
					probabilidadeClassificacaoCII += crpp.getProbabilidade();
				}
			}
			cp.setQtdeClassificacaoCII(probabilidadeClassificacaoCII);
			cp.setProbabilidadeClassificacaoCII(probabilidadeClassificacaoCII.doubleValue()/getNumeroSimulacoes(semana));

			//CIII
			Integer probabilidadeClassificacaoCIII = 0;
			for (int i = getPosicoesClassificamCIIIMin(); i <= getPosicoesClassificamCIIIMax(); i++ ) {
				crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
				if (crpp != null) {
					probabilidadeClassificacaoCIII += crpp.getProbabilidade();
				}
			}
			cp.setQtdeClassificacaoCIII(probabilidadeClassificacaoCIII);
			cp.setProbabilidadeClassificacaoCIII(probabilidadeClassificacaoCIII.doubleValue()/getNumeroSimulacoes(semana));

			//CNI
			/*
			 * Quando a COPA NACIONAL tem 4 rodadas as estatísticas de classificação para CNI apresentam distorções
			 * devido aos campeões (CI, CII, CIII, CNI e CNII) poderem estar ou não na zona de rebaixamento (14, 15, 16).
			 *  
			 */
			Integer probabilidadeClassificacaoCNI = 0;
			for (int i = getPosicoesClassificamCNIMin(); i <= getPosicoesClassificamCNIMax(); i++ ) {
				crpp = cp.getClubeProbabilidadePosicaoGeral().get(i);
				if (crpp != null) {
					probabilidadeClassificacaoCNI += crpp.getProbabilidade();
				}
			}
			cp.setQtdeClassificacaoCNI(probabilidadeClassificacaoCNI);
			cp.setProbabilidadeClassificacaoCNI(probabilidadeClassificacaoCNI.doubleValue()/getNumeroSimulacoes(semana));

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

		if (nroCompeticoesContinentais != 3) {
			return -1;
		}

		if (cIIIReduzido) {
			return 10;
		}

		return 12;
	}
	
	private Integer getPosicoesClassificamCNIMin() { return 1; }

	/*private Integer getPosicoesClassificamCNIMax() {
		
		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadasCopaNacional)) {
			return 16;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//20
				return 20;
			} else if (nroCompeticoesContinentais == 2) {//24
				return 24;
			}
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 3) {//28
				return 28;
			} else if (nroCompeticoesContinentais == 2) {//32
				return 32;
			}
		}

		return -1;

	}*/
	
	private Integer getPosicoesClassificamCNIMax() {
		
		return parametroService.getNumeroTimesParticipantesCopaNacional();
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
				
				cpp.setPontuacaoMaxima(clasp.getPontos());
				cpp.setPontuacaoMedia(clasp.getPontos().doubleValue());
				cpp.setPontuacaoMinima(clasp.getPontos());
				
				clup.getClubeProbabilidadePosicao().put(clasp.getPosicao(), cpp);
			} else {
				
				if (clasp.getPontos() > cpp.getPontuacaoMaxima()) {
					cpp.setPontuacaoMaxima(clasp.getPontos());
				}
				
				if (clasp.getPontos() < cpp.getPontuacaoMinima()) {
					cpp.setPontuacaoMinima(clasp.getPontos());
				}
				
				double media = (cpp.getPontuacaoMedia() * cpp.getProbabilidade() + clasp.getPontos().byteValue())/(cpp.getProbabilidade() + 1);
				cpp.setPontuacaoMedia(media);
				
				cpp.setProbabilidade(cpp.getProbabilidade() + 1);
			}

		}
		
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
			
			r = rodadaEliminatoriaRepository.findFirstByCampeonatoEliminatorioAndNumero(c, c.getNivelCampeonato().isCopaNacional() ? numeroRodadas : 4).get();
			p = partidaEliminatoriaResultadoRepository.findByRodada(r);
			if (c.getNivelCampeonato().isCopaNacional()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC, p.get(0).getClubeVencedor());
			} else if (c.getNivelCampeonato().isCopaNacionalII()) {
				clubesCampeoes.put(ClubeRankingProbabilidadeUtil.CAMP_COPA_NAC_II, p.get(0).getClubeVencedor());
			}
		}
	}
	
	private void inicializarClubeProbabilidade(Map<Clube, ClubeProbabilidade> clubeProbabilidades,
			List<Clube> clubes, Semana semana, Campeonato campeonato) {
		if (clubeProbabilidades.isEmpty()) {
			for (Clube clube : clubes) {
				ClubeProbabilidade clup = new ClubeProbabilidade();
				
				clup.setClube(clube);
				clup.setCampeonato(campeonato);
				clup.setSemana(semana);

				clup.setClubeProbabilidadePosicao(new HashMap<Integer, ClubeProbabilidadePosicao>());
				
				clup.setClubeProbabilidadePosicaoGeral(new HashMap<Integer, ClubeRankingPosicaoProbabilidade>());
				
				clubeProbabilidades.put(clube, clup);
			}
		}
	}
	
	private void jogarRodada(Rodada r, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades) {

		for (PartidaResultado p : r.getPartidas()) {
			jogarPartida(p, classificacaoProbabilidades);
		}

	}

	private void jogarPartida(PartidaResultado p, Map<Clube, ClassificacaoProbabilidade> classificacaoProbabilidades) {

		ClassificacaoProbabilidade clasMandante = classificacaoProbabilidades.get(p.getClubeMandante());

		ClassificacaoProbabilidade clasVisitante = classificacaoProbabilidades.get(p.getClubeVisitante());

		double vitoriaMandante = clasMandante.getProbabilidadeVitoria() + clasVisitante.getProbabilidadeDerrota();

		double empate = clasMandante.getProbabilidadeEmpate() + clasVisitante.getProbabilidadeEmpate();

		//double vitoriaVisitante = clasMandante.getProbabilidadeDerrota() + clasVisitante.getProbabilidadeVitoria();

		double resultado = R.nextDouble() * 2;

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

		clasMandante.calcularProbabilidades();
		clasVisitante.calcularProbabilidades();
	}

	//TODO: transformar em Util
	public static void ordernarClassificacao(List<ClassificacaoProbabilidade> classificacao, boolean desempatar) throws RuntimeException {

		Collections.sort(classificacao, new Comparator<ClassificacaoProbabilidade>() {

			@Override
			public int compare(ClassificacaoProbabilidade o1, ClassificacaoProbabilidade o2) {
				return o1.compareTo(o2);
			}
		});
		
		//Setar posicao inicial
		for (int i = 0; i < classificacao.size(); i++) {
			if (i > 0 && classificacao.get(i-1).compareTo(classificacao.get(i)) == 0) {
				
				List<ClassificacaoProbabilidade> empatados = new ArrayList<ClassificacaoProbabilidade>();
				
				empatados.add(classificacao.get(i));
				
				empatados.add(classificacao.get(i - 1));
				
				int j = i - 1;
				
				while (j > 0 && classificacao.get(j - 1).compareTo(classificacao.get(j)) == 0) {
					empatados.add(classificacao.get(j - 1));
					j--;
				}
				
				//Se entrou aqui, o clube[i] está empatado com clube[i-1]
				//if (desempatar) {
				sortearPosicao(empatados, j + 1);
				//} else {
					//Para manter varios clubes com a mesma classificacao em Caso de empate
					//classificacao.get(i).setPosicao(classificacao.get(i-1).getPosicao()); 
				//}
			} else {
				classificacao.get(i).setPosicao(i+1);
			}
		}

	}

	private static void sortearPosicao(List<ClassificacaoProbabilidade> classificacao, Integer posInicial) {
		Collections.shuffle(classificacao);
		for (ClassificacaoProbabilidade c : classificacao) {
			c.setPosicao(posInicial++);
		}
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
