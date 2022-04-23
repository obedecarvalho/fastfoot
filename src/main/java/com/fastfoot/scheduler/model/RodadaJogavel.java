package com.fastfoot.scheduler.model;

import com.fastfoot.scheduler.model.entity.Semana;

public interface RodadaJogavel {

	public Integer getNumero();

	public Semana getSemana();
	
	public CampeonatoJogavel getCampeonatoJogavel();

	public boolean isUltimaRodadaPontosCorridos();
}
