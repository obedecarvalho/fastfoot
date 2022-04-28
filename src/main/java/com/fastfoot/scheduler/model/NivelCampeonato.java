package com.fastfoot.scheduler.model;

public enum NivelCampeonato {
	
	NULL,
	CONTINENTAL,
	CONTINENTAL_II,
	NACIONAL,
	NACIONAL_II,
	COPA_NACIONAL,
	COPA_NACIONAL_II,
	CONTINENTAL_III,
	CONTINENTAL_OUTROS
	;
	
	public boolean isNacionalNacionalII() {//Campeonato
		return NACIONAL.equals(this) || NACIONAL_II.equals(this);
	}

	public boolean isCopaNacionalCopaNacionalII() {//CampeonatoEliminatorio
		return isCopaNacional() || isCopaNacionalII();
	}
	
	public boolean isCopaNacional() {//CampeonatoEliminatorio
		return COPA_NACIONAL.equals(this);
	}
	
	public boolean isCopaNacionalII() {//CampeonatoEliminatorio
		return COPA_NACIONAL_II.equals(this);
	}
	
	public boolean isContinentalContinentalII() {//CampeonatoMisto
		return CONTINENTAL.equals(this) || CONTINENTAL_II.equals(this);
	}

	public static NivelCampeonato getContinentalPorOrdem(int posicao) {
		NivelCampeonato[] continentaisOrdenado = new NivelCampeonato[]{CONTINENTAL, CONTINENTAL_II, CONTINENTAL_III, CONTINENTAL_OUTROS};
		return continentaisOrdenado[Math.min(posicao, continentaisOrdenado.length - 1)];
	}
}
