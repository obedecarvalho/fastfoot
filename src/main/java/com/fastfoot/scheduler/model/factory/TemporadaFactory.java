package com.fastfoot.scheduler.model.factory;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

public class TemporadaFactory {
	
	public static Temporada criarTempordada(Integer ano) {
		Temporada temporada = new Temporada();
		temporada.setAtual(true);
		temporada.setAno(ano);
		temporada.setSemanas(criarSemanas(temporada));
		temporada.setSemanaAtual(0);
		return temporada;
	}
	
	private static List<Semana> criarSemanas(Temporada temporada) {
		Semana semana = null;
		List<Semana> semanas = new ArrayList<Semana>();
		for (int i = 1; i <= Constantes.NUM_SEMANAS; i++) {
			semana = new Semana();
			semana.setNumero(i);
			semana.setTemporada(temporada);
			semanas.add(semana);
		}
		return semanas;
	}
}
