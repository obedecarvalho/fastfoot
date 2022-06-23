package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaService {
	
	/*
	 * Taxa desconto: 33%, Max/Inicial: 103%
	 * Taxa desconto: 30%, Max/Inicial: 94%
	 * Taxa desconto: 25%, Max/Inicial: 77%
	 */
	private static final Double TAXA_DESCONTO = 0.33;//0.25, 0.30
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}
		
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public void calcularValorTransferencia(Jogador jogador) {

		Double valor = 0d;
		
		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {
			
			double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			
			double valorAj = (ajuste * jogador.getForcaGeralPotencial()) /  Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade());
			
			valor += valorAj;
		}
		
		jogador.setValorTransferencia(valor);

	}
}
