package com.fastfoot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.repository.JogoRepository;

@Service
public class JogoCRUDService implements CRUDService<Jogo, Long> {

	@Autowired
	private JogoRepository jogoRepository;

	@Override
	public List<Jogo> getAll() {
		return jogoRepository.findAll();
	}

	@Override
	public Jogo getById(Long id) {
		Optional<Jogo> jogoOpt = jogoRepository.findById(id);
		if (jogoOpt.isPresent()) {
			return jogoOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		jogoRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		jogoRepository.deleteAll();
	}

	@Override
	public Jogo create(Jogo t) {
		return jogoRepository.save(t);
	}

	@Override
	public Jogo update(Jogo t) {
		return jogoRepository.save(t);
	}

}
