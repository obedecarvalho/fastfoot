package com.fastfoot.scheduler.service.util;

import java.util.Set;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;

public class PromotorContinentalImplMelhoresClassificadosFaseGrupo extends PromotorContinental {

	@Override
	public void promover(Set<CampeonatoMisto> campeonatos) {
		for (CampeonatoMisto c : campeonatos) {
			promover(c);
		}
	}

}
