package com.fastfoot.club.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.OrdemClassificacaoGeral;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.ValidatorUtil;

public class ClubeRankingUtil {
	
	private static final Comparator<CampeonatoMisto> COMPARATOR = new Comparator<CampeonatoMisto>() {

		@Override
		public int compare(CampeonatoMisto o1, CampeonatoMisto o2) {
			return o1.getNivelCampeonato().compareTo(o2.getNivelCampeonato());
		}
	};
	
	public static List<ClubeRanking> rankearClubesTemporada(Temporada temporada, List<Clube> clubes) {
		List<ClubeRanking> rankings =  gerarClubeRankingInicial(clubes, temporada);

		Map<Clube, ClubeRanking> clubeRankings = rankings.stream().collect(Collectors.toMap(ClubeRanking::getClube, Function.identity()));
		
		List<CampeonatoEliminatorio> copasNacionais = temporada.getCampeonatosCopasNacionais().stream()
				.filter(c -> c.getNivelCampeonato().isCopaNacional()).collect(Collectors.toList());
		List<CampeonatoEliminatorio> copasNacionaisII = temporada.getCampeonatosCopasNacionais().stream()
				.filter(c -> c.getNivelCampeonato().isCopaNacionalII()).collect(Collectors.toList());
		
		Collections.sort(temporada.getCampeonatosContinentais(), COMPARATOR);

		rankearCampeonatoNacional(clubeRankings, temporada.getCampeonatosNacionais());
		rankearCopaNacional(clubeRankings, copasNacionais);
		rankearCopaNacional(clubeRankings, copasNacionaisII);//Esperado que CNII subscreva CNI
		rankearContinental(clubeRankings, temporada.getCampeonatosContinentais());//Esperado que CIII subscreva CII e CII subscreva CI
		
		gerarPosicaoGeral(rankings);
		gerarPosicaoGlobal(rankings);
		
		return rankings;
	}

	protected static void gerarPosicaoGeral(List<ClubeRanking> rankings) {
		List<ClubeRanking> rankingsLiga = null;
		for (Liga l : Liga.getAll()) {
			rankingsLiga = rankings.stream().filter(r -> l.equals(r.getClube().getLiga())).collect(Collectors.toList());
			gerarPosicaoGeralLiga(rankingsLiga);
		}
	}
	
	protected static void gerarPosicaoGeralLiga(List<ClubeRanking> rankingsLiga) {
		
		Map<ClassificacaoContinental, List<ClubeRanking>> rkContinental = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoContinental));
		Map<ClassificacaoCopaNacional, List<ClubeRanking>> rkCopaNacional = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoCopaNacional));
		Map<ClassificacaoNacional, List<ClubeRanking>> rkNacional = rankingsLiga.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoNacional));
		
		List<ClubeRanking> tmps = null;
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
				for (ClubeRanking cr : tmps) {
					if (cr.getPosicaoGeral() == -1) {
						cr.setPosicaoGeral(posFinal);
						qtdeAtualizada++;
					}
				}
				posFinal += qtdeAtualizada;
			}

		}
		
	}
	
	protected static void gerarPosicaoGlobal(List<ClubeRanking> rankings) {
		
		Map<ClassificacaoContinental, List<ClubeRanking>> rkContinental = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoContinental));
		Map<ClassificacaoCopaNacional, List<ClubeRanking>> rkCopaNacional = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoCopaNacional));
		Map<ClassificacaoNacional, List<ClubeRanking>> rkNacional = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoNacional));
		
		List<ClubeRanking> tmps = null;
		int posFinal = 1;
		OrdemClassificacaoGeral ocg = null;
		
		for (int i = 0; i < OrdemClassificacaoGeral.ORDEM_GLOBAL.length; i++) {
			ocg = OrdemClassificacaoGeral.ORDEM_GLOBAL[i];

			tmps = null;
			
			if (ocg.isContinental()) {
				tmps = rkContinental.get(ocg.getClassificacaoContinental());
			} else if (ocg.isNacional()) {
				tmps = rkNacional.get(ocg.getClassificacaoNacional());
			} else if (ocg.isCopaNacional()) {
				tmps = rkCopaNacional.get(ocg.getClassificacaoCopaNacional());
			}
			
			if (!ValidatorUtil.isEmpty(tmps)) {
				
				//Filtrar apenas os que não foram setados
				tmps = tmps.stream().filter(cr -> cr.getPosicaoInternacional() == -1).collect(Collectors.toList());
				
				if (tmps.size() > 1) {
					desempatar(tmps, i + 1, posFinal, true);
					posFinal += tmps.size();
				} else if (tmps.size() == 1) {
					if (tmps.get(0).getPosicaoInternacional() == -1) {
						tmps.get(0).setPosicaoInternacional(posFinal++);
					}
				}
				
				/*qtdeAtualizada = 0;
				for (ClubeRanking cr : tmps) {
					if (cr.getPosicaoGlobal() == -1) {
						cr.setPosicaoGlobal(posFinal);
						qtdeAtualizada++;
					}
				}
				posFinal += qtdeAtualizada;*/
			}
		}
	}
	
	public static void desempatar(List<ClubeRanking> rankings, int posOrdem, int posFinal, boolean unique) {
		
		Map<ClassificacaoContinental, List<ClubeRanking>> rkContinental = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoContinental));
		Map<ClassificacaoCopaNacional, List<ClubeRanking>> rkCopaNacional = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoCopaNacional));
		Map<ClassificacaoNacional, List<ClubeRanking>> rkNacional = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClassificacaoNacional));
		
		List<ClubeRanking> tmps = null;
		OrdemClassificacaoGeral ocg = null;
		
		for (int i = posOrdem; i < OrdemClassificacaoGeral.ORDEM_GLOBAL.length; i++) {
			ocg = OrdemClassificacaoGeral.ORDEM_GLOBAL[i];

			tmps = null;
			
			if (ocg.isContinental()) {
				tmps = rkContinental.get(ocg.getClassificacaoContinental());
			} else if (ocg.isNacional()) {
				tmps = rkNacional.get(ocg.getClassificacaoNacional());
			} else if (ocg.isCopaNacional()) {
				tmps = rkCopaNacional.get(ocg.getClassificacaoCopaNacional());
			}
			
			if (!ValidatorUtil.isEmpty(tmps)) {
				
				//Filtrar apenas os que não foram setados
				tmps = tmps.stream().filter(cr -> cr.getPosicaoInternacional() == -1).collect(Collectors.toList());
				
				if (tmps.size() > 1) {
					desempatar(tmps, i + 1, posFinal, unique);
					posFinal += tmps.size();
				} else if (tmps.size() == 1) {
					if (tmps.get(0).getPosicaoInternacional() == -1) {
						tmps.get(0).setPosicaoInternacional(posFinal++);
					}
				}
				
				/*qtdeAtualizada = 0;
				for (ClubeRanking cr : tmps) {
					if (cr.getPosicaoGlobal() == -1) {
						cr.setPosicaoGlobal(posFinal);
						qtdeAtualizada++;
					}
				}
				posFinal += qtdeAtualizada;*/
			}
		}
		
		if (unique) {
			Collections.shuffle(rankings);
			for (ClubeRanking cr : rankings) {
				if (cr.getPosicaoInternacional() == -1) {
					cr.setPosicaoInternacional(posFinal++);
				}
			}
		} else {
			final Integer posFinalX = posFinal;
			rankings.stream().filter(cr -> cr.getPosicaoInternacional() == -1).forEach(cr -> cr.setPosicaoInternacional(posFinalX));
		}

	}

	protected static List<ClubeRanking> gerarClubeRankingInicial(List<Clube> clubes, Temporada temporada) {
		List<ClubeRanking> rankings = new ArrayList<ClubeRanking>();
		ClubeRanking clubeRanking = null;
		for (Clube c : clubes) {
			clubeRanking = new ClubeRanking();
			clubeRanking.setClube(c);
			clubeRanking.setAno(temporada.getAno());
			clubeRanking.setTemporada(temporada);
			clubeRanking.setClassificacaoNacional(ClassificacaoNacional.NULL);
			clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacional.NAO_PARTICIPOU);
			clubeRanking.setClassificacaoContinental(ClassificacaoContinental.NAO_PARTICIPOU);
			clubeRanking.setPosicaoGeral(-1);
			clubeRanking.setPosicaoInternacional(-1);
			rankings.add(clubeRanking);
		}
		return rankings;
	}

	protected static void rankearCampeonatoNacional(Map<Clube, ClubeRanking> rankings, List<Campeonato> campeonatos) {
		ClubeRanking clubeRanking = null;
		for (Campeonato c : campeonatos) {
			for (Classificacao cl : c.getClassificacao()) {
				clubeRanking = rankings.get(cl.getClube());
				clubeRanking.setClassificacaoNacional(
						ClassificacaoNacional.getClassificacao(c.getNivelCampeonato(), cl.getPosicao()));
			}
		}
	}
	
	protected static void rankearCopaNacional(Map<Clube, ClubeRanking> rankings,
			List<CampeonatoEliminatorio> campeonatos) {

		ClubeRanking clubeRanking = null;

		for (CampeonatoEliminatorio c : campeonatos) {
			for (RodadaEliminatoria re : c.getRodadas()) {
				for (PartidaEliminatoriaResultado p : re.getPartidas()) {
					clubeRanking = rankings.get(p.getClubePerdedor());
					clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacional
							.getClassificacao(c.getNivelCampeonato(), re, c.getRodadas().size(), false));

					if (re.getNumero() == c.getRodadas().size()) {
						clubeRanking = rankings.get(p.getClubeVencedor());
						clubeRanking.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(c.getNivelCampeonato()));
					}
				}
			}
		}
	}

	protected static void rankearContinental(Map<Clube, ClubeRanking> rankings, List<CampeonatoMisto> campeonatos) {
		
		ClubeRanking clubeRanking = null;
		List<Clube> clubesFaseEliminatoria = new ArrayList<Clube>();
		List<Clube> clubesFaseGrupos = new ArrayList<Clube>();
		
		for (CampeonatoMisto c : campeonatos) {
			
			clubesFaseEliminatoria.clear();
			clubesFaseGrupos.clear();
			
			for (RodadaEliminatoria re : c.getRodadasEliminatorias()) {
				for (PartidaEliminatoriaResultado p : re.getPartidas()) {
					clubeRanking = rankings.get(p.getClubePerdedor());
					clubeRanking.setClassificacaoContinental(ClassificacaoContinental
							.getClassificacao(c.getNivelCampeonato(), re, false));
					clubesFaseEliminatoria.add(p.getClubePerdedor());

					if (re.getNumero() == 6) {
						clubeRanking = rankings.get(p.getClubeVencedor());
						clubeRanking.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoCampeao(c.getNivelCampeonato()));
						clubesFaseEliminatoria.add(p.getClubeVencedor());
					}
				}
			}
			
			for (GrupoCampeonato gc : c.getGrupos()) {
				clubesFaseGrupos.addAll(gc.getClassificacao().stream().map(Classificacao::getClube).collect(Collectors.toList()));
			}
			
			clubesFaseGrupos.removeAll(clubesFaseEliminatoria);
			
			for (Clube cl : clubesFaseGrupos) {
				clubeRanking = rankings.get(cl);
				clubeRanking.setClassificacaoContinental(ClassificacaoContinental.getClassificacaoFaseGrupo(c.getNivelCampeonato()));
			}
		}
		
	}

}
