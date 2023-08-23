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

public class CampeonatoEliminatorioFactoryImplVinteEOitoClubes extends CampeonatoEliminatorioFactory {
	
	/*
	 * CN: 16 -> 8 -> (4' + 12) -> 8 -> 4 -> 2 [6 RODADAS]
	 */

	/*@Override
	public CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		CampeonatoEliminatorio campeonato = new CampeonatoEliminatorio();
		campeonato.setLiga(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setTemporada(temporada);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<RodadaEliminatoria> rodadas = gerarRodadasCopaNacionalII(clubes, campeonato);
		
		campeonato.setRodadas(rodadas);
		
		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}*/

	@Override
	protected List<RodadaEliminatoria> gerarRodadasCopaNacionalII(List<ClubeRanking> clubes, CampeonatoEliminatorio campeonatoEliminatorio) {

		RodadaEliminatoria rodadaEliminatoria = null, rodadaAnterior;
		List<PartidaEliminatoriaResultado> partidasRodada = null, partidasRodadaAnterior = null;
		PartidaEliminatoriaResultado partidaEliminatoria = null;
		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();

		//29 a 32
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 28 && c.getPosicaoGeral() <= 32).collect(Collectors.toList());

		int nroJogos1Fase = 8, nroJogos2Fase = 4;
		
		//Fase 1
		//16 Clubes
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(1);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos1Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(i < (nroJogos1Fase/2));
			partidaEliminatoria.setClubeMandante(null);//Sera preenchido por Promotor
			if (i < (nroJogos1Fase/2)) {
				partidaEliminatoria.setClubeVisitante(clubes1Fase.get(i).getClube());
			} else {
				partidaEliminatoria.setClubeVisitante(null);//Sera preenchido por Promotor
			}
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;

		int numJogos = nroJogos2Fase;

		rodadas.addAll(gerarRodadas(2, 4, numJogos,	partidasRodadaAnterior, rodadaAnterior, campeonatoEliminatorio, null));

		return rodadas;
	}

	/*@Override
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
	}*/
	
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
		
		//13 a 28
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 12 && c.getPosicaoGeral() <= 28).collect(Collectors.toList());
		//1 a 12
		List<ClubeRanking> clubes3Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() <= 12).collect(Collectors.toList());
		
		int nroJogos1Fase = 8, nroJogos2Fase = 4, nroJogos3Fase = 8;
		
		//Fase 1
		//16 Clubes
		rodadaEliminatoria = new RodadaEliminatoria();
		rodadaEliminatoria.setNumero(1);
		rodadaEliminatoria.setCampeonatoEliminatorio(campeonatoEliminatorio);
		partidasRodada = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (int i = 0; i < nroJogos1Fase; i++) {
			partidaEliminatoria = new PartidaEliminatoriaResultado();
			partidaEliminatoria.setRodada(rodadaEliminatoria);
			partidaEliminatoria.setClassificaAMandante(i < (nroJogos1Fase/2));
			partidaEliminatoria.setClubeMandante(clubes1Fase.get(i).getClube());
			partidaEliminatoria.setClubeVisitante(clubes1Fase.get(clubes1Fase.size() - i - 1).getClube());
			partidasRodada.add(partidaEliminatoria);
		}
		
		rodadaEliminatoria.setPartidas(partidasRodada);
		rodadas.add(rodadaEliminatoria);
		
		partidasRodadaAnterior = partidasRodada;
		rodadaAnterior = rodadaEliminatoria;

		//Fase 2
		//8 Clubes
		
		rodadaEliminatoria = gerarRodadas(2, 2, nroJogos2Fase, partidasRodadaAnterior, rodadaAnterior, campeonatoEliminatorio, null).get(0);
		rodadas.add(rodadaEliminatoria);
		
		for (PartidaEliminatoriaResultado p : rodadaEliminatoria.getPartidas()) {
			p.setClassificaAMandante(false);
		}

		rodadaAnterior.setProximaRodada(rodadaEliminatoria);
		partidasRodadaAnterior = rodadaEliminatoria.getPartidas();
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
			if (i < (nroJogos3Fase/2)) {
				partidasRodadaAnterior.get(nroJogos2Fase - i - 1).setProximaPartida(partidaEliminatoria);
			} else {
				int j = clubes3Fase.size() + (nroJogos3Fase/2) - i - 1;
				partidaEliminatoria.setClubeVisitante(clubes3Fase.get(j).getClube());
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
