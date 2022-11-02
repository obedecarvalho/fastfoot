package com.fastfoot.scheduler.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.QuantitativoPosicaoClubeDTO;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaGrupoRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AgruparHabilidadeValorEstatisticaService;
import com.fastfoot.player.service.AposentarJogadorService;
import com.fastfoot.player.service.AtualizarPassoDesenvolvimentoJogadorService;
import com.fastfoot.player.service.CalcularValorTransferenciaService;
import com.fastfoot.player.service.CriarJogadoresClubeService;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplDezesseisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplTrintaClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplTrintaEDoisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplTrintaEDoisClubesII;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEDoisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEOitoClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEQuatroClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactory;
import com.fastfoot.scheduler.model.factory.RodadaAmistosaFactory;
import com.fastfoot.scheduler.model.factory.TemporadaFactory;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.ParametroService;
import com.fastfoot.service.PreCarregarParametrosService;
import com.fastfoot.service.PreCarregarClubeService;

@Service
public class CriarCalendarioTemporadaService {
	
	private static final Integer NUM_THREAD_ATUALIZAR_PASSO_DESEN = 7;
	
	//########	REPOSITORY	##########
	
	@Autowired
	private TemporadaRepository temporadaRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private GrupoDesenvolvimentoJogadorRepository grupoDesenvolvimentoJogadorRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Autowired
	private HabilidadeValorEstatisticaGrupoRepository habilidadeValorEstatisticaGrupoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;
	
	//#######	SERVICE	#############
	
	@Autowired
	private PreCarregarClubeService preCarregarService;
	
	@Autowired
	private PreCarregarParametrosService preCarregarParametrosService;

	@Autowired
	private ParametroService parametroService;
	
	/*@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private RodadaAmistosaService rodadaAmistosaService;*/

	@Autowired
	private AposentarJogadorService aposentarJogadorService;
	
	@Autowired
	private CalcularValorTransferenciaService calcularValorTransferenciaService;

	@Autowired
	private CriarJogadoresClubeService criarJogadoresClubeService;

	@Autowired
	private AtualizarPassoDesenvolvimentoJogadorService atualizarPassoDesenvolvimentoJogadorService;
	
	@Autowired
	private AgruparHabilidadeValorEstatisticaService agruparHabilidadeValorEstatisticaService;

	@Autowired
	private SalvarCampeonatosService salvarCampeonatosService;
	
	@Autowired
	private CalcularPartidaProbabilidadeResultadoService calcularPartidaProbabilidadeResultadoService;

	@Autowired
	private DistribuirPremiacaoCompeticoesService distribuirPremiacaoCompeticoesService;

	public TemporadaDTO criarTemporada() {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<String> mensagens = new ArrayList<String>();
		long inicio = 0, fim = 0;

		Temporada temporada = null;
		Integer ano = Constantes.ANO_INICIAL;
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		
		stopWatch.split();

		if (temporadaOpt.isPresent()) {
			temporada = temporadaOpt.get();
			
			if (temporada.getSemanaAtual() < Constantes.NUM_SEMANAS) {
				throw new RuntimeException("Temporada ainda não terminada!");
			}

			inicio = stopWatch.getSplitNanoTime();
			agruparHabilidadeValorEstatisticaService.agrupar(temporada);
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#agruparHabilidadeValorEstatistica:" + (fim - inicio));
			
			temporada.setAtual(false);
			temporadaRepository.save(temporada);
			ano = temporada.getAno() + 1;

			//stopWatch.split();
			inicio = stopWatch.getSplitNanoTime();
			atualizarPassoDesenvolvimentoJogador4();
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#atualizarPassoDesenvolvimentoJogador:" + (fim - inicio));
			
			inicio = stopWatch.getSplitNanoTime();
			aposentarJogadores();
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#aposentarJogadores:" + (fim - inicio));

		} else {

			inicio = stopWatch.getSplitNanoTime();
			preCarregarParametrosService.preCarregarParametros();
			preCarregarService.preCarregarClubes();
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#preCarregar:" + (fim - inicio));
			
			inicio = stopWatch.getSplitNanoTime();
			if (jogadorRepository.findAll().isEmpty()) {//TODO: usado em DEV
				List<Clube> clubes = clubeRepository.findAll();
				
				List<CompletableFuture<Boolean>> criarJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
				
				int offset = clubes.size() / FastfootApplication.NUM_THREAD;
				
				for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
					if ((i + 1) == FastfootApplication.NUM_THREAD) {
						criarJogadorFuture.add(criarJogadoresClubeService
								.criarJogadoresClube(clubes.subList(i * offset, clubes.size())));
					} else {
						criarJogadorFuture.add(criarJogadoresClubeService
								.criarJogadoresClube(clubes.subList(i * offset, (i + 1) * offset)));
					}
				}
				
				CompletableFuture.allOf(criarJogadorFuture.toArray(new CompletableFuture<?>[0])).join();

			}
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#criarJogadoresClube:" + (fim - inicio));
		}

		//stopWatch.split();
		
		inicio = stopWatch.getSplitNanoTime();
		calcularValorTransferenciaJogadores();
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#calcularValorTransferenciaJogadores:" + (fim - inicio));

		temporada = TemporadaFactory.criarTempordada(ano);

		List<Campeonato> campeonatosNacionais = new ArrayList<Campeonato>();
		List<CampeonatoMisto> campeonatosContinentais = new ArrayList<CampeonatoMisto>();
		List<CampeonatoEliminatorio> copasNacionais = new ArrayList<CampeonatoEliminatorio>();
		List<RodadaAmistosa> rodadaAmistosaAutomaticas = new ArrayList<RodadaAmistosa>();

		inicio = stopWatch.getSplitNanoTime();
		criarCampeonatos(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#criarCampeonatos:" + (fim - inicio));

		inicio = stopWatch.getSplitNanoTime();
		salvar(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#salvar:" + (fim - inicio));//40%
		
		inicio = stopWatch.getSplitNanoTime();
		criarEstatisticasJogadorTemporada(temporada);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#criarEstatisticasJogadorTemporada:" + (fim - inicio));
		
		inicio = stopWatch.getSplitNanoTime();
		calcularPartidaProbabilidadeResultado(temporada);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (fim - inicio));
		
		inicio = stopWatch.getSplitNanoTime();
		distribuirPremiacaoCompeticoesService.distribuirPremiacaoCompeticoes(temporada);
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#distribuirPremiacaoCompeticoes:" + (fim - inicio));

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getNanoTime());
		
		System.err.println(mensagens);
		
		return TemporadaDTO.convertToDTO(temporada);
	}

	private void criarEstatisticasJogadorTemporada(Temporada temporada) {

		List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);
		
		jogadores.stream().forEach(j -> j.setJogadorEstatisticasTemporadaAtual(
				new JogadorEstatisticasTemporada(j, temporada, j.getClube(), false)));
		jogadorEstatisticasTemporadaRepository.saveAll(
				jogadores.stream().map(Jogador::getJogadorEstatisticasTemporadaAtual).collect(Collectors.toList()));
		
		jogadores.stream().forEach(j -> j.setJogadorEstatisticasAmistososTemporadaAtual(
				new JogadorEstatisticasTemporada(j, temporada, j.getClube(), true)));
		jogadorEstatisticasTemporadaRepository.saveAll(jogadores.stream()
				.map(Jogador::getJogadorEstatisticasAmistososTemporadaAtual).collect(Collectors.toList()));

		jogadorRepository.saveAll(jogadores);
	}
	
	/*private void criarEstatisticasJogadorTemporada(Temporada temporada) {

		List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);

		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;

		List<CompletableFuture<Boolean>> criarEstatisticasJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				criarEstatisticasJogadorFuture.add(salvarCampeonatosService
						.criarEstatisticasJogadorTemporada(jogadores.subList(i * offset, jogadores.size()), temporada));
			} else {
				criarEstatisticasJogadorFuture.add(salvarCampeonatosService
						.criarEstatisticasJogadorTemporada(jogadores.subList(i * offset, (i + 1) * offset), temporada));
			}
		}

		CompletableFuture.allOf(criarEstatisticasJogadorFuture.toArray(new CompletableFuture<?>[0])).join();

	}*/
	
	private void criarCampeonatos(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		Boolean jogarCopaNacII = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_II);
		//String numeroRodadasCopaNacional = parametroService.getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);

		Integer nroCompeticoes = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Boolean cIIIReduzido = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		//String estrategiaPromotorCont = parametroService.getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);

		Boolean marcarAmistosos = parametroService.getParametroBoolean(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE);
		

		//CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = getCampeonatoEliminatorioFactory(numeroRodadasCopaNacional, nroCompeticoes);
		CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = getCampeonatoEliminatorioFactory();

		Integer ano = temporada.getAno();

		List<Clube> clubes = null;
		Campeonato campeonatoNacional = null;
		List<ClubeRanking> clubesRk = null;
		CampeonatoEliminatorio copaNacional = null;

		for (Liga liga : Liga.getAll()) {

			//Nacional I e II
			for (int i = 0; i < Constantes.NRO_DIVISOES; i++) {
				
				/*clubes = clubeRepository.findByLigaAndAnoAndClassificacaoNacionalBetween(liga, ano - 1,
						(i == 0 ? ClassificacaoNacional.getAllNacionalNovoCampeonato()
								: ClassificacaoNacional.getAllNacionalIINovoCampeonato()));*/
				
				clubes = clubeRepository.findByLigaAndAnoAndClassificacaoNacionalBetween(liga, ano - 1, (i == 0
						? parametroService.getClassificacaoNacionalNovoCampeonato(NivelCampeonato.NACIONAL)
						: parametroService.getClassificacaoNacionalNovoCampeonato(NivelCampeonato.NACIONAL_II)));
				
				campeonatoNacional = CampeonatoFactory.criarCampeonato(temporada, liga, clubes,
						(i == 0 ? NivelCampeonato.NACIONAL : NivelCampeonato.NACIONAL_II));

				campeonatosNacionais.add(campeonatoNacional);
			}

			//Copa Nacional I e II
			
			clubesRk = clubeRankingRepository.findByLigaAndAno(liga, ano - 1);
			
			copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacional(temporada, liga, clubesRk,
					NivelCampeonato.COPA_NACIONAL);
			
			copasNacionais.add(copaNacional);
			
			if (jogarCopaNacII) {
				copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacionalII(temporada, liga, clubesRk,
						NivelCampeonato.COPA_NACIONAL_II);
				
				copasNacionais.add(copaNacional);
			}
		}
		
		//Continental I e II
		Map<Liga, List<Clube>> clubesContinental = null;
		CampeonatoMisto campeonatoContinental = null;
		for (int i = 0; i < nroCompeticoes; i++) {
			clubesContinental = new HashMap<Liga, List<Clube>>();
			
			int posInicial = i * Constantes.NRO_CLUBES_POR_LIGA_CONT;
			int posFinal = (i + 1) * Constantes.NRO_CLUBES_POR_LIGA_CONT;
			
			if (i == 2 //CIII
					&& cIIIReduzido 
					//&& ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategiaPromotorCont)
					&& parametroService.isEstrategiaPromotorContinentalMelhorEliminado()
					) {
				posFinal = 10;
			}

			for (Liga liga : Liga.getAll()) {
				clubesContinental.put(liga, clubeRepository.findByLigaAndAnoAndPosicaoGeralBetween(liga, ano - 1, posInicial + 1, posFinal));
			}

			campeonatoContinental = CampeonatoMistoFactory.criarCampeonato(temporada, clubesContinental,
					NivelCampeonato.getContinentalPorOrdem(i));
			campeonatosContinentais.add(campeonatoContinental);
		}
		
		//Amistosos
		if (marcarAmistosos) {
			Map<Liga, List<Clube>> clubesAmistosos = new HashMap<Liga, List<Clube>>();

			int posInicial = nroCompeticoes * Constantes.NRO_CLUBES_POR_LIGA_CONT + 1;
			int posFinal = 32;
			
			if (nroCompeticoes == 3 && cIIIReduzido
					//&& ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategiaPromotorCont)
					&& parametroService.isEstrategiaPromotorContinentalMelhorEliminado()
					) {
				posInicial = 11;
				//posFinal = 30;
			}

			for (Liga liga : Liga.getAll()) {
				clubesAmistosos.put(liga, clubeRepository.findByLigaAndAnoAndPosicaoGeralBetween(liga, ano - 1, posInicial, posFinal));
			}

			rodadaAmistosaAutomaticas.addAll(RodadaAmistosaFactory.criarRodadasAmistosasAgrupaGrupo(temporada, clubesAmistosos));

		}
	}
	
	private CampeonatoEliminatorioFactory getCampeonatoEliminatorioFactory() {
		
		Integer nroCompeticoesContinentais = parametroService.getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Integer numRodadas = parametroService.getNumeroRodadasCopaNacional();
		Boolean cIIIReduzido = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		Boolean jogarCNCompleta = parametroService.getParametroBoolean(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA);
		//TODO: parametroService.isEstrategiaPromotorContinentalMelhorEliminado()

		CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = null;

		if (numRodadas == 4) {
			campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplDezesseisClubes();
		} else if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplTrintaEDoisClubes();
			} else if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					if (jogarCNCompleta) {
						campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplTrintaEDoisClubesII();
					} else {
						campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplTrintaClubes();
					}
				} else {
					campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEOitoClubes();
				}
			}
		} else if (numRodadas == 5) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEQuatroClubes();
			} else if (nroCompeticoesContinentais == 3) {
				if (cIIIReduzido) {
					campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEDoisClubes();
				} else {
					campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteClubes();
				}
			}
		}

		return campeonatoEliminatorioFactory;
	}

	/*private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		salvarTemporada(temporada);

		campeonatoService.salvarCampeonato(campeonatosNacionais);

		campeonatoService.salvarCampeonatoEliminatorio(copasNacionais);

		campeonatoService.salvarCampeonatoMisto(campeonatosContinentais);

		rodadaAmistosaService.salvarRodadasAmistosas(rodadaAmistosaAutomaticas);

	}*/
	
	private void salvarTemporada(Temporada temporada) {
		temporadaRepository.save(temporada);
		semanaRepository.saveAll(temporada.getSemanas());
	}
	
	private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		salvarTemporada(temporada);

		List<CompletableFuture<Boolean>> salvarCampeonatoFuture = new ArrayList<CompletableFuture<Boolean>>();

		int offset = campeonatosNacionais.size() / 4;

		for (int i = 0; i < 4; i++) {
			if ((i + 1) == 4) {
				salvarCampeonatoFuture.add(salvarCampeonatosService
						.salvarCampeonato(campeonatosNacionais.subList(i * offset, campeonatosNacionais.size())));
			} else {
				salvarCampeonatoFuture.add(salvarCampeonatosService
						.salvarCampeonato(campeonatosNacionais.subList(i * offset, (i + 1) * offset)));
			}
		}

		offset = copasNacionais.size() / 2;

		for (int i = 0; i < 2; i++) {
			if ((i + 1) == 2) {
				salvarCampeonatoFuture.add(salvarCampeonatosService
						.salvarCampeonatoEliminatorio(copasNacionais.subList(i * offset, copasNacionais.size())));
			} else {
				salvarCampeonatoFuture.add(salvarCampeonatosService
						.salvarCampeonatoEliminatorio(copasNacionais.subList(i * offset, (i + 1) * offset)));
			}
		}

		salvarCampeonatoFuture.add(salvarCampeonatosService.salvarCampeonatoMisto(campeonatosContinentais));

		salvarCampeonatoFuture.add(salvarCampeonatosService.salvarRodadasAmistosas(rodadaAmistosaAutomaticas));

		CompletableFuture.allOf(salvarCampeonatoFuture.toArray(new CompletableFuture<?>[0])).join();
	}

	/*private void aposentarJogadores() {

		//if (semana.getNumero() == 25) {
			
		List<GrupoDesenvolvimentoJogador> gds = grupoDesenvolvimentoJogadorRepository.findByAtivoAndIdadeJogador(Boolean.TRUE, JogadorFactory.IDADE_MAX);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = gds.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, gds.size())));
			} else {
				desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, (i+1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}*/
	
	private void aposentarJogadores() {

		//if (semana.getNumero() == 25) {
			
		List<GrupoDesenvolvimentoJogador> gds = grupoDesenvolvimentoJogadorRepository
				.findByAtivoAndIdadeJogador(Boolean.TRUE, JogadorFactory.IDADE_MAX);		
		Map<Integer, Map<Clube, List<GrupoDesenvolvimentoJogador>>> grupoDesenvolvimentoClube = gds.stream()
				.collect(Collectors.groupingBy(
						gd -> (gd.getJogador().getClube().getId() % FastfootApplication.NUM_THREAD),
						Collectors.groupingBy(gd -> gd.getJogador().getClube())));
		
		Map<Integer, Map<Clube, List<QuantitativoPosicaoClubeDTO>>> quantitativoPosicaoPorClubeMap = getQuantitativoPosicaoClube()
				.stream().collect(Collectors.groupingBy(q -> (q.getClube().getId() % FastfootApplication.NUM_THREAD),
						Collectors.groupingBy(q -> q.getClube())));
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(grupoDesenvolvimentoClube.get(i),
					quantitativoPosicaoPorClubeMap.get(i)));
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}
	
	public List<QuantitativoPosicaoClubeDTO> getQuantitativoPosicaoClube() {
		List<Map<String, Object>> result = grupoDesenvolvimentoJogadorRepository
				.findQtdeJogadorPorPosicaoPorClube(Posicao.GOLEIRO.ordinal(), JogadorFactory.IDADE_MAX);
		
		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeList = new ArrayList<QuantitativoPosicaoClubeDTO>();
		QuantitativoPosicaoClubeDTO dto = null;
		
		for (Map<String, Object> map : result) {
			dto = new QuantitativoPosicaoClubeDTO();
			dto.setClube(new Clube((Integer) map.get("id_clube")));
			dto.setPosicao(Posicao.values()[(Integer) map.get("posicao")]);
			dto.setQtde(((BigInteger) map.get("total")).intValue());
			quantitativoPosicaoClubeList.add(dto);
		}
		
		return quantitativoPosicaoClubeList;
	}

	private void calcularValorTransferenciaJogadores() {
		//if (semana.getNumero() == 1) {
			
		List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(calcularValorTransferenciaService
						.calcularValorTransferencia(jogadores.subList(i * offset, jogadores.size())));
			} else {
				desenvolverJogadorFuture.add(calcularValorTransferenciaService
						.calcularValorTransferencia(jogadores.subList(i * offset, (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}

	@SuppressWarnings("unused")
	private void atualizarPassoDesenvolvimentoJogador() {
		//if (semana.getNumero() == 1) {
		
		//TODO: jogadorRepository.incrementarIdade();??

		List<Jogador> jogadores = jogadorRepository.findByStatusJogadorFetchHabilidades(StatusJogador.ATIVO);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService
						.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, jogadores.size())));
			} else {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService
						.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}

	private void atualizarPassoDesenvolvimentoJogador4() {
		jogadorRepository.incrementarIdade();
		
		int offset = 3;//(JogadorFactory.IDADE_MAX - JogadorFactory.IDADE_MIN) / FastfootApplication.NUM_THREAD;
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();

		for (int i = 0; i < NUM_THREAD_ATUALIZAR_PASSO_DESEN; i++) {
			if ((i + 1) == NUM_THREAD_ATUALIZAR_PASSO_DESEN) {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						(JogadorFactory.IDADE_MIN + i * offset), JogadorFactory.IDADE_MAX));
			} else {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						(JogadorFactory.IDADE_MIN + i * offset), (JogadorFactory.IDADE_MIN + (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}

	@SuppressWarnings("unused")
	private void atualizarPassoDesenvolvimentoJogador2() {
		//if (semana.getNumero() == 1) {

		Temporada temporada = null;
		temporada = temporadaRepository.findFirstByOrderByAnoDesc().get();

		HabilidadeEstatisticaPercentil hep = agruparHabilidadeValorEstatisticaService.getPercentilHabilidadeValor(temporada);
		List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = habilidadeValorEstatisticaGrupoRepository
				.findByTemporadaAndAmistoso(temporada, false);
		
		Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap = estatisticasGrupo.stream()
				.collect(Collectors.toMap(HabilidadeValorEstatisticaGrupo::getHabilidadeValor, Function.identity()));
			
		List<Jogador> jogadores = jogadorRepository.findByStatusJogadorFetchHabilidades(StatusJogador.ATIVO);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						jogadores.subList(i * offset, jogadores.size()), hep, estatisticasGrupoMap));
			} else {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						jogadores.subList(i * offset, (i + 1) * offset), hep, estatisticasGrupoMap));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
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
}
