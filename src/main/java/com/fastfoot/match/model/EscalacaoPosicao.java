package com.fastfoot.match.model;

import java.util.Arrays;
import java.util.List;

public enum EscalacaoPosicao {
	P_1(true),
	P_2(true),
	P_3(true),
	P_4(true),
	P_5(true),
	P_6(true),
	P_7(true),
	P_8(true),
	P_9(true),
	P_10(true),
	P_11(true),
	//Suprentes
	P_12(false), //GOL
	P_13(false), //ZAG
	P_15(false), //VOL
	P_16(false), //LAT
	P_17(false), //MEI
	P_19(false); //ATA
	
	private boolean titular;
	
	private EscalacaoPosicao (boolean titular) {
		this.titular = titular;
	}
	
	public static List<EscalacaoPosicao> getEscalacaoTitulares() {
		return Arrays.asList(P_1, P_2, P_3, P_4, P_6, P_5, P_8, P_7, P_10, P_9, P_11);
	}

	public boolean isTitular() {
		return titular;
	}
}
