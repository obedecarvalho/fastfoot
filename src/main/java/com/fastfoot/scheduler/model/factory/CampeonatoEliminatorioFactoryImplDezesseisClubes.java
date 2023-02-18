package com.fastfoot.scheduler.model.factory;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoEliminatorioFactoryImplDezesseisClubes extends CampeonatoEliminatorioFactory {

	public CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		//17 a 32
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 16 && c.getPosicaoGeral() <= 32).collect(Collectors.toList());

		CampeonatoEliminatorio campeonato = criarCampeonato(temporada, liga, clubes1Fase, nivelCampeonato);

		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}

	public CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, Liga liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		//1 a 16
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 0 && c.getPosicaoGeral() <= 16).collect(Collectors.toList());

		CampeonatoEliminatorio campeonato = criarCampeonato(temporada, liga, clubes1Fase, nivelCampeonato);

		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}

}
