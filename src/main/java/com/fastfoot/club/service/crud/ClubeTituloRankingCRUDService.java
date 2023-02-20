package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.service.CRUDService;

@Service
public class ClubeTituloRankingCRUDService implements CRUDService<ClubeTituloRanking, Integer> {
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;

	@Override
	public List<ClubeTituloRanking> getAll() {
		return clubeTituloRankingRepository.findAll();
	}

	@Override
	public ClubeTituloRanking getById(Integer id) {
		Optional<ClubeTituloRanking> clubeOpt = clubeTituloRankingRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Integer id) {
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
	
	public List<ClubeTituloRanking> getByLiga(String ligaStr){

		Liga liga = null;

		try {
			liga = Liga.valueOf(ligaStr);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return getByLiga(liga);
	}
	
	public List<ClubeTituloRanking> getByLiga(Integer ligaOrdinal){
		Liga liga = Liga.values()[ligaOrdinal];
		return getByLiga(liga);
	}

	public List<ClubeTituloRanking> getByLiga(Liga liga){
		return clubeTituloRankingRepository.findByLiga(liga);
	}
}
