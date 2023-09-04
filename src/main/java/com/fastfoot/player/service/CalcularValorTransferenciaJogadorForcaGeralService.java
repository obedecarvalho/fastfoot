package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorForcaGeralService extends CalcularValorTransferenciaJogadorService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(LigaJogo liga, boolean primeirosIds) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}

		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}

		jogadorRepository.saveAll(jogadores);

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
