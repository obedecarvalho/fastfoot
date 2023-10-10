package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.entity.Temporada;

public interface CampeonatoJogavel {
	
	public Long getId();
	
	//public Liga getLiga();
	
	public LigaJogo getLigaJogo();

	public String getNome();

	public NivelCampeonato getNivelCampeonato();
	
	public Integer getRodadaAtual();
	
	public Temporada getTemporada();
	
	public Integer getTotalRodadas();
}
