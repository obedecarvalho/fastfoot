package com.fastfoot.scheduler.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public class PromotorEliminatoriaImplDozeClubes extends PromotorEliminatoria {

	@Override
	public void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN,
			RodadaEliminatoria rodada2FaseCN) {
		
		//8
		List<Clube> clubes1Fase = rodada1FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());
		//4
		List<Clube> clubes2Fase = rodada2FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());
		
		Collections.shuffle(clubes1Fase);
		Collections.shuffle(clubes2Fase);

		List<PartidaEliminatoriaResultado> p1 = rodadaCNII.getPartidas().stream().filter(p -> p.getClubeVisitante() != null).collect(Collectors.toList());
		List<PartidaEliminatoriaResultado> p2 = rodadaCNII.getPartidas().stream().filter(p -> p.getClubeVisitante() == null).collect(Collectors.toList());
		
		for (int i = 0; i < p1.size(); i++) {
			p1.get(i).setClubeMandante(clubes2Fase.get(i));
		}
		
		for (int i = 0; i < p2.size(); i++) {
			p2.get(i).setClubeMandante(clubes1Fase.get(i));
			p2.get(i).setClubeVisitante(clubes1Fase.get(clubes1Fase.size() - i - 1));
		}
		
	}

}
