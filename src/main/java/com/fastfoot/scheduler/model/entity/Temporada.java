package com.fastfoot.scheduler.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Temporada {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer ano;
	
	private Boolean atual;
	
	private Integer semanaAtual;
	
	@Transient
	private List<Semana> semanas;

	@Transient
	private List<Campeonato> campeonatosNacionais;
	
	@Transient
	private List<CampeonatoEliminatorio> campeonatosCopasNacionais;
	
	@Transient
	private List<CampeonatoMisto> campeonatosContinentais;
	
	@Transient
	private List<RodadaAmistosa> rodadasAmistosas;
	
	public Temporada() {

	}

	public Temporada(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Boolean getAtual() {
		return atual;
	}

	public void setAtual(Boolean atual) {
		this.atual = atual;
	}

	public List<Semana> getSemanas() {
		return semanas;
	}

	public void setSemanas(List<Semana> semanas) {
		this.semanas = semanas;
	}

	public Integer getSemanaAtual() {
		return semanaAtual;
	}

	public void setSemanaAtual(Integer semanaAtual) {
		this.semanaAtual = semanaAtual;
	}

	public List<Campeonato> getCampeonatosNacionais() {
		return campeonatosNacionais;
	}

	public void setCampeonatosNacionais(List<Campeonato> campeonatosNacionais) {
		this.campeonatosNacionais = campeonatosNacionais;
	}

	public List<CampeonatoEliminatorio> getCampeonatosCopasNacionais() {
		return campeonatosCopasNacionais;
	}

	public void setCampeonatosCopasNacionais(List<CampeonatoEliminatorio> campeonatosCopasNacionais) {
		this.campeonatosCopasNacionais = campeonatosCopasNacionais;
	}

	public List<CampeonatoMisto> getCampeonatosContinentais() {
		return campeonatosContinentais;
	}

	public void setCampeonatosContinentais(List<CampeonatoMisto> campeonatosContinentais) {
		this.campeonatosContinentais = campeonatosContinentais;
	}

	public List<RodadaAmistosa> getRodadasAmistosas() {
		return rodadasAmistosas;
	}

	public void setRodadasAmistosas(List<RodadaAmistosa> rodadasAmistosas) {
		this.rodadasAmistosas = rodadasAmistosas;
	}

	@Override
	public String toString() {
		return "Temporada [ano=" + ano + ", atual=" + atual + ", semanaAtual=" + semanaAtual + "]";
	}

}
