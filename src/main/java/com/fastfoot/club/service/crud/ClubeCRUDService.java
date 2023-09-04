package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.service.CRUDServiceJogavel;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class ClubeCRUDService implements CRUDServiceJogavel<Clube, Long> {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Override
	public List<Clube> getAll() {
		return clubeRepository.findAll();
	}
	
	@Override
	public List<Clube> getByJogo(Jogo jogo) {
		return clubeRepository.findByJogo(jogo);
	}

	@Override
	public Clube getById(Long id) {
		Optional<Clube> clubeOpt = clubeRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
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
	
	public List<Clube> getByLiga(Jogo jogo, Integer ligaOrdinal){
		
		Liga liga = Liga.values()[ligaOrdinal];
		
		LigaJogo ligaJogo = ligaJogoCRUDService.getByJogo(jogo, liga);

		return clubeRepository.findByLigaJogo(ligaJogo);

	}

}
