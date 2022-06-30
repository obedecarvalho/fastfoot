package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AgruparHabilidadeValorEstatisticaService;
import com.fastfoot.player.service.AposentarJogadorService;
import com.fastfoot.player.service.AtualizarPassoDesenvolvimentoJogadorService;
import com.fastfoot.player.service.CalcularValorTransferenciaService;
import com.fastfoot.player.service.CriarJogadoresClubeService;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplDezesseisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplTrintaEDoisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEOitoClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEQuatroClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactory;
import com.fastfoot.scheduler.model.factory.RodadaAmistosaFactory;
import com.fastfoot.scheduler.model.factory.TemporadaFactory;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.ParametroService;
import com.fastfoot.service.PreCarregarParametrosService;
import com.fastfoot.service.PreCarregarClubeService;

@Service
public class CriarCalendarioTemporadaService {
	
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
	
	//#######	SERVICE	#############
	
	@Autowired
	private PreCarregarClubeService preCarregarService;
	
	@Autowired
	private PreCarregarParametrosService preCarregarParametrosService;

	/*@Autowired
	private JogadorService jogadorService;*/

	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private RodadaAmistosaService rodadaAmistosaService;

	@Autowired
	private AposentarJogadorService aposentarJogadorService;
	
	@Autowired
	private CalcularValorTransferenciaService calcularValorTransferenciaService;
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	@Autowired
	private CriarJogadoresClubeService criarJogadoresClubeService;

	@Autowired
	private AtualizarPassoDesenvolvimentoJogadorService atualizarPassoDesenvolvimentoJogadorService;
	
	@Autowired
	private AgruparHabilidadeValorEstatisticaService agruparHabilidadeValorEstatisticaService;

	public TemporadaDTO criarTemporada() {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<String> mensagens = new ArrayList<String>();
		long inicio = 0, fim = 0;

		Temporada temporada = null;
		Integer ano = Constantes.ANO_INICIAL;
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);

		if (temporadaOpt.isPresent()) {
			temporada = temporadaOpt.get();
			
			if (temporada.getSemanaAtual() < Constantes.NUM_SEMANAS) {
				throw new RuntimeException("Temporada ainda não terminada!");
			}
			
			//
			agruparHabilidadeValorEstatisticaService.agrupar(temporada);
			//
			
			temporada.setAtual(false);
			temporadaRepository.save(temporada);
			ano = temporada.getAno() + 1;
			
			//TODO: mesclar atualizarPassoDesenvolvimentoJogador() e aposentarJogadores()??

			stopWatch.split();
			inicio = stopWatch.getSplitNanoTime();
			atualizarPassoDesenvolvimentoJogador();
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#atualizarPassoDesenvolvimentoJogador:" + (fim - inicio));//35%
			
			inicio = stopWatch.getSplitNanoTime();
			aposentarJogadores();
			stopWatch.split();
			fim = stopWatch.getSplitNanoTime();
			mensagens.add("\t#aposentarJogadores:" + (fim - inicio));

		} else {

			preCarregarParametrosService.preCarregarParametros();

			preCarregarService.preCarregarClubes();
			
			if (jogadorRepository.findAll().isEmpty()) {//TODO: usado em DEV
				List<Clube> clubes = clubeRepository.findAll();
				
				List<CompletableFuture<Boolean>> criarJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
				
				int offset = clubes.size() / FastfootApplication.NUM_THREAD;
				
				for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
					if ((i + 1) == FastfootApplication.NUM_THREAD) {
						criarJogadorFuture.add(criarJogadoresClubeService.criarJogadoresClube(clubes.subList(i * offset, clubes.size())));
						//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
					} else {
						criarJogadorFuture.add(criarJogadoresClubeService.criarJogadoresClube(clubes.subList(i * offset, (i+1) * offset)));
						//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
					}
				}
				
				CompletableFuture.allOf(criarJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
				
				/*CompletableFuture<Boolean> cj1 = jogadorService.criarJogadoresClube(clubes.subList(0, 16));
				CompletableFuture<Boolean> cj5 = jogadorService.criarJogadoresClube(clubes.subList(16, 32));
				CompletableFuture<Boolean> cj2 = jogadorService.criarJogadoresClube(clubes.subList(32, 48));
				CompletableFuture<Boolean> cj6 = jogadorService.criarJogadoresClube(clubes.subList(48, 64));
				CompletableFuture<Boolean> cj3 = jogadorService.criarJogadoresClube(clubes.subList(64, 80));
				CompletableFuture<Boolean> cj7 = jogadorService.criarJogadoresClube(clubes.subList(80, 96));
				CompletableFuture<Boolean> cj4 = jogadorService.criarJogadoresClube(clubes.subList(96, 112));
				CompletableFuture<Boolean> cj8 = jogadorService.criarJogadoresClube(clubes.subList(112, 128));

				CompletableFuture.allOf(cj1, cj2, cj3, cj4, cj5, cj6, cj7, cj8).join();*/
			}
		}

		stopWatch.split();
		inicio = stopWatch.getSplitNanoTime();
		escalarClubes();
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("\t#escalarClubes:" + (fim - inicio));
		
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
		
		stopWatch.stop();
		mensagens.add("\tTempo total:" + stopWatch.getNanoTime());
		
		System.err.println(mensagens);
		
		return TemporadaDTO.convertToDTO(temporada);
	}
	
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
				
				clubes = clubeRepository.findByLigaAndAnoAndClassificacaoNacionalBetween(liga, ano - 1,
						(i == 0 ? ClassificacaoNacionalFinal.getAllNacionalNovoCampeonato() : ClassificacaoNacionalFinal.getAllNacionalIINovoCampeonato()));
				
				campeonatoNacional = CampeonatoFactory.criarCampeonato(temporada, liga, clubes, (i == 0 ? NivelCampeonato.NACIONAL : NivelCampeonato.NACIONAL_II));

				campeonatosNacionais.add(campeonatoNacional);
			}

			//Copa Nacional I e II
			
			clubesRk = clubeRankingRepository.findByLigaAndAno(liga, ano - 1);
			
			copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacional(temporada, liga, clubesRk, NivelCampeonato.COPA_NACIONAL);
			
			copasNacionais.add(copaNacional);
			
			if (jogarCopaNacII) {
				copaNacional = campeonatoEliminatorioFactory.criarCampeonatoCopaNacionalII(temporada, liga, clubesRk, NivelCampeonato.COPA_NACIONAL_II);
				
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

			campeonatoContinental = CampeonatoMistoFactory.criarCampeonato(temporada, clubesContinental, NivelCampeonato.getContinentalPorOrdem(i));
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

		CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = null;

		if (numRodadas == 4) {
			campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplDezesseisClubes();
		} else if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplTrintaEDoisClubes();
			} else if (nroCompeticoesContinentais == 3) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEOitoClubes();
			}
		} else if (numRodadas == 5) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEQuatroClubes();
			} else if (nroCompeticoesContinentais == 3) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteClubes();
			}
		}

		return campeonatoEliminatorioFactory;
	}

	private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {

		salvarTemporada(temporada);

		/*for(Campeonato campeonato : campeonatosNacionais) {
			campeonatoService.salvarCampeonato(campeonato);
		}*/
		campeonatoService.salvarCampeonato(campeonatosNacionais);

		/*for(CampeonatoEliminatorio campeonatoEliminatorio : copasNacionais) {
			campeonatoService.salvarCampeonatoEliminatorio(campeonatoEliminatorio);
		}*/
		campeonatoService.salvarCampeonatoEliminatorio(copasNacionais);

		/*for(CampeonatoMisto campeonatoMisto : campeonatosContinentais) {
			campeonatoService.salvarCampeonatoMisto(campeonatoMisto);
		}*/
		campeonatoService.salvarCampeonatoMisto(campeonatosContinentais);

		rodadaAmistosaService.salvarRodadasAmistosas(rodadaAmistosaAutomaticas);

	}
	
	private void salvarTemporada(Temporada temporada) {
		temporadaRepository.save(temporada);
		semanaRepository.saveAll(temporada.getSemanas());
	}

	private void aposentarJogadores() {

		//if (semana.getNumero() == 25) {
			
		List<GrupoDesenvolvimentoJogador> gds = grupoDesenvolvimentoJogadorRepository.findByAtivoAndIdadeJogador(Boolean.TRUE, JogadorFactory.IDADE_MAX);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = gds.size() / FastfootApplication.NUM_THREAD;
		
		//System.err.println("\t\t->Total: " + gds.size());
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, gds.size())));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
			} else {
				desenvolverJogadorFuture.add(aposentarJogadorService.aposentarJogador(gds.subList(i * offset, (i+1) * offset)));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}

	private void calcularValorTransferenciaJogadores() {
		//if (semana.getNumero() == 1) {
			
		List<Jogador> jogadores = jogadorRepository.findByAposentado(Boolean.FALSE);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(calcularValorTransferenciaService.calcularValorTransferencia(jogadores.subList(i * offset, jogadores.size())));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
			} else {
				desenvolverJogadorFuture.add(calcularValorTransferenciaService.calcularValorTransferencia(jogadores.subList(i * offset, (i+1) * offset)));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}

	private void escalarClubes() {
		//if (semana.getNumero() % 5 == 0) {
		List<Clube> clubes = clubeRepository.findAll(); 
		
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

	private void atualizarPassoDesenvolvimentoJogador() {
		//if (semana.getNumero() == 1) {
			
		List<Jogador> jogadores = jogadorRepository.findByAposentadoFetchHabilidades(Boolean.FALSE);
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = jogadores.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, jogadores.size())));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + gds.size());
			} else {
				desenvolverJogadorFuture.add(atualizarPassoDesenvolvimentoJogadorService.ajustarPassoDesenvolvimento(jogadores.subList(i * offset, (i+1) * offset)));
				//System.err.println("\t\t->I: " + (i * offset) + ", F: " + ((i+1) * offset));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//}
	}
}
