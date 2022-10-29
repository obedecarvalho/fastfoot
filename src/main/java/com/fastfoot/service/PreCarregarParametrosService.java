package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.ParametroRepository;

@Service
public class PreCarregarParametrosService {
	
	@Autowired
	private ParametroRepository parametroRepository;

	public void preCarregarParametros() {//TODO: usar ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_ELI em vez de "..."
		
		parametroRepository.deleteAll();

		if (parametroRepository.findAll().isEmpty()) {

			List<Parametro> parametros = new ArrayList<Parametro>();

			parametros.add(new Parametro(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS, "3", "2, 3"));

			//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
			parametros.add(new Parametro(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL, "MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR", "SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR"));

			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_II, "true", "true, false"));

			//4 (16 TIMES), 6 (28 a 32 TIMES)
			parametros.add(new Parametro(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R));

			parametros.add(new Parametro(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE, "false", "true, false"));
			
			parametros.add(new Parametro(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS, "3", "3"));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.CALCULAR_PROBABILIDADES, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.USAR_APOSTAS_ESPORTIVAS, "false", "false, true"));
			
			parametros.add(
					new Parametro(ParametroConstantes.ESCALACAO_PADRAO, ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132,
							ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_4222 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_41212));

			parametroRepository.saveAll(parametros);
		}
	}
}
