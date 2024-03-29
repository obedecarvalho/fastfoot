package com.fastfoot.scheduler.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.RodadaJogavel;

/**
 * 
 * @author obede
 *
 */

@Entity
public class Semana {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semanaSequence")	
	@SequenceGenerator(name = "semanaSequence", sequenceName = "semana_seq")
	private Long id;
	
	private Integer numero;//Semanas vão de 1 a 25 durante a temporada
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

	@Transient
	private List<Rodada> rodadas;
	
	@Transient
	private List<RodadaEliminatoria> rodadasEliminatorias;
	
	@Transient
	private List<RodadaAmistosa> rodadasAmistosas;

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

	public List<RodadaAmistosa> getRodadasAmistosas() {
		return rodadasAmistosas;
	}

	public void setRodadasAmistosas(List<RodadaAmistosa> rodadasAmistosas) {
		this.rodadasAmistosas = rodadasAmistosas;
	}

	public void addRodada(Rodada rodada) {
		if (rodadas == null) {
			rodadas = new ArrayList<Rodada>();
		}
		rodadas.add(rodada);
	}
	
	public void addRodada(RodadaEliminatoria rodada) {
		if (rodadasEliminatorias == null) {
			rodadasEliminatorias = new ArrayList<RodadaEliminatoria>();
		}
		rodadasEliminatorias.add(rodada);
	}
	
	public void addRodada(RodadaAmistosa rodada) {
		if (rodadasAmistosas == null) {
			rodadasAmistosas = new ArrayList<RodadaAmistosa>();
		}
		rodadasAmistosas.add(rodada);
	}

	public boolean isUltimaSemana() {
		if (getNumero() == Constantes.NUM_SEMANAS) {
			return true;
		}
		return false;
	}

	public List<? extends RodadaJogavel> getRodadasJogavel() {
		return (rodadas != null) ? rodadas : rodadasEliminatorias;
	}

	@Override
	public String toString() {
		return "Semana [" + temporada.getAno() + "/" + numero + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Semana other = (Semana) obj;
		return Objects.equals(id, other.id);
	}

}
