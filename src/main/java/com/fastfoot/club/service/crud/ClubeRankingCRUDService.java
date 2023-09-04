package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.service.CRUDServiceJogavel;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ClubeRankingCRUDService implements CRUDServiceJogavel<ClubeRanking, Long> {
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Override
	public List<ClubeRanking> getAll() {
		return clubeRankingRepository.findAll();
	}
	
	@Override
	public List<ClubeRanking> getByJogo(Jogo jogo) {
		return clubeRankingRepository.findByJogo(jogo);
	}

	@Override
	public ClubeRanking getById(Long id) {
		Optional<ClubeRanking> clubeOpt = clubeRankingRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		clubeRankingRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		clubeRankingRepository.deleteAll();
	}

	@Override
	public ClubeRanking create(ClubeRanking t) {
		return clubeRankingRepository.save(t);
	}

	@Override
	public ClubeRanking update(ClubeRanking t) {
		return clubeRankingRepository.save(t);
	}
	
	public List<ClubeRanking> getByClube(Clube clube){
		return clubeRankingRepository.findByClube(clube);
	}

	public List<ClubeRanking> getByLigaAndAno(Jogo jogo, Integer ligaOrdinal, Integer ano){

		Liga liga = Liga.values()[ligaOrdinal];
		
		LigaJogo ligaJogo = ligaJogoCRUDService.getByJogo(jogo, liga);
		
		return clubeRankingRepository.findByLigaJogoAndAno(ligaJogo, ano);

	}
}
