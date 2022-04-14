package com.fastfoot.scheduler.service.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;

public class SemanaUtil {
	
	private static final Map<Integer, Integer> RODADA_SEMANA_CAMPEONATO_NACIONAL = new HashMap<Integer, Integer>();
	private static final Map<Integer, Integer> RODADA_SEMANA_COPA_NACIONAL = new HashMap<Integer, Integer>();
	private static final Map<Integer, Integer> RODADA_SEMANA_CONTINENTAL = new HashMap<Integer, Integer>();
	private static final Map<Integer, Integer> RODADA_SEMANA_COPA_NACIONAL_II = new HashMap<Integer, Integer>();
	
	static {
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(1, 1);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(2, 2);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(3, 3);
		
		RODADA_SEMANA_COPA_NACIONAL.put(1, 4);
		RODADA_SEMANA_CONTINENTAL.put(1, 4);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(4, 5);
		
		RODADA_SEMANA_COPA_NACIONAL.put(2, 6);
		RODADA_SEMANA_CONTINENTAL.put(2, 6);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(5, 7);
		
		RODADA_SEMANA_COPA_NACIONAL.put(3, 8);
		RODADA_SEMANA_COPA_NACIONAL_II.put(1, 8);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(6, 9);
		
		RODADA_SEMANA_COPA_NACIONAL.put(4, 10);
		RODADA_SEMANA_COPA_NACIONAL_II.put(2, 10);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(7, 11);
		RODADA_SEMANA_CONTINENTAL.put(3, 12);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(8, 13);
		RODADA_SEMANA_CONTINENTAL.put(4, 14);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(9, 15);
		
		RODADA_SEMANA_COPA_NACIONAL_II.put(3, 16);
		RODADA_SEMANA_COPA_NACIONAL.put(5, 16);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(10, 17);
		
		RODADA_SEMANA_COPA_NACIONAL.put(6, 18);
		RODADA_SEMANA_COPA_NACIONAL_II.put(4, 18);
		
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(11, 19);
		RODADA_SEMANA_CONTINENTAL.put(5, 20);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(12, 21);
		RODADA_SEMANA_CONTINENTAL.put(6, 22);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(13, 23);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(14, 24);
		RODADA_SEMANA_CAMPEONATO_NACIONAL.put(15, 25);

	}

	public static void associarRodadaNacionalSemana(Campeonato campeonato) {

		associarRodadaSemana(campeonato.getRodadas(), listSemanaToMap(campeonato.getTemporada().getSemanas()), RODADA_SEMANA_CAMPEONATO_NACIONAL);
	}

	public static void associarRodadaCopaNacionalSemana(CampeonatoEliminatorio campeonato) {

		associarRodadaEliminatoriaSemana(campeonato.getRodadas(), listSemanaToMap(campeonato.getTemporada().getSemanas()), RODADA_SEMANA_COPA_NACIONAL);
	}

	public static void associarRodadaCopaNacionalIISemana(CampeonatoEliminatorio campeonato) {

		associarRodadaEliminatoriaSemana(campeonato.getRodadas(), listSemanaToMap(campeonato.getTemporada().getSemanas()), RODADA_SEMANA_COPA_NACIONAL_II);
	}

	public static void associarRodadaContinentalSemana(CampeonatoMisto campeonato) {

		for (GrupoCampeonato gc : campeonato.getGrupos()) {
			associarRodadaSemana(gc.getRodadas(), listSemanaToMap(campeonato.getTemporada().getSemanas()), RODADA_SEMANA_CONTINENTAL);
		}
		associarRodadaEliminatoriaSemana(campeonato.getRodadasEliminatorias(), listSemanaToMap(campeonato.getTemporada().getSemanas()), RODADA_SEMANA_CONTINENTAL);
	}
	
	private static Map<Integer, Semana> listSemanaToMap(List<Semana> semanas) {
		return semanas.stream().collect(Collectors.toMap(Semana::getNumero, Function.identity()));
	}

	private static void associarRodadaEliminatoriaSemana(List<RodadaEliminatoria> rodadas, Map<Integer, Semana> nroSemana, Map<Integer, Integer> mapRodadaSemana) {

		Semana s = null;
		
		for (RodadaEliminatoria r : rodadas) {
			s = nroSemana.get(mapRodadaSemana.get(r.getNumero()));
			r.setSemana(s);
			s.addRodadaEliminatoria(r);
		}
	}

	private static void associarRodadaSemana(List<Rodada> rodadas, Map<Integer, Semana> nroSemana, Map<Integer, Integer> mapRodadaSemana) {

		Semana s = null;
		
		for (Rodada r : rodadas) {
			s = nroSemana.get(mapRodadaSemana.get(r.getNumero()));
			r.setSemana(s);
			s.addRodada(r);
		}
	}

}
