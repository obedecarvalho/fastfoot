package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.ValorTransferenciaHabilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorPorHabilidadeService {

	/*
	 * DESATUALIZADO
	 * Taxa desconto: 33%, Max/Inicial: 103%
	 * Taxa desconto: 30%, Max/Inicial: 94%
	 * Taxa desconto: 25%, Max/Inicial: 77%
	 */
	private static final Double TAXA_DESCONTO = 0.25;//0.25, 0.30, 0.33
	
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
	
	private static final Map<Habilidade, ValorTransferenciaHabilidade> VALOR_TRANSFERENCIA_HABILIDADE;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	static {
		VALOR_TRANSFERENCIA_HABILIDADE = new HashMap<Habilidade, ValorTransferenciaHabilidade>();

		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.PASSE, ValorTransferenciaHabilidade.PASSE);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.FINALIZACAO, ValorTransferenciaHabilidade.FINALIZACAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.CRUZAMENTO, ValorTransferenciaHabilidade.CRUZAMENTO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.ARMACAO, ValorTransferenciaHabilidade.ARMACAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.MARCACAO, ValorTransferenciaHabilidade.MARCACAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DESARME, ValorTransferenciaHabilidade.DESARME);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.INTERCEPTACAO, ValorTransferenciaHabilidade.INTERCEPTACAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.VELOCIDADE, ValorTransferenciaHabilidade.VELOCIDADE);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DRIBLE, ValorTransferenciaHabilidade.DRIBLE);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.FORCA, ValorTransferenciaHabilidade.FORCA);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.CABECEIO, ValorTransferenciaHabilidade.CABECEIO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.POSICIONAMENTO, ValorTransferenciaHabilidade.POSICIONAMENTO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DOMINIO, ValorTransferenciaHabilidade.DOMINIO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.REFLEXO, ValorTransferenciaHabilidade.REFLEXO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.JOGO_AEREO, ValorTransferenciaHabilidade.JOGO_AEREO);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}
		
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	public void calcularValorTransferencia(Jogador jogador) {
		
		Double pesoHabilidade = null;
		
		Map<HabilidadeValor, Double> habilidadeValorPeso = new HashMap<HabilidadeValor, Double>();
		
		for (HabilidadeValor habilidadeValor : jogador.getHabilidades()) {
			
			pesoHabilidade = VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidade()).getPeso();
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}
		

		Double valor = 0d;

		Double habilidadeComPeso = null;

		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {

			//double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			double ajuste = jogador.getJogadorDetalhe().getModoDesenvolvimentoJogador().getValorAjuste()[i
					- JogadorFactory.IDADE_MIN];

			for (HabilidadeValor habilidadeValor : jogador.getHabilidades()) {
				/*double valorAj = Math.pow((ajuste * jogador.getForcaGeralPotencialEfetiva()), FORCA_N_POWER)
					/ Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade());*/

				/*pesoHabilidade = VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidade()).getPeso();

				double valorAj = Math.pow(
						(ajuste * habilidadeValor.getPotencialDesenvolvimentoEfetivo() * pesoHabilidade), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];*/
				
				habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
				
				double valorAj = Math.pow(
						(ajuste * habilidadeComPeso), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];

				valor += valorAj;
			}

		}

		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 100) / 100d);

	}

	/*public void calcularValorTransferencia(Jogador jogador) {

		Double valor = 0d;

		Double pesoHabilidade = null;

		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {

			//double ajuste = JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN);
			double ajuste = jogador.getJogadorDetalhe().getModoDesenvolvimentoJogador().getValorAjuste()[i
					- JogadorFactory.IDADE_MIN];

			for (HabilidadeValor habilidadeValor : jogador.getHabilidades()) {
				/*double valorAj = Math.pow((ajuste * jogador.getForcaGeralPotencialEfetiva()), FORCA_N_POWER)
					/ Math.pow(1 + TAXA_DESCONTO, i - jogador.getIdade());* /

				pesoHabilidade = VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidade()).getPeso();

				double valorAj = Math.pow(
						(ajuste * habilidadeValor.getPotencialDesenvolvimentoEfetivo() * pesoHabilidade), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];

				valor += valorAj;
			}

		}

		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 100) / 100d);

	}*/
}
