package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.repository.ClubeRepository;
import com.fastfoot.player.service.JogadorService;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.factory.CampeonatoEliminatorioFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoFactory;
import com.fastfoot.scheduler.model.factory.CampeonatoMistoFactory;
import com.fastfoot.scheduler.model.factory.TemporadaFactory;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.scheduler.service.util.ClubeRankingUtil;
import com.fastfoot.service.PreCarregarService;

@Service
public class TemporadaService {
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private SemanaRepository semanaRepository;
	
	@Autowired
	private CampeonatoService campeonatoService;
	
	@Autowired
	private PreCarregarService preCarregarService;
	
	@Autowired
	private JogadorService jogadorService;

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
			jogadorService.criarJogadoresClube();
		}

		temporada = TemporadaFactory.criarTempordada(ano);	
		
		List<Campeonato> campeonatosNacionais = new ArrayList<Campeonato>();
		List<CampeonatoMisto> campeonatosContinentais = new ArrayList<CampeonatoMisto>();
		List<CampeonatoEliminatorio> copasNacionais = new ArrayList<CampeonatoEliminatorio>();
		
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
			
			copaNacional = CampeonatoEliminatorioFactory.criarCampeonatoCopaNacional(temporada, liga, clubesRk, NivelCampeonato.COPA_NACIONAL);
			
			copasNacionais.add(copaNacional);
			
			copaNacional = CampeonatoEliminatorioFactory.criarCampeonatoCopaNacionalII(temporada, liga, NivelCampeonato.COPA_NACIONAL_II);
			
			copasNacionais.add(copaNacional);
		}
		
		//Continental I e II
		Map<Liga, List<Clube>> clubesContinental = null;
		CampeonatoMisto campeonatoContinental = null;
		for (int i = 0; i < Constantes.NRO_COMPETICOES_CONT; i++) {
			clubesContinental = new HashMap<Liga, List<Clube>>();
			
			int posInicial = i*Constantes.NRO_CLUBES_POR_LIGA_CONT; 
			int posFinal = (i+1)*Constantes.NRO_CLUBES_POR_LIGA_CONT;
			
			
			for (Liga liga : Liga.getAll()) {
				clubesContinental.put(liga, clubeRepository.findByLigaAndAnoAndPosicaoGeralBetween(liga, ano - 1, posInicial + 1, posFinal));
			}
			
			campeonatoContinental = CampeonatoMistoFactory.criarCampeonato(temporada, clubesContinental, (i == 0 ? NivelCampeonato.CONTINENTAL : NivelCampeonato.CONTINENTAL_II));
			campeonatosContinentais.add(campeonatoContinental);
		}
		salvar(temporada, campeonatosNacionais, campeonatosContinentais, copasNacionais);
		
		return TemporadaDTO.convertToDTO(temporada);
	}

	private void salvar(Temporada temporada, List<Campeonato> campeonatosNacionais, List<CampeonatoMisto> campeonatosContinentais, List<CampeonatoEliminatorio> copasNacionais) {
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
	}
	
	private void salvarTemporada(Temporada temporada) {
		temporadaRepository.save(temporada);
		semanaRepository.saveAll(temporada.getSemanas());
	}

	public void classificarClubesTemporadaAtual() {
		Temporada temporada = temporadaRepository.findByAtual(true).get(0);
		campeonatoService.carregarCampeonatosTemporada(temporada);
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubeRepository.findAll());
		clubeRankingRepository.saveAll(rankings);
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
