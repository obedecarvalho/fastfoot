package com.fastfoot.player.model;

public enum CelulaDesenvolvimento {
	CELULA_1,
	CELULA_2,
	CELULA_3,
	CELULA_4,
	CELULA_5;
	
	private static final CelulaDesenvolvimento[] ALL = new CelulaDesenvolvimento[] { CELULA_1, CELULA_2, CELULA_3,
			CELULA_4, CELULA_5 };
	
	public static CelulaDesenvolvimento[] getAll() {
		return ALL;
	}
}
