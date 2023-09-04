package com.fastfoot.service.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DatabaseUtil {

	/**
	 * Trata variações de Decimal em retorno de banco de dados.
	 * 
	 * Usado para H2 Database.
	 * 
	 * @param object
	 * @return
	 */
	public static Double getValueDecimal(Object object) {
		
		if (object instanceof Double) {
			return (Double) object;
		} else if (object instanceof BigDecimal) {
			return ((BigDecimal) object).doubleValue();
		}
		
		throw new RuntimeException("Tipo de dado não mapeado: " + object.getClass());
		
	}
	
	/**
	 * Trata variações de Long em retorno de banco de dados.
	 * 
	 * Usado para MySQL
	 * 
	 * 
	 * @param object
	 * @return
	 */
	public static Long getValueLong(Object object) {
		
		if (object instanceof Long) {
			return (Long) object;
		} else if (object instanceof BigInteger) {
			return ((BigInteger) object).longValue();
		} else if (object instanceof BigDecimal) {
			return ((BigDecimal) object).longValue();
		}
		
		throw new RuntimeException("Tipo de dado não mapeado: " + object.getClass());
		
	}

	/**
	 * Trata variações de Inteiros em retorno de banco de dados.
	 * 
	 * Usado para MySQL
	 * 
	 * 
	 * @param object
	 * @return
	 */
	public static Integer getValueInteger(Object object) {
		
		if (object instanceof Integer) {
			return (Integer) object;
		} else if (object instanceof BigInteger) {
			return ((BigInteger) object).intValue();
		} else if (object instanceof BigDecimal) {
			return ((BigDecimal) object).intValue();
		}
		
		throw new RuntimeException("Tipo de dado não mapeado: " + object.getClass());
		
	}
}
