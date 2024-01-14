package com.fastfoot.match.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
public class PartidaTorcida {
	//TODO: salvar renda mandante e visitante
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaTorcidaSequence")
	@SequenceGenerator(name = "partidaTorcidaSequence", sequenceName = "partida_torcida_seq")
	private Long id;
	
	private Integer publico;
	
	@Transient
	private PartidaResultadoJogavel partida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPublico() {
		return publico;
	}

	public void setPublico(Integer publico) {
		this.publico = publico;
	}

	public PartidaResultadoJogavel getPartida() {
		return partida;
	}

	public void setPartida(PartidaResultadoJogavel partida) {
		this.partida = partida;
	}

}
