package com.fastfoot.player.model.dto;

import com.fastfoot.player.model.entity.Jogador;

public class ArtilhariaDTO {
	
	private Jogador jogador;
	
	private Integer qtdeGols;
	
	private Integer qtdeGolsAmistosos;//TODO
	
	private Integer numeroJogos;//TODO
	
	private Integer numeroTemporadas;//TODO

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Integer getQtdeGols() {
		return qtdeGols;
	}

	public void setQtdeGols(Integer qtdeGols) {
		this.qtdeGols = qtdeGols;
	}

	public Integer getQtdeGolsAmistosos() {
		return qtdeGolsAmistosos;
	}

	public void setQtdeGolsAmistosos(Integer qtdeGolsAmistosos) {
		this.qtdeGolsAmistosos = qtdeGolsAmistosos;
	}

	public Integer getNumeroJogos() {
		return numeroJogos;
	}

	public void setNumeroJogos(Integer numeroJogos) {
		this.numeroJogos = numeroJogos;
	}

	public Integer getNumeroTemporadas() {
		return numeroTemporadas;
	}

	public void setNumeroTemporadas(Integer numeroTemporadas) {
		this.numeroTemporadas = numeroTemporadas;
	}

}
