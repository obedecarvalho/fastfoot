package com.fastfoot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.model.Liga;

@Service
public class ClubeCRUDService implements CRUDService<Clube, Integer> {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Override
	public List<Clube> getAll() {
		return clubeRepository.findAll();
	}

	@Override
	public Clube getById(Integer id) {
		Optional<Clube> clubeOpt = clubeRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Integer id) {
		clubeRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		clubeRepository.deleteAll();
	}

	@Override
	public Clube create(Clube t) {
		return clubeRepository.save(t);
	}

	@Override
	public Clube update(Clube t) {
		return clubeRepository.save(t);
	}
	
	public List<Clube> getByLiga(Integer ligaOrdinal){

		Liga liga = Liga.values()[ligaOrdinal];
		return clubeRepository.findByLiga(liga);

	}

}
