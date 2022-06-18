package com.fastfoot.probability.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.OrdemClassificacaoGeral;
import com.fastfoot.service.util.ValidatorUtil;

public class ClubeRankingProbabilidadeCompletoUtil {
	
	public static final Integer CAMP_CONT = 1;
	
	public static final Integer CAMP_CONT_II = 2;
	
	public static final Integer CAMP_CONT_III = 3;
	
	public static final Integer CAMP_COPA_NAC = 4;
	
	public static final Integer CAMP_COPA_NAC_II = 5;
	
	public static List<ClubeRankingProbabilidade> rankearClubesTemporada(List<Clube> clubesLiga,
			List<ClassificacaoProbabilidade> classificacaoI, List<ClassificacaoProbabilidade> classificacaoII,
			Map<Integer, Clube> clubesCampeoes) {

		List<ClubeRankingProbabilidade> rankings =  gerarClubeRankingInicial(clubesLiga);

		Map<Clube, ClubeRankingProbabilidade> clubeRankings = rankings.stream()
				.collect(Collectors.toMap(ClubeRankingProbabilidade::getClube, Function.identity()));
		
		rankearCampeonatoNacional(clubeRankings, classificacaoI, NivelCampeonato.NACIONAL);
		rankearCampeonatoNacional(clubeRankings, classificacaoII, NivelCampeonato.NACIONAL_II);

		Clube clube = null;
		
		clube = clubesCampeoes.get(CAMP_CONT);
		
		if (clube != null && clubeRankings.get(clube) != null) {
			clubeRankings.get(clube).setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacaoCampeao(NivelCampeonato.CONTINENTAL));
		}
		
		clube = clubesCampeoes.get(CAMP_CONT_II);
		
		if (clube != null && clubeRankings.get(clube) != null) {
			clubeRankings.get(clube).setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacaoCampeao(NivelCampeonato.CONTINENTAL_II));
		}
		
		clube = clubesCampeoes.get(CAMP_CONT_III);
		
		if (clube != null && clubeRankings.get(clube) != null) {
			clubeRankings.get(clube).setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacaoCampeao(NivelCampeonato.CONTINENTAL_III));
		}
		
		clube = clubesCampeoes.get(CAMP_COPA_NAC);
		
		if (clube != null) {
			clubeRankings.get(clube).setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.getClassificacaoCampeao(NivelCampeonato.COPA_NACIONAL));
		}
		
		clube = clubesCampeoes.get(CAMP_COPA_NAC_II);
		
		if (clube != null) {
			clubeRankings.get(clube).setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.getClassificacaoCampeao(NivelCampeonato.COPA_NACIONAL_II));
		}

		gerarPosicaoGeralLiga(rankings);

		return rankings;
	}

	private static void gerarPosicaoGeralLiga(List<ClubeRankingProbabilidade> rankingsLiga) {
		
		Map<ClassificacaoContinentalFinal, List<ClubeRankingProbabilidade>> rkContinental = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRankingProbabilidade::getClassificacaoContinental));
		Map<ClassificacaoCopaNacionalFinal, List<ClubeRankingProbabilidade>> rkCopaNacional = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRankingProbabilidade::getClassificacaoCopaNacional));
		Map<ClassificacaoNacionalFinal, List<ClubeRankingProbabilidade>> rkNacional = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRankingProbabilidade::getClassificacaoNacional));
		
		List<ClubeRankingProbabilidade> tmps = null;
		int posFinal = 1, qtdeAtualizada = 0;
		
		for (OrdemClassificacaoGeral ocg: OrdemClassificacaoGeral.ORDEM) {
			tmps = null;
			
			if (ocg.isContinental()) {
				tmps = rkContinental.get(ocg.getClassificacaoContinental());
			} else if (ocg.isNacional()) {
				tmps = rkNacional.get(ocg.getClassificacaoNacional());
			} else if (ocg.isCopaNacional()) {
				tmps = rkCopaNacional.get(ocg.getClassificacaoCopaNacional());
			}
			
			if (!ValidatorUtil.isEmpty(tmps)) {
				qtdeAtualizada = 0;
				for (ClubeRankingProbabilidade cr : tmps) {
					if (cr.getPosicaoGeral() == -1) {
						cr.setPosicaoGeral(posFinal);
						qtdeAtualizada++;
					}
				}
				posFinal += qtdeAtualizada;
			}	
		}		
	}

	private static List<ClubeRankingProbabilidade> gerarClubeRankingInicial(List<Clube> clubes) {
		List<ClubeRankingProbabilidade> rankings = new ArrayList<ClubeRankingProbabilidade>();
		ClubeRankingProbabilidade clubeRanking = null;
		for (Clube c : clubes) {
			clubeRanking = new ClubeRankingProbabilidade();
			clubeRanking.setClube(c);
			clubeRanking.setClassificacaoNacional(ClassificacaoNacionalFinal.NULL);
			clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU);
			clubeRanking.setClassificacaoContinental(ClassificacaoContinentalFinal.NAO_PARTICIPOU);
			clubeRanking.setPosicaoGeral(-1);
			rankings.add(clubeRanking);
		}
		return rankings;
	}
	
	private static void rankearCampeonatoNacional(Map<Clube, ClubeRankingProbabilidade> rankings,
			List<ClassificacaoProbabilidade> classificacao, NivelCampeonato nivelCampeonato) {
		ClubeRankingProbabilidade clubeRanking = null;

		for (ClassificacaoProbabilidade cl : classificacao) {
			clubeRanking = rankings.get(cl.getClube());
			clubeRanking.setClassificacaoNacional(
					ClassificacaoNacionalFinal.getClassificacao(nivelCampeonato, cl.getPosicao()));
		}
	}

}
