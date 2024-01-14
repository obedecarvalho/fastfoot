package com.fastfoot.match.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class PartidaDisputaPenalties {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaDisputaPenaltiesSequence")
	@SequenceGenerator(name = "partidaDisputaPenaltiesSequence", sequenceName = "partida_disputa_penalties_seq")
	private Long id;
	
	private Integer golsMandantePenalties;
	
	private Integer golsVisitantePenalties;
	
	private Integer penaltiesBatidosMandante;
	
	private Integer penaltiesBatidosVisitante;
	
	private Integer penaltiesDefendidosMandante;
	
	private Integer penaltiesDefendidosVisitante;
	
	private Integer penaltiesForaMandante;
	
	private Integer penaltiesForaVisitante;
	
	public PartidaDisputaPenalties() {
		this.golsMandantePenalties = 0;
		this.golsVisitantePenalties = 0;
		this.penaltiesBatidosMandante = 0;
		this.penaltiesBatidosVisitante = 0;
		this.penaltiesDefendidosMandante = 0;
		this.penaltiesDefendidosVisitante = 0;
		this.penaltiesForaMandante = 0;
		this.penaltiesForaVisitante = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGolsMandantePenalties() {
		return golsMandantePenalties;
	}

	public void setGolsMandantePenalties(Integer golsMandantePenalties) {
		this.golsMandantePenalties = golsMandantePenalties;
	}

	public Integer getGolsVisitantePenalties() {
		return golsVisitantePenalties;
	}

	public void setGolsVisitantePenalties(Integer golsVisitantePenalties) {
		this.golsVisitantePenalties = golsVisitantePenalties;
	}

	public Integer getPenaltiesBatidosMandante() {
		return penaltiesBatidosMandante;
	}

	public void setPenaltiesBatidosMandante(Integer penaltiesBatidosMandante) {
		this.penaltiesBatidosMandante = penaltiesBatidosMandante;
	}

	public Integer getPenaltiesDefendidosMandante() {
		return penaltiesDefendidosMandante;
	}

	public void setPenaltiesDefendidosMandante(Integer penaltiesDefendidosMandante) {
		this.penaltiesDefendidosMandante = penaltiesDefendidosMandante;
	}

	public Integer getPenaltiesDefendidosVisitante() {
		return penaltiesDefendidosVisitante;
	}

	public void setPenaltiesDefendidosVisitante(Integer penaltiesDefendidosVisitante) {
		this.penaltiesDefendidosVisitante = penaltiesDefendidosVisitante;
	}

	public Integer getPenaltiesForaMandante() {
		return penaltiesForaMandante;
	}

	public void setPenaltiesForaMandante(Integer penaltiesForaMandante) {
		this.penaltiesForaMandante = penaltiesForaMandante;
	}

	public Integer getPenaltiesForaVisitante() {
		return penaltiesForaVisitante;
	}

	public void setPenaltiesForaVisitante(Integer penaltiesForaVisitante) {
		this.penaltiesForaVisitante = penaltiesForaVisitante;
	}

	public Integer getPenaltiesBatidosVisitante() {
		return penaltiesBatidosVisitante;
	}

	public void setPenaltiesBatidosVisitante(Integer penaltiesBatidosVisitante) {
		this.penaltiesBatidosVisitante = penaltiesBatidosVisitante;
	}

}
