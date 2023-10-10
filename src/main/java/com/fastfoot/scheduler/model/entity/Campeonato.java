package com.fastfoot.scheduler.model.entity;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.NivelCampeonatoAttributeConverter;

@Entity
public class Campeonato implements CampeonatoJogavel {
	
	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campeonatoSequence")
	@SequenceGenerator(name = "campeonatoSequence", sequenceName = "campeonato_seq")
	private Long id;
	
	private String nome;

	private Integer rodadaAtual;
	
	private Integer totalRodadas;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

	@Convert(converter = NivelCampeonatoAttributeConverter.class)
	private NivelCampeonato nivelCampeonato;
	
	@ManyToOne
	@JoinColumn(name = "id_liga_jogo")
	private LigaJogo ligaJogo;
	
	@Transient
	private List<Classificacao> classificacao;
	
	@Transient
	private List<Rodada> rodadas;
	
	public Campeonato() {
	}
	
	public Campeonato(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public Integer getRodadaAtual() {
		return rodadaAtual;
	}

	public void setRodadaAtual(Integer rodadaAtual) {
		this.rodadaAtual = rodadaAtual;
	}

	@Override
	public Integer getTotalRodadas() {
		return totalRodadas;
	}

	public void setTotalRodadas(Integer totalRodadas) {
		this.totalRodadas = totalRodadas;
	}

	public List<Classificacao> getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(List<Classificacao> classificacao) {
		this.classificacao = classificacao;
	}

	@Override
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

	@Override
	public LigaJogo getLigaJogo() {
		return ligaJogo;
	}

	public void setLigaJogo(LigaJogo ligaJogo) {
		this.ligaJogo = ligaJogo;
	}

	@JsonIgnore
	public boolean isNacional() {
		return NivelCampeonato.NACIONAL.equals(nivelCampeonato);
	}

	@JsonIgnore
	public boolean isNacionalII() {
		return NivelCampeonato.NACIONAL_II.equals(nivelCampeonato);
	}

	@Override
	public String toString() {
		return "Campeonato [" + ligaJogo.getLiga().name() + ", " + temporada.getAno() + ", " + nivelCampeonato.name() + "]";
	}

}
