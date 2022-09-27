package com.fastfoot.scheduler.model;

import javax.persistence.AttributeConverter;

public class ClassificacaoContinentalAttributeConverter implements AttributeConverter<ClassificacaoContinental, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ClassificacaoContinental attribute) {
		
		if (attribute == null)
			return null;

		switch (attribute) {
		case NAO_PARTICIPOU:
			return -1;
		case C_FASE_GRUPOS:
			return 116;
		case C_QUARTAS_FINAL:
			return 108;
		case C_SEMI_FINAL:
			return 104;
		case C_VICE_CAMPEAO:
			return 102;
		case C_CAMPEAO:
			return 101;
		case CII_FASE_GRUPOS:
			return 216;
		case CII_QUARTAS_FINAL:
			return 208;
		case CII_SEMI_FINAL:
			return 204;
		case CII_VICE_CAMPEAO:
			return 202;
		case CII_CAMPEAO:
			return 201;
		case CIII_FASE_GRUPOS:
			return 316;
		case CIII_QUARTAS_FINAL:
			return 308;
		case CIII_SEMI_FINAL:
			return 304;
		case CIII_VICE_CAMPEAO:
			return 302;
		case CIII_CAMPEAO:
			return 301;

		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}

	}

	@Override
	public ClassificacaoContinental convertToEntityAttribute(Integer dbData) {

		if (dbData == null)
			return null;

		switch (dbData) {
		case -1:
			return ClassificacaoContinental.NAO_PARTICIPOU;
		case 116:
			return ClassificacaoContinental.C_FASE_GRUPOS;
		case 108:
			return ClassificacaoContinental.C_QUARTAS_FINAL;
		case 104:
			return ClassificacaoContinental.C_SEMI_FINAL;
		case 102:
			return ClassificacaoContinental.C_VICE_CAMPEAO;
		case 101:
			return ClassificacaoContinental.C_CAMPEAO;
		case 216:
			return ClassificacaoContinental.CII_FASE_GRUPOS;
		case 208:
			return ClassificacaoContinental.CII_QUARTAS_FINAL;
		case 204:
			return ClassificacaoContinental.CII_SEMI_FINAL;
		case 202:
			return ClassificacaoContinental.CII_VICE_CAMPEAO;
		case 201:
			return ClassificacaoContinental.CII_CAMPEAO;
		case 316:
			return ClassificacaoContinental.CIII_FASE_GRUPOS;
		case 308:
			return ClassificacaoContinental.CIII_QUARTAS_FINAL;
		case 304:
			return ClassificacaoContinental.CIII_SEMI_FINAL;
		case 302:
			return ClassificacaoContinental.CIII_VICE_CAMPEAO;
		case 301:
			return ClassificacaoContinental.CIII_CAMPEAO;

		default:
			throw new IllegalArgumentException(dbData + " not supported.");
		}
	}

}
