package com.fastfoot.player.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Semana;

@Entity
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contratoSequence")	
	@SequenceGenerator(name = "contratoSequence", sequenceName = "contrato_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	@ManyToOne
	@JoinColumn(name = "id_semana_inicial")
	private Semana semanaInicial;

	private Boolean ativo;

	/**
	 * Numero de temporadas de duracao do contrato.
	 * 
	 * O contrato dura até a ultima semana da temporada de ano igual a:
	 * 	- (semanaInicial.temporada.ano + temporadasDuracao - 1)
	 */
	private Integer temporadasDuracao;

	private Double salario;
	
	public Contrato() {

	}

	public Contrato(Clube clube, Jogador jogador, Semana semanaInicial, Integer temporadasDuracao, Boolean ativo, Double salario) {
		super();
		this.clube = clube;
		this.jogador = jogador;
		this.semanaInicial = semanaInicial;
		this.ativo = ativo;
		this.temporadasDuracao = temporadasDuracao;
		this.salario = salario;
	}

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

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Semana getSemanaInicial() {
		return semanaInicial;
	}

	public void setSemanaInicial(Semana semanaInicial) {
		this.semanaInicial = semanaInicial;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getTemporadasDuracao() {
		return temporadasDuracao;
	}

	public void setTemporadasDuracao(Integer temporadasDuracao) {
		this.temporadasDuracao = temporadasDuracao;
	}
	
	public Integer getAnoFinalContrato() {
		return this.semanaInicial.getTemporada().getAno() + temporadasDuracao - 1;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return "Contrato [semanaInicial=" + semanaInicial + ", temporadasDuracao=" + temporadasDuracao
				+ ", getAnoFinalContrato()=" + getAnoFinalContrato() + "]";
	}

}
