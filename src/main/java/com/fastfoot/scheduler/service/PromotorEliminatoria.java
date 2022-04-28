package com.fastfoot.scheduler.service;

import java.util.List;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public abstract class PromotorEliminatoria {

	public abstract void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN, RodadaEliminatoria rodada2FaseCN);

	public static void promoverGruposParaRodadasEliminatorias(List<GrupoCampeonato> grupos, RodadaEliminatoria rodadaInicial) {
		List<PartidaEliminatoriaResultado> partidas = rodadaInicial.getPartidas();

		Clube primeiroColocado, segundoColocado; 
		
		for (int i = 0; i < Constantes.NRO_GRUPOS_CONT; i++) {
			int j = (i + (Constantes.NRO_GRUPOS_CONT/2))%Constantes.NRO_GRUPOS_CONT;
			GrupoCampeonato gc = grupos.get(i);

			primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 1).findFirst().get().getClube();
			segundoColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 2).findFirst().get().getClube();
			
			partidas.get(i).setClubeMandante(primeiroColocado);
			partidas.get(j).setClubeVisitante(segundoColocado);
		}
	}

	public static void promoverProximaPartidaEliminatoria(PartidaEliminatoriaResultado partida) {
		if (partida.getClassificaAMandante()) {
			partida.getProximaPartida().setClubeMandante(partida.getClubeVencedor());
		} else {
			partida.getProximaPartida().setClubeVisitante(partida.getClubeVencedor());
		}
	}
}
