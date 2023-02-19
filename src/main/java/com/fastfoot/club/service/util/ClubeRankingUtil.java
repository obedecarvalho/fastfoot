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
	
	public static List<ClubeRanking> rankearClubesTemporada(Temporada temporada, List<Clube> clubes) {
		List<ClubeRanking> rankings =  gerarClubeRankingInicial(clubes, temporada);

		Map<Clube, ClubeRanking> clubeRankings = rankings.stream().collect(Collectors.toMap(ClubeRanking::getClube, Function.identity()));
		
		List<CampeonatoEliminatorio> copasNacionais = temporada.getCampeonatosCopasNacionais().stream()
				.filter(c -> c.getNivelCampeonato().isCopaNacional()).collect(Collectors.toList());
		List<CampeonatoEliminatorio> copasNacionaisII = temporada.getCampeonatosCopasNacionais().stream()
				.filter(c -> c.getNivelCampeonato().isCopaNacionalII()).collect(Collectors.toList());
		
		Collections.sort(temporada.getCampeonatosContinentais(), new Comparator<CampeonatoMisto>() {

			@Override
			public int compare(CampeonatoMisto o1, CampeonatoMisto o2) {
				return o1.getNivelCampeonato().compareTo(o2.getNivelCampeonato());
			}
		});

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

	/*public static void gerarPosicaoGeralLiga(List<ClubeRanking> rankingsLiga) {
		
		//C1, N1, N2, N3, CII1, CN1, N4
		
		//CII1, CN1, N4, CNII1, N5, N6, N7, N8
		
		Optional<ClubeRanking> tmp = null;
		List<ClubeRanking> tmps = null;
		int posFinal = 1, qtdeAtualizada = 0;
		
		//Campeão Continental
		tmp = findClassificacaoContinental(rankingsLiga, ClassificacaoContinentalFinal.C_CAMPEAO);
		if (tmp.isPresent()) {
			tmp.get().setPosicaoGeral(posFinal);
			posFinal++;
		}
		
		//Nacional 1, 2 e 3
		for (ClassificacaoNacionalFinal n : Arrays.asList(ClassificacaoNacionalFinal.N_1, ClassificacaoNacionalFinal.N_2, ClassificacaoNacionalFinal.N_3)) {
			tmps = findClassificacaoNacional(rankingsLiga, n);
			qtdeAtualizada = 0;
			for (ClubeRanking cr : tmps) {
				if (!cr.isCampeaoContinental()) {
					cr.setPosicaoGeral(posFinal);
					qtdeAtualizada++;
				}
			}
			posFinal += qtdeAtualizada;
		}
		
		//Campeão Continental II
		tmp = findClassificacaoContinental(rankingsLiga, ClassificacaoContinentalFinal.CII_CAMPEAO);
		if (tmp.isPresent() 
				&& !ClassificacaoNacionalFinal.isN1N2N3(tmp.get().getClassificacaoNacional())) {
			tmp.get().setPosicaoGeral(posFinal);
			posFinal++;
		}
		
		//Campeão Copa Nacional
		tmp = findClassificacaoCopaNacional(rankingsLiga, ClassificacaoCopaNacionalFinal.CN_CAMPEAO);
		if (tmp.isPresent() 
				&& !ClassificacaoNacionalFinal.isN1N2N3(tmp.get().getClassificacaoNacional())
				&& !tmp.get().isCampeaoContinental() 
				&& !tmp.get().isCampeaoContinentalII()) {
			tmp.get().setPosicaoGeral(posFinal);
			posFinal++;
		}
		
		//Nacional 4
		tmps = findClassificacaoNacional(rankingsLiga, ClassificacaoNacionalFinal.N_4);
		qtdeAtualizada = 0;
		for (ClubeRanking cr : tmps) {
			if (!cr.isCampeaoContinental()
					&& !cr.isCampeaoContinentalII()
					&& !cr.isCampeaoCopaNacional()) {
				cr.setPosicaoGeral(posFinal);
				qtdeAtualizada++;
			}
		}
		posFinal += qtdeAtualizada;

		//Campeão Copa Nacional II
		tmp = findClassificacaoCopaNacional(rankingsLiga, ClassificacaoCopaNacionalFinal.CNII_CAMPEAO);
		if (tmp.isPresent() 
				&& !ClassificacaoNacionalFinal.isN1N2N3(tmp.get().getClassificacaoNacional())
				&& !tmp.get().isCampeaoCopaNacional()
				&& !tmp.get().isCampeaoContinental() 
				&& !tmp.get().isCampeaoContinentalII()
				&& !tmp.get().getClassificacaoNacional().equals(ClassificacaoNacionalFinal.N_4)) {
			tmp.get().setPosicaoGeral(posFinal);
			posFinal++;
		}

		//Nacional 5 a 13
		for (ClassificacaoNacionalFinal n : ClassificacaoNacionalFinal.getAllNacional5a13()) {
			tmps = findClassificacaoNacional(rankingsLiga, n);
			qtdeAtualizada = 0;
			for (ClubeRanking cr : tmps) {
				if (!cr.isCampeaoContinental()
						&& !cr.isCampeaoContinentalII()
						&& !cr.isCampeaoCopaNacional()
						&& !cr.isCampeaoCopaNacionalII()) {
					cr.setPosicaoGeral(posFinal);
					qtdeAtualizada++;
				}
			}
			posFinal += qtdeAtualizada;
		}

		//Nacional II 1 a 3
		for (ClassificacaoNacionalFinal n : ClassificacaoNacionalFinal.getAllNacionalII1a3()) {
			tmps = findClassificacaoNacional(rankingsLiga, n);
			qtdeAtualizada = 0;
			for (ClubeRanking cr : tmps) {
				if (!cr.isCampeaoContinental()
						&& !cr.isCampeaoContinentalII()
						&& !cr.isCampeaoCopaNacional()
						&& !cr.isCampeaoCopaNacionalII()) {
					cr.setPosicaoGeral(posFinal);
					qtdeAtualizada++;
				}
			}
			posFinal += qtdeAtualizada;
		}
		
		//Nacional 14 a 16
		for (ClassificacaoNacionalFinal n : ClassificacaoNacionalFinal.getAllNacional14a16()) {
			tmps = findClassificacaoNacional(rankingsLiga, n);
			qtdeAtualizada = 0;
			for (ClubeRanking cr : tmps) {
				if (!cr.isCampeaoContinental()
						&& !cr.isCampeaoContinentalII()
						&& !cr.isCampeaoCopaNacional()
						&& !cr.isCampeaoCopaNacionalII()) {
					cr.setPosicaoGeral(posFinal);
					qtdeAtualizada++;
				}
			}
			posFinal += qtdeAtualizada;
		}
	
		//Nacional II 4 a 16
		for (ClassificacaoNacionalFinal n : ClassificacaoNacionalFinal.getAllNacionalII4a16()) {
			tmps = findClassificacaoNacional(rankingsLiga, n);
			qtdeAtualizada = 0;
			for (ClubeRanking cr : tmps) {
				if (!cr.isCampeaoContinental()
						&& !cr.isCampeaoContinentalII()
						&& !cr.isCampeaoCopaNacional()
						&& !cr.isCampeaoCopaNacionalII()) {
					cr.setPosicaoGeral(posFinal);
					qtdeAtualizada++;
				}
			}
			posFinal += qtdeAtualizada;
		}
	}*/

	/*private static List<ClubeRanking> findClassificacaoNacional(List<ClubeRanking> rankingsLiga, ClassificacaoNacionalFinal clasNac) {
		return rankingsLiga.stream().filter(r -> clasNac.equals(r.getClassificacaoNacional())).collect(Collectors.toList());
	}
	
	private static Optional<ClubeRanking> findClassificacaoNacionalX(List<ClubeRanking> rankingsLiga, ClassificacaoNacionalFinal clasNac) {
		return rankingsLiga.stream().filter(r -> clasNac.equals(r.getClassificacaoNacional())).findFirst();
	}

	private static Optional<ClubeRanking> findClassificacaoCopaNacional(List<ClubeRanking> rankingsLiga, ClassificacaoCopaNacionalFinal clasCopaNac) {
		return rankingsLiga.stream().filter(r -> clasCopaNac.equals(r.getClassificacaoCopaNacional())).findFirst();
	}

	private static Optional<ClubeRanking> findClassificacaoContinental(List<ClubeRanking> rankingsLiga, ClassificacaoContinentalFinal clasCont) {
		return rankingsLiga.stream().filter(r -> clasCont.equals(r.getClassificacaoContinental())).findFirst();
	}*/

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