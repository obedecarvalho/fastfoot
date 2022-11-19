package com.fastfoot.probability.model;

import com.fastfoot.club.model.entity.Clube;

public class ClubeProbabilidadeFinalizacao {
	
	private Clube clube;
	
	private Double golsPartida;
	
	private Double finalizacoesPartidas;
	
	private Double probabilidadeFinalizacaoNoGol;
	
	private Double probabilidadeGolFinalizacao;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Double getGolsPartida() {
		return golsPartida;
	}

	public void setGolsPartida(Double golsPartida) {
		this.golsPartida = golsPartida;
	}

	public Double getFinalizacoesPartidas() {
		return finalizacoesPartidas;
	}

	public void setFinalizacoesPartidas(Double finalizacoesPartidas) {
		this.finalizacoesPartidas = finalizacoesPartidas;
	}

	public Double getProbabilidadeGolFinalizacao() {
		return probabilidadeGolFinalizacao;
	}

	public void setProbabilidadeGolFinalizacao(Double probabilidadeGolFinalizacao) {
		this.probabilidadeGolFinalizacao = probabilidadeGolFinalizacao;
	}

	public Double getProbabilidadeFinalizacaoNoGol() {
		return probabilidadeFinalizacaoNoGol;
	}

	public void setProbabilidadeFinalizacaoNoGol(Double probabilidadeFinalizacaoNoGol) {
		this.probabilidadeFinalizacaoNoGol = probabilidadeFinalizacaoNoGol;
	}

}
