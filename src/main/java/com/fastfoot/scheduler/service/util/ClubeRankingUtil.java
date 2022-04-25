package com.fastfoot.scheduler.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;
import com.fastfoot.scheduler.model.OrdemClassificacaoGeral;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.ValidatorUtil;

public class ClubeRankingUtil {
	
	public static List<ClubeRanking> rankearClubesTemporada(Temporada temporada, List<Clube> clubes) {
		List<ClubeRanking> rankings =  gerarClubeRankingInicial(clubes, temporada);

		Map<Clube, ClubeRanking> clubeRankings = rankings.stream().collect(Collectors.toMap(ClubeRanking::getClube, Function.identity()));

		rankearCampeonatoNacional(clubeRankings, temporada.getCampeonatosNacionais());
		rankearCopaNacional(clubeRankings, temporada.getCampeonatosCopasNacionais());
		rankearContinental(clubeRankings, temporada.getCampeonatosContinentais());
		
		gerarPosicaoGeral(rankings);
		
		return rankings;
	}

	public static void gerarPosicaoGeral(List<ClubeRanking> rankings) {
		List<ClubeRanking> rankingsLiga = null;
		for (Liga l : Liga.getAll()) {
			rankingsLiga = rankings.stream().filter(r -> l.equals(r.getClube().getLiga())).collect(Collectors.toList());
			gerarPosicaoGeralLigaX(rankingsLiga);
		}
	}
	
	private static final OrdemClassificacaoGeral[] ORDEM = {
			
			new OrdemClassificacaoGeral(1, ClassificacaoCopaNacionalFinal.CNII_VICE_CAMPEAO),//TODO			
			
			new OrdemClassificacaoGeral(1, ClassificacaoContinentalFinal.C_CAMPEAO),
			new OrdemClassificacaoGeral(2, ClassificacaoNacionalFinal.N_1),
			new OrdemClassificacaoGeral(3, ClassificacaoNacionalFinal.N_2),
			new OrdemClassificacaoGeral(4, ClassificacaoNacionalFinal.N_3),
			new OrdemClassificacaoGeral(5, ClassificacaoContinentalFinal.CII_CAMPEAO),
			new OrdemClassificacaoGeral(6, ClassificacaoCopaNacionalFinal.CN_CAMPEAO),
			new OrdemClassificacaoGeral(7, ClassificacaoNacionalFinal.N_4),
			new OrdemClassificacaoGeral(8, ClassificacaoCopaNacionalFinal.CNII_CAMPEAO),
			new OrdemClassificacaoGeral(9, ClassificacaoNacionalFinal.N_5),
			new OrdemClassificacaoGeral(10, ClassificacaoNacionalFinal.N_6),
			new OrdemClassificacaoGeral(11, ClassificacaoNacionalFinal.N_7),
			new OrdemClassificacaoGeral(12, ClassificacaoNacionalFinal.N_8),
			new OrdemClassificacaoGeral(13, ClassificacaoNacionalFinal.N_9),
			new OrdemClassificacaoGeral(14, ClassificacaoNacionalFinal.N_10),
			new OrdemClassificacaoGeral(15, ClassificacaoNacionalFinal.N_11),
			new OrdemClassificacaoGeral(16, ClassificacaoNacionalFinal.N_12),
			new OrdemClassificacaoGeral(17, ClassificacaoNacionalFinal.N_13),
			new OrdemClassificacaoGeral(18, ClassificacaoNacionalFinal.NII_1),
			new OrdemClassificacaoGeral(19, ClassificacaoNacionalFinal.NII_2),
			new OrdemClassificacaoGeral(20, ClassificacaoNacionalFinal.NII_3),
			new OrdemClassificacaoGeral(21, ClassificacaoNacionalFinal.N_14),
			new OrdemClassificacaoGeral(22, ClassificacaoNacionalFinal.N_15),
			new OrdemClassificacaoGeral(23, ClassificacaoNacionalFinal.N_16),
			new OrdemClassificacaoGeral(24, ClassificacaoNacionalFinal.NII_4),
			new OrdemClassificacaoGeral(25, ClassificacaoNacionalFinal.NII_5),
			new OrdemClassificacaoGeral(26, ClassificacaoNacionalFinal.NII_6),
			new OrdemClassificacaoGeral(27, ClassificacaoNacionalFinal.NII_7),
			new OrdemClassificacaoGeral(28, ClassificacaoNacionalFinal.NII_8),
			new OrdemClassificacaoGeral(29, ClassificacaoNacionalFinal.NII_9),
			new OrdemClassificacaoGeral(30, ClassificacaoNacionalFinal.NII_10),
			new OrdemClassificacaoGeral(31, ClassificacaoNacionalFinal.NII_11),
			new OrdemClassificacaoGeral(32, ClassificacaoNacionalFinal.NII_12),
			new OrdemClassificacaoGeral(33, ClassificacaoNacionalFinal.NII_13),
			new OrdemClassificacaoGeral(34, ClassificacaoNacionalFinal.NII_14),
			new OrdemClassificacaoGeral(35, ClassificacaoNacionalFinal.NII_15),
			new OrdemClassificacaoGeral(36, ClassificacaoNacionalFinal.NII_16)
			
		};
	
	public static void gerarPosicaoGeralLigaX(List<ClubeRanking> rankingsLiga) {
		
		Optional<ClubeRanking> tmp = null;
		List<ClubeRanking> tmps = null;
		int posFinal = 1, qtdeAtualizada = 0;
		
		for (OrdemClassificacaoGeral ocg: ORDEM) {
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
				for (ClubeRanking cr : tmps) {
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

	public static void gerarPosicaoGeralLiga(List<ClubeRanking> rankingsLiga) {
		
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
	}

	public static List<ClubeRanking> findClassificacaoNacional(List<ClubeRanking> rankingsLiga, ClassificacaoNacionalFinal clasNac) {
		return rankingsLiga.stream().filter(r -> clasNac.equals(r.getClassificacaoNacional())).collect(Collectors.toList());
	}
	
	public static Optional<ClubeRanking> findClassificacaoNacionalX(List<ClubeRanking> rankingsLiga, ClassificacaoNacionalFinal clasNac) {
		return rankingsLiga.stream().filter(r -> clasNac.equals(r.getClassificacaoNacional())).findFirst();
	}

	public static Optional<ClubeRanking> findClassificacaoCopaNacional(List<ClubeRanking> rankingsLiga, ClassificacaoCopaNacionalFinal clasCopaNac) {
		return rankingsLiga.stream().filter(r -> clasCopaNac.equals(r.getClassificacaoCopaNacional())).findFirst();
	}

	public static Optional<ClubeRanking> findClassificacaoContinental(List<ClubeRanking> rankingsLiga, ClassificacaoContinentalFinal clasCont) {
		return rankingsLiga.stream().filter(r -> clasCont.equals(r.getClassificacaoContinental())).findFirst();
	}

	public static List<ClubeRanking> gerarClubeRankingInicial(List<Clube> clubes, Temporada temporada) {
		List<ClubeRanking> rankings = new ArrayList<ClubeRanking>();
		ClubeRanking clubeRanking = null;
		for (Clube c : clubes) {
			clubeRanking = new ClubeRanking();
			clubeRanking.setClube(c);
			clubeRanking.setAno(temporada.getAno());
			clubeRanking.setTemporada(temporada);
			clubeRanking.setClassificacaoNacional(ClassificacaoNacionalFinal.NULL);
			clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.NAO_PARTICIPOU);
			clubeRanking.setClassificacaoContinental(ClassificacaoContinentalFinal.NAO_PARTICIPOU);
			clubeRanking.setPosicaoGeral(-1);
			rankings.add(clubeRanking);
		}
		return rankings;
	}

	public static void rankearCampeonatoNacional(Map<Clube, ClubeRanking> rankings, List<Campeonato> campeonatos) {
		ClubeRanking clubeRanking = null;
		for (Campeonato c : campeonatos) {
			for (Classificacao cl : c.getClassificacao()) {
				clubeRanking = rankings.get(cl.getClube());
				clubeRanking.setClassificacaoNacional(ClassificacaoNacionalFinal.getClassificacao(c.getNivelCampeonato(), cl.getPosicao()));
			}
		}
	}

	public static void rankearCopaNacional(Map<Clube, ClubeRanking> rankings, List<CampeonatoEliminatorio> campeonatos) {
		ClubeRanking clubeRanking = null;
		for (CampeonatoEliminatorio c : campeonatos) {
			List<PartidaEliminatoriaResultado> partidas = new ArrayList<PartidaEliminatoriaResultado>();
			Clube campeao = null;
			for (RodadaEliminatoria r : c.getRodadas()) {
				partidas.addAll(r.getPartidas());
				if (r.getNumero() == c.getRodadas().size()) {
					campeao = r.getPartidas().get(0).getClubeVencedor();
				}
			}

			Map<Clube, RodadaEliminatoria> clubeRodada = partidas.stream().collect(Collectors.toMap(PartidaEliminatoriaResultado::getClubePerdedor, PartidaEliminatoriaResultado::getRodada));

			for (Entry<Clube, RodadaEliminatoria> x : clubeRodada.entrySet()) {
				clubeRanking = rankings.get(x.getKey());
				clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.getClassificacao(c.getNivelCampeonato(), x.getValue(), x.getKey()));
			}

			clubeRanking = rankings.get(campeao);
			clubeRanking.setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal.getClassificacaoCampeao(c.getNivelCampeonato()));
		}
	}

	public static void rankearContinental(Map<Clube, ClubeRanking> rankings, List<CampeonatoMisto> campeonatos) {
		ClubeRanking clubeRanking = null;
		for (CampeonatoMisto c : campeonatos) {
			List<Clube> clubesFaseGrupos = new ArrayList<Clube>();
			for (GrupoCampeonato gc : c.getGrupos()) {
				clubesFaseGrupos.addAll(gc.getClassificacao().stream().map(Classificacao::getClube).collect(Collectors.toList()));
			}
			
			List<PartidaEliminatoriaResultado> partidas = new ArrayList<PartidaEliminatoriaResultado>();
			Clube campeao = null;
			
			for (RodadaEliminatoria r : c.getRodadasEliminatorias()) {
				partidas.addAll(r.getPartidas());
			}
			
			Map<Clube, RodadaEliminatoria> clubeRodada = partidas.stream().collect(Collectors.toMap(PartidaEliminatoriaResultado::getClubePerdedor, PartidaEliminatoriaResultado::getRodada));
			for (Entry<Clube, RodadaEliminatoria> x : clubeRodada.entrySet()) {
				clubeRanking = rankings.get(x.getKey());
				clubeRanking.setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacao(c.getNivelCampeonato(), x.getValue(), x.getKey()));
			}
			
			RodadaEliminatoria rodadaFinal = c.getRodadasEliminatorias().stream().max(Comparator.comparing(RodadaEliminatoria::getNumero)).get();
			campeao = rodadaFinal.getPartidas().get(0).getClubeVencedor();			
			clubeRanking = rankings.get(campeao);
			clubeRanking.setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacaoCampeao(c.getNivelCampeonato()));
			
			//Fase Grupos
			clubesFaseGrupos.removeAll(clubeRodada.keySet());
			clubesFaseGrupos.remove(campeao);
			
			for (Clube cl : clubesFaseGrupos) {
				clubeRanking = rankings.get(cl);
				clubeRanking.setClassificacaoContinental(ClassificacaoContinentalFinal.getClassificacaoFaseGrupo(c.getNivelCampeonato()));
			}
		}
	}

}
