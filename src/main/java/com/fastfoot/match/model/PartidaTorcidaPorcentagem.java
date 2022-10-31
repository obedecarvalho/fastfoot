package com.fastfoot.match.model;

import java.util.HashMap;
import java.util.Map;

import com.fastfoot.scheduler.model.NivelCampeonato;

public enum PartidaTorcidaPorcentagem {
	
	PORC_PUBLICO_FINAL,
	PORC_PUBLICO_SEMI_FINAL,
	PORC_PUBLICO_QUARTAS_FINAL,
	PORC_PUBLICO_OITAVAS_FINAL,
	PORC_PUBLICO_FASE_GRUPOS,
	PORC_PUBLICO_FASE_PRELIMINAR;

	private static final Map<PartidaTorcidaPorcentagem, Double> PORC_PUBLICO_CONTINENTAL;
	
	private static final Map<PartidaTorcidaPorcentagem, Double> PORC_PUBLICO_COPA_NACIONAL;
	
	public static final Double TICKET_MEDIO_CONTINENTAL = 120.0d;

	public static final Double TICKET_MEDIO_COPA_NACIONAL = 80.0d;

	public static final Double TICKET_MEDIO_NACIONAL = 50.0d;

	static {
		PORC_PUBLICO_CONTINENTAL = new HashMap<PartidaTorcidaPorcentagem, Double>();
		
		PORC_PUBLICO_COPA_NACIONAL = new HashMap<PartidaTorcidaPorcentagem, Double>();
		
		PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_FASE_PRELIMINAR, 0.6);
		//PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_FASE_GRUPOS, 0.6);
		PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_OITAVAS_FINAL, 0.7);
		PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_QUARTAS_FINAL, 0.8);
		PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_SEMI_FINAL, 0.9);
		PORC_PUBLICO_COPA_NACIONAL.put(PORC_PUBLICO_FINAL, 1.0);
		
		//PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_FASE_PRELIMINAR, 0.6);
		PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_FASE_GRUPOS, 0.7);
		//PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_OITAVAS_FINAL, 0.6);
		PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_QUARTAS_FINAL, 0.9);
		PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_SEMI_FINAL, 1.0);
		PORC_PUBLICO_CONTINENTAL.put(PORC_PUBLICO_FINAL, 1.0);
	}
	
	public static Double get(NivelCampeonato nivelCampeonato, PartidaTorcidaPorcentagem partidaTorcidaPorcentagem) {
		
		if (nivelCampeonato.isCNIOuCNII()) {
			return PORC_PUBLICO_COPA_NACIONAL.get(partidaTorcidaPorcentagem);
		}
		
		if (nivelCampeonato.isCIOuCIIOuCIII()) {
			return PORC_PUBLICO_CONTINENTAL.get(partidaTorcidaPorcentagem);
		}
		
		return null;
	}

}
