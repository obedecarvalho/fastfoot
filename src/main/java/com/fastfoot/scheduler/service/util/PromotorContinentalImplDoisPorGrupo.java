package com.fastfoot.scheduler.service.util;

import java.util.Set;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public class PromotorContinentalImplDoisPorGrupo implements PromotorContinental {
	
	@Override
	public void promover(CampeonatoMisto campeonato) {

		Clube primeiroColocado, segundoColocado; 

		for (int i = 0; i < Constantes.NRO_GRUPOS_CONT; i++) {
			int j = (i + (Constantes.NRO_GRUPOS_CONT/2))%Constantes.NRO_GRUPOS_CONT;
			GrupoCampeonato gc = campeonato.getGrupos().get(i);

			primeiroColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 1).findFirst().get().getClube();
			segundoColocado = gc.getClassificacao().stream().filter(c -> c.getPosicao() == 2).findFirst().get().getClube();

			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(i).setClubeMandante(primeiroColocado);
			campeonato.getPrimeiraRodadaEliminatoria().getPartidas().get(j).setClubeVisitante(segundoColocado);
		}
	}

	@Override
	public void promover(Set<CampeonatoMisto> campeonatos) {
		for (CampeonatoMisto c : campeonatos) {
			promover(c);
		}
	}

}
