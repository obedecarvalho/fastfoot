package com.fastfoot.scheduler.model.factory;

import java.util.List;
import java.util.Map;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public abstract class CampeonatoMistoFactory {
	
	public CampeonatoMisto criarCampeonato(Temporada temporada, Map<Liga, List<Clube>> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoMisto campeonato = new CampeonatoMisto();
		campeonato.setRodadaAtual(0);
		campeonato.setTotalRodadas(Constantes.NRO_RODADAS_CONTINENTAL);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		campeonato.setNome(String.format("%s %d", nivelCampeonato.name(), temporada.getAno()));
		
		List<GrupoCampeonato> grupos = gerarRodadasFaseInicial(clubes, campeonato);
		List<RodadaEliminatoria> eliminatorias = gerarRodadas(campeonato);
		
		campeonato.setGrupos(grupos);
		campeonato.setRodadasEliminatorias(eliminatorias);
		
		SemanaUtil.associarRodadaContinentalSemana(campeonato);
		
		return campeonato;
	}
	
	protected abstract List<GrupoCampeonato> gerarRodadasFaseInicial(Map<Liga, List<Clube>> clubes, CampeonatoMisto campeonato);

	protected List<RodadaEliminatoria> gerarRodadas(CampeonatoMisto campeonato) {
		int numeroRodadas = numeroRodadas(Constantes.NRO_CLUBES_FASE_FINAL), numJogos = Constantes.NRO_CLUBES_FASE_FINAL/2;
		RodadaEliminatoria rodadaAnterior = null;
		List<PartidaEliminatoriaResultado> partidasAnterior = null;

		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadas(
				Constantes.NRO_PARTIDAS_FASE_GRUPOS + 1, Constantes.NRO_PARTIDAS_FASE_GRUPOS + numeroRodadas, numJogos,
				partidasAnterior, rodadaAnterior, null, campeonato);

		return rodadas;
	}

	protected int numeroRodadas(int n) {
		int i = 0;
		while (n > 1) {
			i++;
			n = n/2;
		}
		return i;
	}

}
