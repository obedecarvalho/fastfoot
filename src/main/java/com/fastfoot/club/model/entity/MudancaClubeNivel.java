package com.fastfoot.club.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
@Table(indexes = { @Index(columnList = "id_clube") })
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

	@Override
	public String toString() {
		return "MudancaClubeNivel [clube=" + clube + ", clubeNivelAntigo=" + clubeNivelAntigo + ", clubeNivelNovo="
				+ clubeNivelNovo + "]";
	}

}
