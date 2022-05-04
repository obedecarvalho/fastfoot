package com.fastfoot.scheduler.service;

import java.util.Set;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;

public interface PromotorContinental {

	public void promover(CampeonatoMisto campeonato);

	public void promover(Set<CampeonatoMisto> campeonatos);
}
