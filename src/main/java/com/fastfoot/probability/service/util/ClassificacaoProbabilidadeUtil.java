package com.fastfoot.probability.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fastfoot.probability.model.ClassificacaoProbabilidade;

public class ClassificacaoProbabilidadeUtil {

	public static void ordernarClassificacao(List<ClassificacaoProbabilidade> classificacao, boolean desempatar) throws RuntimeException {

		Collections.sort(classificacao, new Comparator<ClassificacaoProbabilidade>() {

			@Override
			public int compare(ClassificacaoProbabilidade o1, ClassificacaoProbabilidade o2) {
				return o1.compareTo(o2);
			}
		});
		
		//Setar posicao inicial
		for (int i = 0; i < classificacao.size(); i++) {
			if (i > 0 && classificacao.get(i-1).compareTo(classificacao.get(i)) == 0) {
				
				List<ClassificacaoProbabilidade> empatados = new ArrayList<ClassificacaoProbabilidade>();
				
				empatados.add(classificacao.get(i));
				
				empatados.add(classificacao.get(i - 1));
				
				int j = i - 1;
				
				while (j > 0 && classificacao.get(j - 1).compareTo(classificacao.get(j)) == 0) {
					empatados.add(classificacao.get(j - 1));
					j--;
				}
				
				//Se entrou aqui, o clube[i] está empatado com clube[i-1]
				//if (desempatar) {
				sortearPosicao(empatados, j + 1);
				//} else {
					//Para manter varios clubes com a mesma classificacao em Caso de empate
					//classificacao.get(i).setPosicao(classificacao.get(i-1).getPosicao()); 
				//}
			} else {
				classificacao.get(i).setPosicao(i+1);
			}
		}

	}
	
	private static void sortearPosicao(List<ClassificacaoProbabilidade> classificacao, Integer posInicial) {
		Collections.shuffle(classificacao);
		for (ClassificacaoProbabilidade c : classificacao) {
			c.setPosicao(posInicial++);
		}
	}
}
