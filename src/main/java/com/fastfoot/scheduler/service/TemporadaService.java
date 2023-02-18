package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.club.service.util.ClubeRankingUtil;
import com.fastfoot.club.service.util.ClubeTituloRankingUtil;
import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;

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
	private CampeonatoService campeonatoService;

	public void classificarClubesTemporadaAtual() {
		Temporada temporada = temporadaRepository.findFirstByAtual(true).get();
		campeonatoService.carregarCampeonatosTemporada(temporada);
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubeRepository.findAll());
		List<ClubeTituloRanking> rankingsTitulos = clubeTituloRankingRepository.findAll();
		ClubeTituloRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
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
	
	public Temporada getTemporadaAnterior() {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstAnteriorAtual();
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
