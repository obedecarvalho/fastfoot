package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.util.SemanaUtil;

public class CampeonatoFactory {
	
	public static Campeonato criarCampeonato (Temporada temporada, LigaJogo liga, List<Clube> clubes, NivelCampeonato nivelCampeonato) {
		Campeonato campeonato = new Campeonato();
		campeonato.setTemporada(temporada);
		campeonato.setLigaJogo(liga);
		campeonato.setRodadaAtual(0);
		campeonato.setNivelCampeonato(nivelCampeonato);
		
		List<Classificacao> classificacao = ClassificacaoFactory.gerarClassificacaoInicial(clubes, campeonato, null);
		
		List<Rodada> rodadas = CampeonatoFactory.gerarRodadas(clubes, campeonato, null);
		
		campeonato.setRodadas(rodadas);
		campeonato.setClassificacao(classificacao);
		
		SemanaUtil.associarRodadaNacionalSemana(campeonato);
		
		return campeonato;
	}
	
	public static List<Rodada> gerarRodadas(List<Clube> clubes, Campeonato campeonato, GrupoCampeonato grupoCampeonato) {
		int nTimes = clubes.size();
		
		int nRodadas = nTimes-1, nPartidas = (int) nTimes/2;
		
		List<Clube> tmp1 = clubes.subList(0, nPartidas);
		List<Clube> tmp2 = clubes.subList(nPartidas, nTimes);
		
		Clube m, v, aux;
		
		PartidaResultado p;
		
		Rodada r;
		
		List<PartidaResultado> partidasRodada = null;
		List<Rodada> rodadas = new ArrayList<Rodada>();
		
		for (int i=0; i<nRodadas; i++) {
			
			r = new Rodada();
			r.setNumero(i+1);
			r.setCampeonato(campeonato);
			r.setGrupoCampeonato(grupoCampeonato);
			
			partidasRodada = new ArrayList<PartidaResultado>();
			
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
