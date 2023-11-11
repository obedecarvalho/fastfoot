package com.fastfoot.club.service.util;

import java.util.HashMap;
import java.util.Map;

import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.scheduler.model.NivelCampeonato;

public class ClubeTituloRankingUtil {//TODO: renomear para ClubePontuacaoUtil?
	
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_I = 100;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_II = 84;
	private static final Integer PONTUACAO_TITULOS_CONTINENTAL_III = 72;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_I = 84;
	private static final Integer PONTUACAO_TITULOS_COPA_NACIONAL_II = 60;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_I = 96;
	private static final Integer PONTUACAO_TITULOS_NACIONAL_II = 48;
	
	private static final Integer PONTUACAO_VITORIA_NACIONAL = 9;
	private static final Integer PONTUACAO_EMPATE_NACIONAL = 3;
	private static final Integer PONTUACAO_VITORIA_NACIONAL_II = 3;
	private static final Integer PONTUACAO_EMPATE_NACIONAL_II = 1;
	private static final Integer PONTUACAO_VITORIA_COPA_NACIONAL = 18;
	private static final Integer PONTUACAO_EMPATE_COPA_NACIONAL = 6;
	private static final Integer PONTUACAO_VITORIA_COPA_NACIONAL_II = 9;
	private static final Integer PONTUACAO_EMPATE_COPA_NACIONAL_II = 3;
	private static final Integer PONTUACAO_VITORIA_CONTINENTAL = 24;
	private static final Integer PONTUACAO_EMPATE_CONTINENTAL = 8;
	private static final Integer PONTUACAO_VITORIA_CONTINENTAL_II = 18;
	private static final Integer PONTUACAO_EMPATE_CONTINENTAL_II = 6;
	private static final Integer PONTUACAO_VITORIA_CONTINENTAL_III = 12;
	private static final Integer PONTUACAO_EMPATE_CONTINENTAL_III = 4;
	private static final Integer PONTUACAO_VITORIA_CONTINENTAL_OUTROS = 0;
	private static final Integer PONTUACAO_EMPATE_CONTINENTAL_OUTROS = 0;
	private static final Integer PONTUACAO_VITORIA_AMISTOSO_INTERNACIONAL = 0;
	private static final Integer PONTUACAO_EMPATE_AMISTOSO_INTERNACIONAL = 0;
	private static final Integer PONTUACAO_VITORIA_AMISTOSO_NACIONAL = 0;
	private static final Integer PONTUACAO_EMPATE_AMISTOSO_NACIONAL = 0;
	
	private static final Map<NivelCampeonato, Integer> PONTUACAO_VITORIA;
	
	private static final Map<NivelCampeonato, Integer> PONTUACAO_EMPATE;
	
	static {
		
		PONTUACAO_VITORIA = new HashMap<NivelCampeonato, Integer>();
		
		PONTUACAO_EMPATE = new HashMap<NivelCampeonato, Integer>();
		
		PONTUACAO_VITORIA.put(NivelCampeonato.NACIONAL, PONTUACAO_VITORIA_NACIONAL);
		PONTUACAO_VITORIA.put(NivelCampeonato.NACIONAL_II, PONTUACAO_VITORIA_NACIONAL_II);
		PONTUACAO_VITORIA.put(NivelCampeonato.COPA_NACIONAL, PONTUACAO_VITORIA_COPA_NACIONAL);
		PONTUACAO_VITORIA.put(NivelCampeonato.COPA_NACIONAL_II, PONTUACAO_VITORIA_COPA_NACIONAL_II);
		PONTUACAO_VITORIA.put(NivelCampeonato.CONTINENTAL, PONTUACAO_VITORIA_CONTINENTAL);
		PONTUACAO_VITORIA.put(NivelCampeonato.CONTINENTAL_II, PONTUACAO_VITORIA_CONTINENTAL_II);
		PONTUACAO_VITORIA.put(NivelCampeonato.CONTINENTAL_III, PONTUACAO_VITORIA_CONTINENTAL_III);
		PONTUACAO_VITORIA.put(NivelCampeonato.CONTINENTAL_OUTROS, PONTUACAO_VITORIA_CONTINENTAL_OUTROS);
		PONTUACAO_VITORIA.put(NivelCampeonato.AMISTOSO_INTERNACIONAL, PONTUACAO_VITORIA_AMISTOSO_INTERNACIONAL);
		PONTUACAO_VITORIA.put(NivelCampeonato.AMISTOSO_NACIONAL, PONTUACAO_VITORIA_AMISTOSO_NACIONAL);
		
		PONTUACAO_EMPATE.put(NivelCampeonato.NACIONAL, PONTUACAO_EMPATE_NACIONAL);
		PONTUACAO_EMPATE.put(NivelCampeonato.NACIONAL_II, PONTUACAO_EMPATE_NACIONAL_II);
		PONTUACAO_EMPATE.put(NivelCampeonato.COPA_NACIONAL, PONTUACAO_EMPATE_COPA_NACIONAL);
		PONTUACAO_EMPATE.put(NivelCampeonato.COPA_NACIONAL_II, PONTUACAO_EMPATE_COPA_NACIONAL_II);
		PONTUACAO_EMPATE.put(NivelCampeonato.CONTINENTAL, PONTUACAO_EMPATE_CONTINENTAL);
		PONTUACAO_EMPATE.put(NivelCampeonato.CONTINENTAL_II, PONTUACAO_EMPATE_CONTINENTAL_II);
		PONTUACAO_EMPATE.put(NivelCampeonato.CONTINENTAL_III, PONTUACAO_EMPATE_CONTINENTAL_III);
		PONTUACAO_EMPATE.put(NivelCampeonato.CONTINENTAL_OUTROS, PONTUACAO_EMPATE_CONTINENTAL_OUTROS);
		PONTUACAO_EMPATE.put(NivelCampeonato.AMISTOSO_INTERNACIONAL, PONTUACAO_EMPATE_AMISTOSO_INTERNACIONAL);
		PONTUACAO_EMPATE.put(NivelCampeonato.AMISTOSO_NACIONAL, PONTUACAO_EMPATE_AMISTOSO_NACIONAL);
	}

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

	/*public static void atualizarRankingTitulos(List<ClubeRanking> rankings, List<ClubeTituloRanking> rankingsTitulos) {
		atualizarRankingTitulos(rankings,
				rankingsTitulos.stream().collect(Collectors.toMap(ClubeTituloRanking::getClube, Function.identity())));
	}*/
	
	/*protected static void atualizarRankingTitulos(List<ClubeRanking> rankings, Map<Clube, ClubeTituloRanking> rankingsTitulos) {
	
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
	}*/
	
	public static void calcularPontuacao(ClubeTituloRanking ctr) {

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
	
	public static void calcularPontuacao(ClubeResumoTemporada clubeResumoTemporada) {

		Integer pontuacao = 0;
		
		pontuacao += PONTUACAO_VITORIA.get(clubeResumoTemporada.getNivelCampeonato()) * clubeResumoTemporada.getVitorias();
		
		pontuacao += PONTUACAO_EMPATE.get(clubeResumoTemporada.getNivelCampeonato()) * clubeResumoTemporada.getEmpates();
		
		clubeResumoTemporada.setPontuacao(pontuacao);
	}
}
