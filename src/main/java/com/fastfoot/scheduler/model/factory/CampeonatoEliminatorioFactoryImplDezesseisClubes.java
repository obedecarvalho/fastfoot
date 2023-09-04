package com.fastfoot.scheduler.model.factory;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoEliminatorioFactoryImplDezesseisClubes extends CampeonatoEliminatorioFactory {

	@Override
	public CampeonatoEliminatorio criarCampeonatoCopaNacionalII(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		//17 a 32
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 16 && c.getPosicaoGeral() <= 32).collect(Collectors.toList());

		CampeonatoEliminatorio campeonato = criarCampeonato(temporada, liga, clubes1Fase, nivelCampeonato);

		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}
	
	@Override
	protected List<RodadaEliminatoria> gerarRodadasCopaNacionalII(List<ClubeRanking> clubes,
			CampeonatoEliminatorio campeonatoEliminatorio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CampeonatoEliminatorio criarCampeonatoCopaNacional(Temporada temporada, LigaJogo liga, List<ClubeRanking> clubes, NivelCampeonato nivelCampeonato) {
		//1 a 16
		List<ClubeRanking> clubes1Fase =  clubes.stream().filter(c -> c.getPosicaoGeral() > 0 && c.getPosicaoGeral() <= 16).collect(Collectors.toList());

		CampeonatoEliminatorio campeonato = criarCampeonato(temporada, liga, clubes1Fase, nivelCampeonato);

		SemanaUtil.associarRodadaCopaNacionalQuatroRodadasSemana(campeonato);

		return campeonato;
	}

	@Override
	protected List<RodadaEliminatoria> gerarRodadasCopaNacional(List<ClubeRanking> clubes,
			CampeonatoEliminatorio campeonatoEliminatorio) {
		// TODO Auto-generated method stub
		return null;
	}
}
