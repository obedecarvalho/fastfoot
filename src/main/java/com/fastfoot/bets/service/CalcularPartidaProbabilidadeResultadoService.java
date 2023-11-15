package com.fastfoot.bets.service;

import java.util.concurrent.CompletableFuture;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;

public interface CalcularPartidaProbabilidadeResultadoService {
	
	public PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado);
	
	public CompletableFuture<Boolean> calcularPartidaProbabilidadeResultado(RodadaJogavel rodada);

}
