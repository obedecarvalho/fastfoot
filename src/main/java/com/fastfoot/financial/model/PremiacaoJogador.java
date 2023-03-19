package com.fastfoot.financial.model;

import com.fastfoot.scheduler.model.NivelCampeonato;

public enum PremiacaoJogador {

	PREMIACAO_CAMPEAO_NACIONAL(1.0),
	PREMIACAO_CAMPEAO_NACIONAL_II(0.5),
	PREMIACAO_CAMPEAO_COPA_NACIONAL(0.75),
	PREMIACAO_CAMPEAO_COPA_NACIONAL_II(0.3),//0.75 * 0.40
	PREMIACAO_CAMPEAO_CONTINENTAL(1.0),
	PREMIACAO_CAMPEAO_CONTINENTAL_II(0.666),
	PREMIACAO_CAMPEAO_CONTINENTAL_III(0.333),
	;
	
	private Double porcentagemSalario;
	
	private PremiacaoJogador(Double porcentagemSalario) {
		this.porcentagemSalario = porcentagemSalario;
	}

	public Double getPorcentagemSalario() {
		return porcentagemSalario;
	}

	public static PremiacaoJogador getPremiacaoJogadorNivelCampeonato(NivelCampeonato nivelCampeonato) {
		if (NivelCampeonato.NACIONAL.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_NACIONAL;
		if (NivelCampeonato.NACIONAL_II.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_NACIONAL_II;
		if (NivelCampeonato.COPA_NACIONAL.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_COPA_NACIONAL;
		if (NivelCampeonato.COPA_NACIONAL_II.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_COPA_NACIONAL_II;
		if (NivelCampeonato.CONTINENTAL.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_CONTINENTAL;
		if (NivelCampeonato.CONTINENTAL_II.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_CONTINENTAL_II;
		if (NivelCampeonato.CONTINENTAL_III.equals(nivelCampeonato))
			return PREMIACAO_CAMPEAO_CONTINENTAL_III;
		return null;
	}
}
