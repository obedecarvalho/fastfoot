package com.fastfoot.player.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class JogadorService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;

	public List<Jogador> getJogadoresPorClube(Integer idClube) {
		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);

		if (clubeOpt.isPresent()) {
			return jogadorRepository.findByClubeAndAposentado(clubeOpt.get(), false);
		}

		return null;
	}
}
