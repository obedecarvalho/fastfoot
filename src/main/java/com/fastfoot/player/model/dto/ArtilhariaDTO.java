package com.fastfoot.player.model.dto;

import com.fastfoot.player.model.entity.Jogador;

public class ArtilhariaDTO {
	
	private Jogador jogador;
	
	private Integer qtdeGols;

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

}
