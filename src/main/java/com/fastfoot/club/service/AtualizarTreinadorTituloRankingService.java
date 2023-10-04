package com.fastfoot.club.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.TreinadorTituloRankingRepository;
import com.fastfoot.club.service.util.ClubeRankingUtil;
import com.fastfoot.club.service.util.TreinadorTituloRankingUtil;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class AtualizarTreinadorTituloRankingService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	/*@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;*/
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Autowired
	private TreinadorTituloRankingRepository treinadorTituloRankingRepository;

	public void atualizarTreinadorTituloRanking(Temporada temporada) {
		//carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		List<Clube> clubes = clubeRepository.findByJogo(temporada.getJogo());
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubes, ligaJogos);
		
		List<TreinadorTituloRanking> rankingsTitulos = treinadorTituloRankingRepository.findByJogo(temporada.getJogo());
		TreinadorTituloRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
		treinadorTituloRankingRepository.saveAll(rankingsTitulos);
	}

}
