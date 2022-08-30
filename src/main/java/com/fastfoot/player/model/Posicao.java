package com.fastfoot.player.model;

public enum Posicao {
	GOLEIRO, 
	ZAGUEIRO, 
	LATERAL, 
	VOLANTE,
	/*@Deprecated
	MEIA_LATERAL,*/ 
	MEIA, 
	ATACANTE;
	
	public boolean isGoleiro() {
		return this == GOLEIRO;
	}
	
	public boolean isZagueiro() {
		return this == ZAGUEIRO;
	}
	
	public boolean isLateral() {
		return this == LATERAL;
	}
	
	public boolean isVolante() {
		return this == VOLANTE;
	}
	
	public boolean isMeia() {
		return this == MEIA;
	}
	
	public boolean isAtacante() {
		return this == ATACANTE;
	}

}
