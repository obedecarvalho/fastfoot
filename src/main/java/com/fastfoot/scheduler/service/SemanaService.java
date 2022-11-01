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
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.match.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.PagarSalarioJogadoresService;
import com.fastfoot.probability.service.CalcularProbabilidadeService;
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
	private CalcularProbabilidadeService calcularProbabilidadeCompletoService;

	@Autowired
	private EscalarClubeService escalarClubeService;
	
	@Autowired
	private CalcularPartidaProbabilidadeResultadoService calcularPartidaProbabilidadeResultadoService;
	
	@Autowired
	private DistribuirPremiacaoCompeticoesService distribuirPremiacaoCompeticoesService;
	
	@Autowired
	private PagarSalarioJogadoresService pagarSalarioJogadoresService;

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
		mensagens.add("\t#carregar:" + (fim - inicio));

		inicio = stopWatch.getSplitNanoTime();
		
		//Escalar clubes
		/*if (semana.getNumero() % 5 == 1) {
			escalarClubes(semana);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("#escalarClubes:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}*/

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		List<RodadaAmistosa> rodadasAmistosas = rodadaAmistoraRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);
		semana.setRodadasAmistosas(rodadasAmistosas);
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#carregar2:" + (fim - inicio));

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
		mensagens.add("\t#executarRodada:" + (fim - inicio));

		inicio = stopWatch.getSplitNanoTime();
		//Promover
		promover(semana);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#promover:" + (fim - inicio));

		inicio = stopWatch.getSplitNanoTime();
		//Incrementar rodada atual
		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#incrementarRodadaAtualCampeonato:" + (fim - inicio));

		inicio = stopWatch.getSplitNanoTime();

		//calcular probabilidades
		if (semana.getNumero() >= 22 && semana.getNumero() <= 24
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		if ((semana.getNumero() == 21 || semana.getNumero() == 19 || semana.getNumero() == 17)
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {//TODO: criar parametro ...PROBABILIDADE_COMPLETOS??
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}

		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			temporadaService.classificarClubesTemporadaAtual();
			
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#classificarClubesTemporadaAtual:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}

		//Desenvolver jogadores
		//desenvolverJogadores(semana);
		if (semana.getNumero() % 5 == 0) {
			habilidadeValorRepository.desenvolverTodasHabilidades();
			jogadorRepository.calcularForcaGeral();

			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#desenvolverTodasHabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitNanoTime();
		}
		
		//Bets
		calcularPartidaProbabilidadeResultado(temporada);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (fim - inicio));
		
		inicio = stopWatch.getSplitNanoTime();
		distribuirPremiacaoCompeticoesService.distribuirPremiacaoCompeticoes(semana);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#distribuirPremiacaoCompeticoes:" + (fim - inicio));
		
		inicio = stopWatch.getSplitNanoTime();
		pagarSalarioJogadoresService.pagarSalarioJogadores(semana);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#pagarSalarioJogadores:" + (fim - inicio));

		//inicio = stopWatch.getSplitNanoTime();

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getNanoTime());
		System.err.println(mensagens);

		return SemanaDTO.convertToDTO(semana);
	}
	
	private void calcularPartidaProbabilidadeResultado(Temporada temporada) {
		
		if (parametroService.getParametroBoolean(ParametroConstantes.USAR_APOSTAS_ESPORTIVAS)) {
		
			Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual() + 1);
			
			if (!semanaOpt.isPresent()) return;
			
			Semana semana = semanaOpt.get();
			
			List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
			List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
			
			semana.setRodadas(rodadas);
			semana.setRodadasEliminatorias(rodadaEliminatorias);
			
			List<CompletableFuture<Boolean>> simularPartidasFuture = new ArrayList<CompletableFuture<Boolean>>();
	
			for (Rodada r : semana.getRodadas()) {
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoService.simularPartidas(r));
			}
	
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoService.simularPartidas(r));
			}
			
			CompletableFuture.allOf(simularPartidasFuture.toArray(new CompletableFuture<?>[0])).join();

		}
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
	
	@SuppressWarnings("unused")
	private void escalarClubes(Semana semana) {
		//if (semana.getNumero() % 5 == 0) {
		List<Clube> clubes = clubeRepository.findAll(); 
		
		escalacaoJogadorPosicaoRepository.desativarTodas();//TODO: transformar em delete
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(escalarClubeService.escalarClubes(clubes.subList(i * offset, clubes.size())));
			} else {
				desenvolverJogadorFuture.add(escalarClubeService.escalarClubes(clubes.subList(i * offset, (i+1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}
	
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
		Boolean jogarCNCompleta = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA);
		//TODO: parametroService.isEstrategiaPromotorContinentalMelhorEliminado()
		
		PromotorEliminatoria promotorEliminatoria = null;
		
		if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					if (jogarCNCompleta) {
						promotorEliminatoria = new PromotorEliminatoriaImplTrintaEDoisClubesII();
					} else {
						promotorEliminatoria = new PromotorEliminatoriaImplTrintaClubes();
					}
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
	
	public Semana getProximaSemana() {
		Optional<Semana> s = semanaRepository.findProximaSemana();
		return s.isPresent() ? s.get() : null;
	}
}
