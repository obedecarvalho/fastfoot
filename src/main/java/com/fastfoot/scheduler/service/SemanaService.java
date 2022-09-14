package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.probability.service.CalcularProbabilidadeCompletoService;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.GrupoCampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaAmistoraRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.ParametroService;

/*
 * https://www.baeldung.com/apache-commons-collections-vs-guava
 * https://thorben-janssen.com/tips-to-boost-your-hibernate-performance/
 * https://thorben-janssen.com/criteria-updatedelete-easy-way-to/
 * 
 */

@Service
public class SemanaService {
	
	//#######	REPOSITORY	################

	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private SemanaRepository semanaRepository;

	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;

	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Autowired
	private GrupoCampeonatoRepository grupoCampeonatoRepository;
	
	@Autowired
	private ClassificacaoRepository classificacaoRepository;
	
	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;
	
	/*@Autowired
	private GrupoDesenvolvimentoJogadorRepository grupoDesenvolvimentoJogadorRepository;*/

	@Autowired
	private JogadorRepository jogadorRepository;

	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;
	
	//#####	SERVICE	############
	
	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private RodadaService rodadaService;

	@Autowired
	private ParametroService parametroService;

	/*@Autowired
	private DesenvolverJogadorService desenvolverJogadorService;*/
	
	@Autowired
	private CalcularProbabilidadeCompletoService calcularProbabilidadeCompletoService;
	
	/*@Autowired
	private AposentarJogadorService aposentarJogadorService;*/

	@Autowired
	private EscalarClubeService escalarClubeService;

	/*@Autowired
	private CalcularValorTransferenciaService calcularValorTransferenciaService;*/

	public SemanaDTO proximaSemana() {
		
		StopWatch stopWatch = new StopWatch();		
		stopWatch.start();
		List<String> mensagens = new ArrayList<String>();
		long inicio = 0, fim = 0;

		stopWatch.split();
		inicio = stopWatch.getSplitNanoTime();
		
		//Carregar dados
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#carregar:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();
		
		//Escalar clubes
		if (semana.getNumero() % 5 == 1) {
			escalarClubes(semana);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#escalarClubes:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		
		/*stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#escalarClubes:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();*/

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		List<RodadaAmistosa> rodadasAmistosas = rodadaAmistoraRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);
		semana.setRodadasAmistosas(rodadasAmistosas);
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#carregar2:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();

		//Jogar Rodada
		List<CompletableFuture<RodadaJogavel>> rodadasFuture = new ArrayList<CompletableFuture<RodadaJogavel>>();

		for (Rodada r : semana.getRodadas()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}

		for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}
		
		for (RodadaAmistosa r : semana.getRodadasAmistosas()) {
			rodadasFuture.add(rodadaService.executarRodada(r));
		}

		CompletableFuture.allOf(rodadasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#executarRodada:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();

		//Promover
		promover(semana);
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#promover:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();

		//Incrementar rodada atual
		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#incrementarRodadaAtualCampeonato:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();

		//calcular probabilidades
		if (semana.getNumero() >= 22 && semana.getNumero() <= 24) {
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		if (semana.getNumero() == 21 || semana.getNumero() == 19 || semana.getNumero() == 17) {
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		
		/*stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#calcularProbabilidades:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();*/

		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			temporadaService.classificarClubesTemporadaAtual();
			
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#classificarClubesTemporadaAtual:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		
		/*stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#classificarClubesTemporadaAtual:" + (fim - inicio));
		inicio = stopWatch.getSplitNanoTime();*/

		//Desenvolver jogadores
		//desenvolverJogadores(semana);
		if (semana.getNumero() % 5 == 0) {
			habilidadeValorRepository.desenvolverTodasHabilidades();
			jogadorRepository.calcularForcaGeral();

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#desenvolverTodasHabilidades:" + (fim - inicio));
		}

		/*stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#desenvolverTodasHabilidades:" + (fim - inicio));*/
		
		//Escalar clubes
		/*if (semana.getNumero() % 5 == 0) {
			escalarClubes(semana);
		}*/
		
		stopWatch.stop();
		mensagens.add("#tempoTotal:" + stopWatch.getNanoTime());
		//System.err.println(mensagens);
		
		//calcularValorTransferenciaJogadores(semana);
		
		//aposentarJogadores(semana);

		//System.err.println("\t\t-> " + semana.getNumero() + "-PORC DES JOG: " + new Double(stopWatch2.getNanoTime())/stopWatch.getNanoTime());

		return SemanaDTO.convertToDTO(semana);
	}

	//private static final Integer NUM_SPLITS_GRUPO_DESENVOLVIMENTO = FastfootApplication.NUM_THREAD; 
	
	/*private void desenvolverJogadores(Semana semana) {
		if (semana.getNumero() <= 25) {
			CelulaDesenvolvimento cd = CelulaDesenvolvimento.getAll()[semana.getNumero()
					% CelulaDesenvolvimento.getAll().length];
			
			List<GrupoDesenvolvimentoJogador> celulasDevJog = grupoDesenvolvimentoJogadorRepository.findByCelulaDesenvolvimentoAndAtivoFetchJogadorHabilidades(cd, Boolean.TRUE);
			
			List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
			
			int offset = celulasDevJog.size() / FastfootApplication.NUM_THREAD;
			
			//System.err.println("\t\t->Total: " + gds.size());
			
			for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
				if ((i + 1) == FastfootApplication.NUM_THREAD) {
					desenvolverJogadorFuture.add(desenvolverJogadorService.desenvolverGrupo(celulasDevJog.subList(i * offset, celulasDevJog.size())));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
				} else {
					desenvolverJogadorFuture.add(desenvolverJogadorService.desenvolverGrupo(celulasDevJog.subList(i * offset, (i+1) * offset)));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
				}
			}
			
			CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		}
	}*/
	
	/*private void aposentarJogadores(Semana semana) {

		if (semana.getNumero() == 25) {
			
			List<GrupoDesenvolvimentoJogador> gds = grupoDesenvolvimentoJogadorRepository.findByAtivoAndIdadeJogador(Boolean.TRUE, JogadorFactory.IDADE_MAX);
			
			List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
			
			int offset = gds.size() / NUM_SPLITS_GRUPO_DESENVOLVIMENTO;
			
			//System.err.println("\t\t->Total: " + gds.size());
			
			for (int i = 0; i < NUM_SPLITS_GRUPO_DESENVOLVIMENTO; i++) {
				if ((i + 1) == NUM_SPLITS_GRUPO_DESENVOLVIMENTO) {
					desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, gds.size())));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
				} else {
					desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, (i+1) * offset)));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
				}
			}
			
			CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		}
	}*/

	private void escalarClubes(Semana semana) {
		//if (semana.getNumero() % 5 == 0) {
		List<Clube> clubes = clubeRepository.findAll(); 
		
		escalacaoJogadorPosicaoRepository.desativarTodas();
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(escalarClubeService.escalarClubes(clubes.subList(i * offset, clubes.size())));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
			} else {
				desenvolverJogadorFuture.add(escalarClubeService.escalarClubes(clubes.subList(i * offset, (i+1) * offset)));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}
	
	/*private void calcularValorTransferenciaJogadores(Semana semana) {
		if (semana.getNumero() == 1) {
			
			List<Jogador> jogadores = jogadorRepository.findByAposentado(Boolean.FALSE);
			
			List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
			
			int offset = jogadores.size() / NUM_SPLITS_GRUPO_DESENVOLVIMENTO;
			
			for (int i = 0; i < NUM_SPLITS_GRUPO_DESENVOLVIMENTO; i++) {
				if ((i + 1) == NUM_SPLITS_GRUPO_DESENVOLVIMENTO) {
					desenvolverJogadorFuture.add(calcularValorTransferenciaService.calcularValorTransferencia(jogadores.subList(i * offset, jogadores.size())));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
				} else {
					desenvolverJogadorFuture.add(calcularValorTransferenciaService.calcularValorTransferencia(jogadores.subList(i * offset, (i+1) * offset)));
					//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
				}
			}
			
			CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		}
	}*/

	private void calcularProbabilidades(Semana semana, Temporada temporada) {
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(temporada);
		
		Map<Liga, Map<NivelCampeonato, List<Campeonato>>> campeonatosMap =
		campeonatos.stream().collect(Collectors.groupingBy(Campeonato::getLiga, Collectors.groupingBy(Campeonato::getNivelCampeonato)));
		
		List<CompletableFuture<Boolean>> calculoProbabilidadesFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (Liga l : Liga.getAll()) {
			calculoProbabilidadesFuture.add(calcularProbabilidadeCompletoService.calcularProbabilidadesCampeonato(
					semana, campeonatosMap.get(l).get(NivelCampeonato.NACIONAL).get(0),
					campeonatosMap.get(l).get(NivelCampeonato.NACIONAL_II).get(0)));
		}

		CompletableFuture.allOf(calculoProbabilidadesFuture.toArray(new CompletableFuture<?>[0])).join();
	}

	private void incrementarRodadaAtualCampeonato(List<Rodada> rodadas, List<RodadaEliminatoria> rodadaEliminatorias) {//TODO: avaliar necessidade
		Set<Campeonato> camps1 = new HashSet<Campeonato>();
		Set<CampeonatoEliminatorio> camps2 = new HashSet<CampeonatoEliminatorio>();
		Set<CampeonatoMisto> camps3 = new HashSet<CampeonatoMisto>();

		camps1.addAll(rodadas.stream().filter(r -> r.getCampeonato() != null).map(r -> r.getCampeonato()).collect(Collectors.toSet()));
		camps2.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoEliminatorio() != null).map(r -> r.getCampeonatoEliminatorio()).collect(Collectors.toSet()));
		camps3.addAll(rodadas.stream().filter(r -> r.getGrupoCampeonato() != null).map(r -> r.getGrupoCampeonato().getCampeonato()).collect(Collectors.toSet()));
		camps3.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoMisto() != null).map(r -> r.getCampeonatoMisto()).collect(Collectors.toSet()));

		for (Campeonato c : camps1) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		for (CampeonatoEliminatorio c : camps2) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		for (CampeonatoMisto c : camps3) {
			c.setRodadaAtual(c.getRodadaAtual() + 1);
		}

		campeonatoRepository.saveAllAndFlush(camps1);
		campeonatoEliminatorioRepository.saveAllAndFlush(camps2);
		campeonatoMistoRepository.saveAllAndFlush(camps3);
	}

	private void promover(Semana semana) {
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CONTINENTAL) {

			//String estrategiaPromotorCont = parametroService.getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);

			Set<CampeonatoMisto> campeonatosMisto = semana.getRodadas().stream()
					.map(r -> r.getGrupoCampeonato().getCampeonato()).collect(Collectors.toSet());

			for (CampeonatoMisto c : campeonatosMisto) {
				c.setGrupos(grupoCampeonatoRepository.findByCampeonato(c));
				for (GrupoCampeonato g : c.getGrupos()) {
					g.setClassificacao(classificacaoRepository.findByGrupoCampeonato(g));
				}
				c.setPrimeiraRodadaEliminatoria(rodadaEliminatoriaRepository
						.findFirstByCampeonatoMistoAndNumero(c, Constantes.NRO_PARTIDAS_FASE_GRUPOS + 1).get());
				c.getPrimeiraRodadaEliminatoria()
						.setPartidas(partidaEliminatoriaRepository.findByRodada(c.getPrimeiraRodadaEliminatoria()));
			}

			//getPromotorContinental(estrategiaPromotorCont).promover(campeonatosMisto);
			getPromotorContinental().promover(campeonatosMisto);
			for (CampeonatoMisto c : campeonatosMisto) {
				partidaEliminatoriaRepository.saveAll(c.getPrimeiraRodadaEliminatoria().getPartidas());
			}
		}
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CNII) {

			//String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
			Boolean jogarCopaNacII = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_II);
			//Integer nroCompeticoes = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
			
			if (jogarCopaNacII) {
				List<CampeonatoEliminatorio> campeonatos = semana.getRodadasEliminatorias().stream()
						.filter(r -> r.getCampeonatoEliminatorio() != null
								&& NivelCampeonato.COPA_NACIONAL.equals(r.getCampeonatoEliminatorio().getNivelCampeonato()))
						.map(RodadaEliminatoria::getCampeonatoEliminatorio)
						.collect(Collectors.toList());
				
				RodadaEliminatoria rodadaCNII = null, rodada1Fase, rodada2Fase;
				
				for (CampeonatoEliminatorio c : campeonatos) {
					CampeonatoEliminatorio copaNacionalII = campeonatoEliminatorioRepository
							.findFirstByTemporadaAndLigaAndNivelCampeonato(c.getTemporada(), c.getLiga(), NivelCampeonato.COPA_NACIONAL_II).get();
					
					rodadaCNII = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(copaNacionalII, 1).get(0);
					rodadaCNII.setPartidas(partidaEliminatoriaRepository.findByRodada(rodadaCNII));
					
					rodada1Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 1).get(0);
					rodada1Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada1Fase));
	
					rodada2Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 2).get(0);
					rodada2Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada2Fase));
	
					//PromotorEliminatoria promotorEliminatoria = getPromotorEliminatoria(numeroRodadasCopaNacional, nroCompeticoes);
					PromotorEliminatoria promotorEliminatoria = getPromotorEliminatoria();
					promotorEliminatoria.classificarCopaNacionalII(rodadaCNII, rodada1Fase, rodada2Fase);
					
					partidaEliminatoriaRepository.saveAll(rodadaCNII.getPartidas());
				}
			}
		}

	}
	
	private PromotorContinental getPromotorContinental() {
		//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
		PromotorContinental promotorContinental = null;

		if (parametroService.isEstrategiaPromotorContinentalSegundoMelhorGrupo()) {
			promotorContinental = new PromotorContinentalImplDoisPorGrupo();
		} else if (parametroService.isEstrategiaPromotorContinentalMelhorEliminado()) {	
			promotorContinental = new PromotorContinentalImplInterCampeonatos();
		}

		return promotorContinental;
	}

	private PromotorEliminatoria getPromotorEliminatoria() {
		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Integer numRodadas = parametroService.getNumeroRodadasCopaNacional();
		Boolean cIIIReduzido = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		
		PromotorEliminatoria promotorEliminatoria = null;
		
		if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					promotorEliminatoria = new PromotorEliminatoriaImplTrintaClubes();
				} else {
					promotorEliminatoria = new PromotorEliminatoriaImplVinteEOitoClubes();
				}
			} else if (nroCompeticoesContinentais == 2) {
				promotorEliminatoria = new PromotorEliminatoriaImplTrintaEDoisClubes();
			}
		} else if (numRodadas == 5) {
			if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					promotorEliminatoria = new PromotorEliminatoriaImplVinteEDoisClubes();
				} else {
					promotorEliminatoria = new PromotorEliminatoriaImplVinteClubes();
				}
			} else if (nroCompeticoesContinentais == 2) {
				promotorEliminatoria = new PromotorEliminatoriaImplVinteEQuatroClubes();
			}
		}
		
		return promotorEliminatoria;
	}

	public Semana getSemanaAtual() {
		Optional<Semana> s = semanaRepository.findSemanaAtual();
		return s.isPresent() ? s.get() : null;
	}
}
