package com.fastfoot.scheduler.model.entity;

import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.NivelCampeonatoAttributeConverter;

@Entity
public class CampeonatoEliminatorio implements CampeonatoJogavel {

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
	private List<RodadaEliminatoria> rodadas;

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

	public List<RodadaEliminatoria> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<RodadaEliminatoria> rodadas) {
		this.rodadas = rodadas;
	}

	@Override
	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	@Override
	public LigaJogo getLigaJogo() {
		return ligaJogo;
	}

	public void setLigaJogo(LigaJogo ligaJogo) {
		this.ligaJogo = ligaJogo;
	}

	@Override
	public NivelCampeonato getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	@Override
	public String toString() {
		return "CampeonatoEliminatorio [" + ligaJogo.getLiga().name() + ", " + temporada.getAno() + ", " + nivelCampeonato.name() + "]";
	}
}
