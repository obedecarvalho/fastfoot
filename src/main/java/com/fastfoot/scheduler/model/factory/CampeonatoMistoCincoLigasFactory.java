package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoMistoCincoLigasFactory {
	
	public static CampeonatoMisto criarCampeonato(Temporada temporada, Map<Liga, List<Clube>> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoMisto campeonato = new CampeonatoMisto();
		campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<GrupoCampeonato> grupos = gerarRodadasGrupo(clubes, campeonato);
		List<RodadaEliminatoria> eliminatorias = gerarRodadas(campeonato);
		
		campeonato.setGrupos(grupos);
		campeonato.setRodadasEliminatorias(eliminatorias);
		
		SemanaUtil.associarRodadaContinentalSemana(campeonato);
		
		return campeonato;
	}

	private static List<GrupoCampeonato> gerarRodadasGrupo(Map<Liga, List<Clube>> clubes, CampeonatoMisto campeonato) {
		List<Liga> ligas = new ArrayList<Liga>(clubes.keySet());
		
		List<Clube> clubesGrupo = new ArrayList<Clube>();
		List<GrupoCampeonato> grupoCampeonatos = new ArrayList<GrupoCampeonato>();
		GrupoCampeonato grupoCampeonato = null;
		List<Clube> clubesLiga = null;
		
		int nroGruposCont = Constantes.NRO_GRUPOS_CONT;
		//int nroGruposCont = clubes.get(ligas.toArray()[0]).size();
		
		Queue<Clube> clubeQ = new LinkedList<Clube>(clubes.get(ligas.get(Constantes.NRO_CLUBES_GRUPOS)));

		for (int i = 0; i < nroGruposCont; i++) {
			
			/*int j = 0, pos = 0;
			for (Liga l : ligas) {
				pos = (i + j) % nroGruposCont;
				clubesGrupo.add(clubes.get(l).get(pos));
				j++;
			}*/

			//
			int pos = 0;
			for (int j = 0; j < Constantes.NRO_CLUBES_GRUPOS; j++) {
				Liga l = ligas.get(j);

				clubesLiga = clubes.get(l);
				
				pos = (i + j) % nroGruposCont;
				
				if (pos < (Constantes.NRO_CLUBES_GRUPOS - 1)) {
					clubesGrupo.add(clubesLiga.get(pos));
				} else if (pos == (Constantes.NRO_CLUBES_GRUPOS - 1)) {
					if (clubesLiga.size() == Constantes.NRO_CLUBES_GRUPOS) {
						clubesGrupo.add(clubesLiga.get(pos));
					} else {
						/*clubesLiga = clubes.get(ligas.get(Constantes.NRO_CLUBES_GRUPOS));
						clubesGrupo.add(clubesLiga.get(i));*/
						clubesGrupo.add(clubeQ.poll());
					}
				}
			}
			//
			
			grupoCampeonato = new GrupoCampeonato();
			grupoCampeonato.setCampeonato(campeonato);
			grupoCampeonato.setNumero(i + 1);
			
			List<Rodada> rodadas = CampeonatoFactory.gerarRodadas(clubesGrupo, null, grupoCampeonato);
			List<Classificacao> classificacao = ClassificacaoFactory.gerarClassificacaoInicial(clubesGrupo, null, grupoCampeonato);
			
			grupoCampeonato.setRodadas(rodadas);
			grupoCampeonato.setClassificacao(classificacao);
			
			grupoCampeonatos.add(grupoCampeonato);
			
			clubesGrupo.clear();
		}

		return grupoCampeonatos;
	}

	private static List<RodadaEliminatoria> gerarRodadas(CampeonatoMisto campeonato) {
		int numeroRodadas = numeroRodadas(Constantes.NRO_CLUBES_FASE_FINAL), numJogos = Constantes.NRO_CLUBES_FASE_FINAL/2;
		RodadaEliminatoria rodadaAnterior = null;
		List<PartidaEliminatoriaResultado> partidasAnterior = null;

		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadas(
				Constantes.NRO_PARTIDAS_FASE_GRUPOS + 1, Constantes.NRO_PARTIDAS_FASE_GRUPOS + numeroRodadas, numJogos,
				partidasAnterior, rodadaAnterior, null, campeonato);

		return rodadas;
	}

	private static int numeroRodadas(int n) {
		int i = 0;
		while (n > 1) {
			i++;
			n = n/2;
		}
		return i;
	}
}
