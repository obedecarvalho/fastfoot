package com.fastfoot.player.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.HabilidadeEstatisticaPercentil;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.StatusJogador;
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
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(Jogo jogo, int idadeMin, int idadeMax) {

		for (int i = idadeMin; i < idadeMax; i++) {
			habilidadeValorRepository.atualizarPassoDesenvolvimento(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.REGULAR.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.REGULAR.ordinal(), jogo.getId());
			habilidadeValorRepository.atualizarPassoDesenvolvimento(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.PRECOCE.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.PRECOCE.ordinal(), jogo.getId());
			habilidadeValorRepository.atualizarPassoDesenvolvimento(i,
					JogadorFactory.QTDE_DESENVOLVIMENTO_ANO_JOGADOR.intValue(),
					ModoDesenvolvimentoJogador.TARDIO.getValorAjuste()[i - JogadorFactory.IDADE_MIN + 1],
					ModoDesenvolvimentoJogador.TARDIO.ordinal(), jogo.getId());
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(List<Jogador> jogadores) {
		
		for (Jogador j : jogadores) {
			if ((j.getIdade() + 1) < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j);
			}
		}
		
		List<HabilidadeValor> habilidadeValores = new ArrayList<HabilidadeValor>();
		for (Jogador jog : jogadores) {
			habilidadeValores.addAll(jog.getHabilidadesValor());
		}
		
		//jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(LigaJogo liga, boolean primeirosIds) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}

		for (Jogador j : jogadores) {
			if ((j.getIdade() + 1) < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j);
			}
		}

		//jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(
				jogadores.stream().flatMap(j -> j.getHabilidadesValor().stream()).collect(Collectors.toList()));

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	/*@Async("defaultExecutor")
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
			habilidadeValores.addAll(jog.getHabilidadesValor());
		}
		
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidadeValores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> ajustarPassoDesenvolvimento(LigaJogo liga, boolean primeirosIds,
			HabilidadeEstatisticaPercentil hep,
			Map<HabilidadeValor, HabilidadeValorEstatisticaGrupo> estatisticasGrupoMap) {

		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}

		for (Jogador j : jogadores) {
			if ((j.getIdade() + 1) < JogadorFactory.IDADE_MAX) {
				JogadorFactory.getInstance().ajustarPassoDesenvolvimento(j, hep, estatisticasGrupoMap);
			}
		}

		// jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(
				jogadores.stream().flatMap(j -> j.getHabilidadesValor().stream()).collect(Collectors.toList()));

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

}
