package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class CampeonatoCRUDService implements CRUDService<Campeonato, Long> {
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaService;

	@Override
	public List<Campeonato> getAll() {
		return campeonatoRepository.findAll();
	}

	@Override
	public Campeonato getById(Long id) {
		Optional<Campeonato> campeonatoOpt = campeonatoRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		campeonatoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		campeonatoRepository.deleteAll();
	}

	@Override
	public Campeonato create(Campeonato t) {
		return campeonatoRepository.save(t);
	}

	@Override
	public Campeonato update(Campeonato t) {
		return campeonatoRepository.save(t);
	}

	public List<Campeonato> getAllTemporadaAtual() {
		return campeonatoRepository.findByTemporada(temporadaService.getTemporadaAtual());
	}
}
