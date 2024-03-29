package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class CampeonatoMistoCRUDService implements CRUDService<CampeonatoMisto, Long> {
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

	@Override
	public List<CampeonatoMisto> getAll() {
		return campeonatoMistoRepository.findAll();
	}

	@Override
	public CampeonatoMisto getById(Long id) {
		Optional<CampeonatoMisto> campeonatoOpt = campeonatoMistoRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		campeonatoMistoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		campeonatoMistoRepository.deleteAll();
	}

	@Override
	public CampeonatoMisto create(CampeonatoMisto t) {
		return campeonatoMistoRepository.save(t);
	}

	@Override
	public CampeonatoMisto update(CampeonatoMisto t) {
		return campeonatoMistoRepository.save(t);
	}

	public List<CampeonatoMisto> getAllTemporadaAtual(Jogo jogo) {
		return campeonatoMistoRepository.findByTemporada(temporadaCRUDService.getTemporadaAtual(jogo));
	}
}
