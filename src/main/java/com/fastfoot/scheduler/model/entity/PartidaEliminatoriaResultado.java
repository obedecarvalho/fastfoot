package com.fastfoot.scheduler.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
//@Table(indexes = { @Index(columnList = "id_rodada_eliminatoria")})
public class PartidaEliminatoriaResultado implements PartidaResultadoJogavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaEliminatoriaResultadoSequence")	
	@SequenceGenerator(name = "partidaEliminatoriaResultadoSequence", sequenceName = "partida_eliminatoria_resultado_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_rodada_eliminatoria")
	private RodadaEliminatoria rodada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_mandante")
	private Clube clubeMandante;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_visitante")
	private Clube clubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;
	
	@ManyToOne
	@JoinColumn(name = "id_proxima_partida")
	private PartidaEliminatoriaResultado proximaPartida;

	private Boolean classificaAMandante;
	
	private Boolean partidaJogada;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_estatisticas")
	private PartidaEstatisticas partidaEstatisticas;
	
	private Integer golsMandantePenalts;
	
	private Integer golsVisitantePenalts;

	public PartidaEliminatoriaResultado() {
		this.golsMandante = 0;
		this.golsVisitante = 0;
		this.partidaJogada = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public RodadaEliminatoria getRodada() {
		return rodada;
	}

	public void setRodada(RodadaEliminatoria rodada) {
		this.rodada = rodada;
	}

	@Override
	public Clube getClubeMandante() {
		return clubeMandante;
	}

	public void setClubeMandante(Clube clubeMandante) {
		this.clubeMandante = clubeMandante;
	}

	@Override
	public Clube getClubeVisitante() {
		return clubeVisitante;
	}

	public void setClubeVisitante(Clube clubeVisitante) {
		this.clubeVisitante = clubeVisitante;
	}

	@Override
	public Integer getGolsMandante() {
		return golsMandante;
	}

	@Override
	public void setGolsMandante(Integer golsMandante) {
		this.golsMandante = golsMandante;
	}

	@Override
	public Integer getGolsVisitante() {
		return golsVisitante;
	}

	@Override
	public Boolean getPartidaJogada() {
		return partidaJogada;
	}

	@Override
	public void setPartidaJogada(Boolean partidaJogada) {
		this.partidaJogada = partidaJogada;
	}

	public PartidaEstatisticas getPartidaEstatisticas() {
		return partidaEstatisticas;
	}

	@Override
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas) {
		this.partidaEstatisticas = partidaEstatisticas;
	}

	public Integer getGolsMandantePenalts() {
		return golsMandantePenalts;
	}

	public void setGolsMandantePenalts(Integer golsMandantePenalts) {
		this.golsMandantePenalts = golsMandantePenalts;
	}

	public Integer getGolsVisitantePenalts() {
		return golsVisitantePenalts;
	}

	public void setGolsVisitantePenalts(Integer golsVisitantePenalts) {
		this.golsVisitantePenalts = golsVisitantePenalts;
	}

	@Override
	public void setGolsVisitante(Integer golsVisitante) {
		this.golsVisitante = golsVisitante;
	}

	public PartidaEliminatoriaResultado getProximaPartida() {
		return proximaPartida;
	}

	public void setProximaPartida(PartidaEliminatoriaResultado proximaPartida) {
		this.proximaPartida = proximaPartida;
	}
	
	public Boolean getClassificaAMandante() {
		return classificaAMandante;
	}

	public void setClassificaAMandante(Boolean classificaAMandante) {
		this.classificaAMandante = classificaAMandante;
	}

	@Override
	public boolean isAmistoso() {
		return false;
	}
	
	//###	METODOS AUXILIARES	###

	@Override
	public Clube getClubeVencedor() {
		if (partidaJogada) {
			if (golsMandante > golsVisitante) return clubeMandante;
			if (golsVisitante > golsMandante) return clubeVisitante;		
			if (golsMandante == golsVisitante && golsMandantePenalts > golsVisitantePenalts) return clubeMandante;
			if (golsVisitante == golsMandante && golsVisitantePenalts > golsMandantePenalts) return clubeVisitante;
		}
		return null;
	}

	@Override
	public Clube getClubePerdedor() {
		if (partidaJogada) {
			if (golsMandante < golsVisitante) return clubeMandante;
			if (golsVisitante < golsMandante) return clubeVisitante;
			if (golsMandante == golsVisitante && golsMandantePenalts < golsVisitantePenalts) return clubeMandante;
			if (golsVisitante == golsMandante && golsVisitantePenalts < golsMandantePenalts) return clubeVisitante;
		}
		return null;
	}
	
	@Override
	public boolean isResultadoEmpatado() {
		return partidaJogada && (golsMandante == golsVisitante);
	}
	
	@Override
	public boolean isDisputarPenalts() {
		return true;
	}

	@Override
	public void incrementarGol(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.golsMandante++;
		} else {
			this.golsVisitante++;
		}
	}
	
	@Override
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarFinalizacaoDefendida(posseBolaMandante);
	}

	@Override
	public void incrementarFinalizacaoFora(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarFinalizacaoFora(posseBolaMandante);
	}

	@Override
	public void incrementarLance(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarLance(posseBolaMandante);
	}

	@Override
	public String toString() {
		return "PartidaEliminatoria [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " "
				+ golsMandante + " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
	}
}
