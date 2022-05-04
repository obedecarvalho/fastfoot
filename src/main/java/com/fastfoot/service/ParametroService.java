package com.fastfoot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;

@Service
public class ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;
	
	public Parametro getParametro(String nome) {
		Optional<Parametro> parametro = parametroRepository.findFirstByNome(nome);
		return parametro.get();
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
}
