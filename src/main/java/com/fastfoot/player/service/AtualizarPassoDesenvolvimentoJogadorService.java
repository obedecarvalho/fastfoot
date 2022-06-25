package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AtualizarPassoDesenvolvimentoJogadorService {
	
	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			j.setIdade(j.getIdade() + 1);
			if (j.getIdade() < JogadorFactory.IDADE_MAX) {
				ajustarPassoDesenvolvimento(j);
			}
		}
		
		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	private void ajustarPassoDesenvolvimento(Jogador j) {

		Double ajusteForca = JogadorFactory.getAjusteForca(j.getIdade());
		Double ajusteForcaProx = JogadorFactory.getAjusteForca(j.getIdade() + 1);
		Integer potencialSorteado = null;
		Double forca = null;
		Double passoProx = null;
		
		for (HabilidadeValor hv : j.getHabilidades()) {
			potencialSorteado = hv.getPotencialDesenvolvimento();
			forca = potencialSorteado * ajusteForca;
			passoProx = ((potencialSorteado * ajusteForcaProx) - forca) / JogadorFactory.NUMERO_DESENVOLVIMENTO_ANO_JOGADOR;
			hv.setPassoDesenvolvimento(passoProx);
		}
	}
}
