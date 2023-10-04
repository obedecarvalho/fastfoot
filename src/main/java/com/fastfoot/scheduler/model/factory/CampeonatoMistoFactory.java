package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class CampeonatoMistoFactory {
	
	/*
	 * * Criar variação para 3ro do grupo no CI se classifique para quarta de final do CII 
	 */
	
	public static CampeonatoMisto criarCampeonato(Temporada temporada, Map<Liga, List<Clube>> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoMisto campeonato = new CampeonatoMisto();
		campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		campeonato.setNome(String.format("%s %d", nivelCampeonato.name(), temporada.getAno()));
		
		List<GrupoCampeonato> grupos = CampeonatoMistoFactory.gerarRodadasGrupo(clubes, campeonato);
		List<RodadaEliminatoria> eliminatorias = CampeonatoMistoFactory.gerarRodadas(campeonato);
		
		campeonato.setGrupos(grupos);
		campeonato.setRodadasEliminatorias(eliminatorias);
		
		SemanaUtil.associarRodadaContinentalSemana(campeonato);
		
		return campeonato;
	}

	private static List<GrupoCampeonato> gerarRodadasGrupo(Map<Liga, List<Clube>> clubes, CampeonatoMisto campeonato) {
		Set<Liga> ligas = clubes.keySet();
		
		List<Clube> clubesGrupo = new ArrayList<Clube>();
		List<GrupoCampeonato> grupoCampeonatos = new ArrayList<GrupoCampeonato>();
		GrupoCampeonato grupoCampeonato = null;
		
		//int nroGruposCont = Constantes.NRO_GRUPOS_CONT;
		int nroGruposCont = clubes.get(ligas.toArray()[0]).size();

		for (int i = 0; i < nroGruposCont; i++) {
			
			int j = 0, pos = 0;
			for (Liga l : ligas) {
				pos = (i + j) % nroGruposCont;
				clubesGrupo.add(clubes.get(l).get(pos));
				j++;
			}
			
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
