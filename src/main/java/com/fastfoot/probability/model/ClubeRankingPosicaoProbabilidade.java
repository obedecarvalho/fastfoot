package com.fastfoot.probability.model;

import com.fastfoot.probability.model.entity.ClubeProbabilidade;

public class ClubeRankingPosicaoProbabilidade {

	private ClubeProbabilidade clubeProbabilidade;
	
	private Integer posicaoGeral;
	
	private Integer probabilidade;

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

}
