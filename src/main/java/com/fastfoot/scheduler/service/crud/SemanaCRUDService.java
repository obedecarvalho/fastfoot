package com.fastfoot.scheduler.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.service.CRUDService;

@Service
public class SemanaCRUDService implements CRUDService<Semana, Long> {

	@Autowired
	private SemanaRepository semanaRepository;

	@Override
	public List<Semana> getAll() {
		return semanaRepository.findAll();
	}

	@Override
	public Semana getById(Long id) {
		Optional<Semana> semanaOpt = semanaRepository.findById(id);
		if (semanaOpt.isPresent()) {
			return semanaOpt.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		semanaRepository.deleteById(id);		
	}

	@Override
	public void deleteAll() {
		semanaRepository.deleteAll();
	}

	@Override
	public Semana create(Semana t) {
		return semanaRepository.save(t);
	}

	@Override
	public Semana update(Semana t) {
		return semanaRepository.save(t);
	}
	
	public Semana getSemanaAtual() {
		Optional<Semana> semanaOpt = semanaRepository.findSemanaAtual();
		if (semanaOpt.isPresent()) {
			return semanaOpt.get();
		}
		return null;
	}
	
	public Semana getProximaSemana() {
		Optional<Semana> semanaOpt = semanaRepository.findProximaSemana();
		if (semanaOpt.isPresent()) {
			return semanaOpt.get();
		}
		return null;
	}
	
	public Semana getByNumeroSemanaTemporadaAtual(Integer numeroSemana) {
		Optional<Semana> semanaOpt = semanaRepository.findByNumeroSemanaTemporadaAtual(numeroSemana);
		if (semanaOpt.isPresent()) {
			return semanaOpt.get();
		}
		return null;
	}
}
