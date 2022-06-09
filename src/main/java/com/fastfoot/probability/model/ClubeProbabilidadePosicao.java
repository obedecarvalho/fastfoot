package com.fastfoot.probability.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class ClubeProbabilidadePosicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeProbabilidadePosicaoSequence")	
	@SequenceGenerator(name = "clubeProbabilidadePosicaoSequence", sequenceName = "clube_probabilidade_posicao_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private ClubeProbabilidade clubeProbabilidade;
	
	private Integer posicao;
	
	private Integer probabilidade;

	public ClubeProbabilidade getClubeProbabilidade() {
		return clubeProbabilidade;
	}

	public void setClubeProbabilidade(ClubeProbabilidade clubeProbabilidade) {
		this.clubeProbabilidade = clubeProbabilidade;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public Integer getProbabilidade() {
		return probabilidade;
	}

	public void setProbabilidade(Integer probabilidade) {
		this.probabilidade = probabilidade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
