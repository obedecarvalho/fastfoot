package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;
import com.fastfoot.service.CRUDServiceJogavel;

@Service
public class TemporadaCRUDService implements CRUDServiceJogavel<Temporada, Long> {

	@Autowired
	private TemporadaRepository temporadaRepository;

	@Override
	public List<Temporada> getAll() {
		return temporadaRepository.findAll();
	}
	
	@Override
	public List<Temporada> getByJogo(Jogo jogo) {
		return temporadaRepository.findByJogo(jogo);
	}

	@Override
	public Temporada getById(Long id) {
		Optional<Temporada> temporadaOpt = temporadaRepository.findById(id);
		if (temporadaOpt.isPresent()) {
			return temporadaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		temporadaRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		temporadaRepository.deleteAll();
	}

	@Override
	public Temporada create(Temporada t) {
		return temporadaRepository.save(t);
	}

	@Override
	public Temporada update(Temporada t) {
		return temporadaRepository.save(t);
	}
	
	public Temporada getTemporadaAtual(Jogo jogo) {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByJogoAndAtual(jogo, true);
		if (temporadaOpt.isPresent()) {
			return temporadaOpt.get();
		}
		return null;
	}
	
	public Temporada getTemporadaByAno(Jogo jogo, Integer ano) {
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByJogoAndAno(jogo, ano);
		if (temporadaOpt.isPresent()) {
			return temporadaOpt.get();
		}
		return null;
	}
}
