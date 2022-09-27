package com.fastfoot.scheduler.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public class PromotorEliminatoriaImplTrintaEDoisClubesII  extends PromotorEliminatoria {

	@Override
	public void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN, RodadaEliminatoria rodada2FaseCN) {
		
		//10
		List<Clube> clubes1Fase = rodada1FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());
		//6
		clubes1Fase.addAll(rodada2FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList()));
		
		Collections.shuffle(clubes1Fase);
		
		for (int i = 0; i < rodadaCNII.getPartidas().size(); i++) {
			rodadaCNII.getPartidas().get(i).setClubeMandante(clubes1Fase.get(i * 2));
			rodadaCNII.getPartidas().get(i).setClubeVisitante(clubes1Fase.get(i * 2 + 1));
		}
	}
}
