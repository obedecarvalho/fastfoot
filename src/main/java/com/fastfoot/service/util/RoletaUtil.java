package com.fastfoot.service.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RoletaUtil {

	private static final Random R = new Random();

	public static ElementoRoleta sortear(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = e1.getValor() + e2.getValor();
		int x = R.nextInt(valor);
		return (x < e1.getValor()) ? e1 : e2;
	}

	public static ElementoRoleta sortearN(ElementoRoleta e1, ElementoRoleta e2) {
		int valor = e1.getValorN() + e2.getValorN();
		int x = R.nextInt(valor);
		return (x < e1.getValorN()) ? e1 : e2;
	}

	public static boolean isPrimeiroVencedor(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = e1.getValor() + e2.getValor();
		Integer x = R.nextInt(valor);
		return (x < e1.getValor()) ? true : false;
	}

	public static boolean isPrimeiroVencedorN(ElementoRoleta e1, ElementoRoleta e2) {
		Integer valor = e1.getValorN() + e2.getValorN();
		Integer x = R.nextInt(valor);
		return (x < e1.getValorN()) ? true : false;
	}
	
	public static ElementoRoleta sortear(List<? extends ElementoRoleta> elementos) {
		int valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValor();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos.get(0).getValor();
		
		while (v >= x) {
			i++;
			x +=  elementos.get(i).getValor();
		}

		return elementos.get(i);
	}

	public static ElementoRoleta sortearN(List<? extends ElementoRoleta> elementos) {
		int valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValorN();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos.get(0).getValorN();
		
		while (v >= x) {
			i++;
			x +=  elementos.get(i).getValorN();
		}

		return elementos.get(i);
	}
	
	public static ElementoRoleta sortear(ElementoRoleta[] elementos) {
		int valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValor();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos[0].getValor();
		
		while (v >= x) {
			i++;
			x +=  elementos[i].getValor();
		}

		return elementos[i];
	}

	public static ElementoRoleta sortearN(ElementoRoleta[] elementos) {
		int valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValorN();
		}
		
		int v = R.nextInt(valor);
		
		int i = 0, x = elementos[0].getValorN();
		
		while (v >= x) {
			i++;
			x +=  elementos[i].getValorN();
		}

		return elementos[i];
	}
	
	public static <T> T sortearPesoUm(List<T> elements) {
		
		if (elements == null || elements.isEmpty()) return null;
		
		return elements.get(R.nextInt(elements.size()));
	}
	
	public static <T> T sortearPesoUm(T[] elements) {
		
		if (elements == null || elements.length == 0) return null;
		
		return elements[R.nextInt(elements.length)];
	}
	
	public static boolean sortearProbabilidade(double probabilidade) {
		return R.nextDouble() <= probabilidade;
	}
	
	public static int sortearIntervalo(int min, int max) {
		return min + R.nextInt(max);
	}
	
	public static ElementoRoleta sortearAsDouble(List<? extends ElementoRoleta> elementos) {
		double valor = 0.0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValorAsDouble();
		}
		
		double v = R.nextDouble() * valor;
		
		int i = 0;
		double x = elementos.get(0).getValorAsDouble();
		
		while (v >= x) {
			i++;
			x +=  elementos.get(i).getValorAsDouble();
		}

		return elementos.get(i);
	}
	
	public static ElementoRoleta sortearNAsDouble(List<? extends ElementoRoleta> elementos) {
		double valor = 0;
		
		for (Iterator<? extends ElementoRoleta> iterator = elementos.iterator(); iterator.hasNext();) {
			ElementoRoleta elementoRoleta = (ElementoRoleta) iterator.next();
			valor += elementoRoleta.getValorNAsDouble();
		}
		
		double v = R.nextDouble() * valor;
		
		int i = 0;
		double x = elementos.get(0).getValorNAsDouble();
		
		while (v >= x) {
			i++;
			x +=  elementos.get(i).getValorNAsDouble();
		}

		return elementos.get(i);
	}
	
	public static ElementoRoleta sortearAsDouble(ElementoRoleta[] elementos) {
		double valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValorAsDouble();
		}
		
		double v = R.nextDouble() * valor;
		
		int i = 0;
		double x = elementos[0].getValorAsDouble();
		
		while (v >= x) {
			i++;
			x +=  elementos[i].getValorAsDouble();
		}

		return elementos[i];
	}

	public static ElementoRoleta sortearNAsDouble(ElementoRoleta[] elementos) {
		double valor = 0;
		
		for (ElementoRoleta er : elementos) {
			valor += er.getValorNAsDouble();
		}
		
		double v = R.nextDouble() * valor;
		
		int i = 0;
		double x = elementos[0].getValorNAsDouble();
		
		while (v >= x) {
			i++;
			x +=  elementos[i].getValorNAsDouble();
		}

		return elementos[i];
	}
	
	public static <T> T sortear(Map<T, Integer> elementos) {

		int valor = 0;
		
		for (T er : elementos.keySet()) {
			valor += elementos.get(er);
		}
		
		int v = R.nextInt(valor);
		
		int soma = 0;
		
		for (T er : elementos.keySet()) {

			soma += elementos.get(er);
			
			if (v < soma) {
				return er;
			}
			
		}

		return null;
	}
	
	public static <T> T sortearAsDouble(Map<T, Double> elementos) {

		double valor = 0;
		
		for (T er : elementos.keySet()) {
			valor += elementos.get(er);
		}
		
		double v = R.nextDouble() * valor;
		
		double soma = 0;
		
		for (T er : elementos.keySet()) {

			soma += elementos.get(er);
			
			if (v < soma) {
				return er;
			}
			
		}

		return null;
	}
}
