package com.fastfoot.scheduler.model.entity;

import java.util.List;
import java.util.Objects;

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
public class CampeonatoMisto implements CampeonatoJogavel {

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

	@Transient
	private List<GrupoCampeonato> grupos;

	@Transient
	private List<RodadaEliminatoria> rodadasEliminatorias;
	
	@Transient
	private RodadaEliminatoria primeiraRodadaEliminatoria;

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

	public List<GrupoCampeonato> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoCampeonato> grupos) {
		this.grupos = grupos;
	}

	public List<RodadaEliminatoria> getRodadasEliminatorias() {
		return rodadasEliminatorias;
	}

	public void setRodadasEliminatorias(List<RodadaEliminatoria> rodadasFaseFinal) {
		this.rodadasEliminatorias = rodadasFaseFinal;
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
	
	public RodadaEliminatoria getPrimeiraRodadaEliminatoria() {
		return primeiraRodadaEliminatoria;
	}

	public void setPrimeiraRodadaEliminatoria(RodadaEliminatoria primeiraRodadaEliminatoria) {
		this.primeiraRodadaEliminatoria = primeiraRodadaEliminatoria;
	}

	/*@Override
	public Liga getLiga() {
		return null;
	}*/
	
	@Override
	public LigaJogo getLigaJogo() {
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampeonatoMisto other = (CampeonatoMisto) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "CampeonatoMisto [" + nivelCampeonato.name() + ", " + temporada.getAno() + "]";
	}
}
