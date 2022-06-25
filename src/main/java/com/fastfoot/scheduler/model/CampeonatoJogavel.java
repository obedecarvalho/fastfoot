package com.fastfoot.scheduler.model;

import com.fastfoot.model.Liga;

public interface CampeonatoJogavel {
	
	public Long getId();
	
	public Liga getLiga();

	public String getNome();

	public NivelCampeonato getNivelCampeonato();
}
