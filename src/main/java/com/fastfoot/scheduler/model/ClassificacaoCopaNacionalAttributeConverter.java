package com.fastfoot.scheduler.model;

import javax.persistence.AttributeConverter;

public class ClassificacaoCopaNacionalAttributeConverter implements AttributeConverter<ClassificacaoCopaNacional, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ClassificacaoCopaNacional attribute) {
		
		if (attribute == null)
			return null;
		
		switch (attribute) {
		case NAO_PARTICIPOU:
			return -1;
		case CN_OITAVAS_FINAL:
			return 116;
		case CN_QUARTAS_FINAL:
			return 108;
		case CN_SEMI_FINAL:
			return 104;
		case CN_VICE_CAMPEAO:
			return 102;
		case CN_CAMPEAO:
			return 101;
		case CNII_OITAVAS_FINAL:
			return 216;
		case CNII_QUARTAS_FINAL:
			return 208;
		case CNII_SEMI_FINAL:
			return 204;
		case CNII_VICE_CAMPEAO:
			return 202;
		case CNII_CAMPEAO:
			return 201;

		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public ClassificacaoCopaNacional convertToEntityAttribute(Integer dbData) {

		if (dbData == null)
			return null;

		switch (dbData) {
		case -1:
			return ClassificacaoCopaNacional.NAO_PARTICIPOU;
		case 116:
			return ClassificacaoCopaNacional.CN_OITAVAS_FINAL;
		case 108:
			return ClassificacaoCopaNacional.CN_QUARTAS_FINAL;
		case 104:
			return ClassificacaoCopaNacional.CN_SEMI_FINAL;
		case 102:
			return ClassificacaoCopaNacional.CN_VICE_CAMPEAO;
		case 101:
			return ClassificacaoCopaNacional.CN_CAMPEAO;
		case 216:
			return ClassificacaoCopaNacional.CNII_OITAVAS_FINAL;
		case 208:
			return ClassificacaoCopaNacional.CNII_QUARTAS_FINAL;
		case 204:
			return ClassificacaoCopaNacional.CNII_SEMI_FINAL;
		case 202:
			return ClassificacaoCopaNacional.CNII_VICE_CAMPEAO;
		case 201:
			return ClassificacaoCopaNacional.CNII_CAMPEAO;

		default:
			throw new IllegalArgumentException(dbData + " not supported.");
		}
	}

}
