package com.fastfoot.scheduler.model;

public enum NivelCampeonato {
	
	NULL(0), CONTINENTAL(1), CONTINENTAL_II(2), NACIONAL(3), NACIONAL_II(4), COPA_NACIONAL(5), COPA_NACIONAL_II(6);

	private final Integer id;
	
	private NivelCampeonato(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

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
}
