package com.fastfoot.scheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.dto.CampeonatoDTO;
import com.fastfoot.scheduler.model.dto.TemporadaDTO;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;

@Service
public class TemporadaService {

	@Autowired
	private TemporadaRepository temporadaRepository;

	@Autowired
	private CarregarCampeonatoService campeonatoService;

	@Deprecated
	public List<CampeonatoDTO> getCampeonatosTemporada(String nivel) {//'NACIONAL', 'COPA NACIONAL', 'CONTINENTAL'
		
		List<CampeonatoDTO> campeonatos = null;
		Optional<Temporada> temporadaOpt = temporadaRepository.findFirstByAtual(true);
		
		if (temporadaOpt.isPresent()) {
			campeonatos = CampeonatoDTO.convertToDTO(campeonatoService.carregarCampeonatosTemporada(temporadaOpt.get(), nivel));
		}
		return campeonatos;
	}

	public List<TemporadaDTO> getTemporadas() {
		return TemporadaDTO.convertToDTO(temporadaRepository.findAll());
	}

	@Deprecated
	public List<Integer> getAnosTemporadas(){
		return temporadaRepository.getAnosTemporadas();
	}
}
