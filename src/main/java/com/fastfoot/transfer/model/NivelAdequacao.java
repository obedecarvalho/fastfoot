package com.fastfoot.transfer.model;

import com.fastfoot.player.model.factory.JogadorFactory;

public enum NivelAdequacao {

	A(0.875, 30), 
	B(0.775, 32), 
	C(0.675, 34), 
	D(0.500, JogadorFactory.IDADE_MAX), 
	E(0.240, JogadorFactory.IDADE_MAX),
	F(0d, JogadorFactory.IDADE_MAX);

	private Double porcentagemMinima;

	private Integer idadeMaxContratar;
	
	private NivelAdequacao(Double porcentagemMinima, Integer idadeMaxContratar) {
		this.porcentagemMinima = porcentagemMinima;
		this.idadeMaxContratar = idadeMaxContratar;
	}

	public Double getPorcentagemMinima() {
		return porcentagemMinima;
	}

	public Integer getIdadeMaxContratar() {
		return idadeMaxContratar;
	}

}
