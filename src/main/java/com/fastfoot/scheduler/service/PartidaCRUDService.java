package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.service.CRUDService;

@Service
public class PartidaCRUDService implements CRUDService<PartidaResultado, Long>{
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;

	@Override
	public List<PartidaResultado> getAll() {
		return partidaResultadoRepository.findAll();
	}

	@Override
	public PartidaResultado getById(Long id) {
		Optional<PartidaResultado> partidaOpt = partidaResultadoRepository.findById(id);
		if (partidaOpt.isPresent()) {
			return partidaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaResultadoRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaResultadoRepository.deleteAll();
	}

	@Override
	public PartidaResultado create(PartidaResultado t) {
		return partidaResultadoRepository.save(t);
	}

	@Override
	public PartidaResultado update(PartidaResultado t) {
		return partidaResultadoRepository.save(t);
	}

}
