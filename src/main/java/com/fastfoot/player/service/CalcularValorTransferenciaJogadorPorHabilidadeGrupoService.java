package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.ValorTransferenciaHabilidadeGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularValorTransferenciaJogadorPorHabilidadeGrupoService extends CalcularValorTransferenciaJogadorService {

	//public static final Map<HabilidadeGrupo, ValorTransferenciaHabilidadeGrupo> VALOR_TRANSFERENCIA_HABILIDADE;
	
	public static final Map<HabilidadeGrupo, Double> VALOR_TRANSFERENCIA_HABILIDADE;
	
	static {
		/*VALOR_TRANSFERENCIA_HABILIDADE = new HashMap<HabilidadeGrupo, ValorTransferenciaHabilidadeGrupo>();

		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.DEFESA, ValorTransferenciaHabilidadeGrupo.DEFESA);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CONCLUSAO, ValorTransferenciaHabilidadeGrupo.CONCLUSAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CRIACAO, ValorTransferenciaHabilidadeGrupo.CRIACAO);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.POSSE_BOLA, ValorTransferenciaHabilidadeGrupo.POSSE_BOLA);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.QUEBRA_LINHA, ValorTransferenciaHabilidadeGrupo.QUEBRA_LINHA);
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.GOLEIRO, ValorTransferenciaHabilidadeGrupo.GOLEIRO);*/
		
		VALOR_TRANSFERENCIA_HABILIDADE = new HashMap<HabilidadeGrupo, Double>();
		
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.DEFESA, ValorTransferenciaHabilidadeGrupo.DEFESA.getPeso());
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CONCLUSAO, ValorTransferenciaHabilidadeGrupo.CONCLUSAO.getPeso());
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.CRIACAO, ValorTransferenciaHabilidadeGrupo.CRIACAO.getPeso());
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.POSSE_BOLA, ValorTransferenciaHabilidadeGrupo.POSSE_BOLA.getPeso());
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.QUEBRA_LINHA, ValorTransferenciaHabilidadeGrupo.QUEBRA_LINHA.getPeso());
		VALOR_TRANSFERENCIA_HABILIDADE.put(HabilidadeGrupo.GOLEIRO, ValorTransferenciaHabilidadeGrupo.GOLEIRO.getPeso());

	}
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularValorTransferencia(Liga liga, boolean primeirosIds) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidadesGrupo(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidadesGrupo(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
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

	//@Override
	/*public void calcularValorTransferencia(Jogador jogador) {
		
		Double pesoHabilidade = null;
		
		Map<HabilidadeGrupoValor, Double> habilidadeValorPeso = new HashMap<HabilidadeGrupoValor, Double>();
		
		for (HabilidadeGrupoValor habilidadeValor : jogador.getHabilidadesGrupoValor()) {
			
			pesoHabilidade = VALOR_TRANSFERENCIA_HABILIDADE.get(habilidadeValor.getHabilidadeGrupo()).getPeso();
			
			habilidadeValorPeso.put(habilidadeValor, pesoHabilidade * habilidadeValor.getPotencialDesenvolvimento());
		}

		Double valor = 0d;

		Double habilidadeComPeso = null;

		for (int i = jogador.getIdade(); i < JogadorFactory.IDADE_MAX; i++) {

			double ajuste = jogador.getModoDesenvolvimentoJogador().getValorAjuste()[i - JogadorFactory.IDADE_MIN];

			for (HabilidadeGrupoValor habilidadeValor : jogador.getHabilidadesGrupoValor()) {

				habilidadeComPeso = habilidadeValorPeso.get(habilidadeValor);
				
				double valorAj = Math.pow((ajuste * habilidadeComPeso), FORCA_N_POWER)
						/ TAXA_DESCONTO_TEMPO[i - jogador.getIdade()];

				valor += valorAj;
			}

		}

		//Aproveitando para arredondar também
		jogador.setValorTransferencia(Math.round(valor * 300) / 100d);

	}*/

}
