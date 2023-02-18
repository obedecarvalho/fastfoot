package com.fastfoot.match.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.repository.PartidaEstatisticasRepository;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.service.CRUDService;

@Service
public class PartidaEstatisticasCRUDService implements CRUDService<PartidaEstatisticas, Long> {

	@Autowired
	private PartidaEstatisticasRepository partidaEstatisticasRepository;

	@Override
	public List<PartidaEstatisticas> getAll() {
		return partidaEstatisticasRepository.findAll();
	}

	@Override
	public PartidaEstatisticas getById(Long id) {
		Optional<PartidaEstatisticas> clubeOpt = partidaEstatisticasRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaEstatisticasRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaEstatisticasRepository.deleteAll();
	}

	@Override
	public PartidaEstatisticas create(PartidaEstatisticas t) {
		return partidaEstatisticasRepository.save(t);
	}

	@Override
	public PartidaEstatisticas update(PartidaEstatisticas t) {
		return partidaEstatisticasRepository.save(t);
	}
}
