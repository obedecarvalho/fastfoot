package com.fastfoot.transfer.model;

public enum NivelAdequacao {

	A(0.875), 
	B(0.775), 
	C(0.675), 
	D(0.500), 
	E(0.240),
	F(0d);

	private Double porcentagemMinima;
	
	private NivelAdequacao(Double porcentagemMinima) {
		this.porcentagemMinima = porcentagemMinima;
	}

	public Double getPorcentagemMinima() {
		return porcentagemMinima;
	}

}
