package com.fastfoot.scheduler.model;

import com.fastfoot.model.Liga;

public class PosicaoClassificacaoContinentalLiga {
	
	private NivelCampeonato nivelCampeonato;
	
	private Liga liga;
	
	private Integer posicaoInicial;
	
	private Integer posicaoFinal;

	public NivelCampeonato getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public Integer getPosicaoInicial() {
		return posicaoInicial;
	}

	public void setPosicaoInicial(Integer posicaoInicial) {
		this.posicaoInicial = posicaoInicial;
	}

	public Integer getPosicaoFinal() {
		return posicaoFinal;
	}

	public void setPosicaoFinal(Integer posicaoFinal) {
		this.posicaoFinal = posicaoFinal;
	}

	@Override
	public String toString() {
		return "PosicaoClassificacaoContinentalLiga [liga=" + liga + ", nivel=" + nivelCampeonato
				+ ", posInicial=" + posicaoInicial + ", posFinal=" + posicaoFinal + "]";
	}

}
