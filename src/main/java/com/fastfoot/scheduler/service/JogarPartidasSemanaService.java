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
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoFacadeService;
import com.fastfoot.club.service.AtualizarTreinadorTituloRankingService;
import com.fastfoot.club.service.ClassificarClubesTemporadaService;
import com.fastfoot.club.service.GerarClubeResumoTemporadaService;
import com.fastfoot.club.service.GerarTreinadorResumoTemporadaService;
import com.fastfoot.financial.service.CalcularJurosBancariosService;
import com.fastfoot.financial.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.financial.service.GerarDemonstrativoFinanceiroTemporadaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.CalcularHabilidadeGrupoValorService;
import com.fastfoot.player.service.DesenvolverJogadorService;
import com.fastfoot.player.service.PagarSalarioJogadoresService;
import com.fastfoot.probability.service.CalcularProbabilidadeEstatisticasSimplesService;
import com.fastfoot.probability.service.CalcularProbabilidadeSimularPartidaService;
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
import com.fastfoot.service.LigaJogoCRUDService;

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
	private HabilidadeValorRepository habilidadeValorRepository;

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
	private CarregarParametroService carregarParametroService;
	
	@Autowired
	private CalcularProbabilidadeEstatisticasSimplesService calcularProbabilidadeEstatisticasSimplesService;

	@Autowired
	private CalcularProbabilidadeSimularPartidaService calcularProbabilidadeSimularPartidaService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoFacadeService calcularPartidaProbabilidadeResultadoFacadeService;

	@Autowired
	private DistribuirPremiacaoCompeticoesService distribuirPremiacaoCompeticoesService;
	
	@Autowired
	private PagarSalarioJogadoresService pagarSalarioJogadoresService;
	
	@Autowired
	private MarcarAmistososSemanalmenteService marcarAmistososSemanalmenteService;
	
	@Autowired
	private GerarClubeResumoTemporadaService gerarClubeResumoTemporadaService;
	
	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;
	
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
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Autowired
	private GerarTreinadorResumoTemporadaService gerarTreinadorResumoTemporadaService;

	@Autowired
	private AtualizarTreinadorTituloRankingService atualizarTreinadorTituloRankingService;

	public SemanaDTO jogarPartidasSemana(Jogo jogo) {
		
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		//Carregar dados
		Temporada temporada = temporadaCRUDService.getTemporadaAtual(jogo);
		temporada.setSemanaAtual(temporada.getSemanaAtual() + 1);
		Semana semana = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual()).get();

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
		
		boolean agrupado = false;

		for (Rodada r : semana.getRodadas()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r, agrupado));
		}

		for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r, agrupado));
		}
		
		for (RodadaAmistosa r : semana.getRodadasAmistosas()) {
			rodadasFuture.add(jogarRodadaService.executarRodada(r, agrupado));
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
				&& carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.CALCULAR_PROBABILIDADES)) {
			calcularProbabilidades(semana, temporada);
		}
		if ((semana.getNumero() == 21 || semana.getNumero() == 19 || semana.getNumero() == 17)
				&& carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.CALCULAR_PROBABILIDADES)) {//TODO: criar parametro ...PROBABILIDADE_COMPLETOS??
			calcularProbabilidades(semana, temporada);
		}
		stopWatch.split();
		mensagens.add("\t#calcularProbabilidades:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		if (semana.isUltimaSemana()) {
			carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#carregarCampeonatosTemporada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			classificarClubesTemporadaService.classificarClubesTemporadaAtual(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#classificarClubesTemporadaAtual:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		//gerar ClubeRanking
		if (semana.isUltimaSemana()) {
			atualizarTreinadorTituloRankingService.atualizarTreinadorTituloRanking(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#atualizarTreinadorTituloRanking:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		if (semana.isUltimaSemana()) {
			gerarClubeResumoTemporada(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#gerarClubeResumoTemporada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		if (semana.isUltimaSemana()) {
			gerarTreinadorResumoTemporadaService.gerarTreinadorResumoTemporada(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#gerarTreinadorResumoTemporada:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Desenvolver jogadores
		if (semana.getNumero() % 5 == 0) {//TODO: mudar pra não usar na semana 25
			habilidadeValorRepository.desenvolverTodasHabilidades(jogo.getId());//TODO: criar service
			jogadorRepository.calcularForcaGeral(jogo.getId());
		}
		stopWatch.split();
		mensagens.add("\t#desenvolverTodasHabilidades:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		if (semana.getNumero() % 5 == 0) {//TODO: mudar pra não usar na semana 25
			calcularHabilidadeGrupoValor3(jogo);
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

		//jogadorEnergiaRepository.inserirRecuperacaoEnergiaTodosJogadores(Constantes.ENERGIA_INICIAL, Constantes.REPOSICAO_ENERGIA_SEMANAL);
		recuperarEnergiaJogadores(jogo);
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
	
		System.err.print(String.format("\t%02d\t", semana.getNumero()));
		System.err.println(mensagens);

		return SemanaDTO.convertToDTO(semana);
	}
	
	private void recuperarEnergiaJogadores(Jogo jogo) {
		jogadorEnergiaRepository.inserirRecuperacaoEnergiaJogadores(Constantes.ENERGIA_INICIAL,
				Constantes.RECUPERACAO_ENERGIA_SEMANAL_ATE_23, JogadorFactory.IDADE_MIN,
				Constantes.IDADE_MAX_JOGADOR_JOVEM, jogo.getId());
		jogadorEnergiaRepository.inserirRecuperacaoEnergiaJogadores(Constantes.ENERGIA_INICIAL,
				Constantes.RECUPERACAO_ENERGIA_SEMANAL_24_A_32, Constantes.IDADE_MAX_JOGADOR_JOVEM + 1, 32,
				jogo.getId());
		jogadorEnergiaRepository.inserirRecuperacaoEnergiaJogadores(Constantes.ENERGIA_INICIAL,
				Constantes.RECUPERACAO_ENERGIA_SEMANAL_APOS_33, 33, JogadorFactory.IDADE_MAX, jogo.getId());
	}
	
	private void calcularPartidaProbabilidadeResultado(Temporada temporada) {
		
		if (carregarParametroService.getParametroBoolean(temporada.getJogo(), ParametroConstantes.USAR_APOSTAS_ESPORTIVAS)
				&& temporada.getSemanaAtual() > 1) {//TODO: não está calculado probabilidade semana 1 e 2
		
			Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual() + 1);
			
			if (!semanaOpt.isPresent()) return;
			
			Semana semana = semanaOpt.get();
			
			List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
			List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
			
			semana.setRodadas(rodadas);
			semana.setRodadasEliminatorias(rodadaEliminatorias);
			
			List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
	
			for (Rodada r : semana.getRodadas()) {
				completableFutures.add(calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(r,
						carregarParametroService.getTipoCampeonatoClubeProbabilidade(temporada.getJogo())));
			}
	
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				completableFutures.add(calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(r,
						carregarParametroService.getTipoCampeonatoClubeProbabilidade(temporada.getJogo())));
			}
			
			CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

		}
	}

	private void desenvolverJogadores(Jogo jogo) {

		List<Jogador> jogadores = jogadorRepository.findByJogoAndStatusJogadorFetchHabilidades(jogo, StatusJogador.ATIVO);

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				completableFutures.add(desenvolverJogadorService.desenvolverJogador(jogadores.subList(i * offset, jogadores.size())));
			} else {
				completableFutures.add(desenvolverJogadorService.desenvolverJogador(jogadores.subList(i * offset, (i+1) * offset)));
			}
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

	}
	
	private void calcularHabilidadeGrupoValor3(Jogo jogo) {

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		for (HabilidadeGrupo hg : HabilidadeGrupo.getAll()) {
			completableFutures.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogo, hg));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
	
	private void calcularHabilidadeGrupoValor(Jogo jogo) {

		habilidadeGrupoValorRepository.deleteAllInBatch();
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);
		for (LigaJogo liga : ligaJogos) {
			completableFutures
					.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(liga, true));
			completableFutures
					.add(calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
	
	private void calcularProbabilidades(Semana semana, Temporada temporada) {
		
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(temporada);
		
		Map<LigaJogo, Map<NivelCampeonato, List<Campeonato>>> campeonatosMap = campeonatos.stream().collect(
				Collectors.groupingBy(Campeonato::getLigaJogo, Collectors.groupingBy(Campeonato::getNivelCampeonato)));
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo l : ligaJogos) {
			completableFutures.add(calcularProbabilidadeSimularPartidaService.calcularProbabilidadesCampeonato(
					semana, campeonatosMap.get(l).get(NivelCampeonato.NACIONAL).get(0),
					campeonatosMap.get(l).get(NivelCampeonato.NACIONAL_II).get(0),
					carregarParametroService.getTipoCampeonatoClubeProbabilidade(temporada.getJogo())));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
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

			getPromotorContinental(semana.getTemporada().getJogo()).promover(campeonatosMisto);
			partidaEliminatoriaRepository.saveAll(
					campeonatosMisto.stream().flatMap(c -> c.getPrimeiraRodadaEliminatoria().getPartidas().stream())
							.collect(Collectors.toList()));
		}
		if (semana.getNumero() == Constantes.SEMANA_PROMOCAO_CNII) {

			Boolean jogarCopaNacII = carregarParametroService.getParametroBoolean(semana.getTemporada().getJogo(), ParametroConstantes.JOGAR_COPA_NACIONAL_II);
			
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
							.findFirstByTemporadaAndLigaJogoAndNivelCampeonato(c.getTemporada(), c.getLigaJogo(), NivelCampeonato.COPA_NACIONAL_II).get();
					
					rodadaCNII = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(copaNacionalII, 1).get(0);
					rodadaCNII.setPartidas(partidaEliminatoriaRepository.findByRodada(rodadaCNII));
					
					rodada1Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 1).get(0);
					rodada1Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada1Fase));
	
					rodada2Fase = rodadaEliminatoriaRepository.findByCampeonatoEliminatorioAndNumero(c, 2).get(0);
					rodada2Fase.setPartidas(partidaEliminatoriaRepository.findByRodada(rodada2Fase));
	
					PromotorEliminatoria promotorEliminatoria = getPromotorEliminatoria(semana.getTemporada().getJogo());
					promotorEliminatoria.classificarCopaNacionalII(rodadaCNII, rodada1Fase, rodada2Fase);
					
					partidasSalvar.addAll(rodadaCNII.getPartidas());
				}

				partidaEliminatoriaRepository.saveAll(partidasSalvar);
			}
		}

	}
	
	private PromotorContinental getPromotorContinental(Jogo jogo) {
		//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
		PromotorContinental promotorContinental = null;

		if (carregarParametroService.isEstrategiaPromotorContinentalSegundoMelhorGrupo(jogo)) {
			promotorContinental = new PromotorContinentalImplDoisPorGrupo();
		} else if (carregarParametroService.isEstrategiaPromotorContinentalMelhorEliminado(jogo)) {	
			promotorContinental = new PromotorContinentalImplInterCampeonatos();
		}

		return promotorContinental;
	}

	private PromotorEliminatoria getPromotorEliminatoria(Jogo jogo) {
		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Integer numRodadas = carregarParametroService.getNumeroRodadasCopaNacional(jogo);
		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		Boolean jogarCNCompleta = carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA_32_TIMES);

		
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
		
		boolean marcarAmistosos = carregarParametroService.isMarcarAmistososAutomaticamenteSemanaASemana(semana.getTemporada().getJogo())
				|| carregarParametroService.isMarcarAmistososAutomaticamenteInicioTemporadaESemanaASemana(semana.getTemporada().getJogo());
		
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
		//carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		gerarClubeResumoTemporadaService.gerarClubeResumoTemporada(temporada);
	}
}
