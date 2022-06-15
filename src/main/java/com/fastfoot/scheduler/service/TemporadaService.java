package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.repository.ClubeRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.JogadorService;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplTrintaEDoisClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEOitoClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplVinteEQuatroClubes;
import com.fastfoot.scheduler.model.factory.CampeonatoFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactory;
import com.fastfoot.scheduler.model.factory.RodadaAmistosaFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactoryImplDezesseisClubes;
import com.fastfoot.scheduler.model.factory.TemporadaFactory;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.scheduler.service.util.ClubeRankingUtil;
import com.fastfoot.service.ParametroService;
import com.fastfoot.service.PreCarregarService;

@Service
public class TemporadaService {
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;
	
	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private SemanaRepository semanaRepository;

	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private PreCarregarService preCarregarService;
	
	@Autowired
	private JogadorService jogadorService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private RodadaAmistosaService rodadaAmistosaService;

	public TemporadaDTO criarTemporada() {

		Temporada temporada = null;
		Integer ano = Constantes.ANO_INICIAL;
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);

		if (temporadaOpt.isPresent()) {
			temporada = temporadaOpt.get();
			
			if (temporada.getSemanaAtual() < Constantes.NUM_SEMANAS) {
				throw new RuntimeException("Temporada ainda não terminada!");
			}
			
			temporada.setAtual(false);
			temporadaRepository.save(temporada);
			ano = temporada.getAno() + 1;
		} else {
			preCarregarService.preCarregarClubes();
			
			if (jogadorRepository.findAll().isEmpty()) {//TODO
				List<Clube> clubes = clubeRepository.findAll();
				CompletableFuture<Boolean> cj1 = jogadorService.criarJogadoresClube(clubes.subList(0, 16));
				CompletableFuture<Boolean> cj5 = jogadorService.criarJogadoresClube(clubes.subList(16, 32));
				CompletableFuture<Boolean> cj2 = jogadorService.criarJogadoresClube(clubes.subList(32, 48));
				CompletableFuture<Boolean> cj6 = jogadorService.criarJogadoresClube(clubes.subList(48, 64));
				CompletableFuture<Boolean> cj3 = jogadorService.criarJogadoresClube(clubes.subList(64, 80));
				CompletableFuture<Boolean> cj7 = jogadorService.criarJogadoresClube(clubes.subList(80, 96));
				CompletableFuture<Boolean> cj4 = jogadorService.criarJogadoresClube(clubes.subList(96, 112));
				CompletableFuture<Boolean> cj8 = jogadorService.criarJogadoresClube(clubes.subList(112, 128));
				
				CompletableFuture.allOf(cj1, cj2, cj3, cj4, cj5, cj6, cj7, cj8).join();//TODO: remover??
			}
		}

		temporada = TemporadaFactory.criarTempordada(ano);	
		
		List<Campeonato> campeonatosNacionais = new ArrayList<Campeonato>();
		List<CampeonatoMisto> campeonatosContinentais = new ArrayList<CampeonatoMisto>();
		List<CampeonatoEliminatorio> copasNacionais = new ArrayList<CampeonatoEliminatorio>();
		List<RodadaAmistosa> rodadaAmistosaAutomaticas = new ArrayList<RodadaAmistosa>();

		criarCampeonatos(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);

		salvar(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais, rodadaAmistosaAutomaticas);
		
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

	/*private CampeonatoEliminatorioFactory getCampeonatoEliminatorioFactory(String numeroRodadasCopaNacional, Integer nroCompeticoesContinentais) {

		CampeonatoEliminatorioFactory campeonatoEliminatorioFactory = null;

		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadasCopaNacional)) {
			campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplDezesseisClubes();
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplTrintaEDoisClubes();
			} else if (nroCompeticoesContinentais == 3) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEOitoClubes();
			}
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadasCopaNacional)) {
			if (nroCompeticoesContinentais == 2) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteEQuatroClubes();
			} else if (nroCompeticoesContinentais == 3) {
				campeonatoEliminatorioFactory = new CampeonatoEliminatorioFactoryImplVinteClubes();
			}
		}

		return campeonatoEliminatorioFactory;
	}*/

	private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais,
			List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais,
			List<RodadaAmistosa> rodadaAmistosaAutomaticas) {
		salvarTemporada(temporada);
		for(Campeonato campeonato : campeonatosNacionais) {
			campeonatoService.salvarCampeonato(campeonato);
		}
		for(CampeonatoEliminatorio campeonatoEliminatorio : copasNacionais) {
			campeonatoService.salvarCampeonatoEliminatorio(campeonatoEliminatorio);
		}
		for(CampeonatoMisto campeonatoMisto : campeonatosContinentais) {
			campeonatoService.salvarCampeonatoMisto(campeonatoMisto);
		}
		rodadaAmistosaService.salvarRodadasAmistosas(rodadaAmistosaAutomaticas);
	}
	
	private void salvarTemporada(Temporada temporada) {
		temporadaRepository.save(temporada);
		semanaRepository.saveAll(temporada.getSemanas());
	}

	public void classificarClubesTemporadaAtual() {
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		campeonatoService.carregarCampeonatosTemporada(temporada);
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubeRepository.findAll());
		List<ClubeTituloRanking> rankingsTitulos = clubeTituloRankingRepository.findAll();
		ClubeRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
		clubeRankingRepository.saveAll(rankings);
		clubeTituloRankingRepository.saveAll(rankingsTitulos);
	}

	public List<CampeonatoDTO> getCampeonatosTemporada(String nivel) {//'NACIONAL', 'COPA NACIONAL', 'CONTINENTAL'
		
		List<CampeonatoDTO> campeonatos = null;
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		
		if (temporadaOpt.isPresent()) {
			campeonatos = CampeonatoDTO.convertToDTO(campeonatoService.carregarCampeonatosTemporada(temporadaOpt.get(), nivel));
		}
		return campeonatos;
	}

	public Temporada getTemporadaAtual() {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		if (temporadaOpt.isPresent()) {
			return temporadaOpt.get();
		}
		return null;
	}

	public List<TemporadaDTO> getTemporadas() {
		return TemporadaDTO.convertToDTO(temporadaRepository.findAll());
	}

	public List<Integer> getAnosTemporadas(){
		return temporadaRepository.getAnosTemporadas();
	}
}
