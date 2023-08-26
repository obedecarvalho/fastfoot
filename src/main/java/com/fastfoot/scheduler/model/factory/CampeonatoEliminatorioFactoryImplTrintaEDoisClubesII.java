package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoEliminatorioFactoryImplTrintaEDoisClubesII extends CampeonatoEliminatorioFactory {
	
	/*
	 * CN: 20 -> (10' + 2) -> (6' + 10) -> 8 -> 4 -> 2 [6 RODADAS]
	 */

	@Override
	protected List<RodadaEliminatoria> gerarRodadasCopaNacionalII(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonato) {
		int numeroRodadas = numeroRodadas(Constantes.NRO_CLUBES_CP_NAC_II);
		int numJogos = numeroRodadas*2;

		List<RodadaEliminatoria> rodadas = gerarRodadas(1, 4, numJogos,	null, null, campeonato, null);

		return rodadas;
	}

	@Override
	public CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {

		CampeonatoEliminatorio campeonato = super.criarCampeonatoCopaNacional(temporada, liga, clubes, nivelCampeonato);

		SemanaUtil.associarRodadaCopaNacionalSeisRodadasSemana(campeonato);

		return campeonato;
	}

	@Override
	protected List<RodadaEliminatoria> gerarRodadasCopaNacional(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {
		
		RodadaEliminatoria rodadaEliminatoria = null, rodadaAnterior;
		List<PartidaEliminatoriaResultado> partidasRodada = null, partidasRodadaAnterior = null;
		PartidaEliminatoriaResultado partidaEliminatoria = null;
		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();
		
		//13 a 32
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 12).collect(Collectors.toList());
		//11 a 12
		List<ClubeRanking> clubes2Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 10 && c.getPosicaoGeral() <= 12).collect(Collectors.toList());
		//1 a 10
		List<ClubeRanking> clubes3Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() <= 10).collect(Collectors.toList());
		
		int nroJogos1Fase = 10, nroJogos2Fase = 6, nroJogos3Fase = 8;
		
		//Fase 1
		//16 Clubes
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(1);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos1Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(i < 4);//Apenas 4 jogos classificam a mandante
			partidaEliminatoria.setClubeMandante(clubes1Fase.get(i).getClube());
			partidaEliminatoria.setClubeVisitante(clubes1Fase.get(clubes1Fase.size() - i - 1).getClube());
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;

		//Fase 2
		//12 Clubes
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(2);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos2Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(false);
			if (i < 2) {
				partidaEliminatoria.setClubeMandante(clubes2Fase.get(i).getClube());
			} else {
				partidasRodadaAnterior.get(i - 2).setProximaPartida(partidaEliminatoria);//Mandante
			}
			partidasRodadaAnterior.get(nroJogos1Fase - i - 1).setProximaPartida(partidaEliminatoria);//Visitante
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
			if (i < 6) {
				partidasRodadaAnterior.get(i).setProximaPartida(partidaEliminatoria);
			} else {
				//{6, 8} e {7, 9}
				partidaEliminatoria.setClubeVisitante(clubes3Fase.get(i + 2).getClube());
			}
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
