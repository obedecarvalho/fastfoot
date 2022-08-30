package com.fastfoot.transfer.model;

import com.fastfoot.player.model.factory.JogadorFactory;

public enum NivelAdequacao {//TODO: adequar porcentagemMinima
	
	A(0.875), 
	B(0.800), 
	C(0.700), 
	D(0.500), 
	E(JogadorFactory.POT_DES_PORC_INICIAL / 100),
	F(0d);

	//private Double porcentagemMaxima;
	
	private Double porcentagemMinima;
	
	private NivelAdequacao(Double porcentagemMinima /*, Double porcentagemMaxima*/) {
		//this.porcentagemMaxima = porcentagemMaxima;
		this.porcentagemMinima = porcentagemMinima;
	}

	/*public Double getPorcentagemMaxima() {
		return porcentagemMaxima;
	}*/

	public Double getPorcentagemMinima() {
		return porcentagemMinima;
	}
	
	/*public List<NivelAdequacao> getAll(){
		
	}*/
}
