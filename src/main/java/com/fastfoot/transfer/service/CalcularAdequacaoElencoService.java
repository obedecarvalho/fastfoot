package com.fastfoot.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularAdequacaoElencoService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	public void calcularAdequacaoElenco(Clube clube) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndAposentado(clube, false);
	}
	
	/**
	 * 
	 * Recebe jogadores de determinada posicao
	 * 
	 * @param jogadores
	 */
	private void adequacaoJogadoresPosicao(List<Jogador> jogadores) {
		
	}
}
