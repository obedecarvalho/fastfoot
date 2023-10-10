package com.fastfoot.match.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class PartidaDisputaPenalties {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaDisputaPenaltiesSequence")
	@SequenceGenerator(name = "partidaDisputaPenaltiesSequence", sequenceName = "partida_disputa_penalties_seq")
	private Long id;
	
	private Integer golsMandantePenalties;
	
	private Integer golsVisitantePenalties;
	
	private Integer numeroPenaltiesBatidosMandante;
	
	private Integer numeroPenaltiesBatidosVisitante;
	
	private Integer penaltiesDefendidosMandante;
	
	private Integer penaltiesDefendidosVisitante;
	
	private Integer penaltiesForaMandante;
	
	private Integer penaltiesForaVisitante;
	
	public PartidaDisputaPenalties() {
		this.golsMandantePenalties = 0;
		this.golsVisitantePenalties = 0;
		this.numeroPenaltiesBatidosMandante = 0;
		this.numeroPenaltiesBatidosVisitante = 0;
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

	public Integer getNumeroPenaltiesBatidosMandante() {
		return numeroPenaltiesBatidosMandante;
	}

	public void setNumeroPenaltiesBatidosMandante(Integer numeroPenaltiesBatidosMandante) {
		this.numeroPenaltiesBatidosMandante = numeroPenaltiesBatidosMandante;
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

	public Integer getNumeroPenaltiesBatidosVisitante() {
		return numeroPenaltiesBatidosVisitante;
	}

	public void setNumeroPenaltiesBatidosVisitante(Integer numeroPenaltiesBatidosVisitante) {
		this.numeroPenaltiesBatidosVisitante = numeroPenaltiesBatidosVisitante;
	}

}
