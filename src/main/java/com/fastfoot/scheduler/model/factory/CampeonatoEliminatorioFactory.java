package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoEliminatorioFactory {

	public static CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, Liga liga, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		//campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadasCopaNacionalII(campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalIISemana(campeonato);

		return campeonato;
	}

	public static List<RodadaEliminatoria> gerarRodadasCopaNacionalII(CampeonatoEliminatorio campeonato) {
		int numeroRodadas = numeroRodadas(Constantes.NRO_CLUBES_CP_NAC_II);
		int numJogos = numeroRodadas*2;

		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadas(1, 4, numJogos,	null, null, campeonato, null);

		return rodadas;
	}

	public static CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		//campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadasCopaNacional(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalSemana(campeonato);

		return campeonato;
	}

	public static List<RodadaEliminatoria> gerarRodadasCopaNacional(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {
		
		RodadaEliminatoria rodadaEliminatoria = null, rodadaAnterior;
		List<PartidaEliminatoriaResultado> partidasRodada = null, partidasRodadaAnterior = null;
		PartidaEliminatoriaResultado partidaEliminatoria = null;
		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();
		
		//17 a 32
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 16).collect(Collectors.toList());
		//9 a 16
		List<ClubeRanking> clubes2Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 8 && c.getPosicaoGeral() <= 16).collect(Collectors.toList());
		//1 a 8
		List<ClubeRanking> clubes3Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() <= 8).collect(Collectors.toList());
		
		int nroJogos1Fase = 8, nroJogos2Fase = 8, nroJogos3Fase = 8;
		
		//Fase 1
		//16 Clubes
		
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(1);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos1Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(false);
			partidaEliminatoria.setClubeMandante(clubes1Fase.get(i).getClube());
			partidaEliminatoria.setClubeVisitante(clubes1Fase.get(clubes1Fase.size() - i - 1).getClube());
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;
		
		//Fase 2
		//16 Clubes
		
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(2);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos2Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(false);
			partidaEliminatoria.setClubeMandante(clubes2Fase.get(i).getClube());
			partidasRodadaAnterior.get(nroJogos1Fase - i - 1).setProximaPartida(partidaEliminatoria);
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		rodadaAnterior.setProximaRodada(rodadaEliminatoria);
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;
		
		//Fase 3
		//16 Clubes
		
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(3);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos3Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(i < (nroJogos3Fase/2));
			partidaEliminatoria.setClubeMandante(clubes3Fase.get(i).getClube());
			partidasRodadaAnterior.get(nroJogos1Fase - i - 1).setProximaPartida(partidaEliminatoria);
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		rodadaAnterior.setProximaRodada(rodadaEliminatoria);
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;
		
		//Outras fases - 4 a 6
		
		int numJogos = nroJogos3Fase/2;
		
		rodadas.addAll(CampeonatoEliminatorioFactory.gerarRodadas(4, Constantes.NRO_RODADAS_CP_NAC, numJogos, partidasRodadaAnterior, rodadaAnterior, campeonatoEliminatorio, null));

		return rodadas;
	}
	
	public static CampeonatoEliminatorio criarCampeonato(Temporada temporada, Liga liga, List<Clube> clubes) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		//campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		
		List<RodadaEliminatoria> rodadas = CampeonatoEliminatorioFactory.gerarRodadas(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);

		return campeonato;
	}

	public static List<RodadaEliminatoria> gerarRodadas(List<Clube> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {
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
			partidaEliminatoria.setClubeMandante(clubes.get(j));
			partidaEliminatoria.setClubeVisitante(clubes.get(2*numJogos - j - 1));
			partidas.add(partidaEliminatoria);
		}

		rodadaEliminatoria.setPartidas(partidas);
		rodadas.add(rodadaEliminatoria);
		partidasAnterior = partidas;
		rodadaAnterior = rodadaEliminatoria;
		
		numJogos = numJogos/2;
		rodadas.addAll(CampeonatoEliminatorioFactory.gerarRodadas(2, numeroRodadas, numJogos, partidasAnterior, rodadaAnterior, campeonatoEliminatorio, null));

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
