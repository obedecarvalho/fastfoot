package com.fastfoot.service.util;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RoletaUtil {

	private static final Random R = new Random();

	public static ElementoRoleta executar(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = e1.getValor() + e2.getValor();
		int x = R.nextInt(valor);
		return (x <= e1.getValor()) ? e1 : e2;
	}

	public static ElementoRoleta executarN2(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = (int)(Math.pow(e1.getValor(), 2) + Math.pow(e2.getValor(), 2));
		int x = R.nextInt(valor);
		return (x <= Math.pow(e1.getValor(), 2)) ? e1 : e2;
	}

	public static boolean isPrimeiroVencedor(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = e1.getValor() + e2.getValor();
		Integer x = R.nextInt(valor);
		return (x <= e1.getValor()) ? true : false;
	}
	
	public static ElementoRoleta executar(List<? extends ElementoRoleta> elementos) {
		int valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValor();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos.get(0).getValor();
		
		while (v > x) {
			i++;
			x +=  elementos.get(i).getValor();
		}

		return elementos.get(i);
	}
	
	public static ElementoRoleta executar(ElementoRoleta[] elementos) {
		int valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValor();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos[0].getValor();
		
		while (v > x) {
			i++;
			x +=  elementos[i].getValor();
		}

		return elementos[i];
	}
}
