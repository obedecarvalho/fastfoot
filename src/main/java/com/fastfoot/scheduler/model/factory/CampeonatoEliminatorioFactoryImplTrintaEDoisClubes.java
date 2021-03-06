package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

/**
 * 
 * @author obede
 *
 */
public class CampeonatoEliminatorioFactoryImplTrintaEDoisClubes extends CampeonatoEliminatorioFactory {//CampeonatoEliminatorio

	public CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<RodadaEliminatoria> rodadas = gerarRodadasCopaNacionalII(campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}

	private List<RodadaEliminatoria> gerarRodadasCopaNacionalII(CampeonatoEliminatorio campeonato) {
		int numeroRodadas = numeroRodadas(Constantes.NRO_CLUBES_CP_NAC_II);
		int numJogos = numeroRodadas*2;

		List<RodadaEliminatoria> rodadas = gerarRodadas(1, 4, numJogos,	null, null, campeonato, null);

		return rodadas;
	}

	public CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<RodadaEliminatoria> rodadas = gerarRodadasCopaNacional(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalSeisRodadasSemana(campeonato);

		return campeonato;
	}

	private List<RodadaEliminatoria> gerarRodadasCopaNacional(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {
		
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
			partidasRodadaAnterior.get(nroJogos2Fase - i - 1).setProximaPartida(partidaEliminatoria);
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		rodadaAnterior.setProximaRodada(rodadaEliminatoria);
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;
		
		//Outras fases - 4 a 6
		
		int numJogos = nroJogos3Fase/2;
		
		rodadas.addAll(gerarRodadas(4, Constantes.NRO_RODADAS_CP_NAC, numJogos, partidasRodadaAnterior, rodadaAnterior, campeonatoEliminatorio, null));

		return rodadas;
	}

}
