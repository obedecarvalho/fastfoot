package com.fastfoot.scheduler.service.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public class PromotorEliminatoriaImplVinteClubes extends PromotorEliminatoria {

	@Override
	public void classificarCopaNacionalII(RodadaEliminatoria rodadaCNII, RodadaEliminatoria rodada1FaseCN,
			RodadaEliminatoria rodada2FaseCN) {

		//4
		List<Clube> clubes1Fase = rodada1FaseCN.getPartidas().stream().map(PartidaEliminatoriaResultado::getClubePerdedor).collect(Collectors.toList());

		Collections.shuffle(clubes1Fase);

		List<PartidaEliminatoriaResultado> p2 = rodadaCNII.getPartidas().stream().filter(p -> p.getClubeMandante() == null).collect(Collectors.toList());

		for (int i = 0; i < p2.size(); i++) {
			p2.get(i).setClubeMandante(clubes1Fase.get(i));
		}

	}

}
