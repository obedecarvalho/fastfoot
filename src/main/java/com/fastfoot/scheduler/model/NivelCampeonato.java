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
	
	/*public boolean isNacionalNacionalII() {//Campeonato
		return isNacional() || isNacionalII();
	}*/
	
	public boolean isNacional() {//Campeonato
		return NACIONAL.equals(this);
	}
	
	public boolean isNacionalII() {//Campeonato
		return NACIONAL_II.equals(this);
	}

	public boolean isCNIOuCNII() {//CampeonatoEliminatorio
		return isCopaNacional() || isCopaNacionalII();
	}
	
	public boolean isCopaNacional() {//CampeonatoEliminatorio
		return COPA_NACIONAL.equals(this);
	}
	
	public boolean isCopaNacionalII() {//CampeonatoEliminatorio
		return COPA_NACIONAL_II.equals(this);
	}
	
	public boolean isContinental() {//CampeonatoMisto
		return CONTINENTAL.equals(this);
	}
	
	public boolean isContinentalII() {//CampeonatoMisto
		return CONTINENTAL_II.equals(this);
	}
	
	public boolean isContinentalIII() {//CampeonatoMisto
		return CONTINENTAL_III.equals(this);
	}
	
	/*public boolean isContinentalContinentalII() {//CampeonatoMisto
		return isContinental() || isContinentalII();
	}*/
	
	public boolean isCIOuCIIOuCIII() {//CampeonatoMisto
		return isContinental() || isContinentalII() || isContinentalIII();
	}

	public static NivelCampeonato getContinentalPorOrdem(int posicao) {
		NivelCampeonato[] continentaisOrdenado = new NivelCampeonato[]{CONTINENTAL, CONTINENTAL_II, CONTINENTAL_III, CONTINENTAL_OUTROS};
		return continentaisOrdenado[Math.min(posicao, continentaisOrdenado.length - 1)];
	}
}
