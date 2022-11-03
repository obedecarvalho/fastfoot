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
	
	private static final Map<Integer, Double> PORC_PUBLICO_NACIONAL;
	
	private static final Double TICKET_MEDIO_CONTINENTAL = 120.0d;

	private static final Double TICKET_MEDIO_COPA_NACIONAL = 80.0d;

	private static final Double TICKET_MEDIO_NACIONAL = 60.0d;
	
	private static final Double TICKET_MEDIO_AMISTOSO = 30.0d;
	
	private static final Double PORC_PUBLICO_AMISTOSO = 0.75d;
	
	private static final Double MULTIPLICADOR_INGRESSOS_FINAL = 1.5d;
	
	private static final Double MULTIPLICADOR_INGRESSOS_SEMI_FINAL = 1.25d;
	
	private static final Double DIVISOR_NACIONAL_II = 3d;
	
	private static final Double DIVISOR_COPA_NACIONAL_II = 4d;
	
	private static final Double DIVISOR_CONTINENTAL_II = 1.5d;//2/3
	
	private static final Double DIVISOR_CONTINENTAL_III = 3.0d;//1/3

	static {
		PORC_PUBLICO_CONTINENTAL = new HashMap<PartidaTorcidaPorcentagem, Double>();
		
		PORC_PUBLICO_COPA_NACIONAL = new HashMap<PartidaTorcidaPorcentagem, Double>();
		
		PORC_PUBLICO_NACIONAL = new HashMap<Integer, Double>();
		
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
		
		PORC_PUBLICO_NACIONAL.put(1, 1.0);
		PORC_PUBLICO_NACIONAL.put(2, 1.0);
		PORC_PUBLICO_NACIONAL.put(3, 0.9);
		PORC_PUBLICO_NACIONAL.put(4, 0.9);
		PORC_PUBLICO_NACIONAL.put(5, 0.9);
		PORC_PUBLICO_NACIONAL.put(6, 0.8);
		PORC_PUBLICO_NACIONAL.put(7, 0.8);
		PORC_PUBLICO_NACIONAL.put(8, 0.8);
		PORC_PUBLICO_NACIONAL.put(9, 0.6);
		PORC_PUBLICO_NACIONAL.put(10, 0.6);
		PORC_PUBLICO_NACIONAL.put(11, 0.6);
		PORC_PUBLICO_NACIONAL.put(12, 0.5);
		PORC_PUBLICO_NACIONAL.put(13, 0.5);
		PORC_PUBLICO_NACIONAL.put(14, 0.5);
		PORC_PUBLICO_NACIONAL.put(15, 0.4);
		PORC_PUBLICO_NACIONAL.put(16, 0.4);
	}
	
	public static Double getPorcentagem(NivelCampeonato nivelCampeonato, PartidaTorcidaPorcentagem partidaTorcidaPorcentagem) {
		
		if (nivelCampeonato.isCNIOuCNII()) {
			return PORC_PUBLICO_COPA_NACIONAL.get(partidaTorcidaPorcentagem);
		}
		
		if (nivelCampeonato.isCIOuCIIOuCIII()) {
			return PORC_PUBLICO_CONTINENTAL.get(partidaTorcidaPorcentagem);
		}
		
		return null;
	}
	
	public static Double getPorcentagem(NivelCampeonato nivelCampeonato, Integer posicao) {
		
		if (nivelCampeonato.isNIOuNII()) {
			return PORC_PUBLICO_NACIONAL.get(posicao);
		}
		
		return null;
	}
	
	public static Double getPorcentagemAmistoso() {
		return PORC_PUBLICO_AMISTOSO;
	}
	
	public static Double getRendaIngressosAmistosos(Integer qtdeTorcida) {
		return TICKET_MEDIO_AMISTOSO * qtdeTorcida;
	}

	public static Double getRendaIngressos(NivelCampeonato nivelCampeonato, Integer qtdeTorcida, Integer numRodada, Integer numRodadasCN) {
		
		double renda = 0.0;
		
		if (nivelCampeonato.isCIOuCIIOuCIII()) {
			if (numRodada == 5) {
				renda = qtdeTorcida * TICKET_MEDIO_CONTINENTAL *  MULTIPLICADOR_INGRESSOS_SEMI_FINAL;
			} else if (numRodada == 6) {
				renda = qtdeTorcida * TICKET_MEDIO_CONTINENTAL *  MULTIPLICADOR_INGRESSOS_FINAL;
			} else {
				renda = qtdeTorcida * TICKET_MEDIO_CONTINENTAL;
			}

			if (nivelCampeonato.isContinentalII()) {
				renda = renda / DIVISOR_CONTINENTAL_II;
			} else if (nivelCampeonato.isContinentalIII()) {
				renda = renda / DIVISOR_CONTINENTAL_III;
			}
		} else if (nivelCampeonato.isNIOuNII()) {
			renda = qtdeTorcida * TICKET_MEDIO_NACIONAL;

			if (nivelCampeonato.isNacionalII()) {
				renda = renda / DIVISOR_NACIONAL_II;
			}
		} else if (nivelCampeonato.isCopaNacionalII()) {
			if (numRodada == 3) {
				renda = (qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL * MULTIPLICADOR_INGRESSOS_SEMI_FINAL)
						/ DIVISOR_COPA_NACIONAL_II;
			} else if (numRodada == 4) {
				renda = (qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL * MULTIPLICADOR_INGRESSOS_FINAL)
						/ DIVISOR_COPA_NACIONAL_II;
			} else {
				renda = (qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL) / DIVISOR_COPA_NACIONAL_II;
			}
		} else if (nivelCampeonato.isCopaNacional()) {

			if (numRodada == (numRodadasCN - 1)) {
				renda = qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL * MULTIPLICADOR_INGRESSOS_SEMI_FINAL;
			} else if (numRodada == numRodadasCN) {
				renda = qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL * MULTIPLICADOR_INGRESSOS_FINAL;
			} else {
				renda = qtdeTorcida * TICKET_MEDIO_COPA_NACIONAL;
			}

		}

		return renda;
	}
}
