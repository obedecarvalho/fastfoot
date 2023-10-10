package com.fastfoot.club.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.service.util.ClubeRankingUtil;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.CarregarCampeonatoService;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ClassificarClubesTemporadaService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	public void classificarClubesTemporadaAtual(Temporada temporada) {
		carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		List<Clube> clubes = clubeRepository.findByJogo(temporada.getJogo());
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubes, ligaJogos);
		/* Substituido por AtualizarClubeTituloRankingService
		List<ClubeTituloRanking> rankingsTitulos = clubeTituloRankingRepository.findByJogo(temporada.getJogo());
		ClubeTituloRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
		clubeTituloRankingRepository.saveAll(rankingsTitulos);
		*/
		clubeRankingRepository.saveAll(rankings);
		
	}

}
