package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class RodadaAmistosaFactory {

	public static List<RodadaAmistosa> criarRodadasAmistosasAgrupaGrupo(Temporada temporada, Map<Liga, List<Clube>> clubes) {
		
		int nroCompeticoes = Double.valueOf(Math.ceil(clubes.get(Liga.GENEBE).size()/Constantes.NRO_CLUBES_POR_LIGA_CONT.doubleValue())).intValue();
		
		Map<Liga, List<Clube>> clubesGrupo = new HashMap<Liga, List<Clube>>();
		List<RodadaAmistosa> rodadasGeral = new ArrayList<RodadaAmistosa>();
		List<RodadaAmistosa> rodadas = null;
		
		for (int i = 0; i < nroCompeticoes; i++) {
			
			int posInicial = i * Constantes.NRO_CLUBES_POR_LIGA_CONT;
			int posFinal = Math.min((i + 1) * Constantes.NRO_CLUBES_POR_LIGA_CONT, clubes.get(Liga.ENGLND).size());
			
			for (Liga l : clubes.keySet()) {
				clubesGrupo.put(l, clubes.get(l).subList(posInicial, posFinal));
			}
			rodadas = Arrays.asList(new RodadaAmistosa(101, NivelCampeonato.AMISTOSO_INTERNACIONAL),
					new RodadaAmistosa(102, NivelCampeonato.AMISTOSO_INTERNACIONAL),
					new RodadaAmistosa(103, NivelCampeonato.AMISTOSO_INTERNACIONAL));
			gerarPartidasGrupos(clubesGrupo, rodadas);
			rodadasGeral.addAll(rodadas);
			clubesGrupo.clear();
		}
		SemanaUtil.associarRodadaAmistosaSemana(temporada, rodadasGeral);
		return rodadasGeral;
	}
	
	
	
	private static void gerarPartidasGrupos(Map<Liga, List<Clube>> clubes, List<RodadaAmistosa> rodadas) {//Esperado 16 clubes
		Set<Liga> ligas = clubes.keySet();
		
		List<Clube> clubesGrupo = new ArrayList<Clube>();
		
		boolean reduzido = clubes.get(ligas.toArray()[0]).size() == 2;
		int numGrupos = !reduzido ? Constantes.NRO_GRUPOS_AMISTOSOS : 2;
		int numClubes = !reduzido ? Constantes.NRO_CLUBES_GRUPOS_AMISTOSOS : 2;
		
		for (int i = 0; i < numGrupos; i++) {
			
			int j = 0, pos = 0;
			for (Liga l : ligas) {
				pos = (i + j) % numClubes;
				clubesGrupo.add(clubes.get(l).get(pos));
				j++;
			}

			gerarPartidasRodadas(clubesGrupo, rodadas);

			clubesGrupo.clear();
		}

	}

	private static void gerarPartidasRodadas(List<Clube> clubes, List<RodadaAmistosa> rodadas) {
		int nTimes = clubes.size();
		
		int nRodadas = rodadas.size(); //nTimes-1;
		int nPartidas = (int) nTimes/2;
		
		List<Clube> tmp1 = clubes.subList(0, nPartidas);
		List<Clube> tmp2 = clubes.subList(nPartidas, nTimes);
		
		Clube m, v, aux;
		
		PartidaAmistosaResultado p;
		
		RodadaAmistosa r;
		
		List<PartidaAmistosaResultado> partidasRodada = null;
		
		for (int i=0; i<nRodadas; i++) {
			
			r = rodadas.get(i);
			
			partidasRodada = new ArrayList<PartidaAmistosaResultado>();
			
			for (int j = 0; j < nPartidas; j++) {
				m = tmp1.get(j);
				v = tmp2.get(j);
				p = new PartidaAmistosaResultado();
				
				if (i%2 == 1 && j == 0) {
					p.setClubeMandante(v);
					p.setClubeVisitante(m);
				} else if (j%2==0) {
					p.setClubeMandante(m);
					p.setClubeVisitante(v);
				} else {
					p.setClubeMandante(v);
					p.setClubeVisitante(m);
				}
				p.setRodada(r);
				partidasRodada.add(p);
			}

			r.addAllPartidas(partidasRodada);
			
			//Alterando ordem
			aux = tmp2.get(0);
			
			for (int j = 0; j < (nPartidas - 1); j++) {
				tmp2.set(j, tmp2.get(j+1));
			}
			
			tmp2.set((nPartidas - 1), tmp1.get(nPartidas - 1));
			
			for (int j = (nPartidas - 1); j > 1; j--) {
				tmp1.set(j, tmp1.get(j-1));
			}
			
			tmp1.set(1, aux);

		}
	}
}
