package com.fastfoot.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeansUtil {
	
	private static final Double DIFF_MAX = 0.0000000001;
	
	private static final Integer NUMERO_MAX_ITERACOES = 1000;
	
	protected static final Random R = new Random();
	
	public static ClusterKMeans[] classificar(List<? extends ElementoKMeans> elementos, ClusterKMeans[] clusters) {

		ClusterKMeans clusterMaisProximo;
		double distanciaAtual, distanciaMaisProxima = Double.MAX_VALUE;

		for (ElementoKMeans e : elementos) {
			
			//resetando
			clusterMaisProximo = null;
			distanciaMaisProxima = Double.MAX_VALUE;

			for (ClusterKMeans c : clusters) {
				distanciaAtual = calcularDistancia(c, e);
				
				if (clusterMaisProximo == null || distanciaAtual < distanciaMaisProxima) {
					clusterMaisProximo = c;
					distanciaMaisProxima = distanciaAtual;
				}
			}
			
			clusterMaisProximo.addElemento(e);
		}

		return clusters;
	}
	
	public static ClusterKMeans[] executarKMeans(List<? extends ElementoKMeans> elementos, int numeroClusters) {

		ClusterKMeans[] clusters = new ClusterKMeans[numeroClusters];
		
		List<Integer> posicoesIniciais = sortearPosicoes(elementos.size(), numeroClusters);

		//criando centroides iniciais
		for (int i = 0; i < numeroClusters; i++) {
			clusters[i] = new ClusterKMeans(i, elementos.get(posicoesIniciais.get(i)).getAtributos());
		}
		
		ClusterKMeans clusterMaisProximo;
		double distanciaAtual, distanciaMaisProxima = Double.MAX_VALUE;
		int rodada = 1;
		boolean alterado = true;
		
		//for (int rodada = 0; rodada < 100; rodada++) {
		while (rodada <= NUMERO_MAX_ITERACOES && alterado) {
			
			for (ClusterKMeans c : clusters) {
				c.getElementos().clear();
			}
			
			for (ElementoKMeans e : elementos) {
				
				//resetando
				clusterMaisProximo = null;
				distanciaMaisProxima = Double.MAX_VALUE;

				for (ClusterKMeans c : clusters) {
					distanciaAtual = calcularDistancia(c, e);
					
					if (clusterMaisProximo == null || distanciaAtual < distanciaMaisProxima) {
						clusterMaisProximo = c;
						distanciaMaisProxima = distanciaAtual;
					}
				}
				
				clusterMaisProximo.addElemento(e);
			}

			alterado = false;
			
			for (ClusterKMeans c : clusters) {
				alterado = alterado || calcularCentroide(c);
			}
			
			rodada++;
		}
		
		//System.err.println(rodada);

		return clusters;
	}
	
	protected static List<Integer> sortearPosicoes(Integer posicaoMax, Integer qtdeSortear) {

		List<Integer> posicoes = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 

		while (qtde < qtdeSortear) {
			
			sorteado = R.nextInt(posicaoMax);
			
			if (!posicoes.contains(sorteado)) {
				posicoes.add(sorteado);
				qtde++;
			}

		}
		
		return posicoes;
	}

	private static boolean calcularCentroide(ClusterKMeans cluster) {
		
		boolean alterado = false;
		
		int numeroAtributos = cluster.getCentroide().length;
		
		Double[] centroide = new Double[numeroAtributos];
		
		Arrays.fill(centroide, 0.0);//inicializado array com 0's
		
		for (int i = 0; i < numeroAtributos; i++) {
			int j = i;
			centroide[i] = cluster.getElementos().stream().mapToDouble(e -> e.getAtributos()[j]).average().getAsDouble();
		}
		
		alterado = !equals(cluster.getCentroide(), centroide);
		
		cluster.setCentroide(centroide);
		
		return alterado;
	}
	
	private static boolean equals(Double[] a, Double[] b) {
		boolean equals = true;
		
		for (int i = 0; i < a.length; i++) {
			//if (a[i] != b[i]) {
			if (Math.abs(a[i] - b[i]) > DIFF_MAX) {
				equals = false;
			}
		}
		
		return equals;
	}
	
	private static double calcularDistancia(ClusterKMeans cluster, ElementoKMeans elemento) {
		int numeroAtributos = cluster.getCentroide().length;
		
		double soma = 0;
		
		for (int i = 0; i < numeroAtributos; i++) {
			soma += Math.pow(cluster.getCentroide()[i] - elemento.getAtributos()[i], 2);
		}
		
		return Math.sqrt(soma);
	}
}
