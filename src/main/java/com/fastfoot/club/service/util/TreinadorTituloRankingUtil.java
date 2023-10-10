package com.fastfoot.club.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.TreinadorTituloRanking;

public class TreinadorTituloRankingUtil {
	
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_I = 100;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_II = 84;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_III = 72;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_I = 84;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_II = 60;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_I = 96;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_II = 48;

	public static void atualizarRankingTitulos(List<ClubeRanking> rankings, List<TreinadorTituloRanking> rankingsTitulos) {
		atualizarRankingTitulos(rankings, rankingsTitulos.stream().collect(Collectors.toMap(t -> t.getTreinador().getClube(), Function.identity())));
	}
	
	protected static void atualizarRankingTitulos(List<ClubeRanking> rankings, Map<Clube, TreinadorTituloRanking> rankingsTitulos) {
	
		List<ClubeRanking> campeoesContinentais = rankings.stream().filter(r -> r.isCampeaoContinental()).collect(Collectors.toList());
		List<ClubeRanking> campeoesContinentaisII = rankings.stream().filter(r -> r.isCampeaoContinentalII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesContinentaisIII = rankings.stream().filter(r -> r.isCampeaoContinentalIII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesNacionais = rankings.stream().filter(r -> r.isCampeaoNacional()).collect(Collectors.toList());
		List<ClubeRanking> campeoesNacionaisII = rankings.stream().filter(r -> r.isCampeaoNacionalII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesCopaNacionais = rankings.stream().filter(r -> r.isCampeaoCopaNacional()).collect(Collectors.toList());
		List<ClubeRanking> campeoesCopaNacionaisII = rankings.stream().filter(r -> r.isCampeaoCopaNacionalII()).collect(Collectors.toList());
	
		TreinadorTituloRanking ctr = null;
		
		List<TreinadorTituloRanking> tituloRankingsRecalcular = new ArrayList<TreinadorTituloRanking>();
	
		for (ClubeRanking cr : campeoesContinentais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinental(ctr.getTitulosContinental()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesContinentaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinentalII(ctr.getTitulosContinentalII()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesContinentaisIII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinentalIII(ctr.getTitulosContinentalIII()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesNacionais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosNacional(ctr.getTitulosNacional()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesNacionaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosNacionalII(ctr.getTitulosNacionalII()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesCopaNacionais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosCopaNacional(ctr.getTitulosCopaNacional()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		for (ClubeRanking cr : campeoesCopaNacionaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosCopaNacionalII(ctr.getTitulosCopaNacionalII()+1);
			//calcularPontuacao(ctr);
			tituloRankingsRecalcular.add(ctr);
		}
		
		tituloRankingsRecalcular.stream().forEach(tr -> calcularPontuacao(tr));
	}
	
	public static void calcularPontuacao(TreinadorTituloRanking ctr) {

		Integer pontuacao = 0;
		
		pontuacao += PONTUACAO_TITULOS_CONTINENTAL_I * ctr.getTitulosContinental();
		pontuacao += PONTUACAO_TITULOS_CONTINENTAL_II * ctr.getTitulosContinentalII();
		pontuacao += PONTUACAO_TITULOS_CONTINENTAL_III * ctr.getTitulosContinentalIII();
		
		pontuacao += PONTUACAO_TITULOS_COPA_NACIONAL_I * ctr.getTitulosCopaNacional();
		pontuacao += PONTUACAO_TITULOS_COPA_NACIONAL_II * ctr.getTitulosCopaNacionalII();
		
		pontuacao += PONTUACAO_TITULOS_NACIONAL_I * ctr.getTitulosNacional();
		pontuacao += PONTUACAO_TITULOS_NACIONAL_II * ctr.getTitulosNacionalII();
		
		
		ctr.setPontuacao(pontuacao);
	}

}
