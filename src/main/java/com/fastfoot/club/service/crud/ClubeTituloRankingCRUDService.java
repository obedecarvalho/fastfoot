package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.service.CRUDServiceJogavel;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ClubeTituloRankingCRUDService implements CRUDServiceJogavel<ClubeTituloRanking, Long> {
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Override
	public List<ClubeTituloRanking> getAll() {
		return clubeTituloRankingRepository.findAll();
	}
	
	@Override
	public List<ClubeTituloRanking> getByJogo(Jogo jogo) {
		return clubeTituloRankingRepository.findByJogo(jogo);
	}

	@Override
	public ClubeTituloRanking getById(Long id) {
		Optional<ClubeTituloRanking> clubeOpt = clubeTituloRankingRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		clubeTituloRankingRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		clubeTituloRankingRepository.deleteAll();
	}

	@Override
	public ClubeTituloRanking create(ClubeTituloRanking t) {
		return clubeTituloRankingRepository.save(t);
	}

	@Override
	public ClubeTituloRanking update(ClubeTituloRanking t) {
		return clubeTituloRankingRepository.save(t);
	}
	
	public List<ClubeTituloRanking> getByLiga(Jogo jogo, String ligaStr){

		Liga liga = null;

		try {
			liga = Liga.valueOf(ligaStr);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return getByLiga(jogo, liga);
	}
	
	public List<ClubeTituloRanking> getByLiga(Jogo jogo, Integer ligaOrdinal){
		Liga liga = Liga.values()[ligaOrdinal];
		return getByLiga(jogo, liga);
	}

	public List<ClubeTituloRanking> getByLiga(Jogo jogo, Liga liga){
		LigaJogo ligaJogo = ligaJogoCRUDService.getByJogo(jogo, liga);
		return clubeTituloRankingRepository.findByLigaJogo(ligaJogo);
	}
}
