package com.fastfoot.scheduler.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
public class PartidaAmistosaResultado implements PartidaResultadoJogavel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaAmistosaResultadoSequence")	
	@SequenceGenerator(name = "partidaAmistosaResultadoSequence", sequenceName = "partida_amistosa_resultado_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_rodada_amistosa")
	private RodadaAmistosa rodada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_mandante")
	private Clube clubeMandante;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_visitante")
	private Clube clubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;

	@Transient
	private PartidaResumo partidaResumo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public RodadaAmistosa getRodada() {
		return rodada;
	}

	public void setRodada(RodadaAmistosa rodada) {
		this.rodada = rodada;
	}

	public Clube getClubeVencedor() {
		if (golsMandante == null || golsVisitante == null) return null;//Partida nao realizada
		if (golsMandante > golsVisitante) return clubeMandante;
		if (golsVisitante > golsMandante) return clubeVisitante;
		return null;//Empate
	}

	public Clube getClubePerdedor() {
		if (golsMandante == null || golsVisitante == null) return null;//Partida nao realizada
		if (golsMandante < golsVisitante) return clubeMandante;
		if (golsVisitante < golsMandante) return clubeVisitante;
		return null;//Empate
	}

	@Override
	public String toString() {
		return "PartidaAmistosaResultado [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " " + golsMandante
				+ " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
	}

	@Override
	public PartidaResumo getPartidaResumo() {
		return partidaResumo;
	}

	@Override
	public void setPartidaResumo(PartidaResumo partidaResumo) {
		this.partidaResumo = partidaResumo;
	}
}
