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
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService;
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoSimularPartidaService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.service.GerarClubeResumoTemporadaService;
import com.fastfoot.financial.service.CalcularJurosBancariosService;
import com.fastfoot.financial.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.financial.service.GerarDemonstrativoFinanceiroTemporadaService;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.CalcularHabilidadeGrupoValorService;
import com.fastfoot.player.service.DesenvolverJogadorService;
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
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;

	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;
	
	//#####	SERVICE	############
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;

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
	private CalcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService;
	
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
	
	@Autowired
	private CalcularJurosBancariosService calcularJurosBancariosService;
	
	@Autowired
	private DesenvolverJogadorService desenvolverJogadorService;

	@Autowired
	private GerarDemonstrativoFinanceiroTemporadaService gerarDemonstrativoFinanceiroTemporadaService;

	public SemanaDTO jogarPartidasSemana() {
		
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		//Carregar dados
		Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();
		
		/*stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#carregar:" + (fim - inicio));

		inicio = stopWatch.getSplitTime();*/
		
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
		mensagens.add("\t#carregar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

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
		mensagens.add("\t#executarRodada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Promover
		promover(semana);
		stopWatch.split();
		mensagens.add("\t#promover:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Marcar amistosos
		marcarAmistososSemanalmente(semana);
		stopWatch.split();
		mensagens.add("\t#marcarAmistososSemanalmente:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Incrementar rodada atual
		incrementarRodadaAtualCampeonato(rodadas, rodadaEliminatorias);
		stopWatch.split();
		mensagens.add("\t#incrementarRodadaAtualCampeonato:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//calcular probabilidades
		if (semana.getNumero() >= 22 && semana.getNumero() <= 24
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {
			calcularProbabilidades(semana, temporada);
		}
		if ((semana.getNumero() == 21 || semana.getNumero() == 19 || semana.getNumero() == 17)
				&& parametroService.getParametroBoolean(ParametroConstantes.CALCULAR_PROBABILIDADES)) {//TODO: criar parametro ...PROBABILIDADE_COMPLETOS??
			calcularProbabilidades(semana, temporada);
		}
		stopWatch.split();
		mensagens.add("\t#calcularProbabilidades:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			classificarClubesTemporadaService.classificarClubesTemporadaAtual();
		}
		stopWatch.split();
		mensagens.add("\t#classificarClubesTemporadaAtual:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		if (semana.isUltimaSemana()) {
			gerarClubeResumoTemporada(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#gerarClubeResumoTemporada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Desenvolver jogadores
		if (semana.getNumero() % 5 == 0) {
			habilidadeValorRepository.desenvolverTodasHabilidades2();
			jogadorRepository.calcularForcaGeral2();
		}
		stopWatch.split();
		mensagens.add("\t#desenvolverTodasHabilidades:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		if (semana.getNumero() % 5 == 0) {
			calcularHabilidadeGrupoValor3();
		}
		stopWatch.split();
		mensagens.add("\t#calcularHabilidadeGrupoValor:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Bets
		calcularPartidaProbabilidadeResultado(temporada);
		stopWatch.split();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		distribuirPremiacaoCompeticoesService.distribuirPremiacaoCompeticoes(semana);
		stopWatch.split();
		mensagens.add("\t#distribuirPremiacaoCompeticoes:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//pagarSalarioJogadoresService.pagarSalarioJogadores(semana);
		pagarSalarioJogadoresService.pagarSalarioJogadoresPorContrato(semana);
		stopWatch.split();
		mensagens.add("\t#pagarSalarioJogadores:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//TODO: recuperação especifica por idade jogador
		jogadorEnergiaRepository.inserirRecuperacaoEnergiaTodosJogadores(Constantes.ENERGIA_INICIAL, Constantes.REPOSICAO_ENERGIA_SEMANAL);
		stopWatch.split();
		mensagens.add("\t#recuperarEnergia:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		calcularJurosBancariosService.calcularJurosBancarios(semana);
		stopWatch.split();
		mensagens.add("\t#calcularJurosBancarios:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		if (semana.getNumero() == Constantes.NUM_SEMANAS) {
			gerarDemonstrativoFinanceiroTemporadaService.gerarDemonstrativoFinanceiroTemporada(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#gerarDemonstrativoFinanceiroTemporada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
	
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
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService.simularPartidas(r));
			}
	
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				simularPartidasFuture.add(calcularPartidaProbabilidadeResultadoSimularPartidaHabilidadeGrupoService.simularPartidas(r));
			}
			
			CompletableFuture.allOf(simularPartidasFuture.toArray(new CompletableFuture<?>[0])).join();

		}
	}

	//private static final Integer NUM_SPLITS_GRUPO_DESENVOLVIMENTO = FastfootApplication.NUM_THREAD; 
	
	private void desenvolverJogadores() {

		List<Jogador> jogadores = jogadorRepository.findByStatusJogadorFetchHabilidades(StatusJogador.ATIVO);

		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(desenvolverJogadorService.desenvolverJogador(jogadores.subList(i * offset, jogadores.size())));
			} else {
				desenvolverJogadorFuture.add(desenvolverJogadorService.desenvolverJogador(jogadores.subList(i * offset, (i+1) * offset)));
			}
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();

	}
	
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
	
	private void calcularHabilidadeGrupoValor2() {
		for (HabilidadeGrupo hg : HabilidadeGrupo.getAll()) {
			habilidadeGrupoValorRepository.calcular(hg.ordinal(), hg.getHabilidadesOrdinal());
		}
	}
	
	private void calcularHabilidadeGrupoValor3() {

		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (HabilidadeGrupo hg : HabilidadeGrupo.getAll()) {
			desenvolverJogadorFuture.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(hg));
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}
	
	private void calcularHabilidadeGrupoValor() {

		habilidadeGrupoValorRepository.deleteAllInBatch();
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		for (Liga liga : Liga.getAll()) {
			desenvolverJogadorFuture
					.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(liga, true));
			desenvolverJogadorFuture
					.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(liga, false));
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}
	
	private void calcularProbabilidades(Semana semana, Temporada temporada) {
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(temporada);
		
		Map<Liga, Map<NivelCampeonato, List<Campeonato>>> campeonatosMap = campeonatos.stream().collect(
				Collectors.groupingBy(Campeonato::getLiga, Collectors.groupingBy(Campeonato::getNivelCampeonato)));
		
		List<CompletableFuture<Boolean>> calculoProbabilidadesFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (Liga l : Liga.getAll()) {
			calculoProbabilidadesFuture.add(calcularProbabilidadeCompletoService.calcularProbabilidadesCampeonato(
					semana, campeonatosMap.get(l).get(NivelCampeonato.NACIONAL).get(0),
					campeonatosMap.get(l).get(NivelCampeonato.NACIONAL_II).get(0)));
		}

		CompletableFuture.allOf(calculoProbabilidadesFuture.toArray(new CompletableFuture<?>[0])).join();
	}

	private void incrementarRodadaAtualCampeonato(List<Rodada> rodadas, List<RodadaEliminatoria> rodadaEliminatorias, boolean old) {

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
