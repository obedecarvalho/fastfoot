package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.Clube;

public interface PartidaResultadoJogavel {

	public Clube getClubeMandante();
	
	public Clube getClubeVisitante();
	
	public void setGolsMandante(Integer golsMandante);
	
	public Integer getGolsMandante();
	
	public void setGolsVisitante(Integer golsVisitante);
	
	public Integer getGolsVisitante();
	
	public RodadaJogavel getRodada();

}
