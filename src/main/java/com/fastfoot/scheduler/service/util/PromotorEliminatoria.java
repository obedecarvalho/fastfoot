package com.fastfoot.scheduler.service.util;

import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public abstract class PromotorEliminatoria {

	public abstract void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN, RodadaEliminatoria rodada2FaseCN);

	public static void promoverProximaPartidaEliminatoria(PartidaEliminatoriaResultado partida) {
		if (partida.getClassificaAMandante()) {
			partida.getProximaPartida().setClubeMandante(partida.getClubeVencedor());
		} else {
			partida.getProximaPartida().setClubeVisitante(partida.getClubeVencedor());
		}
	}
}
