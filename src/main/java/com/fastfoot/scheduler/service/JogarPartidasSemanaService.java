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
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoSimularPartidaService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.service.GerarClubeResumoTemporadaService;
import com.fastfoot.financial.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.PagarSalarioJogadoresService;
import com.fastfoot.probability.service.CalcularProbabilidadeEstatisticasSimplesService;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.dto.SemanaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
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
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.scheduler.service.util.PromotorContinental;
import com.fastfoot.scheduler.service.util.PromotorContinentalImplDoisPorGrupo;
import com.fastfoot.scheduler.service.util.PromotorContinentalImplInterCampeonatos;
import com.fastfoot.scheduler.service.util.PromotorEliminatoria;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplTrintaClubes;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplTrintaEDoisClubes;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplTrintaEDoisClubesII;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplVinteClubes;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplVinteEDoisClubes;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplVinteEOitoClubes;
import com.fastfoot.scheduler.service.util.PromotorEliminatoriaImplVinteEQuatroClubes;
import com.fastfoot.service.CarregarParametroService;

/*
 * https://www.baeldung.com/apache-commons-collections-vs-guava
 * https://thorben-janssen.com/tips-to-boost-your-hibernate-performance/
 * https://thorben-janssen.com/criteria-updatedelete-easy-way-to/
 * 
 */

@Service
public class JogarPartidasSemanaService {
	
	//#######	REPOSITORY	################

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
	private JogarRodadaService jogarRodadaService;

	@Autowired
	private CarregarParametroService parametroService;
	
	@Autowired
	private CalcularProbabilidadeEstatisticasSimplesService calcularProbabilidadeCompletoService;

	@Autowired
	private EscalarClubeService escalarClubeService;
	
	@Autowired
	private CalcularPartidaProbabilidadeResultadoSimularPartidaService calcularPartidaProbabilidadeResultadoSimularPartidaService;//TODO: avaliar quais dos metodos implementados deve ser usado
	
	@Autowired
	private DistribuirPremiacaoCompeticoesService distribuirPremiacaoCompeticoesService;
	
	@Autowired
	private PagarSalarioJogadoresService pagarSalarioJogadoresService;
	
	@Autowired
	private MarcarAmistososSemanalmenteService marcarAmistososSemanalmenteService;
	
	@Autowired
	private GerarClubeResumoTemporadaService gerarClubeResumoTemporadaService;
	
	@Autowired
	private CarregarCampeonatoService campeonatoService;
	
	@Autowired
	private ClassificarClubesTemporadaService classificarClubesTemporadaService;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

	public SemanaDTO jogarPartidasSemana() {
		
		StopWatch stopWatch = new StopWatch();		
		stopWatch.start();
		List<String> mensagens = new ArrayList<String>();
		long inicio = 0, fim = 0;

		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		//Carregar dados
		//Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregar:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();
		
		//Escalar clubes
		/*if (semana.getNumero() % 5 == 1) {
			escalarClubes(semana);

			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("#escalarClubes:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
		}*/

		List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
		List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
		List<RodadaAmistosa> rodadasAmistosas = rodadaAmistoraRepository.findBySemana(semana);
		
		semana.setRodadas(rodadas);
		semana.setRodadasEliminatorias(rodadaEliminatorias);
		semana.setRodadasAmistosas(rodadasAmistosas);
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregar2:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();

		//Jogar Rodada
		List<CompletableFuture<RodadaJogavel>> rodadasFuture = new ArrayList<CompletableFuture<RodadaJogavel>>();

		for (Rodada r : semana.getRodadas()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r));
		}

		for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r));
		}
		
		for (RodadaAmistosa r : semana.getRodadasAmistosas()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r));
		}

		CompletableFuture.allOf(rodadasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#executarRodada:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();
		//Promover
		promover(semana);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#promover:" + (fim - inicio));
		
		inicio = stopWatch.getSplitTime();
		//Promover
		marcarAmistososSemanalmente(semana);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#marcarAmistososSemanalmente:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();
		//Incrementar rodada atual
		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#incrementarRodadaAtualCampeonato:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();

		//calcular probabilidades
		if (semana.getNumero() >= 22 && semana.getNumero() <= 24
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
		}
		if ((semana.getNumero() == 21 || semana.getNumero() == 19 || semana.getNumero() == 17)
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {//TODO: criar parametro ...PROBABILIDADE_COMPLETOS??
			calcularProbabilidades(semana, temporada);

			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#calcularProbabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
		}

		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			//temporadaService.classificarClubesTemporadaAtual();
			classificarClubesTemporadaService.classificarClubesTemporadaAtual();
			
			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#classificarClubesTemporadaAtual:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
			
			gerarClubeResumoTemporada(temporada);
			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#gerarClubeResumoTemporada:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
		}

		//Desenvolver jogadores
		//desenvolverJogadores(semana);
		if (semana.getNumero() % 5 == 0) {
			habilidadeValorRepository.desenvolverTodasHabilidades();
			jogadorRepository.calcularForcaGeral();

			stopWatch.split();
			fim = stopWatch.getSplitTime();
			mensagens.add("\t#desenvolverTodasHabilidades:" + (fim - inicio));
			inicio = stopWatch.getSplitTime();
		}
		
		//Bets
		calcularPartidaProbabilidadeResultado(temporada);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (fim - inicio));
		
		inicio = stopWatch.getSplitTime();
		distribuirPremiacaoCompeticoesService.distribuirPremiacaoCompeticoes(semana);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#distribuirPremiacaoCompeticoes:" + (fim - inicio));
		
		inicio = stopWatch.getSplitTime();
		pagarSalarioJogadoresService.pagarSalarioJogadores(semana);
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#pagarSalarioJogadores:" + (fim - inicio));

		//inicio = stopWatch.getSplitTime();

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
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
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoSimularPartidaService.simularPartidas(r));
			}
	
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoSimularPartidaService.simularPartidas(r));
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

	@SuppressWarnings("unused")
	private void incrementarRodadaAtualCampeonato(List<Rodada> rodadas, List<RodadaEliminatoria> rodadaEliminatorias, boolean old) {
		//TODO: avaliar necessidade
		//TODO: transformar em UPDATE
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
	
	private void incrementarRodadaAtualCampeonato(List<Rodada> rodadas, List<RodadaEliminatoria> rodadaEliminatorias) {
		//TODO: avaliar necessidade
		Set<Long> camps1 = new HashSet<Long>();
		Set<Long> camps2 = new HashSet<Long>();
		Set<Long> camps3 = new HashSet<Long>();

		camps1.addAll(rodadas.stream().filter(r -> r.getCampeonato() != null).map(r -> r.getCampeonato().getId())
				.collect(Collectors.toSet()));
		camps2.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoEliminatorio() != null)
				.map(r -> r.getCampeonatoEliminatorio().getId()).collect(Collectors.toSet()));
		camps3.addAll(rodadas.stream().filter(r -> r.getGrupoCampeonato() != null)
				.map(r -> r.getGrupoCampeonato().getCampeonato().getId()).collect(Collectors.toSet()));
		camps3.addAll(rodadaEliminatorias.stream().filter(r -> r.getCampeonatoMisto() != null)
				.map(r -> r.getCampeonatoMisto().getId()).collect(Collectors.toSet()));

		campeonatoRepository.incrementarRodadaAtual(camps1);
		campeonatoEliminatorioRepository.incrementarRodadaAtual(camps2);
		campeonatoMistoRepository.incrementarRodadaAtual(camps3);
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
			/*for (CampeonatoMisto c : campeonatosMisto) {
				partidaEliminatoriaRepository.saveAll(c.getPrimeiraRodadaEliminatoria().getPartidas());
			}*/
			partidaEliminatoriaRepository.saveAll(
					campeonatosMisto.stream().flatMap(c -> c.getPrimeiraRodadaEliminatoria().getPartidas().stream())
							.collect(Collectors.toList()));
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
				List<PartidaEliminatoriaResultado> partidasSalvar = new ArrayList<PartidaEliminatoriaResultado>();
				
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
					
					//partidaEliminatoriaRepository.saveAll(rodadaCNII.getPartidas());
					partidasSalvar.addAll(rodadaCNII.getPartidas());
				}

				partidaEliminatoriaRepository.saveAll(partidasSalvar);
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

	/*public Semana getSemanaAtual() {
		Optional<Semana> s = semanaRepository.findSemanaAtual();
		return s.isPresent() ? s.get() : null;
	}
	
	public Semana getProximaSemana() {
		Optional<Semana> s = semanaRepository.findProximaSemana();
		return s.isPresent() ? s.get() : null;
	}*/
	
	private void marcarAmistososSemanalmente(Semana semana) {
		
		boolean marcarAmistosos = parametroService.isMarcarAmistososAutomaticamenteSemanaASemana()
				|| parametroService.isMarcarAmistososAutomaticamenteInicioTemporadaESemanaASemana();
		
		//Jogos amistosos aconteceram nas semanas de 10 a 22
		//a marcação acontece duas semanas antes
		if (marcarAmistosos && semana.getNumero() >= 8 && semana.getNumero() <= 20 && semana.getNumero() % 2 == 0) {
			
			Optional<Semana> semanaAmistosoOpt = semanaRepository.findFirstByTemporadaAndNumero(semana.getTemporada(),
					semana.getNumero() + 2);
			
			if (semanaAmistosoOpt.isPresent()) {
				marcarAmistososSemanalmenteService.marcarAmistososSemanalmente(semanaAmistosoOpt.get());
			}
		}
	}
	
	private void gerarClubeResumoTemporada(Temporada temporada) {
		campeonatoService.carregarCampeonatosTemporada(temporada);
		gerarClubeResumoTemporadaService.gerarClubeResumoTemporada(temporada);
	}
}
