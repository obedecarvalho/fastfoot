package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public abstract class CampeonatoEliminatorioFactory {

	public abstract CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes);

	//public abstract CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato);
	
	protected abstract List<RodadaEliminatoria> gerarRodadasCopaNacionalII(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio);
	
	protected abstract List<RodadaEliminatoria> gerarRodadasCopaNacional(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio);
	
	public CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLigaJogo(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTotalRodadas(numeroRodadas(Constantes.NRO_CLUBES_CP_NAC_II));
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(NivelCampeonato.COPA_NACIONAL_II);
		campeonato.setNome(String.format("%s %s %d", liga.getLiga().name(), NivelCampeonato.COPA_NACIONAL_II.name(), temporada.getAno()));
		
		List<RodadaEliminatoria> rodadas = gerarRodadasCopaNacionalII(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}
	
	public CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes, int totalRodadas) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLigaJogo(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTotalRodadas(totalRodadas);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(NivelCampeonato.COPA_NACIONAL);
		campeonato.setNome(String.format("%s %s %d", liga.getLiga().name(), NivelCampeonato.COPA_NACIONAL.name(), temporada.getAno()));
		
		List<RodadaEliminatoria> rodadas = gerarRodadasCopaNacional(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);

		return campeonato;
	}

	public CampeonatoEliminatorio criarCampeonato(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLigaJogo(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTotalRodadas(numeroRodadas(clubes.size()));
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		campeonato.setNome(String.format("%s %s %d", liga.getLiga().name(), nivelCampeonato.name(), temporada.getAno()));

		List<RodadaEliminatoria> rodadas = gerarRodadas(clubes, campeonato);

		campeonato.setRodadas(rodadas);

		return campeonato;
	}

	private List<RodadaEliminatoria> gerarRodadas(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {
		int numeroRodadas = numeroRodadas(clubes.size()), numJogos = clubes.size()/2;
		RodadaEliminatoria rodadaEliminatoria, rodadaAnterior = null;
		PartidaEliminatoriaResultado partidaEliminatoria;
		List<PartidaEliminatoriaResultado> partidas = null;
		List<PartidaEliminatoriaResultado> partidasAnterior = null;
		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();
		
		
		//Rodada Inicial
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(1);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);

		partidas = new ArrayList<PartidaEliminatoriaResultado>();

		for (int j = 0; j < numJogos; j++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(j < (numJogos/2));
			partidaEliminatoria.setClubeMandante(clubes.get(j).getClube());
			partidaEliminatoria.setClubeVisitante(clubes.get(2*numJogos - j - 1).getClube());
			partidas.add(partidaEliminatoria);
		}

		rodadaEliminatoria.setPartidas(partidas);
		rodadas.add(rodadaEliminatoria);
		partidasAnterior = partidas;
		rodadaAnterior = rodadaEliminatoria;
		
		numJogos = numJogos/2;
		rodadas.addAll(gerarRodadas(2, numeroRodadas, numJogos, partidasAnterior, rodadaAnterior, campeonatoEliminatorio, null));

		return rodadas;
	}
	
	public int numeroRodadas(int n) {
		int i = 0;
		while (n > 1) {
			i++;
			n = n/2;
		}
		return i;
	}

	/**
	 * 
	 * @param rodadaInicial
	 * @param rodadaFinal
	 * @param numJogos
	 * @param partidasAnterior
	 * @param rodadaAnterior
	 * @return 
	 */
	public static List<RodadaEliminatoria> gerarRodadas(Integer rodadaInicial, Integer rodadaFinal, Integer numJogos,
			List<PartidaEliminatoriaResultado> partidasAnterior, RodadaEliminatoria rodadaAnterior,
			CampeonatoEliminatorio campeonatoEliminatorio, CampeonatoMisto campeonatoMisto) {
		
		RodadaEliminatoria rodadaEliminatoria = null;
		List<PartidaEliminatoriaResultado> partidas = null;
		PartidaEliminatoriaResultado partidaEliminatoria = null;
		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();
		
		for (int i = rodadaInicial; i <= rodadaFinal; i++) {
			rodadaEliminatoria = new RodadaEliminatoria();
			rodadaEliminatoria.setNumero(i);
			rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
			rodadaEliminatoria.setCampeonatoMisto(campeonatoMisto);
			
			partidas = new ArrayList<PartidaEliminatoriaResultado>();
			
			for (int j = 0; j < numJogos; j++) {
				partidaEliminatoria = new PartidaEliminatoriaResultado();
				partidaEliminatoria.setRodada(rodadaEliminatoria);
				partidaEliminatoria.setClassificaAMandante(j < (numJogos/2));
				if (partidasAnterior != null) {
					partidasAnterior.get(j).setProximaPartida(partidaEliminatoria);
					partidasAnterior.get(numJogos*2 - j - 1).setProximaPartida(partidaEliminatoria);
				}

				partidas.add(partidaEliminatoria);
			}
			
			rodadaEliminatoria.setPartidas(partidas);
			if (rodadaAnterior != null) {
				rodadaAnterior.setProximaRodada(rodadaEliminatoria);
			}

			rodadas.add(rodadaEliminatoria);

			partidasAnterior = partidas;
			rodadaAnterior = rodadaEliminatoria;

			numJogos = numJogos/2;
		}
		
		return rodadas;
	}
}
