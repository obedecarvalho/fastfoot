package com.fastfoot.scheduler.model;

import com.fastfoot.club.model.entity.Clube;

public interface PartidaResultadoJogavel {

	public Clube getClubeMandante();
	
	public Clube getClubeVisitante();
	
	public void setGolsMandante(Integer golsMandante);
	
	public Integer getGolsMandante();
	
	public void setGolsVisitante(Integer golsVisitante);
	
	public Integer getGolsVisitante();
	
	public RodadaJogavel getRodada();

	//public PartidaResumo getPartidaResumo();

	//public void setPartidaResumo(PartidaResumo partidaResumo);

	public void incrementarLance(boolean posseBolaMandante);
	
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante);
	
	public void incrementarFinalizacaoFora(boolean posseBolaMandante);

	public void incrementarGol(boolean posseBolaMandante);

	public Clube getClubeVencedor();

	public Clube getClubePerdedor();

	public void setPartidaJogada(Boolean partidaJogada);
	
	public Boolean getPartidaJogada();

	public boolean isAmistoso();
}
