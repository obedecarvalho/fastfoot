package com.fastfoot.scheduler.model;

import jakarta.persistence.AttributeConverter;

public class NivelCampeonatoAttributeConverter implements AttributeConverter<NivelCampeonato, String> {

	@Override
	public String convertToDatabaseColumn(NivelCampeonato attribute) {
		if (attribute == null)
			return null;

		switch (attribute) {
		case CONTINENTAL:
			return "CI";
		case CONTINENTAL_II:
			return "CII";
		case CONTINENTAL_III:
			return "CIII";
		case CONTINENTAL_OUTROS:
			return "CO";
		case NACIONAL:
			return "NI";
		case NACIONAL_II:
			return "NII";
		case COPA_NACIONAL:
			return "CNI";
		case COPA_NACIONAL_II:
			return "CNII";
		case AMISTOSO_INTERNACIONAL:
			return "AI";
		case AMISTOSO_NACIONAL:
			return "AN";
		case NULL:
			return "NULL";
		

		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public NivelCampeonato convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;

		switch (dbData) {
		case "NULL":
			return NivelCampeonato.NULL;
		case "CI":
			return NivelCampeonato.CONTINENTAL;
		case "CII":
			return NivelCampeonato.CONTINENTAL_II;
		case "NI":
			return NivelCampeonato.NACIONAL;
		case "NII":
			return NivelCampeonato.NACIONAL_II;
		case "CNI":
			return NivelCampeonato.COPA_NACIONAL;
		case "CNII":
			return NivelCampeonato.COPA_NACIONAL_II;
		case "CIII":
			return NivelCampeonato.CONTINENTAL_III;
		case "CO":
			return NivelCampeonato.CONTINENTAL_OUTROS;
		case "AI":
			return NivelCampeonato.AMISTOSO_INTERNACIONAL;
		case "AN":
			return NivelCampeonato.AMISTOSO_NACIONAL;

		default:
			throw new IllegalArgumentException(dbData + " not supported.");
		}
	}

}
