package com.fastfoot.scheduler.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

public class ClassificacaoUtil {

	/*public static void ordernarClassificacao(List<Classificacao> classificacao) {
		ordernarClassificacao(classificacao, null, true);
	}*/

	public static void ordernarClassificacao(List<Classificacao> classificacao, boolean desempatar) {
		ordernarClassificacao(classificacao, null, desempatar);
	}
	
	public static void ordernarClassificacao(List<Classificacao> classificacao, List<PartidaResultado> partidas) throws RuntimeException {
		ordernarClassificacao(classificacao, partidas, partidas == null);
	}

	public static void ordernarClassificacao(List<Classificacao> classificacao, List<PartidaResultado> partidas,
			boolean desempatar) throws RuntimeException {

		Collections.sort(classificacao, new Comparator<Classificacao>() {

			@Override
			public int compare(Classificacao o1, Classificacao o2) {
				return o1.compareTo(o2);
			}
		});
		
		//Setar posicao inicial
		for (int i = 0; i < classificacao.size(); i++) {
			if (i > 0 && classificacao.get(i-1).compareTo(classificacao.get(i)) == 0) {
				//Se entrou aqui, o clube[i] está empatado com clube[i-1]
				if (desempatar) {
					//
					List<Classificacao> empatados = new ArrayList<Classificacao>();

					empatados.add(classificacao.get(i));

					empatados.add(classificacao.get(i - 1));

					int j = i - 1;

					while (j > 0 && classificacao.get(j - 1).compareTo(classificacao.get(j)) == 0) {
						empatados.add(classificacao.get(j - 1));
						j--;
					}

					sortearPosicao(empatados, j + 1);
					//
					//sortearPosicao(Arrays.asList(classificacao.get(i-1), classificacao.get(i)), i);
				} else {
					//Para manter varios clubes com a mesma classificacao em Caso de empate
					classificacao.get(i).setPosicao(classificacao.get(i-1).getPosicao()); 
				}
			} else {
				classificacao.get(i).setPosicao(i+1);
			}
		}
		
		if (partidas != null && !desempatar) {
			Map<Integer, List<Classificacao>> posClas = classificacao.stream().collect(Collectors.groupingBy(Classificacao::getPosicao));
			
			List<Classificacao> aux = null;
			long vitoriasC1, vitoriasC2;
	
			for (Integer pos : posClas.keySet()) {
				aux = posClas.get(pos);
	
				if (aux.size() == 2) {
					Clube c1 = aux.get(0).getClube();
					Clube c2 = aux.get(1).getClube();
					vitoriasC1 = partidas.stream().filter(p -> c1.equals(p.getClubeVencedor()) && c2.equals(p.getClubePerdedor())).count();
					vitoriasC2 = partidas.stream().filter(p -> c2.equals(p.getClubeVencedor()) && c1.equals(p.getClubePerdedor())).count();
					
					compararVitorias2Clubes(aux, pos, vitoriasC1, vitoriasC2);
					
				} else if (aux.size() > 2) {
					sortearPosicao(aux, pos);
				}
			}
		}
	}
	
	private static void compararVitorias2Clubes(List<Classificacao> classificacao, Integer posInicial, long vitoriaC1, long vitoriaC2) {
		if (vitoriaC1 > vitoriaC2) {
			classificacao.get(0).setPosicao(posInicial);
			classificacao.get(1).setPosicao(posInicial+1);
		} else if (vitoriaC2 > vitoriaC1) {
			classificacao.get(1).setPosicao(posInicial);
			classificacao.get(0).setPosicao(posInicial+1);
		} else {//Empate
			sortearPosicao(classificacao, posInicial);
		}
	}
	
	private static void sortearPosicao(List<Classificacao> classificacao, Integer posInicial) {
		Collections.shuffle(classificacao);
		for (Classificacao c : classificacao) {
			c.setPosicao(posInicial++);
		}
	}

	public static void atualizarClassificacao(List<Classificacao> classificacao, List<PartidaResultado> partidas) {
		
		Map<Clube, Classificacao> classificacaoMap = classificacao.stream().collect(Collectors.toMap(Classificacao::getClube, Function.identity()));
		
		for (PartidaResultado p : partidas) {
			
			int golMandante = p.getGolsMandante(), golVisitante = p.getGolsVisitante();
			Classificacao clasMandante = classificacaoMap.get(p.getClubeMandante());
			Classificacao clasVisitante = classificacaoMap.get(p.getClubeVisitante());
			
			clasMandante.setNumJogos(clasMandante.getNumJogos() + 1);
			clasVisitante.setNumJogos(clasVisitante.getNumJogos() + 1);
			
			if (clasMandante != null && clasVisitante != null) {
				if (golMandante == golVisitante) {
					clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_EMPATE);
					//clasMandante.setVitorias(null);
					clasMandante.setGolsPro(clasMandante.getGolsPro() + golMandante);				
					//clasMandante.setSaldoGols(null);
	
					clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_EMPATE);
					//clasVisitante.setVitorias(null);
					clasVisitante.setGolsPro(clasVisitante.getGolsPro() + golVisitante);
					//clasVisitante.setSaldoGols(null);
				} else if (golMandante > golVisitante) {
					clasMandante.setPontos(clasMandante.getPontos() + Constantes.PTOS_VITORIA);
					clasMandante.setVitorias(clasMandante.getVitorias() + 1);
					clasMandante.setGolsPro(clasMandante.getGolsPro() + golMandante);				
					clasMandante.setSaldoGols(clasMandante.getSaldoGols() + (golMandante - golVisitante));
	
					//clasVisitante.setPontos(null);
					//clasVisitante.setVitorias(null);
					clasVisitante.setGolsPro(clasVisitante.getGolsPro() + golVisitante);
					clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + (golVisitante - golMandante));
				} else if (golMandante < golVisitante) {
					clasVisitante.setPontos(clasVisitante.getPontos() + Constantes.PTOS_VITORIA);
					clasVisitante.setVitorias(clasVisitante.getVitorias() + 1);
					clasVisitante.setGolsPro(clasVisitante.getGolsPro() + golVisitante);				
					clasVisitante.setSaldoGols(clasVisitante.getSaldoGols() + (golVisitante - golMandante));
	
					//clasMandante.setPontos(null);
					//clasMandante.setVitorias(null);
					clasMandante.setGolsPro(clasMandante.getGolsPro() + golMandante);
					clasMandante.setSaldoGols(clasMandante.getSaldoGols() + (golMandante - golVisitante));				
				}
			}
		}
	}
}
