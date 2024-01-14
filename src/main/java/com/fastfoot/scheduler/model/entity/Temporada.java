package com.fastfoot.scheduler.model.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.model.entity.Jogo;

@Entity
public class Temporada {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temporadaSequence")	
	@SequenceGenerator(name = "temporadaSequence", sequenceName = "temporada_seq")
	private Long id;
	
	private Integer ano;
	
	private Boolean atual;
	
	private Integer semanaAtual;
	
	@ManyToOne
	@JoinColumn(name = "id_jogo")
	private Jogo jogo;
	
	@Transient
	@JsonIgnore
	private List<Semana> semanas;

	@Transient
	@JsonIgnore
	private List<Campeonato> campeonatosNacionais;
	
	@Transient
	@JsonIgnore
	private List<CampeonatoEliminatorio> campeonatosCopasNacionais;
	
	@Transient
	@JsonIgnore
	private List<CampeonatoMisto> campeonatosContinentais;
	
	@Transient
	@JsonIgnore
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

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	@Override
	public String toString() {
		return "Temporada [ano=" + ano + ", atual=" + atual + ", semanaAtual=" + semanaAtual + "]";
	}

}
