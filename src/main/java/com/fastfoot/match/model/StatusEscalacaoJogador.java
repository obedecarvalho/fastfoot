package com.fastfoot.match.model;

public enum StatusEscalacaoJogador {

	TITULAR,
	RESERVA,
	TITULAR_SUBSTITUIDO,//Titular que saiu
	RESERVA_SUBSTITUTO,//Reserva que entrou
	RESERVA_SUBSTITUIDO,//Reserva que entrou e saiu
	;
	
	public boolean isPossivelSubstituido() {
		return TITULAR.equals(this) || RESERVA_SUBSTITUTO.equals(this);
	}
	
	public boolean isPossivelSubstituto() {
		return RESERVA.equals(this);
	}

	public StatusEscalacaoJogador getStatusSubstituido() {
		if (TITULAR.equals(this)) {
			return TITULAR_SUBSTITUIDO;
		}
		if (RESERVA_SUBSTITUTO.equals(this)){
			return RESERVA_SUBSTITUIDO;
		}
		return null;
	}
}
