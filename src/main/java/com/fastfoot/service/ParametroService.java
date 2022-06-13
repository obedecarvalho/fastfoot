package com.fastfoot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;

@Service
public class ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	private static final Map<String, Parametro> PARAMETRO_CACHE = new HashMap<String, Parametro>();
	
	public Parametro getParametro(String nome) {
		//return getParametro(nome, false);//TODO
		return getParametro(nome, true);
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
}
