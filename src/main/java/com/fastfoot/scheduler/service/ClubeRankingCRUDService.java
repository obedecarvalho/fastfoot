package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.repository.ClubeRankingRepository;
import com.fastfoot.service.CRUDService;

@Service
public class ClubeRankingCRUDService implements CRUDService<ClubeRanking, Integer> {
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;

	@Override
	public List<ClubeRanking> getAll() {
		return clubeRankingRepository.findAll();
	}

	@Override
	public ClubeRanking getById(Integer id) {
		Optional<ClubeRanking> clubeOpt = clubeRankingRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Integer id) {
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

	public List<ClubeRanking> getByLigaAndAno(Integer ligaOrdinal, Integer ano){

		Liga liga = Liga.values()[ligaOrdinal];
		return clubeRankingRepository.findByLigaAndAno(liga, ano);

	}
}
