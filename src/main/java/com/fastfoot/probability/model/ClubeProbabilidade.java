package com.fastfoot.probability.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	/*@Transient
	private Map<Integer, List<ClassificacaoProbabilidade>> posicaoQtde;*/
	
	@Transient
	private Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	/*public Map<Integer, List<ClassificacaoProbabilidade>> getPosicaoQtde() {
		return posicaoQtde;
	}

	public void setPosicaoQtde(Map<Integer, List<ClassificacaoProbabilidade>> posicaoQtde) {
		this.posicaoQtde = posicaoQtde;
	}*/

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

}
