package com.fastfoot.club.service.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;

public class ClubeTituloRankingUtil {
	
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_I = 40;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_II = 20;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_III = 10;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_I = 24;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_II = 6;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_I = 32;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_II = 8;

	/*
		select cr.ano, cr.classificacao_nacional, count(*)
		from clube_ranking cr 
		where cr.classificacao_nacional in (1, 17)
		group by cr.ano, cr.classificacao_nacional
		order by count(*);
		
		select cr.ano, cr.classificacao_copa_nacional, count(*), array_agg(c.liga) 
		from clube_ranking cr 
		inner join clube c on cr.id_clube = c.id 
		where cr.classificacao_copa_nacional in (5, 10)
		group by cr.ano, cr.classificacao_copa_nacional
		order by count(*), ano;
		
		select cr.ano, cr.classificacao_continental, count(*)
		from clube_ranking cr 
		where cr.classificacao_continental in (5, 10)
		group by cr.ano, cr.classificacao_continental
		order by count(*), ano;
	*/

	public static void atualizarRankingTitulos(List<ClubeRanking> rankings, List<ClubeTituloRanking> rankingsTitulos) {//TODO: transformar em consulta SQL
		atualizarRankingTitulos(rankings, rankingsTitulos.stream().collect(Collectors.toMap(ClubeTituloRanking::getClube, Function.identity())));
	}
	
	protected static void atualizarRankingTitulos(List<ClubeRanking> rankings, Map<Clube, ClubeTituloRanking> rankingsTitulos) {
	
		List<ClubeRanking> campeoesContinentais = rankings.stream().filter(r -> r.isCampeaoContinental()).collect(Collectors.toList());
		List<ClubeRanking> campeoesContinentaisII = rankings.stream().filter(r -> r.isCampeaoContinentalII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesContinentaisIII = rankings.stream().filter(r -> r.isCampeaoContinentalIII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesNacionais = rankings.stream().filter(r -> r.isCampeaoNacional()).collect(Collectors.toList());
		List<ClubeRanking> campeoesNacionaisII = rankings.stream().filter(r -> r.isCampeaoNacionalII()).collect(Collectors.toList());
		List<ClubeRanking> campeoesCopaNacionais = rankings.stream().filter(r -> r.isCampeaoCopaNacional()).collect(Collectors.toList());
		List<ClubeRanking> campeoesCopaNacionaisII = rankings.stream().filter(r -> r.isCampeaoCopaNacionalII()).collect(Collectors.toList());
	
		ClubeTituloRanking ctr = null;
	
		for (ClubeRanking cr : campeoesContinentais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinental(ctr.getTitulosContinental()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesContinentaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinentalII(ctr.getTitulosContinentalII()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesContinentaisIII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosContinentalIII(ctr.getTitulosContinentalIII()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesNacionais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosNacional(ctr.getTitulosNacional()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesNacionaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosNacionalII(ctr.getTitulosNacionalII()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesCopaNacionais) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosCopaNacional(ctr.getTitulosCopaNacional()+1);
			calcularPontuacao(ctr);
		}
		
		for (ClubeRanking cr : campeoesCopaNacionaisII) {
			ctr = rankingsTitulos.get(cr.getClube());
			ctr.setTitulosCopaNacionalII(ctr.getTitulosCopaNacionalII()+1);
			calcularPontuacao(ctr);
		}
	}
	
	private static void calcularPontuacao(ClubeTituloRanking ctr) {

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
