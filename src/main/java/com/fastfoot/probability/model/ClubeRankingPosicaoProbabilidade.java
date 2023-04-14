package com.fastfoot.probability.model;

import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;

public class ClubeRankingPosicaoProbabilidade {

	private CampeonatoClubeProbabilidade clubeProbabilidade;
	
	private Integer posicaoGeral;
	
	private Integer probabilidade;

	public CampeonatoClubeProbabilidade getClubeProbabilidade() {
		return clubeProbabilidade;
	}

	public void setClubeProbabilidade(CampeonatoClubeProbabilidade clubeProbabilidade) {
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
