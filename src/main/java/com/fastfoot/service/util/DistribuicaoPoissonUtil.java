package com.fastfoot.service.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

public class DistribuicaoPoissonUtil {
	
	public static final Double DISTRIBUICAO_MINIMA = 0.999;
	
	public static List<DistribuicaoPoissonElemento> calcularTotalProbabilidades(Double lambda) {
		
		List<DistribuicaoPoissonElemento> probabilidades = new ArrayList<DistribuicaoPoissonElemento>();
		
		DistribuicaoPoissonElemento probabilidade = null;
		
		int x = 0;
		
		double totalDistribuicao = 0.0;
		
		while (totalDistribuicao < DISTRIBUICAO_MINIMA) {
			probabilidade = new DistribuicaoPoissonElemento();
			
			probabilidade.setValorDescritivo(x);
			probabilidade.setValorProbabilidade(calcularProbabilidade(lambda, x));
			
			probabilidades.add(probabilidade);
			
			totalDistribuicao += probabilidade.getValorProbabilidade();
			x++;
		}
		
		return probabilidades;
	}
	
	/**
	 * 
	 * @param lambda média
	 * @param x
	 */
	public static Double calcularProbabilidade(Double lambda, Integer x) {
		return (Math.pow(lambda, x) * Math.exp(-lambda))/CombinatoricsUtils.factorial(x);
	}

}
