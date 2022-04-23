package com.fastfoot.scheduler.service.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public class PromotorEliminatoriaUtil {

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

	public static void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN, RodadaEliminatoria rodada2FaseCN) {
		
		List<Clube> clubes1Fase = rodada1FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());
		List<Clube> clubes2Fase = rodada2FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());
		
		Collections.shuffle(clubes1Fase);
		Collections.shuffle(clubes2Fase);
		
		for (int i = 0; i < rodadaCNII.getPartidas().size(); i++) {
			rodadaCNII.getPartidas().get(i).setClubeMandante(clubes2Fase.get(i));
			rodadaCNII.getPartidas().get(i).setClubeVisitante(clubes1Fase.get(i));
		}
	}
}
