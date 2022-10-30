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
	
	static {
		PREMIACAO_CONTINENTAL = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_CONTINENTAL_II = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_CONTINENTAL_III = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_COPA_NACIONAL = new HashMap<PremiacaoClassificacao, Double>();
		PREMIACAO_COPA_NACIONAL_II = new HashMap<PremiacaoClassificacao, Double>();
		
		PREMIACAO_CONTINENTAL.put(CAMPEAO, 12000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FINAL, 6000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_SEMI_FINAL, 3000d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_QUARTAS_FINAL, 1500d);
		//PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FASE_GRUPOS, 1500d);
		//PREMIACAO_CONTINENTAL.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_CONTINENTAL_II.put(CAMPEAO, 8000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FINAL, 4000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_SEMI_FINAL, 2000d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_QUARTAS_FINAL, 1000d);
		//PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FASE_GRUPOS, 1000d);
		//PREMIACAO_CONTINENTAL_II.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_CONTINENTAL_III.put(CAMPEAO, 4000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FINAL, 2000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_SEMI_FINAL, 1000d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_QUARTAS_FINAL, 500d);
		//PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_OITAVAS_FINAL, 1d);
		PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FASE_GRUPOS, 500d);
		//PREMIACAO_CONTINENTAL_III.put(CLASSIFICACAO_FASE_PRELIMINAR, 1d);
		
		PREMIACAO_COPA_NACIONAL.put(CAMPEAO, 8000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FINAL, 4000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_SEMI_FINAL, 2000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_QUARTAS_FINAL, 2000d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_OITAVAS_FINAL, 500d);
		//PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FASE_GRUPOS, 1d);
		PREMIACAO_COPA_NACIONAL.put(CLASSIFICACAO_FASE_PRELIMINAR, 500d);
		
		PREMIACAO_COPA_NACIONAL_II.put(CAMPEAO, 2000d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_FINAL, 1000d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_SEMI_FINAL, 500d);
		PREMIACAO_COPA_NACIONAL_II.put(CLASSIFICACAO_QUARTAS_FINAL, 500d);
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
	

}
