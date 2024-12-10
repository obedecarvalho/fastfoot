package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;

public class CampeonatoMistoFactoryImplFaseLiga extends CampeonatoMistoFactory {

	@Override
	protected List<GrupoCampeonato> gerarRodadasFaseInicial(Map<Liga, List<Clube>> clubes, CampeonatoMisto campeonato) {
		Set<Liga> ligas = clubes.keySet();
		
		List<Clube> clubesGrupo = new ArrayList<Clube>();
		/*
		List<GrupoCampeonato> grupoCampeonatos = new ArrayList<GrupoCampeonato>();
		GrupoCampeonato grupoCampeonato = null;
		*/
		GrupoCampeonato grupoCampeonato = new GrupoCampeonato();
		grupoCampeonato.setCampeonato(campeonato);
		grupoCampeonato.setNumero(0);
		grupoCampeonato.setRodadas(new ArrayList<Rodada>());
		grupoCampeonato.setClassificacao(new ArrayList<Classificacao>());
		//Rodadas Fase Liga
		Rodada r;
		for (int i = 0; i < Constantes.NRO_RODADAS_FASE_LIGA; i++) {
			r = new Rodada();
			r.setNumero(i+1);
			r.setGrupoCampeonato(grupoCampeonato);
			r.setPartidas(new ArrayList<PartidaResultado>());
			grupoCampeonato.getRodadas().add(r);
		}
		
		//int nroGruposCont = Constantes.NRO_GRUPOS_CONT;
		int nroGruposCont = clubes.get(ligas.toArray()[0]).size();

		for (int i = 0; i < nroGruposCont; i++) {
			
			int j = 0, pos = 0;
			for (Liga l : ligas) {
				pos = (i + j) % nroGruposCont;
				clubesGrupo.add(clubes.get(l).get(pos));
				j++;
			}
			
			/*
			grupoCampeonato = new GrupoCampeonato();
			grupoCampeonato.setCampeonato(campeonato);
			grupoCampeonato.setNumero(i + 1);
			*/
			
			/*
			List<Rodada> rodadas = CampeonatoFactory.gerarRodadas(clubesGrupo, null, grupoCampeonato);
			List<Classificacao> classificacao = ClassificacaoFactory.gerarClassificacaoInicial(clubesGrupo, null, grupoCampeonato);
			*/
			
			gerarPartidasFaseLiga(clubesGrupo, grupoCampeonato);
			grupoCampeonato.getClassificacao().addAll(ClassificacaoFactory.gerarClassificacaoInicial(clubesGrupo, null, grupoCampeonato));
			
			/*
			grupoCampeonato.setRodadas(rodadas);
			grupoCampeonato.setClassificacao(classificacao);
			*/
			
			//grupoCampeonatos.add(grupoCampeonato);
			
			clubesGrupo.clear();
		}

		//return grupoCampeonatos;
		return Arrays.asList(grupoCampeonato);
	}

	private void gerarPartidasFaseLiga(List<Clube> clubes, GrupoCampeonato grupoCampeonato) {
		int nTimes = clubes.size();
		
		int nRodadas = nTimes-1, nPartidas = (int) nTimes/2;
		
		List<Clube> tmp1 = clubes.subList(0, nPartidas);
		List<Clube> tmp2 = clubes.subList(nPartidas, nTimes);
		
		Clube m, v, aux;
		
		PartidaResultado p;
		
		Rodada r;
		
		//List<PartidaResultado> partidasRodada = null;
		//List<Rodada> rodadas = new ArrayList<Rodada>();
		
		for (int i=0; i<nRodadas; i++) {
			
			/*
			r = new Rodada();
			r.setNumero(i+1);
			r.setCampeonato(campeonato);
			r.setGrupoCampeonato(grupoCampeonato);
			*/
			r = grupoCampeonato.getRodadas().get(i);
			
			//partidasRodada = new ArrayList<PartidaResultado>();
			
			for (int j = 0; j < nPartidas; j++) {
				m = tmp1.get(j);
				v = tmp2.get(j);
				p = new PartidaResultado();
				
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
				//partidasRodada.add(p);
				r.getPartidas().add(p);
			}
			
			//r.setPartidas(partidasRodada);
			//rodadas.add(r);
			
			
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
		
		//return rodadas;		
	}

}
