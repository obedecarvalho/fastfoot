package com.fastfoot.scheduler.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class GrupoCampeonato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
