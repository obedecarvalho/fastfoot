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

@Entity
public class GrupoCampeonato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupoCampeonatoSequence")	
	@SequenceGenerator(name = "grupoCampeonatoSequence", sequenceName = "grupo_campeonato_seq")
	private Long id;
	
	private Integer numero;

	@ManyToOne
	@JoinColumn(name = "id_campeonato_misto")
	private CampeonatoMisto campeonato;

	@Transient
	private List<Rodada> rodadas;

	@Transient
	private List<Classificacao> classificacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CampeonatoMisto getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(CampeonatoMisto campeonato) {
		this.campeonato = campeonato;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public List<Classificacao> getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(List<Classificacao> classificacao) {
		this.classificacao = classificacao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "GrupoCampeonato [" + numero + "]";
	}

}
