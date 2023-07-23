package com.fastfoot.financial.model;

import javax.persistence.AttributeConverter;

public class TipoMovimentacaoFinanceiraAttributeConverter implements AttributeConverter<TipoMovimentacaoFinanceira, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TipoMovimentacaoFinanceira attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.getId();

	}

	@Override
	public TipoMovimentacaoFinanceira convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}

		for (TipoMovimentacaoFinanceira tmf : TipoMovimentacaoFinanceira.values()) {
			if (tmf.getId().equals(dbData)) {
				return tmf;
			}
		}

		throw new IllegalArgumentException(dbData + " not supported.");
	}

	private static final TipoMovimentacaoFinanceiraAttributeConverter INSTANCE = new TipoMovimentacaoFinanceiraAttributeConverter();  
	
	public static TipoMovimentacaoFinanceiraAttributeConverter getInstance() {
		return INSTANCE;
	}
}
