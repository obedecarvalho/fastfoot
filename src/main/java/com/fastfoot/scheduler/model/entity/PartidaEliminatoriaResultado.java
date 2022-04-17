package com.fastfoot.scheduler.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
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
	
	@Transient
	private PartidaEstatisticas partidaEstatisticas;

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

	public Integer getGolsMandante() {
		return golsMandante;
	}

	@Override
	public void setGolsMandante(Integer golsMandante) {
		this.golsMandante = golsMandante;
	}

	public Integer getGolsVisitante() {
		return golsVisitante;
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

	public Clube getClubeVencedor() {
		if (golsMandante >= golsVisitante) return clubeMandante;
		if (golsVisitante > golsMandante) return clubeVisitante;
		//TODO: penalts
		return null;
	}

	public Clube getClubePerdedor() {
		if (golsMandante < golsVisitante) return clubeMandante;
		if (golsVisitante <= golsMandante) return clubeVisitante;
		//TODO: penalts
		return null;
	}

	@Override
	public String toString() {
		return "PartidaEliminatoria [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " "
				+ golsMandante + " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
	}

	@Override
	public PartidaEstatisticas getPartidaEstatisticas() {
		return partidaEstatisticas;
	}

	@Override
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas) {
		this.partidaEstatisticas = partidaEstatisticas;
	}
}
