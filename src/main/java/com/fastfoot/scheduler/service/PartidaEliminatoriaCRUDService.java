package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class PartidaEliminatoriaCRUDService implements CRUDService<PartidaEliminatoriaResultado, Long>{

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Override
	public List<PartidaEliminatoriaResultado> getAll() {
		return partidaEliminatoriaResultadoRepository.findAll();
	}

	@Override
	public PartidaEliminatoriaResultado getById(Long id) {
		Optional<PartidaEliminatoriaResultado> partidaOpt = partidaEliminatoriaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaEliminatoriaResultadoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaEliminatoriaResultadoRepository.deleteAll();
	}

	@Override
	public PartidaEliminatoriaResultado create(PartidaEliminatoriaResultado t) {
		return partidaEliminatoriaResultadoRepository.save(t);
	}

	@Override
	public PartidaEliminatoriaResultado update(PartidaEliminatoriaResultado t) {
		return partidaEliminatoriaResultadoRepository.save(t);
	}

}
