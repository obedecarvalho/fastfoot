package com.fastfoot.scheduler.model;

public enum ClassificacaoNacional {

	NULL,
	N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9, N_10, N_11, N_12, N_13,
	N_14, N_15, N_16,//Rebaixados
	NII_1, NII_2, NII_3,//Promovidos
	NII_4, NII_5, NII_6, NII_7, NII_8, NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16;
	
	public static final ClassificacaoNacional[] NACIONAL = { N_1, N_2, N_3, N_4, N_5, N_6, N_7, N_8, N_9, N_10, N_11,
			N_12, N_13, N_14, N_15, N_16 };

	public static final ClassificacaoNacional[] NACIONAL_II = { NII_1, NII_2, NII_3, NII_4, NII_5, NII_6, NII_7, NII_8,
			NII_9, NII_10, NII_11, NII_12, NII_13, NII_14, NII_15, NII_16 };

	public static ClassificacaoNacional getClassificacao(NivelCampeonato nivel, Integer posicao) {

		if (NivelCampeonato.NACIONAL.equals(nivel)) {
			if (posicao >= 1 && posicao <= 16) return NACIONAL[posicao-1];
		}

		if (NivelCampeonato.NACIONAL_II.equals(nivel)) {
			if (posicao >= 1 && posicao <= 16) return NACIONAL_II[posicao-1];
		}

		return NULL;
	}

}
