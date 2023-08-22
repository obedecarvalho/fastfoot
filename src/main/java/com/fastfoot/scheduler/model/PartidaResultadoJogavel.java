package com.fastfoot.scheduler.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaEstatisticas;

public interface PartidaResultadoJogavel {

	public Clube getClubeMandante();
	
	public Clube getClubeVisitante();
	
	public void setGolsMandante(Integer golsMandante);
	
	public Integer getGolsMandante();
	
	public void setGolsVisitante(Integer golsVisitante);
	
	public Integer getGolsVisitante();
	
	public RodadaJogavel getRodada();

	public void incrementarLance(boolean posseBolaMandante);
	
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante);
	
	public void incrementarFinalizacaoFora(boolean posseBolaMandante);

	public void incrementarGol(boolean posseBolaMandante);

	public Clube getClubeVencedor();

	public Clube getClubePerdedor();

	public void setPartidaJogada(Boolean partidaJogada);
	
	public Boolean getPartidaJogada();

	public boolean isAmistoso();
	
	public boolean isResultadoEmpatado();
	
	public boolean isMandanteVencedor();
	
	public boolean isVisitanteVencedor();
	
	public boolean isDisputarPenalts();
	
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas);
	
	public PartidaEstatisticas getPartidaEstatisticas();
	
	public NivelCampeonato getNivelCampeonato();

	public EscalacaoClube getEscalacaoMandante();

	public EscalacaoClube getEscalacaoVisitante();

	public void setEscalacaoMandante(EscalacaoClube escalacaoMandante);

	public void setEscalacaoVisitante(EscalacaoClube escalacaoVisitante);
}
