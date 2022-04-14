package com.fastfoot.scheduler.model.entity;

import java.util.List;
import java.util.Objects;

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
public class CampeonatoMisto implements CampeonatoJogavel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;

	//private Integer rodadaAtual;//TODO: remover

	//private Integer ano;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private NivelCampeonato nivelCampeonato;

	@Transient
	private List<GrupoCampeonato> grupos;

	@Transient
	private List<RodadaEliminatoria> rodadasEliminatorias;

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

	/*public Integer getRodadaAtual() {
		return rodadaAtual;
	}

	public void setRodadaAtual(Integer rodadaAtual) {
		this.rodadaAtual = rodadaAtual;
	}*/

	/*public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}*/

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
	public Liga getLiga() {
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
