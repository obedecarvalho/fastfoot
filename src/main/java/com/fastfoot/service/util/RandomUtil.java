package com.fastfoot.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;

public class RandomUtil {
	
	private static final SplittableRandom R = new SplittableRandom();

	private static final Random R_GAUSSIAN = new Random();//TODO: usar outra opção mais performatica
	
	public static int sortearIntervalo(int min, int max) {
		//return min + R.nextInt(max - min);
		return sortearMinAndRange(min, max - min);
	}
	
	public static int sortearMinAndRange(int min, int range) {
		return min + R.nextInt(range);
	}
	
	public static boolean sortearProbabilidade(double probabilidade) {
		return R.nextDouble() <= probabilidade;
	}

	public static double getNextGaussianByAvgAndStdDev(Double average, Double stdDev) {
		return average + R_GAUSSIAN.nextGaussian() * stdDev;
	}
	
	public static List<Integer> getRandomDistinctRangeValues(int maxValue, int qtdeTotal){
		List<Integer> generatedValues = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 

		while (qtde < qtdeTotal) {
			
			sorteado = R.nextInt(maxValue);
			
			if (!generatedValues.contains(sorteado)) {
				generatedValues.add(sorteado);
				qtde++;
			}

		}
		
		return generatedValues;
	}
	
	public static List<Integer> getRandomDistinctRangeValuesWithoutValues(Integer maxValue, Integer qtdeTotal, List<Integer> otherValues) {

		List<Integer> posicoes = new ArrayList<Integer>();
		
		int qtde = 0;
		
		Integer sorteado = null; 

		while (qtde < qtdeTotal) {
			
			sorteado = R.nextInt(maxValue);
			
			if (!posicoes.contains(sorteado) && !otherValues.contains(sorteado)) {
				posicoes.add(sorteado);
				qtde++;
			}

		}
		
		return posicoes;
	}
}
