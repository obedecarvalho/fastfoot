package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaService {
	
	/*
	 * DESATUALIZADO
	 * Taxa desconto: 33%, Max/Inicial: 103%
	 * Taxa desconto: 30%, Max/Inicial: 94%
	 * Taxa desconto: 25%, Max/Inicial: 77%
	 */
	//private static final Double TAXA_DESCONTO = 0.25;//0.25, 0.30, 0.33
	
	private static final Integer FORCA_N_POWER = 3;

	/**
	 * 
	 * 
	 * Calculado como: Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade())
	 */
	private static final Double[] TAXA_DESCONTO_TEMPO = new Double[] { 1.0, 1.25, 1.5625, 1.953125, 2.44140625,
			3.051757813, 3.814697266, 4.768371582, 5.960464478, 7.450580597, 9.313225746, 11.64153218, 14.55191523,
			18.18989404, 22.73736754, 28.42170943, 35.52713679, 44.40892099, 55.51115123, 69.38893904, 86.7361738,
			108.4202172 };
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia2(List<Clube> clubes) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		List<Jogador> jogadores = new ArrayList<Jogador>();
		
		for (Clube c : clubes) {	
			jogadores.addAll(jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO));//TODO: otimizar
		}
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#findByClubeAndStatusJogador:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularValorTransferencia:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		jogadorRepository.saveAll(jogadores);
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#saveAll:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(Liga liga) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		List<Jogador> jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO);
		
		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularValorTransferencia:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		jogadorRepository.saveAll(jogadores);
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#saveAll:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(List<Jogador> jogadores) {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularValorTransferencia:" + (fim - inicio));
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		jogadorRepository.saveAll(jogadores);
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#saveAll:" + (fim - inicio));
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public void calcularValorTransferencia(Jogador jogador) {

		Double valor = 0d;
		
		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {
			
			//double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			double ajuste = jogador.getJogadorDetalhe().getModoDesenvolvimentoJogador().getValorAjuste()[i - JogadorFactory.IDADE_MIN];
			
			/*double valorAj = Math.pow((ajuste * jogador.getForcaGeralPotencialEfetiva()), FORCA_N_POWER)
					/ Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade());*/
			
			double valorAj = Math.pow((ajuste * jogador.getForcaGeralPotencialEfetiva()), FORCA_N_POWER)
					/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];
			
			valor += valorAj;
		}
		
		//Tem que multiplicar o valor por
		//	FORCA_N_POWER == 2: 1000
		//	FORCA_N_POWER == 3: 10
		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 1000) / 100d);

	}

	/*
	public void calcularValorTransferencia(Jogador jogador) {

		Double valor = 0d;
		
		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {
			
			double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			
			double valorAj = (ajuste * jogador.getForcaGeralPotencialEfetiva()) /  Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade());
			
			valor += valorAj;
		}
		
		jogador.setValorTransferencia(valor);

	}
	*/
	
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
