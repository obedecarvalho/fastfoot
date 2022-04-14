package com.fastfoot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.dto.ClubeDTO;
import com.fastfoot.scheduler.model.dto.ClubeRankingDTO;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.scheduler.service.TemporadaService;

@Service
public class ClubeService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Autowired
	private TemporadaService temporadaService;

	public List<ClubeDTO> getClubesPorLiga(String liga) {//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		List<Clube> clubes = clubeRepository.findByLiga(Liga.valueOf(liga));
		return ClubeDTO.convertToDTO(clubes);
	}
	
	public List<ClubeRankingDTO> getClubesRankings(String liga){//'GENEBE', 'SPAPOR', 'ITAFRA', 'ENGLND'
		Temporada temporada = temporadaService.getTemporadaAtual();
		return temporada != null ? ClubeRankingDTO.convertToDTO(clubeRankingRepository.findByLigaAndAno(Liga.valueOf(liga), temporada.getAno()-1)) : null;
	}
}
