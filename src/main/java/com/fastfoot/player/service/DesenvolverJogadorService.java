package com.fastfoot.player.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.util.JogadorCalcularForcaUtil;

@Service
public class DesenvolverJogadorService {

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> desenvolverJogador(List<Jogador> jogadores) {
		
		for (Jogador jogador : jogadores) {
			desenvolverJogador(jogador);
		}
		
		calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogadores);
		
		habilidadeValorRepository.saveAll(jogadores.stream().flatMap(j -> j.getHabilidadesValor().stream()).collect(Collectors.toList()));
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private void desenvolverJogador(Jogador jogador) {

		Double newValorTotal = null;
		for (HabilidadeValor hv : jogador.getHabilidadesValor()) {
			newValorTotal = hv.getValorTotal() + hv.getPassoDesenvolvimento();
	
			hv.setValor(newValorTotal.intValue());
			hv.setValorDecimal(newValorTotal);
		}
		
		JogadorCalcularForcaUtil.calcularForcaGeral(jogador);
	}

}
