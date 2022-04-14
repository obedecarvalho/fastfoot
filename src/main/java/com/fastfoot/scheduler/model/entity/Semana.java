package com.fastfoot.scheduler.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * 
 * @author obede
 *
 */

@Entity
public class Semana {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer numero;//Semanas vão de 1 a 25 durante a temporada
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@Transient
	List<Rodada> rodadas;
	
	@Transient
	List<RodadaEliminatoria> rodadasEliminatorias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}
	
	public List<RodadaEliminatoria> getRodadasEliminatorias() {
		return rodadasEliminatorias;
	}

	public void setRodadasEliminatorias(List<RodadaEliminatoria> rodadasEliminatorias) {
		this.rodadasEliminatorias = rodadasEliminatorias;
	}

	public void addRodada(Rodada rodada) {
		if (rodadas == null) {
			rodadas = new ArrayList<Rodada>();
		}
		rodadas.add(rodada);
	}
	
	public void addRodadaEliminatoria(RodadaEliminatoria rodada) {
		if (rodadasEliminatorias == null) {
			rodadasEliminatorias = new ArrayList<RodadaEliminatoria>();
		}
		rodadasEliminatorias.add(rodada);
	}

	@Override
	public String toString() {
		return "Semana [" + temporada.getAno() + "/" + numero + "]";
	}

}
