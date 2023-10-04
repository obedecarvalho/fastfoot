package com.fastfoot.club.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.TreinadorResumoTemporada;
import com.fastfoot.club.model.repository.TreinadorResumoTemporadaRepository;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.CRUDServiceJogavel;

@Service
public class TreinadorResumoTemporadaCRUDService implements CRUDServiceJogavel<TreinadorResumoTemporada, Long> {

	@Autowired
	private TreinadorResumoTemporadaRepository treinadorRepository;

	@Override
	public List<TreinadorResumoTemporada> getAll() {
		return treinadorRepository.findAll();
	}
	
	@Override
	public List<TreinadorResumoTemporada> getByJogo(Jogo jogo) {
		return treinadorRepository.findByJogo(jogo);
	}

	@Override
	public TreinadorResumoTemporada getById(Long id) {
		Optional<TreinadorResumoTemporada> treinadorOpt = treinadorRepository.findById(id);
		if (treinadorOpt.isPresent()) {
			return treinadorOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		treinadorRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		treinadorRepository.deleteAll();
	}

	@Override
	public TreinadorResumoTemporada create(TreinadorResumoTemporada t) {
		return treinadorRepository.save(t);
	}

	@Override
	public TreinadorResumoTemporada update(TreinadorResumoTemporada t) {
		return treinadorRepository.save(t);
	}
}
