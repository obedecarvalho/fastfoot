package com.fastfoot.player.model;

import javax.persistence.AttributeConverter;

public class PosicaoAttributeConverter implements AttributeConverter<Posicao, Character> {

	@Override
	public Character convertToDatabaseColumn(Posicao attribute) {
		if (attribute == null)
			return null;

		switch (attribute) {
		case GOLEIRO:
			return 'G';
		case ZAGUEIRO:
			return 'Z';
		case LATERAL:
			return 'L';
		case VOLANTE:
			return 'V';
		case MEIA:
			return 'M';
		case ATACANTE:
			return 'A';
		

		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public Posicao convertToEntityAttribute(Character dbData) {
		if (dbData == null)
			return null;

		switch (dbData) {
		case 'G':
			return Posicao.GOLEIRO;
		case 'Z':
			return Posicao.ZAGUEIRO;
		case 'L':
			return Posicao.LATERAL;
		case 'V':
			return Posicao.VOLANTE;
		case 'M':
			return Posicao.MEIA;
		case 'A':
			return Posicao.ATACANTE;

		default:
			throw new IllegalArgumentException(dbData + " not supported.");
		}
	}

	private static final PosicaoAttributeConverter INSTANCE = new PosicaoAttributeConverter();  
	
	public static PosicaoAttributeConverter getInstance() {
		return INSTANCE;
	}
}
