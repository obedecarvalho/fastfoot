package com.fastfoot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.model.repository.LigaJogoRepository;

@Service
public class LigaJogoCRUDService  implements CRUDService<LigaJogo, Long> {

	@Autowired
	private LigaJogoRepository ligaJogoRepository;

	@Override
	public List<LigaJogo> getAll() {
		return ligaJogoRepository.findAll();
	}

	@Override
	public LigaJogo getById(Long id) {
		Optional<LigaJogo> jogoOpt = ligaJogoRepository.findById(id);
		if (jogoOpt.isPresent()) {
			return jogoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		ligaJogoRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		ligaJogoRepository.deleteAll();
	}

	@Override
	public LigaJogo create(LigaJogo t) {
		return ligaJogoRepository.save(t);
	}

	@Override
	public LigaJogo update(LigaJogo t) {
		return ligaJogoRepository.save(t);
	}

	public List<LigaJogo> getByJogo(Jogo jogo){
		return ligaJogoRepository.findByJogo(jogo);
	}
	
	public LigaJogo getByJogo(Jogo jogo, Liga liga){
		return ligaJogoRepository.findFirstByJogoAndLiga(jogo, liga).get();
	}
}
