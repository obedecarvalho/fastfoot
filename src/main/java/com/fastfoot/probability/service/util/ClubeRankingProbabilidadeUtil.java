package com.fastfoot.probability.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.ClassificacaoProbabilidade;
import com.fastfoot.probability.model.ClubeRankingProbabilidade;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.OrdemClassificacaoGeral;
import com.fastfoot.service.util.ValidatorUtil;

public class ClubeRankingProbabilidadeUtil {
	
	public static final Integer CAMP_CONT = 1;
	
	public static final Integer CAMP_CONT_II = 2;
	
	public static final Integer CAMP_CONT_III = 3;
	
	public static final Integer CAMP_COPA_NAC = 4;
	
	public static final Integer CAMP_COPA_NAC_II = 5;
	
	public static List<ClubeRankingProbabilidade> rankearClubesTemporada(List<Clube> clubesLiga,
			List<ClassificacaoProbabilidade> classificacao, NivelCampeonato nivelCampeonato,
			Map<Integer, Clube> clubesCampeoes) {

		List<ClubeRankingProbabilidade> rankings =  gerarClubeRankingInicial(clubesLiga);

		Map<Clube, ClubeRankingProbabilidade> clubeRankings = rankings.stream().collect(Collectors.toMap(ClubeRankingProbabilidade::getClube, Function.identity()));
		
		rankearCampeonatoNacional(clubeRankings, classificacao, nivelCampeonato);
		
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
		
		Optional<ClubeRankingProbabilidade> tmp = null;
		List<ClubeRankingProbabilidade> tmps = null;
		int posFinal = 1, qtdeAtualizada = 0;
		
		for (OrdemClassificacaoGeral ocg: OrdemClassificacaoGeral.ORDEM) {
			tmp = null; tmps = null;
			
			if (ocg.isContinental()) {
				tmp = findClassificacaoContinental(rankingsLiga, ocg.getClassificacaoContinental());
			} else if (ocg.isNacional()) {
				tmps = findClassificacaoNacional(rankingsLiga, ocg.getClassificacaoNacional());
			} else if (ocg.isCopaNacional()) {
				tmp = findClassificacaoCopaNacional(rankingsLiga, ocg.getClassificacaoCopaNacional());
			}
			
			if (tmp != null && tmp.isPresent()) {
				tmps = Arrays.asList(tmp.get());
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
			
			/*if (tmp.isPresent() && tmp.get().getPosicaoGeral() == -1) {
				tmp.get().setPosicaoGeral(posFinal);
				posFinal++;
			}*/
		}
		
	}
	
	private static List<ClubeRankingProbabilidade> findClassificacaoNacional(List<ClubeRankingProbabilidade> rankingsLiga, ClassificacaoNacionalFinal clasNac) {
		return rankingsLiga.stream().filter(r -> clasNac.equals(r.getClassificacaoNacional())).collect(Collectors.toList());
	}

	private static Optional<ClubeRankingProbabilidade> findClassificacaoCopaNacional(List<ClubeRankingProbabilidade> rankingsLiga, ClassificacaoCopaNacionalFinal clasCopaNac) {
		return rankingsLiga.stream().filter(r -> clasCopaNac.equals(r.getClassificacaoCopaNacional())).findFirst();
	}

	private static Optional<ClubeRankingProbabilidade> findClassificacaoContinental(List<ClubeRankingProbabilidade> rankingsLiga, ClassificacaoContinentalFinal clasCont) {
		return rankingsLiga.stream().filter(r -> clasCont.equals(r.getClassificacaoContinental())).findFirst();
	}

	private static List<ClubeRankingProbabilidade> gerarClubeRankingInicial(List<Clube> clubes) {
		List<ClubeRankingProbabilidade> rankings = new ArrayList<ClubeRankingProbabilidade>();
		ClubeRankingProbabilidade clubeRanking = null;
		for (Clube c : clubes) {
			clubeRanking = new ClubeRankingProbabilidade();
			clubeRanking.setClube(c);
			//clubeRanking.setAno(temporada.getAno());
			//clubeRanking.setTemporada(temporada);
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
