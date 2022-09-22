package com.fastfoot.service.util;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RoletaUtil {//TODO: renomear 'executar' para 'sortear'??

	private static final Random R = new Random();

	//private static final Integer POWER = Constantes.ROLETA_N_POWER;

	public static ElementoRoleta executar(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = e1.getValor() + e2.getValor();
		int x = R.nextInt(valor);
		return (x <= e1.getValor()) ? e1 : e2;
	}

	/*public static ElementoRoleta executarN(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = (int)(Math.pow(e1.getValor(), POWER) + Math.pow(e2.getValor(), POWER));
		int x = R.nextInt(valor);
		return (x <= Math.pow(e1.getValor(), POWER)) ? e1 : e2;
	}*/

	public static ElementoRoleta executarN(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = e1.getValorN() + e2.getValorN();
		int x = R.nextInt(valor);
		return (x <= e1.getValorN()) ? e1 : e2;
	}

	public static boolean isPrimeiroVencedor(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = e1.getValor() + e2.getValor();
		Integer x = R.nextInt(valor);
		return (x <= e1.getValor()) ? true : false;
	}
	
	/*public static boolean isPrimeiroVencedorN(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = (int) (Math.pow(e1.getValor(), POWER) + Math.pow(e2.getValor(), POWER));
		Integer x = R.nextInt(valor);
		return (x <= Math.pow(e1.getValor(), POWER)) ? true : false;
	}*/
	
	public static boolean isPrimeiroVencedorN(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = e1.getValorN() + e2.getValorN();
		Integer x = R.nextInt(valor);
		return (x <= e1.getValorN()) ? true : false;
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

	/*public static ElementoRoleta executarN(List<? extends ElementoRoleta> elementos) {
		int valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += Math.pow(elementoRoleta.getValor(), POWER);
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = (int) Math.pow(elementos.get(0).getValor(), POWER);
		
		while (v > x) {
			i++;
			x +=  Math.pow(elementos.get(i).getValor(), POWER);
		}

		return elementos.get(i);
	}*/

	public static ElementoRoleta executarN(List<? extends ElementoRoleta> elementos) {
		int valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValorN();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos.get(0).getValorN();
		
		while (v > x) {
			i++;
			x +=  elementos.get(i).getValorN();
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

	public static ElementoRoleta executarN(ElementoRoleta[] elementos) {
		int valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValorN();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos[0].getValorN();
		
		while (v > x) {
			i++;
			x +=  elementos[i].getValorN();
		}

		return elementos[i];
	}
	
	public static <T> T sortearPesoUm(List<T> elements) {
		
		if (elements == null || elements.isEmpty()) return null;
		
		return elements.get(R.nextInt(elements.size()));
	}
	
	public static boolean sortearProbabilidade(double probabilidade) {
		return R.nextDouble() <= probabilidade;
	}
}
