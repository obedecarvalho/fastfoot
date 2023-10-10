package com.fastfoot.scheduler.model;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.model.entity.PartidaTorcida;

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
	
	public boolean isDisputarPenalties();
	
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas);
	
	public PartidaEstatisticas getPartidaEstatisticas();
	
	public NivelCampeonato getNivelCampeonato();

	public EscalacaoClube getEscalacaoMandante();

	public EscalacaoClube getEscalacaoVisitante();

	public void setEscalacaoMandante(EscalacaoClube escalacaoMandante);

	public void setEscalacaoVisitante(EscalacaoClube escalacaoVisitante);
	
	public PartidaTorcida getPartidaTorcida();

	public void setPartidaTorcida(PartidaTorcida partidaTorcida);

	public PartidaProbabilidadeResultado getPartidaProbabilidadeResultado();

	public void setPartidaProbabilidadeResultado(PartidaProbabilidadeResultado partidaProbabilidadeResultado);
}
