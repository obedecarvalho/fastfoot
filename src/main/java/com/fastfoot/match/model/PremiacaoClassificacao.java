package com.fastfoot.match.model;

import java.util.HashMap;
import java.util.Map;

import com.fastfoot.scheduler.model.NivelCampeonato;

public enum PremiacaoClassificacao {
	
	CAMPEAO,
	CLASSIFICACAO_FINAL,
	CLASSIFICACAO_SEMI_FINAL,
	CLASSIFICACAO_QUARTAS_FINAL,
	CLASSIFICACAO_OITAVAS_FINAL,
	CLASSIFICACAO_FASE_GRUPOS,
	CLASSIFICACAO_FASE_PRELIMINAR;

	private static final Map<PremiacaoClassificacao, Double> PREMIACAO_CONTINENTAL;

	private static final Map<PremiacaoClassificacao, Double> PREMIACAO_CONTINENTAL_II;

	private static final Map<PremiacaoClassificacao, Double> PREMIACAO_CONTINENTAL_III;

	private static final Map<PremiacaoClassificacao, Double> PREMIACAO_COPA_NACIONAL;

	private static final Map<PremiacaoClassificacao, Double> PREMIACAO_COPA_NACIONAL_II;
	
	private static final Double PREMIACAO_CAMPEAO_NACIONAL = 24000000d;

	private static final Double PERC_DIFF_CAMPEAO_VICE_NACIONAL = 0.1d;

	private static final Double PERC_DIFF_POSICAO_NACIONAL = 0.03d;

	private static final Double DIVISOR_NACIONAL_II = 3d;

	static {
		PREMIACAO_CONTINENTAL = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_CONTINENTAL_II = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_CONTINENTAL_III = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_COPA_NACIONAL = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_COPA_NACIONAL_II = new HashMap<PremiacaoClassificacao, Double>();
		
		PREMIACAO_CONTINENTAL.put(CAMPEAO, 12000000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FINAL, 6000000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_SEMI_FINAL, 3000000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_QUARTAS_FINAL, 1500000d);
		//PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FASE_GRUPOS, 1500000d);
		//PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_CONTINENTAL_II.put(CAMPEAO, 8000000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FINAL, 4000000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_SEMI_FINAL, 2000000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_QUARTAS_FINAL, 1000000d);
		//PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FASE_GRUPOS, 1000000d);
		//PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_CONTINENTAL_III.put(CAMPEAO, 4000000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FINAL, 2000000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_SEMI_FINAL, 1000000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_QUARTAS_FINAL, 500000d);
		//PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FASE_GRUPOS, 500000d);
		//PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_COPA_NACIONAL.put(CAMPEAO, 8000000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FINAL, 4000000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_SEMI_FINAL, 2000000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_QUARTAS_FINAL, 2000000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_OITAVAS_FINAL, 500000d);
		//PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FASE_GRUPOS, 1d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FASE_PRELIMINAR, 500000d);
		
		PREMIACAO_COPA_NACIONAL_II.put(CAMPEAO, 2000000d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_FINAL, 1000000d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_SEMI_FINAL, 500000d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_QUARTAS_FINAL, 500000d);
		//PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		//PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_FASE_GRUPOS, 1d);
		//PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
	}
	
	public static double getPremiacao(NivelCampeonato nivel, PremiacaoClassificacao premiacaoClassificacao) {
		return getPremiacaoMap(nivel).get(premiacaoClassificacao);
	}
	
	private static Map<PremiacaoClassificacao, Double> getPremiacaoMap(NivelCampeonato nivel) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			return PREMIACAO_CONTINENTAL;
		}
		if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			return PREMIACAO_CONTINENTAL_II;
		}
		if (NivelCampeonato.CONTINENTAL_III.equals(nivel)) {
			return PREMIACAO_CONTINENTAL_III;
		}
		if (NivelCampeonato.COPA_NACIONAL.equals(nivel)) {
			return PREMIACAO_COPA_NACIONAL;
		}
		if (NivelCampeonato.COPA_NACIONAL_II.equals(nivel)) {
			return PREMIACAO_COPA_NACIONAL_II;
		}
		return null;
	}
	
	public static double getPremiacaoClassificacaoNacional(NivelCampeonato nivelCampeonato, int posicao) {

		double premiacao = 0d;

		if (posicao == 1) {
			premiacao = PREMIACAO_CAMPEAO_NACIONAL;
		} else {
			premiacao = PREMIACAO_CAMPEAO_NACIONAL - (PREMIACAO_CAMPEAO_NACIONAL * PERC_DIFF_CAMPEAO_VICE_NACIONAL)
					- (PREMIACAO_CAMPEAO_NACIONAL * PERC_DIFF_POSICAO_NACIONAL * (posicao - 2));
		}

		if (nivelCampeonato.isNacionalII()) {
			premiacao = premiacao / DIVISOR_NACIONAL_II;
		}

		return premiacao;
	}
}
