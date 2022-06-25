package com.fastfoot.player.service;

import java.util.Arrays;
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
	
	/*private static final Double TAXA_DESCONTO_I = 0.33;
	private static final Double TAXA_DESCONTO_II = 0.25;
	
	private static final List<Double> PESO = Arrays.asList(0.0d, 0.01d, 0.20d, 0.37d, 0.52d, 0.65d, 0.76d, 0.85d, 0.92d, 0.97d, 0.100d, 0.97d, 0.92d, 0.85d, 0.76d, 0.65d, 0.52d, 0.37d, 0.20d, 0.01d, 0.0d);
	
	public void calcularValorTransferencia(Jogador jogador, Boolean novo) {

		Double valor = 0d;
		
		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {
			
			double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			
			double valorIAj = (ajuste * jogador.getForcaGeralPotencial()) /  Math.pow(1 + TAXA_DESCONTO_I, i - jogador.getIdade());
			
			double valorIIAj = (ajuste * jogador.getForcaGeralPotencial()) /  Math.pow(1 + TAXA_DESCONTO_II, i - jogador.getIdade());
			
			double p = PESO.get(i - JogadorFactory.IDADE_MIN);
			
			valor += valorIAj * (1 - p) + valorIIAj * p;
		}
		
		jogador.setValorTransferencia(valor);

	}*/
}
