package com.fastfoot.model;

import java.util.Arrays;
import java.util.List;

public enum Liga {
	
	NULL, GENEBE, SPAPOR, ITAFRA, ENGLND;

	public static List<Liga> getAll() {
		return Arrays.asList(Liga.GENEBE, Liga.SPAPOR, Liga.ITAFRA, Liga.ENGLND);
	}
}
