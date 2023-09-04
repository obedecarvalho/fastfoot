package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorPorHabilidadeGrupoService extends CalcularValorTransferenciaJogadorService {

	public static final Map<HabilidadeGrupo, Double> VALOR_TRANSFERENCIA_HABILIDADE;
	
	static {
		
		VALOR_TRANSFERENCIA_HABILIDADE = new HashMap<HabilidadeGrupo, Double>();
		
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.DEFESA, 1.15);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CONCLUSAO, 1.275);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CRIACAO, 1.225);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.POSSE_BOLA, 1.075);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.QUEBRA_LINHA, 1.20);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.GOLEIRO, 1.3);

	}
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(LigaJogo liga, boolean primeirosIds) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidadesGrupo(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidadesGrupo(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}

		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}

		jogadorRepository.saveAll(jogadores);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public void calcularValorTransferencia(Jogador j) {
		j.setValorTransferencia(calcularValorTransferencia(j.getHabilidadesGrupoValor(), j.getIdade(),
				j.getModoDesenvolvimentoJogador(), VALOR_TRANSFERENCIA_HABILIDADE, 300));
	}

}
