package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.service.CRUDService;

@Service
public class CampeonatoEliminatorioCRUDService implements CRUDService<CampeonatoEliminatorio, Long> {
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

	@Override
	public List<CampeonatoEliminatorio> getAll() {
		return campeonatoEliminatorioRepository.findAll();
	}

	@Override
	public CampeonatoEliminatorio getById(Long id) {
		Optional<CampeonatoEliminatorio> campeonatoOpt = campeonatoEliminatorioRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		campeonatoEliminatorioRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		campeonatoEliminatorioRepository.deleteAll();
	}

	@Override
	public CampeonatoEliminatorio create(CampeonatoEliminatorio t) {
		return campeonatoEliminatorioRepository.save(t);
	}

	@Override
	public CampeonatoEliminatorio update(CampeonatoEliminatorio t) {
		return campeonatoEliminatorioRepository.save(t);
	}

	public List<CampeonatoEliminatorio> getAllTemporadaAtual(Jogo jogo) {
		return campeonatoEliminatorioRepository.findByTemporada(temporadaCRUDService.getTemporadaAtual(jogo));
	}
}
