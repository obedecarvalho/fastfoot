package com.fastfoot.club.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
//@Table(indexes = { @Index(columnList = "id_clube") })
public class MudancaClubeNivel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mudancaClubeNivelSequence")	
	@SequenceGenerator(name = "mudancaClubeNivelSequence", sequenceName = "mudanca_clube_nivel_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private ClubeNivel clubeNivelAntigo;
	
	private ClubeNivel clubeNivelNovo;
	
	private Boolean internacional;

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

	public ClubeNivel getClubeNivelAntigo() {
		return clubeNivelAntigo;
	}

	public void setClubeNivelAntigo(ClubeNivel clubeNivelAntigo) {
		this.clubeNivelAntigo = clubeNivelAntigo;
	}

	public ClubeNivel getClubeNivelNovo() {
		return clubeNivelNovo;
	}

	public void setClubeNivelNovo(ClubeNivel clubeNivelNovo) {
		this.clubeNivelNovo = clubeNivelNovo;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public Boolean getInternacional() {
		return internacional;
	}

	public void setInternacional(Boolean internacional) {
		this.internacional = internacional;
	}

	@Override
	public String toString() {
		return "MudancaClubeNivel [clube=" + clube + ", clubeNivelAntigo=" + clubeNivelAntigo + ", clubeNivelNovo="
				+ clubeNivelNovo + "]";
	}

}
