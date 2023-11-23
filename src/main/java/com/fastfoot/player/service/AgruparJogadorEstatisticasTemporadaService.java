package com.fastfoot.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.repository.JogadorEstatisticasSemanaRepository;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.scheduler.model.entity.Temporada;

@Service
public class AgruparJogadorEstatisticasTemporadaService {
	
	@Autowired
	private JogadorEstatisticasSemanaRepository jogadorEstatisticasSemanaRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	public void agruparJogadorEstatisticasTemporadaService(Temporada temporadaAtual) {
		jogadorEstatisticasTemporadaRepository.agruparJogadorEstatisticasTemporada(temporadaAtual.getId());
		jogadorEstatisticasSemanaRepository.deleteByIdTemporada(temporadaAtual.getId());
	}

}
