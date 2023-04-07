package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.Liga;
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
	public CompletableFuture<Boolean> calcularHabilidadeGrupoValor(HabilidadeGrupo habilidadeGrupo) {

		habilidadeGrupoValorRepository.calcular2(habilidadeGrupo.ordinal(), habilidadeGrupo.getHabilidadesOrdinal());

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularHabilidadeGrupoValor(Liga liga, boolean primeirosIds) {
		
		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
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
		
		for (HabilidadeGrupo habilidadeGrupo : HabilidadeGrupo.values()) {
			
			habilidadeValores = jogador.getHabilidadeValorByHabilidade(Arrays.asList(habilidadeGrupo.getHabilidades()));

			habilidadeGrupoValor = new HabilidadeGrupoValor();
			
			habilidadeGrupoValor.setJogador(jogador);
			habilidadeGrupoValor.setHabilidadeGrupo(habilidadeGrupo);
			habilidadeGrupoValor.setValor(habilidadeValores.stream().mapToDouble(HabilidadeValor::getValorTotal).average().getAsDouble());
			
			habilidadeGrupoValores.add(habilidadeGrupoValor);

		}

		//System.err.println(habilidadeGrupoValores);
	}
}
