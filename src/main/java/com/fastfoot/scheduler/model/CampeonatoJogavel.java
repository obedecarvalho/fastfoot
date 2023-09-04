package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.LigaJogo;

public interface CampeonatoJogavel {
	
	public Long getId();
	
	//public Liga getLiga();
	
	public LigaJogo getLigaJogo();

	public String getNome();

	public NivelCampeonato getNivelCampeonato();
}
