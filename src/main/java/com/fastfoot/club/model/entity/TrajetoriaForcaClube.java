package com.fastfoot.club.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.entity.Semana;

@Entity
//@Table(indexes = { @Index(columnList = "id_clube") })
public class TrajetoriaForcaClube {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trajetoriaForcaClubeSequence")	
	@SequenceGenerator(name = "trajetoriaForcaClubeSequence", sequenceName = "trajetoria_forca_clube_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	private String trajetoriaForcaTitulares;
	
	private String trajetoriaIdadeTitulares;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public String getTrajetoriaForcaTitulares() {
		return trajetoriaForcaTitulares;
	}

	public void setTrajetoriaForcaTitulares(String trajetoriaForcaTitulares) {
		this.trajetoriaForcaTitulares = trajetoriaForcaTitulares;
	}

	public String getTrajetoriaIdadeTitulares() {
		return trajetoriaIdadeTitulares;
	}

	public void setTrajetoriaIdadeTitulares(String trajetoriaIdadeTitulares) {
		this.trajetoriaIdadeTitulares = trajetoriaIdadeTitulares;
	}

}
