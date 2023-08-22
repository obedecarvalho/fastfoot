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
import com.fastfoot.player.model.ValorTransferenciaHabilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorPorHabilidadeService extends CalcularValorTransferenciaJogadorService {

	public static final Map<Habilidade, ValorTransferenciaHabilidade> VALOR_TRANSFERENCIA_HABILIDADE;
	
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

	//@Override
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

			double ajuste = jogador.getModoDesenvolvimentoJogador().getValorAjuste()[i - JogadorFactory.IDADE_MIN];

			for (HabilidadeValor habilidadeValor : jogador.getHabilidades()) {

				habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
				
				double valorAj = Math.pow((ajuste * habilidadeComPeso), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];

				valor += valorAj;
			}

		}

		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 100) / 100d);

	}

}
