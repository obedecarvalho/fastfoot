package com.fastfoot.match.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Entity
public class PartidaTorcida {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaTorcidaSequence")
	@SequenceGenerator(name = "partidaTorcidaSequence", sequenceName = "partida_torcida_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_amistosa_resultado")
	private PartidaAmistosaResultado partidaAmistosaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;
	
	private Integer publico;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartidaResultado getPartidaResultado() {
		return partidaResultado;
	}

	public void setPartidaResultado(PartidaResultado partidaResultado) {
		this.partidaResultado = partidaResultado;
	}

	public PartidaAmistosaResultado getPartidaAmistosaResultado() {
		return partidaAmistosaResultado;
	}

	public void setPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado) {
		this.partidaAmistosaResultado = partidaAmistosaResultado;
	}

	public PartidaEliminatoriaResultado getPartidaEliminatoriaResultado() {
		return partidaEliminatoriaResultado;
	}

	public void setPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado) {
		this.partidaEliminatoriaResultado = partidaEliminatoriaResultado;
	}

	public Integer getPublico() {
		return publico;
	}

	public void setPublico(Integer publico) {
		this.publico = publico;
	}

}
