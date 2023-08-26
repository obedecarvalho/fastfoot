package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorPorHabilidadeService extends CalcularValorTransferenciaJogadorService {
	
	public static final Map<Habilidade, Double> VALOR_TRANSFERENCIA_HABILIDADE;
	
	static {
		
		VALOR_TRANSFERENCIA_HABILIDADE = new HashMap<Habilidade, Double>();
		
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.PASSE, 1.15);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.FINALIZACAO, 1.30);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.CRUZAMENTO, 1.15);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.ARMACAO, 1.25);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.MARCACAO, 1.0);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DESARME, 1.0);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.INTERCEPTACAO, 1.0);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.VELOCIDADE, 1.25);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DRIBLE, 1.25);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.FORCA, 1.15);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.CABECEIO, 1.25);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.POSICIONAMENTO, 1.15);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.DOMINIO, 1.0);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.REFLEXO, 1.5);
		VALOR_TRANSFERENCIA_HABILIDADE.put(Habilidade.JOGO_AEREO, 1.5);
	}
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(Liga liga, boolean primeirosIds) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
		}

		for (Jogador j : jogadores) {
			calcularValorTransferencia(j);
		}

		jogadorRepository.saveAll(jogadores);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public void calcularValorTransferencia(Jogador j) {
		j.setValorTransferencia(calcularValorTransferencia(j.getHabilidadesValor(), j.getIdade(),
				j.getModoDesenvolvimentoJogador(), VALOR_TRANSFERENCIA_HABILIDADE, 100));
	}

}
