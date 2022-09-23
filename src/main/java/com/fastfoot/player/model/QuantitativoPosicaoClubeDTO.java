package com.fastfoot.player.model;

import com.fastfoot.club.model.entity.Clube;

public class QuantitativoPosicaoClubeDTO implements Comparable<QuantitativoPosicaoClubeDTO> {
	
	private Posicao posicao;
	
	private Clube clube;
	
	private Integer qtde;

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	@Override
	public int compareTo(QuantitativoPosicaoClubeDTO o) {
		return this.qtde.compareTo(o.getQtde());
	}

	@Override
	public String toString() {
		return "QuantitativoPosicaoClubeDTO [posicao=" + posicao + ", qtde=" + qtde + "]";
	}

}
