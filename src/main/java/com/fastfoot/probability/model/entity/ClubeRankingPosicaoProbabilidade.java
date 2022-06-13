package com.fastfoot.probability.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class ClubeRankingPosicaoProbabilidade {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeRankingPosicaoProbabilidadeSequence")	
	@SequenceGenerator(name = "clubeRankingPosicaoProbabilidadeSequence", sequenceName = "clube_ranking_posicao_probabilidade_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private ClubeProbabilidade clubeProbabilidade;
	
	private Integer posicaoGeral;
	
	private Integer probabilidade;

	/*private Integer pontuacaoMaxima;
	
	private Double pontuacaoMedia;
	
	private Integer pontuacaoMinima;*/

	public ClubeProbabilidade getClubeProbabilidade() {
		return clubeProbabilidade;
	}

	public void setClubeProbabilidade(ClubeProbabilidade clubeProbabilidade) {
		this.clubeProbabilidade = clubeProbabilidade;
	}

	public Integer getPosicaoGeral() {
		return posicaoGeral;
	}

	public void setPosicaoGeral(Integer posicaoGeral) {
		this.posicaoGeral = posicaoGeral;
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

	/*public Integer getPontuacaoMaxima() {
		return pontuacaoMaxima;
	}

	public void setPontuacaoMaxima(Integer pontuacaoMaxima) {
		this.pontuacaoMaxima = pontuacaoMaxima;
	}

	public Double getPontuacaoMedia() {
		return pontuacaoMedia;
	}

	public void setPontuacaoMedia(Double pontuacaoMedia) {
		this.pontuacaoMedia = pontuacaoMedia;
	}

	public Integer getPontuacaoMinima() {
		return pontuacaoMinima;
	}

	public void setPontuacaoMinima(Integer pontuacaoMinima) {
		this.pontuacaoMinima = pontuacaoMinima;
	}*/
}
