package com.fastfoot.model;

import java.util.Arrays;
import java.util.List;

public enum Liga {
	
	NULL(0),
	GENEBE(100),
	SPAPOR(200),
	ITAFRA(300),
	ENGLND(400),
	/*BRASIL*/
	;
	
	private Integer idBaseLiga;
	
	private Liga(Integer idBaseLiga) {
		this.idBaseLiga = idBaseLiga;
	}

	public Integer getIdBaseLiga() {
		return idBaseLiga;
	}

	public static List<Liga> getAll() {
		return Arrays.asList(Liga.GENEBE, Liga.SPAPOR, Liga.ITAFRA, Liga.ENGLND);
	}
}
