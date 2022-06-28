package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaAmistoraRepository;

@Service
public class RodadaAmistosaService {

	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;
	
	public void carregarRodadasAmistosasBySemana(Semana semana) {
		List<RodadaAmistosa> rodadaAmistosas = rodadaAmistoraRepository.findBySemana(semana);
		
		for (RodadaAmistosa r : rodadaAmistosas) {
			r.setPartidas(partidaAmistosaResultadoRepository.findByRodada(r));
		}
	}
	
	/*public void salvarRodadasAmistosas(List<RodadaAmistosa> rodadaAmistosas) {
		rodadaAmistoraRepository.saveAll(rodadaAmistosas);
		for (RodadaAmistosa r : rodadaAmistosas) {
			partidaAmistosaResultadoRepository.saveAll(r.getPartidas());
		}
	}*/

	public void salvarRodadasAmistosas(List<RodadaAmistosa> rodadaAmistosas) {
		List<PartidaAmistosaResultado> partidas = new ArrayList<PartidaAmistosaResultado>();

		for (RodadaAmistosa r : rodadaAmistosas) {
			partidas.addAll(r.getPartidas());
		}

		rodadaAmistoraRepository.saveAll(rodadaAmistosas);
		partidaAmistosaResultadoRepository.saveAll(partidas);

	}
}
