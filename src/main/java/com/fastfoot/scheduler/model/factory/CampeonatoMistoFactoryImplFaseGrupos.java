package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Rodada;

public class CampeonatoMistoFactoryImplFaseGrupos extends CampeonatoMistoFactory {
	
	@Override
	protected List<GrupoCampeonato> gerarRodadasFaseInicial(Map<Liga, List<Clube>> clubes, CampeonatoMisto campeonato) {
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

}
