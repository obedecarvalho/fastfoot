package com.fastfoot.player.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class JogadorEnergia {//TODO: tipoenergia inicial/recuperacao/gasto
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEnergiaSequence")
	@SequenceGenerator(name = "jogadorEnergiaSequence", sequenceName = "jogador_energia_seq")
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Integer energia;//Variação de energia
	
	@Transient
	private Integer energiaInicial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Integer getEnergia() {
		return energia;
	}

	public void setEnergia(Integer energia) {
		this.energia = energia;
	}

	public Integer getEnergiaInicial() {
		return energiaInicial;
	}

	public void setEnergiaInicial(Integer energiaInicial) {
		this.energiaInicial = energiaInicial;
	}
	
	public void adicionarEnergia(Integer energia) {
		this.energia += energia;
	}
	
	@Transient
	public Integer getEnergiaAtual() {
		return energiaInicial + energia;
	}

	@Override
	public String toString() {
		return "JogadorEnergia [jogador=" + jogador + ", energia=" + energia + "]";
	}

}
