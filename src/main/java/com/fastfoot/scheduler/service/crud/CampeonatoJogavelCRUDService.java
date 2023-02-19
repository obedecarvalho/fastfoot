package com.fastfoot.scheduler.service.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;

@Service
public class CampeonatoJogavelCRUDService {
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;
	
	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;
	
	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;
	
	@Autowired
	private TemporadaCRUDService temporadaCRUDService;

	public List<CampeonatoJogavel> getAll() {
		
		List<CampeonatoJogavel> campeonatos = new ArrayList<CampeonatoJogavel>();
		
		campeonatos.addAll(campeonatoRepository.findAll());
		campeonatos.addAll(campeonatoEliminatorioRepository.findAll());
		campeonatos.addAll(campeonatoMistoRepository.findAll());

		return campeonatos;
	}

	public CampeonatoJogavel getById(Long id) {

		Optional<? extends CampeonatoJogavel> campeonatoOpt = campeonatoRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}

		campeonatoOpt = campeonatoEliminatorioRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}

		campeonatoOpt = campeonatoMistoRepository.findById(id);
		if (campeonatoOpt.isPresent()) {
			return campeonatoOpt.get();
		}

		return null;
	}
	
	public List<CampeonatoJogavel> getByTemporadaAtual() {
		
		Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		
		List<CampeonatoJogavel> campeonatos = new ArrayList<CampeonatoJogavel>();
		
		campeonatos.addAll(campeonatoRepository.findByTemporada(temporada));
		campeonatos.addAll(campeonatoEliminatorioRepository.findByTemporada(temporada));
		campeonatos.addAll(campeonatoMistoRepository.findByTemporada(temporada));

		return campeonatos;
	}

}
