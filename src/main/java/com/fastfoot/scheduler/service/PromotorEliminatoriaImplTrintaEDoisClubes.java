package com.fastfoot.scheduler.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public class PromotorEliminatoriaImplTrintaEDoisClubes extends PromotorEliminatoria {

	public void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN, RodadaEliminatoria rodada2FaseCN) {
		
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
