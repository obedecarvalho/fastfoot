package com.fastfoot.service.util;

import java.util.ArrayList;
import java.util.List;

public class ArraysUtil {
	
	public static <T> List<T> deslocarListNPosicoes(List<T> list, int n) {

		if (list.size() <= n) return list;

		List<T> novaLista = new ArrayList<T>();

		int i = n;

		for (int j = 0; j < list.size(); j++) {
			novaLista.add(list.get(i));
			i = (i + 1) % list.size();
		}

		return novaLista;
	}

}
