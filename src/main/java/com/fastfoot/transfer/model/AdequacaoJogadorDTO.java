package com.fastfoot.transfer.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

public class AdequacaoJogadorDTO {
	
	private Clube clube;
	
	private Jogador jogador;
	
	private NivelAdequacao nivelAdequacao;
	
	public AdequacaoJogadorDTO() {

	}
	
	public AdequacaoJogadorDTO(Clube clube, Jogador jogador, NivelAdequacao nivelAdequacao) {
		this.clube = clube;
		this.jogador = jogador;
		this.nivelAdequacao = nivelAdequacao;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public NivelAdequacao getNivelAdequacao() {
		return nivelAdequacao;
	}

	public void setNivelAdequacao(NivelAdequacao nivelAdequacao) {
		this.nivelAdequacao = nivelAdequacao;
	}

}
