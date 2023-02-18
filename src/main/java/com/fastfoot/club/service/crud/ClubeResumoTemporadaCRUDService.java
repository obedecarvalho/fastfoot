package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.club.model.repository.ClubeResumoTemporadaRepository;
import com.fastfoot.service.CRUDService;

@Service
public class ClubeResumoTemporadaCRUDService implements CRUDService<ClubeResumoTemporada, Long> {

	@Autowired
	private ClubeResumoTemporadaRepository clubeResumoTemporadaRepository;

	@Override
	public List<ClubeResumoTemporada> getAll() {
		return clubeResumoTemporadaRepository.findAll();
	}

	@Override
	public ClubeResumoTemporada getById(Long id) {
		Optional<ClubeResumoTemporada> clubeOpt = clubeResumoTemporadaRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		clubeResumoTemporadaRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		clubeResumoTemporadaRepository.deleteAll();
	}

	@Override
	public ClubeResumoTemporada create(ClubeResumoTemporada t) {
		return clubeResumoTemporadaRepository.save(t);
	}

	@Override
	public ClubeResumoTemporada update(ClubeResumoTemporada t) {
		return clubeResumoTemporadaRepository.save(t);
	}

	public List<ClubeResumoTemporada> getByClube(Clube clube) {
		return clubeResumoTemporadaRepository.findByClube(clube);
	}
}
