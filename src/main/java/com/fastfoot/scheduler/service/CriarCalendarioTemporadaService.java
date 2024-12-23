package com.fastfoot.scheduler.service;

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
import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.service.CalcularPartidaProbabilidadeResultadoFacadeService;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.service.AnalisarDesempenhoTreinadorService;
import com.fastfoot.club.service.AtualizarClubeNivelService;
import com.fastfoot.club.service.DistribuirPatrocinioService;
import com.fastfoot.financial.service.DistribuirPremiacaoCompeticoesService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.QuantitativoPosicaoClubeDTO;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.HabilidadeValorEstatisticaGrupoRepository;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AdequarModoDesenvolvimentoJogadorService;
import com.fastfoot.player.service.AgruparHabilidadeValorEstatisticaService;
import com.fastfoot.player.service.AgruparJogadorEstatisticasTemporadaService;
import com.fastfoot.player.service.AposentarJogadorService;
import com.fastfoot.player.service.AtualizarPassoDesenvolvimentoJogadorService;
import com.fastfoot.player.service.CalcularValorTransferenciaJogadorPorHabilidadeService;
import com.fastfoot.player.service.ConsultarQuantitativoPosicaoClubeService;
import com.fastfoot.player.service.CalcularValorTransferenciaJogadorForcaGeralService;
import com.fastfoot.player.service.CalcularValorTransferenciaJogadorPorHabilidadeGrupoService;
import com.fastfoot.player.service.RenovarContratosAutomaticamenteService;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
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
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactoryImplFaseGrupos;
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactoryImplFaseLiga;
import com.fastfoot.scheduler.model.factory.RodadaAmistosaFactory;
import com.fastfoot.scheduler.model.factory.TemporadaFactory;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.CarregarParametroService;
import com.fastfoot.service.LigaJogoCRUDService;
import com.fastfoot.transfer.service.ExecutarTransferenciasAutomaticamenteService;

@Service
public class CriarCalendarioTemporadaService {
	
	private static final Integer NUM_THREAD_ATUALIZAR_PASSO_DESEN = 7;

	private static final Boolean SALVAR_HABILIDADE_VALOR_ESTATISTICAS = false;//#DEVEL
	
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
	
	/*@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;*/
	
	@Autowired
	private HabilidadeValorEstatisticaGrupoRepository habilidadeValorEstatisticaGrupoRepository;
	
	@Autowired
	private RodadaRepository rodadaRepository;
	
	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	/*@Autowired
	private JogadorEstatisticasSemanaRepository jogadorEstatisticasSemanaRepository;*/
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;

	@Autowired
	private ContratoRepository contratoRepository;
	
	//#######	SERVICE	#############
	
	/*@Autowired
	private PreCarregarClubeService preCarregarClubeService;
	
	@Autowired
	private PreCarregarParametrosService preCarregarParametrosService;*/

	@Autowired
	private CarregarParametroService carregarParametroService;

	@Autowired
	private AposentarJogadorService aposentarJogadorService;
	
	@Autowired
	private CalcularValorTransferenciaJogadorForcaGeralService calcularValorTransferenciaJogadorForcaGeralService;

	/*@Autowired
	private CriarJogadoresClubeService criarJogadoresClubeService;*/

	@Autowired
	private AtualizarPassoDesenvolvimentoJogadorService atualizarPassoDesenvolvimentoJogadorService;
	
	@Autowired
	private AgruparHabilidadeValorEstatisticaService agruparHabilidadeValorEstatisticaService;

	@Autowired
	private SalvarCampeonatosService salvarCampeonatosService;

	@Autowired
	private CalcularPartidaProbabilidadeResultadoFacadeService calcularPartidaProbabilidadeResultadoFacadeService;

	@Autowired
	private DistribuirPremiacaoCompeticoesService distribuirPremiacaoCompeticoesService;
	
	@Autowired
	private DistribuirPatrocinioService distribuirPatrocinioService;
	
	@Autowired
	private AdequarModoDesenvolvimentoJogadorService adequarModoDesenvolvimentoJogadorService;

	@Autowired
	private CalcularValorTransferenciaJogadorPorHabilidadeService calcularValorTransferenciaJogadorPorHabilidadeService;
	
	@Autowired
	private AnalisarDesempenhoTreinadorService analisarDesempenhoTreinadorService;

	@Autowired
	private CalcularValorTransferenciaJogadorPorHabilidadeGrupoService calcularValorTransferenciaJogadorPorHabilidadeGrupoService;

	@Autowired
	private AtualizarClubeNivelService atualizarClubeNivelService;

	@Autowired
	private RenovarContratosAutomaticamenteService renovarContratosAutomaticamenteService;

	@Autowired
	private ExecutarTransferenciasAutomaticamenteService executarTransferenciasAutomaticamenteService;

	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Autowired
	private ConsultarQuantitativoPosicaoClubeService consultarQuantitativoPosicaoClubeService;
	
	@Autowired
	private AgruparJogadorEstatisticasTemporadaService agruparJogadorEstatisticasTemporadaService;

	public TemporadaDTO criarTemporada(Jogo jogo) {

		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitDuration().toMillis();

		Temporada temporada = null, temporadaAtual = null;
		Optional<Temporada> temporadaAtualOpt = temporadaRepository.findFirstByJogoAndAtual(jogo, true);//TODO: substituir por TemporadaCRUDService

		if (temporadaAtualOpt.isPresent()) {
			if (temporadaAtualOpt.get().getSemanaAtual() < Constantes.NUM_SEMANAS) {
				throw new RuntimeException("Temporada ainda não terminada!");
			}
		}
		
		temporada = TemporadaFactory.criarTemporada(jogo,
				temporadaAtualOpt.isPresent() ? (temporadaAtualOpt.get().getAno() + 1): Constantes.ANO_INICIAL);
		salvarTemporada(temporada);

		if (temporadaAtualOpt.isPresent()) {
			temporadaAtual = temporadaAtualOpt.get();

			if (SALVAR_HABILIDADE_VALOR_ESTATISTICAS) {
			agruparHabilidadeValorEstatisticaService.agrupar2(temporadaAtual);
			stopWatch.split();
			mensagens.add("\t#agruparHabilidadeValorEstatistica:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
			}

			/*
			jogadorEstatisticasTemporadaRepository.agruparJogadorEstatisticasTemporada(temporadaAtual.getId());
			//jogadorEstatisticasSemanaRepository.deleteAllInBatch();
			jogadorEstatisticasSemanaRepository.deleteByIdTemporada(temporadaAtual.getId());
			*/
			agruparJogadorEstatisticasTemporadaService.agruparJogadorEstatisticasTemporadaService(temporadaAtual);
			stopWatch.split();
			mensagens.add("\t#agruparJogadorEstatisticasTemporada:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			temporadaAtual.setAtual(false);
			temporadaRepository.save(temporadaAtual);

			jogadorRepository.incrementarIdade(jogo.getId());
			stopWatch.split();
			mensagens.add("\t#incrementarIdade:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			adequarModoDesenvolvimentoJogador(temporadaAtual);
			stopWatch.split();
			mensagens.add("\t#adequarModoDesenvolvimentoJogador:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			/*if (parametroService.getParametroBoolean(ParametroConstantes.USAR_BANCO_DADOS_EM_MEMORIA)) {
				atualizarPassoDesenvolvimentoJogador();
			} else {*/
			atualizarPassoDesenvolvimentoJogadorOtimizado(jogo);
			//}
			stopWatch.split();
			mensagens.add("\t#atualizarPassoDesenvolvimentoJogador:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			aposentarJogadores(jogo);
			stopWatch.split();
			mensagens.add("\t#aposentarJogadores:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			/*if (carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.USAR_VERSAO_SIMPLIFICADA)) {
				calcularValorTransferenciaJogadoresSimplificado(jogo);
			} else {
				calcularValorTransferenciaJogadores(jogo);
			}*/
			calcularValorTransferencia(jogo);
			stopWatch.split();
			mensagens.add("\t#calcularValorTransferenciaJogadores:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

			renovarContratosAutomaticamente(temporada);
			stopWatch.split();
			mensagens.add("\t#renovarContratosAutomaticamente:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
			
			analisarDesempenhoTreinadorService.analisarDesempenhoTreinador(temporadaAtual);
			stopWatch.split();
			mensagens.add("\t#analisarDesempenhoTreinador:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
			
			clubeRepository.calcularForcaGeral(jogo.getId());
			stopWatch.split();
			mensagens.add("\t#calcularForcaGeralClube:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		} else {

			/*preCarregarParametrosService.preCarregarParametros();
			preCarregarClubeService.preCarregarClubes();
			stopWatch.split();
			mensagens.add("\t#preCarregar:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco*/

			/*if (jogadorRepository.count() == 0) {
				List<Clube> clubes = clubeRepository.findByJogo(jogo);
				
				List<CompletableFuture<Boolean>> criarJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
				
				int offset = clubes.size() / FastfootApplication.NUM_THREAD;
				
				for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
					if ((i + 1) == FastfootApplication.NUM_THREAD) {
						criarJogadorFuture.add(criarJogadoresClubeService
								.criarJogadoresClube(jogo, clubes.subList(i * offset, clubes.size())));
					} else {
						criarJogadorFuture.add(criarJogadoresClubeService
								.criarJogadoresClube(jogo, clubes.subList(i * offset, (i + 1) * offset)));
					}
				}
				
				CompletableFuture.allOf(criarJogadorFuture.toArray(new CompletableFuture<?>[0])).join();

			}

			stopWatch.split();
			mensagens.add("\t#criarJogadoresClube:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco*/

		}

		List<Campeonato> campeonatosNacionais = new ArrayList<Campeonato>();
		List<CampeonatoMisto> campeonatosContinentais = new ArrayList<CampeonatoMisto>();
		List<CampeonatoEliminatorio> copasNacionais = new ArrayList<CampeonatoEliminatorio>();
		List<RodadaAmistosa> rodadaAmistosaAutomaticas = new ArrayList<RodadaAmistosa>();

		criarCampeonatos(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);
		stopWatch.split();
		mensagens.add("\t#criarCampeonatos:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		salvar(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);
		stopWatch.split();
		mensagens.add("\t#salvar:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco
		
		if (!temporadaAtualOpt.isPresent()) {
			setarSemanaInicialContratos(temporada);
		}
		stopWatch.split();
		mensagens.add("\t#setarSemanaInicialContratos:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		calcularPartidaProbabilidadeResultado(temporada);
		stopWatch.split();
		mensagens.add("\t#calcularPartidaProbabilidadeResultado:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		distribuirPremiacaoCompeticoesService.distribuirPremiacaoCompeticoes(temporada);
		stopWatch.split();
		mensagens.add("\t#distribuirPremiacaoCompeticoes:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		distribuirPatrocinioService.distribuirPatrocinio(temporada);
		stopWatch.split();
		mensagens.add("\t#distribuirPatrocinio:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		if (carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.GERAR_TRANSFERENCIA_INICIO_TEMPORADA)) {

			executarTransferenciasAutomaticamenteService.executarTransferenciasAutomaticamente(temporada);
			stopWatch.split();
			mensagens.add("\t#gerarTransferencias:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		}
		
		if (carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.GERAR_MUDANCA_CLUBE_NIVEL)) {

			gerarMudancaClubeNivel(temporada);
			stopWatch.split();
			mensagens.add("\t#gerarMudancaClubeNivel:" + (stopWatch.getSplitDuration().toMillis() - inicio));
			inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		}

		//jogadorEnergiaRepository.deleteAllInBatch();
		jogadorEnergiaRepository.deleteByIdJogo(jogo.getId());
		jogadorEnergiaRepository.inserirEnergiaTodosJogadores(Constantes.ENERGIA_INICIAL, jogo.getId());
		stopWatch.split();
		mensagens.add("\t#inserirEnergiaTodosJogadores:" + (stopWatch.getSplitDuration().toMillis() - inicio));
		inicio = stopWatch.getSplitDuration().toMillis();//inicar outro bloco

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getDuration().toMillis());//Tempo total

		System.err.println(mensagens);

		return TemporadaDTO.convertToDTO(temporada);
	}
	
	private void setarSemanaInicialContratos(Temporada temporada) {
		Semana semana = temporada.getSemanas().stream().filter(s -> s.getNumero() == 1).findFirst().get();
		contratoRepository.updateSemanaInicial(semana);
	}
	
	private void criarCampeonatos(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		Boolean jogarCopaNacII = carregarParametroService.getParametroBoolean(temporada.getJogo(), ParametroConstantes.JOGAR_COPA_NACIONAL_II);

		Integer nroCompeticoes = carregarParametroService.getParametroInteger(temporada.getJogo(), ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(temporada.getJogo(), ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);

		Boolean marcarAmistosos = carregarParametroService.isMarcarAmistososAutomaticamenteInicioTemporada(temporada.getJogo())
				|| carregarParametroService.isMarcarAmistososAutomaticamenteInicioTemporadaESemanaASemana(temporada.getJogo()); 
		

		CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = getCampeonatoEliminatorioFactory(temporada.getJogo());
		CampeonatoMistoFactory campeonatoMistoFactory = getCampeonatoMistoFactory(temporada.getJogo());

		Integer ano = temporada.getAno();

		List<Clube> clubes = null;
		Campeonato campeonatoNacional = null;
		List<ClubeRanking> clubesRk = null;
		CampeonatoEliminatorio copaNacional = null;

		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo liga : ligaJogos) {

			//Nacional I e II
			for (int i = 0; i < Constantes.NRO_DIVISOES; i++) {
				
				clubes = clubeRepository.findByLigaJogoAndAnoAndClassificacaoNacionalBetween(liga, ano - 1, (i == 0
						? carregarParametroService.getClassificacaoNacionalNovoCampeonato(temporada.getJogo(), NivelCampeonato.NACIONAL)
						: carregarParametroService.getClassificacaoNacionalNovoCampeonato(temporada.getJogo(), NivelCampeonato.NACIONAL_II)));
				
				campeonatoNacional = CampeonatoFactory.criarCampeonato(temporada, liga, clubes,
						(i == 0 ? NivelCampeonato.NACIONAL : NivelCampeonato.NACIONAL_II));

				campeonatosNacionais.add(campeonatoNacional);
			}

			//Copa Nacional I e II
			
			clubesRk = clubeRankingRepository.findByLigaJogoAndAno(liga, ano - 1);
			
			copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacional(temporada, liga, clubesRk);
			
			copasNacionais.add(copaNacional);
			
			if (jogarCopaNacII) {
				copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacionalII(temporada, liga, clubesRk);
				
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
					&& carregarParametroService.isEstrategiaPromotorContinentalMelhorEliminado(temporada.getJogo())
					) {
				posFinal = 10;
			}

			for (LigaJogo liga : ligaJogos) {
				clubesContinental.put(liga.getLiga(), clubeRepository.findByLigaJogoAndAnoAndPosicaoGeralBetween(liga, ano - 1, posInicial + 1, posFinal));
			}

			campeonatoContinental = campeonatoMistoFactory.criarCampeonato(temporada, clubesContinental,
					NivelCampeonato.getContinentalPorOrdem(i));
			campeonatosContinentais.add(campeonatoContinental);
		}
		
		//Amistosos
		if (marcarAmistosos) {
			Map<Liga, List<Clube>> clubesAmistosos = new HashMap<Liga, List<Clube>>();

			int posInicial = nroCompeticoes * Constantes.NRO_CLUBES_POR_LIGA_CONT + 1;
			int posFinal = 32;
			
			if (nroCompeticoes == 3 && cIIIReduzido
					&& carregarParametroService.isEstrategiaPromotorContinentalMelhorEliminado(temporada.getJogo())
					) {
				posInicial = 11;
			}

			for (LigaJogo liga : ligaJogos) {
				clubesAmistosos.put(liga.getLiga(), clubeRepository.findByLigaJogoAndAnoAndPosicaoGeralBetween(liga, ano - 1, posInicial, posFinal));
			}

			rodadaAmistosaAutomaticas.addAll(RodadaAmistosaFactory.criarRodadasAmistosasAgrupaGrupo(temporada, clubesAmistosos));

		}
	}
	
	private CampeonatoMistoFactory getCampeonatoMistoFactory(Jogo jogo) {
		if (carregarParametroService.isModoDisputaFaseInicialContinentalFaseGrupos(jogo)) {
			return new CampeonatoMistoFactoryImplFaseGrupos();
		}
		
		if (carregarParametroService.isModoDisputaFaseInicialContinentalFaseLiga(jogo)) {
			return new CampeonatoMistoFactoryImplFaseLiga();
		}
		
		return new CampeonatoMistoFactoryImplFaseGrupos();//TODO: throw parametro não esperado

	}

	private CampeonatoEliminatorioFactory getCampeonatoEliminatorioFactory(Jogo jogo) {
		
		Integer nroCompeticoesContinentais = carregarParametroService.getParametroInteger(jogo, ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		Integer numRodadas = carregarParametroService.getNumeroRodadasCopaNacional(jogo);
		Boolean cIIIReduzido = carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO);
		Boolean jogarCNCompleta = carregarParametroService.getParametroBoolean(jogo, ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA_32_TIMES);

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
	
	private void salvarTemporada(Temporada temporada) {
		temporadaRepository.save(temporada);
		semanaRepository.saveAll(temporada.getSemanas());
	}
	
	private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		int offset = campeonatosNacionais.size() / 4;

		for (int i = 0; i < 4; i++) {
			if ((i + 1) == 4) {
				completableFutures.add(salvarCampeonatosService
						.salvarCampeonato(campeonatosNacionais.subList(i * offset, campeonatosNacionais.size())));
			} else {
				completableFutures.add(salvarCampeonatosService
						.salvarCampeonato(campeonatosNacionais.subList(i * offset, (i + 1) * offset)));
			}
		}

		offset = copasNacionais.size() / 2;

		for (int i = 0; i < 2; i++) {
			if ((i + 1) == 2) {
				completableFutures.add(salvarCampeonatosService
						.salvarCampeonatoEliminatorio(copasNacionais.subList(i * offset, copasNacionais.size())));
			} else {
				completableFutures.add(salvarCampeonatosService
						.salvarCampeonatoEliminatorio(copasNacionais.subList(i * offset, (i + 1) * offset)));
			}
		}

		completableFutures.add(salvarCampeonatosService.salvarCampeonatoMisto(campeonatosContinentais));

		completableFutures.add(salvarCampeonatosService.salvarRodadasAmistosas(rodadaAmistosaAutomaticas));

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}

	private void aposentarJogadores(Jogo jogo) {

		List<Jogador> jogadores = jogadorRepository.findByJogoAndIdadeAndStatusJogador(jogo, JogadorFactory.IDADE_MAX,
				StatusJogador.ATIVO);

		Map<Long, Map<Clube, List<Jogador>>> jogAgrupado = jogadores.stream()
				.collect(Collectors.groupingBy(j -> (j.getClube().getId() % FastfootApplication.NUM_THREAD),
						Collectors.groupingBy(j -> j.getClube())));

		Map<Long, Map<Clube, List<QuantitativoPosicaoClubeDTO>>> quantitativoPosicaoPorClubeMap = consultarQuantitativoPosicaoClubeService
				.consultarQuantitativoPosicaoClube(jogo).stream()
				.collect(Collectors.groupingBy(q -> (q.getClube().getId() % FastfootApplication.NUM_THREAD),
						Collectors.groupingBy(q -> q.getClube())));

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		for (long i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			completableFutures.add(aposentarJogadorService.aposentarJogador(jogo, jogAgrupado.get(i),
					quantitativoPosicaoPorClubeMap.get(i)));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

	}
	
	/*public List<QuantitativoPosicaoClubeDTO> getQuantitativoPosicaoClube(Jogo jogo) {

		List<Map<String, Object>> result = jogadorRepository.findQtdeJogadorPorPosicaoPorClube(
				PosicaoAttributeConverter.getInstance().convertToDatabaseColumn(Posicao.GOLEIRO),
				JogadorFactory.IDADE_MAX, jogo.getId());
		
		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeList = new ArrayList<QuantitativoPosicaoClubeDTO>();
		QuantitativoPosicaoClubeDTO dto = null;
		
		for (Map<String, Object> map : result) {
			dto = new QuantitativoPosicaoClubeDTO();
			dto.setClube(new Clube(DatabaseUtil.getValueLong(map.get("id_clube"))));
			dto.setPosicao(PosicaoAttributeConverter.getInstance().convertToEntityAttribute((String) map.get("posicao")));
			dto.setQtde(((BigInteger) map.get("total")).intValue());
			quantitativoPosicaoClubeList.add(dto);
		}
		
		return quantitativoPosicaoClubeList;
	}*/

	/*private void calcularValorTransferenciaJogadoresSimplificado(Jogo jogo) {

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);
		for (LigaJogo liga : ligaJogos) {
			completableFutures.add(calcularValorTransferenciaJogadorForcaGeralService.calcularValorTransferencia(liga, true));
			completableFutures.add(calcularValorTransferenciaJogadorForcaGeralService.calcularValorTransferencia(liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}*/

	/*private void calcularValorTransferenciaJogadores(Jogo jogo) {
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);
		for (LigaJogo liga : ligaJogos) {
			completableFutures
					.add(calcularValorTransferenciaJogadorPorHabilidadeService.calcularValorTransferencia(liga, true));
			completableFutures
					.add(calcularValorTransferenciaJogadorPorHabilidadeService.calcularValorTransferencia(liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}*/
	
	private void calcularValorTransferencia(Jogo jogo) {
		String metodoCalculo = carregarParametroService.getParametroString(jogo,
				ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA);

		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		if (ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_FORCA_GERAL.equals(metodoCalculo)) {
			for (LigaJogo liga : ligaJogos) {
				completableFutures
						.add(calcularValorTransferenciaJogadorForcaGeralService.calcularValorTransferencia(liga, true));
				completableFutures.add(
						calcularValorTransferenciaJogadorForcaGeralService.calcularValorTransferencia(liga, false));
			}
		} else if (ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_HABILIDADE.equals(metodoCalculo)) {
			for (LigaJogo liga : ligaJogos) {
				completableFutures.add(
						calcularValorTransferenciaJogadorPorHabilidadeService.calcularValorTransferencia(liga, true));
				completableFutures.add(
						calcularValorTransferenciaJogadorPorHabilidadeService.calcularValorTransferencia(liga, false));
			}
		} else if (ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_HABILIDADE_GRUPO
				.equals(metodoCalculo)) {
			for (LigaJogo liga : ligaJogos) {
				completableFutures.add(calcularValorTransferenciaJogadorPorHabilidadeGrupoService
						.calcularValorTransferencia(liga, true));
				completableFutures.add(calcularValorTransferenciaJogadorPorHabilidadeGrupoService
						.calcularValorTransferencia(liga, false));
			}
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}

	/*private void atualizarPassoDesenvolvimentoJogador(Jogo jogo) {

		List<Jogador> jogadores = jogadorRepository.findByJogoAndStatusJogadorFetchHabilidades(jogo, StatusJogador.ATIVO);
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService
						.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, jogadores.size())));
			} else {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService
						.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

	}*/
	
	private void atualizarPassoDesenvolvimentoJogador(Jogo jogo) {

		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);

		for (LigaJogo liga : ligaJogos) {
			completableFutures
					.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(liga, true));
			completableFutures
					.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

	}

	private void atualizarPassoDesenvolvimentoJogadorOtimizado(Jogo jogo) {
		
		int offset = 3;//(JogadorFactory.IDADE_MAX - JogadorFactory.IDADE_MIN) / FastfootApplication.NUM_THREAD;
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();

		for (int i = 0; i < NUM_THREAD_ATUALIZAR_PASSO_DESEN; i++) {
			if ((i + 1) == NUM_THREAD_ATUALIZAR_PASSO_DESEN) {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(jogo,
						(JogadorFactory.IDADE_MIN + i * offset), JogadorFactory.IDADE_MAX));
			} else {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(jogo,
						(JogadorFactory.IDADE_MIN + i * offset), (JogadorFactory.IDADE_MIN + (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}

	/*private void atualizarPassoDesenvolvimentoJogador2(Jogo jogo) {

		Temporada temporada = null;
		temporada = temporadaRepository.findFirstByJogoOrderByAnoDesc(jogo).get();

		HabilidadeEstatisticaPercentil hep = agruparHabilidadeValorEstatisticaService.getPercentilHabilidadeValor(temporada);
		List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = habilidadeValorEstatisticaGrupoRepository
				.findByTemporadaAndAmistoso(temporada, false);
		
		Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap = estatisticasGrupo.stream()
				.collect(Collectors.toMap(HabilidadeValorEstatisticaGrupo::getHabilidadeValor, Function.identity()));
			
		List<Jogador> jogadores = jogadorRepository.findByJogoAndStatusJogadorFetchHabilidades(jogo, StatusJogador.ATIVO);
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						jogadores.subList(i * offset, jogadores.size()), hep, estatisticasGrupoMap));
			} else {
				completableFutures.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(
						jogadores.subList(i * offset, (i + 1) * offset), hep, estatisticasGrupoMap));
			}
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}*/
	
	private void atualizarPassoDesenvolvimentoJogadorComEstatisticas(Jogo jogo) {

		Temporada temporada = null;
		temporada = temporadaRepository.findFirstByJogoOrderByAnoDesc(jogo).get();

		HabilidadeEstatisticaPercentil hep = agruparHabilidadeValorEstatisticaService.getPercentilHabilidadeValor(temporada);
		List<HabilidadeValorEstatisticaGrupo> estatisticasGrupo = habilidadeValorEstatisticaGrupoRepository
				.findByTemporadaAndAmistoso(temporada, false);
		
		Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap = estatisticasGrupo.stream()
				.collect(Collectors.toMap(HabilidadeValorEstatisticaGrupo::getHabilidadeValor, Function.identity()));
			
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(jogo);
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		for (LigaJogo liga : ligaJogos) {
			completableFutures
					.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(liga, true, hep, estatisticasGrupoMap));
			completableFutures
					.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(liga, false, hep, estatisticasGrupoMap));
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
	
	private void calcularPartidaProbabilidadeResultado(Temporada temporada) {
		
		if (carregarParametroService.getParametroBoolean(temporada.getJogo(), ParametroConstantes.USAR_APOSTAS_ESPORTIVAS) &&
				(carregarParametroService.getTipoProbabilidadeResultadoPartida(temporada.getJogo()).equals(TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA) ||
				carregarParametroService.getTipoProbabilidadeResultadoPartida(temporada.getJogo()).equals(TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA_HABILIDADE_GRUPO) ||
				carregarParametroService.getTipoProbabilidadeResultadoPartida(temporada.getJogo()).equals(TipoProbabilidadeResultadoPartida.FORCA_GERAL))) {
		
			Optional<Semana> semanaOpt = semanaRepository.findFirstByTemporadaAndNumero(temporada, temporada.getSemanaAtual() + 1);
			
			if (!semanaOpt.isPresent()) return;
			
			Semana semana = semanaOpt.get();
			
			List<Rodada> rodadas = rodadaRepository.findBySemana(semana);
			List<RodadaEliminatoria> rodadaEliminatorias = rodadaEliminatoriaRepository.findBySemana(semana);
			
			semana.setRodadas(rodadas);
			semana.setRodadasEliminatorias(rodadaEliminatorias);
			
			List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
	
			for (Rodada r : semana.getRodadas()) {
				completableFutures
						.add(calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(r,
								carregarParametroService.getTipoCampeonatoClubeProbabilidade(temporada.getJogo())));
			}
	
			for (RodadaEliminatoria r : semana.getRodadasEliminatorias()) {
				completableFutures
						.add(calcularPartidaProbabilidadeResultadoFacadeService.calcularPartidaProbabilidadeResultado(r,
								carregarParametroService.getTipoCampeonatoClubeProbabilidade(temporada.getJogo())));
			}
			
			CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();

		}
	}
	
	private void adequarModoDesenvolvimentoJogador(Temporada temporada) {
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo liga : ligaJogos) {
			completableFutures.add(
					adequarModoDesenvolvimentoJogadorService.adequarModoDesenvolvimentoJogador(temporada, liga, true));
			completableFutures.add(
					adequarModoDesenvolvimentoJogadorService.adequarModoDesenvolvimentoJogador(temporada, liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}

	private void gerarMudancaClubeNivel(Temporada temporada) {
		
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo l : ligaJogos) {
			completableFutures.add(atualizarClubeNivelService.atualizarClubeNivel(temporada, l));
		}
		
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
		
		/*
		completableFutures.clear();
		completableFutures.add(atualizarClubeNivelService.atualizarClubeNivelInternacional(temporada));
		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
		*/
	}

	private void renovarContratosAutomaticamente(Temporada temporada) {
		List<CompletableFuture<Boolean>> completableFutures = new ArrayList<CompletableFuture<Boolean>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo liga : ligaJogos) {
			completableFutures
					.add(renovarContratosAutomaticamenteService.renovarContratosAutomaticamente(temporada, liga, true));
			completableFutures
					.add(renovarContratosAutomaticamenteService.renovarContratosAutomaticamente(temporada, liga, false));
		}

		CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0])).join();
	}
	

}
