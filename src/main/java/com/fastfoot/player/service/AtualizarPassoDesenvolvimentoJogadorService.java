package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AtualizarPassoDesenvolvimentoJogadorService {
	
	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(int idadeMin, int idadeMax){

		for (int i = idadeMin; i < idadeMax; i++) {
			habilidadeValorRepository.atualizarPassoDesenvolvimento(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					JogadorFactory.VALOR_AJUSTE.get(i - JogadorFactory.IDADE_MIN + 1));
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(int idadeMin, int idadeMax) {

		for (int i = idadeMin; i < idadeMax; i++) {
			habilidadeValorRepository.atualizarPassoDesenvolvimento2(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.REGULAR.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.REGULAR.ordinal());
			habilidadeValorRepository.atualizarPassoDesenvolvimento2(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.PRECOCE.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.PRECOCE.ordinal());
			habilidadeValorRepository.atualizarPassoDesenvolvimento2(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.TARDIO.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.TARDIO.ordinal());
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			//j.setIdade(j.getIdade() + 1);
			if ((j.getIdade() + 1) < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j);
			}
		}
		
		List<HabilidadeValor> habilidadeValores = new ArrayList<HabilidadeValor>();
		for (Jogador jog : jogadores) {
			habilidadeValores.addAll(jog.getHabilidades());
		}
		
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores,
			HabilidadeEstatisticaPercentil hep,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap) {
		
		for (Jogador j : jogadores) {
			j.setIdade(j.getIdade() + 1);
			if (j.getIdade() < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j, hep, estatisticasGrupoMap);
			}
		}
		
		List<HabilidadeValor> habilidadeValores = new ArrayList<HabilidadeValor>();
		for (Jogador jog : jogadores) {
			habilidadeValores.addAll(jog.getHabilidades());
		}
		
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

}
