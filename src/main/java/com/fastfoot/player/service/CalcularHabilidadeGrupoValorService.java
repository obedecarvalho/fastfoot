package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class CalcularHabilidadeGrupoValorService {
	
	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularHabilidadeGrupoValor(Jogo jogo, HabilidadeGrupo habilidadeGrupo) {

		habilidadeGrupoValorRepository.calcular(habilidadeGrupo.ordinal(), habilidadeGrupo.getHabilidadesOrdinal(), jogo.getId());

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularHabilidadeGrupoValor(LigaJogo liga, boolean primeirosIds) {
		
		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}
		
		calcularHabilidadeGrupoValor(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	public void calcularHabilidadeGrupoValor(List<Jogador> jogadores) {
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : jogadores) {
			calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);
	}

	public void calcularHabilidadeGrupoValor(Jogador jogador, List<HabilidadeGrupoValor> habilidadeGrupoValores) {

		List<HabilidadeValor> habilidadeValores;

		HabilidadeGrupoValor habilidadeGrupoValor;
		
		for (HabilidadeGrupo habilidadeGrupo : HabilidadeGrupo.getAll()) {
			
			habilidadeValores = jogador.getHabilidadeValorByHabilidade(Arrays.asList(habilidadeGrupo.getHabilidades()));

			habilidadeGrupoValor = new HabilidadeGrupoValor();
			
			habilidadeGrupoValor.setJogador(jogador);
			habilidadeGrupoValor.setHabilidadeGrupo(habilidadeGrupo);
			habilidadeGrupoValor.setValorTotal(habilidadeValores.stream().mapToDouble(HabilidadeValor::getValorTotal).average().getAsDouble());
			habilidadeGrupoValor.setValor(habilidadeGrupoValor.getValorTotal().intValue());
			habilidadeGrupoValor.setPotencialDesenvolvimento(habilidadeValores.stream()
					.mapToDouble(HabilidadeValor::getPotencialDesenvolvimento).average().getAsDouble());
			
			jogador.addHabilidadeGrupoValor(habilidadeGrupoValor);
			
			habilidadeGrupoValores.add(habilidadeGrupoValor);

		}

		//System.err.println(habilidadeGrupoValores);
	}
}
