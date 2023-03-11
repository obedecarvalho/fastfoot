package com.fastfoot.player.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class JogadorEnergia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEnergiaSequence")
	@SequenceGenerator(name = "jogadorEnergiaSequence", sequenceName = "jogador_energia_seq")
	private Long id;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	/*@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;*/
	
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

	/*public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}*/

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
