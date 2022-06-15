package com.fastfoot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;

@Service
public class ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	private static final Map<String, Parametro> PARAMETRO_CACHE = new HashMap<String, Parametro>();
	
	public Parametro getParametro(String nome) {
		return getParametro(nome, false);
	}
	
	public Parametro getParametro(String nome, boolean force) {
		
		Parametro parametro = PARAMETRO_CACHE.get(nome);
		
		if (parametro == null || force) {		
			Optional<Parametro> parametroOpt = parametroRepository.findFirstByNome(nome);
			parametro = parametroOpt.get();
			PARAMETRO_CACHE.put(nome, parametro);
		}

		return parametro;
	}
	
	public Boolean getParametroBoolean(String nome) {
		Parametro parametro = getParametro(nome);		
		return Boolean.valueOf(parametro.getValor());
	}
	
	public Integer getParametroInteger(String nome) {
		Parametro parametro = getParametro(nome);		
		return Integer.valueOf(parametro.getValor());
	}

	public String getParametroString(String nome) {
		Parametro parametro = getParametro(nome);		
		return parametro.getValor();
	}

	public void reset() {
		PARAMETRO_CACHE.clear();
	}
	
	//######### PARAMETROS ESPECIFICOS
	
	public Integer getNumeroRodadasCopaNacional() {
		String numeroRodadas = getParametroString(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL);
		
		if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R.equals(numeroRodadas)) {
			return 4;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R.equals(numeroRodadas)) {
			return 5;
		} else if (ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R.equals(numeroRodadas)) {
			return 6;
		}
		
		return -1;
	}

	public Integer getNumeroTimesParticipantesCopaNacional() {
		Integer numRodadas = getNumeroRodadasCopaNacional();
		Integer nroCompeticoesContinentais = getParametroInteger(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS);
		
		if (numRodadas == 4) {
			return 16;
		} else if (numRodadas == 5) {
			if (nroCompeticoesContinentais == 3) {//20
				return 20;
			} else if (nroCompeticoesContinentais == 2) {//24
				return 24;
			}
		} else if (numRodadas == 6) {
			if (nroCompeticoesContinentais == 3) {//28
				return 28;
			} else if (nroCompeticoesContinentais == 2) {//32
				return 32;
			}
		}

		return -1;
	}

	public boolean isEstrategiaPromotorContinentalSegundoMelhorGrupo() {
		String estrategiaPromotorCont = getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG.equals(estrategiaPromotorCont);
	}
	
	public boolean isEstrategiaPromotorContinentalMelhorEliminado() {
		String estrategiaPromotorCont = getParametroString(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL);
		return ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI.equals(estrategiaPromotorCont);
	}
}
