package com.fastfoot.scheduler.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.NivelCampeonato;

@Entity
public class Campeonato implements CampeonatoJogavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Liga liga;
	
	private String nome;

	//private Integer rodadaAtual;//TODO: remover
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

	private NivelCampeonato nivelCampeonato;
	
	@Transient
	private List<Classificacao> classificacao;
	
	@Transient
	private List<Rodada> rodadas;
	
	//private Integer ano;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	@Override
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	/*public Integer getRodadaAtual() {
		return rodadaAtual;
	}

	public void setRodadaAtual(Integer rodadaAtual) {
		this.rodadaAtual = rodadaAtual;
	}*/

	public List<Classificacao> getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(List<Classificacao> classificacao) {
		this.classificacao = classificacao;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	@Override
	public NivelCampeonato getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	public boolean isNacional() {
		return NivelCampeonato.NACIONAL.equals(nivelCampeonato);
	}

	public boolean isNacionalII() {
		return NivelCampeonato.NACIONAL_II.equals(nivelCampeonato);
	}

	@Override
	public String toString() {
		return "Campeonato [" + liga.name() + ", " + temporada.getAno() + ", " + nivelCampeonato.name() + "]";
	}

	/*public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}*/
	
	
}
