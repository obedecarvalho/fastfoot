package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorForcaGeralService extends CalcularValorTransferenciaJogadorService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(Liga liga, boolean primeirosIds) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();

		stopWatch.split();
		inicio = stopWatch.getSplitTime();

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
		}

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

		//System.err.println(mensagens);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	//@Override
	public void calcularValorTransferencia(Jogador jogador) {

		Double valor = 0d;

		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {

			double ajuste = jogador.getModoDesenvolvimentoJogador().getValorAjuste()[i - JogadorFactory.IDADE_MIN];

			double valorAj = Math.pow((ajuste * jogador.getForcaGeralPotencial()), FORCA_N_POWER)
					/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];

			valor += valorAj;
		}

		//Tem que multiplicar o valor por
		//	FORCA_N_POWER == 2: 1000
		//	FORCA_N_POWER == 3: 10
		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 1000) / 100d);

	}

}
