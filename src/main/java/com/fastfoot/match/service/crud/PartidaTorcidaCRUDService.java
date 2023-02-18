package com.fastfoot.match.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.match.model.repository.PartidaTorcidaRepository;
import com.fastfoot.service.CRUDService;

@Service
public class PartidaTorcidaCRUDService implements CRUDService<PartidaTorcida, Long> {

	@Autowired
	private PartidaTorcidaRepository partidaTorcidaRepository;

	@Override
	public List<PartidaTorcida> getAll() {
		return partidaTorcidaRepository.findAll();
	}

	@Override
	public PartidaTorcida getById(Long id) {
		Optional<PartidaTorcida> clubeOpt = partidaTorcidaRepository.findById(id);
		if (clubeOpt.isPresent()) {
			return clubeOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		partidaTorcidaRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		partidaTorcidaRepository.deleteAll();
	}

	@Override
	public PartidaTorcida create(PartidaTorcida t) {
		return partidaTorcidaRepository.save(t);
	}

	@Override
	public PartidaTorcida update(PartidaTorcida t) {
		return partidaTorcidaRepository.save(t);
	}
}
