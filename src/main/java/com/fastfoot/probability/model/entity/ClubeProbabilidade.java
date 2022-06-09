package com.fastfoot.probability.model.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;

@Entity
public class ClubeProbabilidade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeProbabilidadeSequence")	
	@SequenceGenerator(name = "clubeProbabilidadeSequence", sequenceName = "clube_probabilidade_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_campeonato")
	private Campeonato campeonato;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@OneToMany(mappedBy = "clubeProbabilidade", fetch = FetchType.LAZY)
	private List<ClubeProbabilidadePosicao> probabilidadePosicao;

	private Integer probabilidadeCampeao;
	
	private Integer probabilidadeRebaixamento;
	
	private Integer probabilidadeAcesso;

	private Integer probabilidadeClassificacaoContinental;
	
	private Integer probabilidadeClassificacaoCopaNacional;

	@Transient
	private Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao;//Key: posicao

	public ClubeProbabilidade() {
		probabilidadeCampeao = 0;
		probabilidadeRebaixamento = 0;
		probabilidadeAcesso = 0;
		probabilidadeClassificacaoContinental = 0;
		probabilidadeClassificacaoCopaNacional = 0;
	}
	
	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Integer, ClubeProbabilidadePosicao> getClubeProbabilidadePosicao() {
		return clubeProbabilidadePosicao;
	}

	public void setClubeProbabilidadePosicao(Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao) {
		this.clubeProbabilidadePosicao = clubeProbabilidadePosicao;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public List<ClubeProbabilidadePosicao> getProbabilidadePosicao() {
		return probabilidadePosicao;
	}

	public void setProbabilidadePosicao(List<ClubeProbabilidadePosicao> probabilidadePosicao) {
		this.probabilidadePosicao = probabilidadePosicao;
	}

	public Integer getProbabilidadeCampeao() {
		return probabilidadeCampeao;
	}

	public void setProbabilidadeCampeao(Integer probabilidadeCampeao) {
		this.probabilidadeCampeao = probabilidadeCampeao;
	}

	public Integer getProbabilidadeRebaixamento() {
		return probabilidadeRebaixamento;
	}

	public void setProbabilidadeRebaixamento(Integer probabilidadeRebaixamento) {
		this.probabilidadeRebaixamento = probabilidadeRebaixamento;
	}

	public Integer getProbabilidadeAcesso() {
		return probabilidadeAcesso;
	}

	public void setProbabilidadeAcesso(Integer probabilidadeAcesso) {
		this.probabilidadeAcesso = probabilidadeAcesso;
	}

	public Integer getProbabilidadeClassificacaoContinental() {
		return probabilidadeClassificacaoContinental;
	}

	public void setProbabilidadeClassificacaoContinental(Integer probabilidadeClassificacaoContinental) {
		this.probabilidadeClassificacaoContinental = probabilidadeClassificacaoContinental;
	}

	public Integer getProbabilidadeClassificacaoCopaNacional() {
		return probabilidadeClassificacaoCopaNacional;
	}

	public void setProbabilidadeClassificacaoCopaNacional(Integer probabilidadeClassificacaoCopaNacional) {
		this.probabilidadeClassificacaoCopaNacional = probabilidadeClassificacaoCopaNacional;
	}

}
