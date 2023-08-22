package com.fastfoot.probability.model;

import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;

public class CampeonatoClubeProbabilidadePosicao {

	private CampeonatoClubeProbabilidade clubeProbabilidade;
	
	private Integer posicao;
	
	private Integer probabilidade;

	public CampeonatoClubeProbabilidade getClubeProbabilidade() {
		return clubeProbabilidade;
	}

	public void setClubeProbabilidade(CampeonatoClubeProbabilidade clubeProbabilidade) {
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

}
