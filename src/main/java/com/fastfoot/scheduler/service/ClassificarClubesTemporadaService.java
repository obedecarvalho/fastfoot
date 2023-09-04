package com.fastfoot.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.club.service.util.ClubeRankingUtil;
import com.fastfoot.club.service.util.ClubeTituloRankingUtil;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ClassificarClubesTemporadaService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;

	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;
	
	/*@Autowired
	private TemporadaCRUDService temporadaCRUDService;*/
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	public void classificarClubesTemporadaAtual(Temporada temporada) {
		//Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		List<Clube> clubes = clubeRepository.findByJogo(temporada.getJogo());
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubes, ligaJogos);
		List<ClubeTituloRanking> rankingsTitulos = clubeTituloRankingRepository.findAll();
		ClubeTituloRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
		clubeRankingRepository.saveAll(rankings);
		clubeTituloRankingRepository.saveAll(rankingsTitulos);
	}

}
