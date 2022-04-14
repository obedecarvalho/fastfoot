package com.fastfoot.scheduler.model;

public enum ClassificacaoNacionalFinal {

	NULL,
	N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13,
	N_14, N_15, N_16,//Rebaixados
	NII_1, NII_2, NII_3,//Promovidos
	NII_4, NII_5, NII_6, NII_7, NII_8, NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16;

	public static ClassificacaoNacionalFinal getClassificacao(NivelCampeonato nivel, Integer posicao) {

		if (NivelCampeonato.NACIONAL.equals(nivel)) {
			if (posicao >= 1 && posicao <= 16) return getAllNacional()[posicao-1];
		}

		if (NivelCampeonato.NACIONAL_II.equals(nivel)) {
			if (posicao >= 1 && posicao <= 16) return getAllNacionalII()[posicao-1];
		}

		return NULL;
	}
	
	public static ClassificacaoNacionalFinal[] getAllNacionalNovoCampeonato() {
		ClassificacaoNacionalFinal[] N = {N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13, NII_1, NII_2, NII_3};
		return N;
	}
	
	public static ClassificacaoNacionalFinal[] getAllNacional() {
		ClassificacaoNacionalFinal[] N = {N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13, N_14, N_15, N_16};
		return N;
	}

	public static ClassificacaoNacionalFinal[] getAllNacional1a3() {
		ClassificacaoNacionalFinal[] N = {N_1, N_2, N_3};
		return N;
	}

	public static ClassificacaoNacionalFinal[] getAllNacional5a16() {
		ClassificacaoNacionalFinal[] N = {N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13, N_14, N_15, N_16};
		return N;
	}

	public static ClassificacaoNacionalFinal[] getAllNacional14a16() {
		ClassificacaoNacionalFinal[] N = {N_14, N_15, N_16};
		return N;
	}

	public static ClassificacaoNacionalFinal[] getAllNacional5a13() {
		ClassificacaoNacionalFinal[] N = {N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13};
		return N;
	}

	public static ClassificacaoNacionalFinal[] getAllNacionalII() {
		ClassificacaoNacionalFinal[] NII = {NII_1, NII_2, NII_3, NII_4, NII_5, NII_6, NII_7, NII_8, NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16};
		return NII;
	}

	public static ClassificacaoNacionalFinal[] getAllNacionalIINovoCampeonato() {
		ClassificacaoNacionalFinal[] NII = {N_14, N_15, N_16, NII_4, NII_5, NII_6, NII_7, NII_8, NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16};
		return NII;
	}

	public static ClassificacaoNacionalFinal[] getAllNacionalII1a3() {
		ClassificacaoNacionalFinal[] NII = {NII_1, NII_2, NII_3};
		return NII;
	}


	public static ClassificacaoNacionalFinal[] getAllNacionalII4a16() {
		ClassificacaoNacionalFinal[] NII = {NII_4, NII_5, NII_6, NII_7, NII_8, NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16};
		return NII;
	}

	public static boolean isN1N2N3(ClassificacaoNacionalFinal p) {
		return N_1.equals(p) || N_2.equals(p) || N_3.equals(p); 
	}
}
