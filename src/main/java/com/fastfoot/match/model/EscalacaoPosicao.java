package com.fastfoot.match.model;

import java.util.Arrays;
import java.util.List;

public enum EscalacaoPosicao {
	P_1(true, "GOL"),
	P_2(true, "LD"),
	P_3(true, "ZD"),
	P_4(true, "ZE"),
	P_6(true, "LE"),
	P_8(true, "VD"),
	P_5(true, "VE"),
	P_7(true, "MD"),
	P_10(true, "ME"),
	P_9(true, "AD"),	
	P_11(true, "AE"),
	//Suprentes
	P_12(false, "RES"), //GOL
	P_13(false, "RES"), //ZAG
	P_15(false, "RES"), //VOL
	P_16(false, "RES"), //LAT
	P_17(false, "RES"), //MEI
	P_19(false, "RES"), //ATA
	P_SUPLENTE(false, "SUP");
	
	private boolean titular;
	
	private String descricao;
	
	private EscalacaoPosicao (boolean titular, String descricao) {
		this.titular = titular;
		this.descricao = descricao;
	}
	
	public static List<EscalacaoPosicao> getEscalacaoTitulares() {
		return Arrays.asList(P_1, P_2, P_3, P_4, P_6, P_5, P_8, P_7, P_10, P_9, P_11);
	}

	public boolean isTitular() {
		return titular;
	}

	public String getDescricao() {
		return descricao;
	}

}
