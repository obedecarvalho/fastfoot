package com.fastfoot.club.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
public class MudancaTreinador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mudancaTreinadorSequence")	
	@SequenceGenerator(name = "mudancaTreinadorSequence", sequenceName = "mudanca_treinador_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_treinador_antigo")
	private Treinador treinadorAntigo;
	
	@ManyToOne
	@JoinColumn(name = "id_treinador_novo")
	private Treinador treinadorNovo;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

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

	public Treinador getTreinadorAntigo() {
		return treinadorAntigo;
	}

	public void setTreinadorAntigo(Treinador treinadorAntigo) {
		this.treinadorAntigo = treinadorAntigo;
	}

	public Treinador getTreinadorNovo() {
		return treinadorNovo;
	}

	public void setTreinadorNovo(Treinador treinadorNovo) {
		this.treinadorNovo = treinadorNovo;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

}
