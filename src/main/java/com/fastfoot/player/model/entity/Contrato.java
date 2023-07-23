package com.fastfoot.player.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

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
	
	/*@ManyToOne
	@JoinColumn(name = "id_temporada_final")
	private Temporada temporadaFinal;*/
	
	/*@ManyToOne
	@JoinColumn(name = "id_semana_rescisao")
	private Semana semanaRescisao;*/
	
	//private Boolean rescindido;

	private Boolean ativo;

	/**
	 * Numero de temporadas de duracao do contrato.
	 * 
	 * O contrato dura até a ultima semana da temporada de ano igual a:
	 * 	- (semanaInicial.temporada.ano + numeroTemporadasDuracao - 1)
	 */
	private Integer numeroTemporadasDuracao;

	private Double salario;
	
	public Contrato() {

	}

	public Contrato(Clube clube, Jogador jogador, Semana semanaInicial, Integer numeroTemporadasDuracao, Boolean ativo, Double salario) {
		super();
		this.clube = clube;
		this.jogador = jogador;
		this.semanaInicial = semanaInicial;
		//this.temporadaFinal = temporadaFinal;
		//this.rescindido = rescindido;
		this.ativo = ativo;
		this.numeroTemporadasDuracao = numeroTemporadasDuracao;
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

	/*public Temporada getTemporadaFinal() {
		return temporadaFinal;
	}

	public void setTemporadaFinal(Temporada temporadaFinal) {
		this.temporadaFinal = temporadaFinal;
	}*/

	/*public Boolean getRescindido() {
		return rescindido;
	}

	public void setRescindido(Boolean rescindido) {
		this.rescindido = rescindido;
	}*/

	/*public Semana getSemanaRescisao() {
		return semanaRescisao;
	}*/

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/*public void setSemanaRescisao(Semana semanaRescisao) {
		this.semanaRescisao = semanaRescisao;
	}*/

	public Integer getNumeroTemporadasDuracao() {
		return numeroTemporadasDuracao;
	}

	public void setNumeroTemporadasDuracao(Integer numeroTemporadasDuracao) {
		this.numeroTemporadasDuracao = numeroTemporadasDuracao;
	}
	
	public Integer getAnoFinalContrato() {
		return this.semanaInicial.getTemporada().getAno() + numeroTemporadasDuracao - 1;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return "Contrato [semanaInicial=" + semanaInicial + ", numeroTemporadasDuracao=" + numeroTemporadasDuracao
				+ ", getAnoFinalContrato()=" + getAnoFinalContrato() + "]";
	}

}
