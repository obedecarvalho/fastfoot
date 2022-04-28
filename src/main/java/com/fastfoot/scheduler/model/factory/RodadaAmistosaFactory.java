package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class RodadaAmistosaFactory {
	
	public static List<RodadaAmistosa> criarRodadasAmistosas(Temporada temporada, Map<Liga, List<Clube>> clubes) {
		List<RodadaAmistosa> rodadasGeral = gerarRodadas(clubes);
		SemanaUtil.associarRodadaAmistosaSemana(temporada, rodadasGeral);
		return rodadasGeral;
	}

	private static List<RodadaAmistosa> gerarRodadas(Map<Liga, List<Clube>> clubes) {//Esperado 16 clubes
		Set<Liga> ligas = clubes.keySet();
		
		List<Clube> clubesGrupo = new ArrayList<Clube>();
		
		List<RodadaAmistosa> rodadasGeral = new ArrayList<RodadaAmistosa>();
		
		for (int i = 0; i < Constantes.NRO_GRUPOS_AMISTOSOS; i++) {
			
			int j = 0, pos = 0;
			for (Liga l : ligas) {
				pos = (i+j)%Constantes.NRO_CLUBES_GRUPOS_AMISTOSOS;
				clubesGrupo.add(clubes.get(l).get(pos));
				j++;
			}

			rodadasGeral.addAll(gerarRodadas(clubesGrupo, 101));

			clubesGrupo.clear();
		}

		return rodadasGeral;
	}

	public static List<RodadaAmistosa> gerarRodadas(List<Clube> clubes, Integer nroRodadaInicial) {
		int nTimes = clubes.size();
		
		int nRodadas = nTimes-1, nPartidas = (int) nTimes/2;
		
		List<Clube> tmp1 = clubes.subList(0, nPartidas);
		List<Clube> tmp2 = clubes.subList(nPartidas, nTimes);
		
		Clube m, v, aux;
		
		PartidaAmistosaResultado p;
		
		RodadaAmistosa r;
		
		List<PartidaAmistosaResultado> partidasRodada = null;
		List<RodadaAmistosa> rodadas = new ArrayList<RodadaAmistosa>();
		
		for (int i=0; i<nRodadas; i++) {
			
			r = new RodadaAmistosa();
			r.setNumero(nroRodadaInicial++);
			
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
			
			r.setPartidas(partidasRodada);
			rodadas.add(r);
			
			
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
		
		return rodadas;		
	}
}
