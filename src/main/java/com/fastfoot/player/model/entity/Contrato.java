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
import com.fastfoot.scheduler.model.entity.Temporada;

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
	
	@ManyToOne
	@JoinColumn(name = "id_temporada_final")
	private Temporada temporadaFinal;
	
	@ManyToOne
	@JoinColumn(name = "id_semana_rescisao")
	private Semana semanaRescisao;
	
	private Boolean rescindido;
	
	public Contrato() {

	}

	public Contrato(Clube clube, Jogador jogador, Semana semanaInicial, Temporada temporadaFinal, Boolean rescindido) {
		super();
		this.clube = clube;
		this.jogador = jogador;
		this.semanaInicial = semanaInicial;
		this.temporadaFinal = temporadaFinal;
		this.rescindido = rescindido;
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

	public Temporada getTemporadaFinal() {
		return temporadaFinal;
	}

	public void setTemporadaFinal(Temporada temporadaFinal) {
		this.temporadaFinal = temporadaFinal;
	}

	public Boolean getRescindido() {
		return rescindido;
	}

	public void setRescindido(Boolean rescindido) {
		this.rescindido = rescindido;
	}

	public Semana getSemanaRescisao() {
		return semanaRescisao;
	}

	public void setSemanaRescisao(Semana semanaRescisao) {
		this.semanaRescisao = semanaRescisao;
	}

}
