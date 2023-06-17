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

	public void preCarregarParametros() {
		
		parametroRepository.deleteAll();//TODO

		if (parametroRepository.count() == 0) {

			List<Parametro> parametros = new ArrayList<Parametro>();
			
			parametros.add(new Parametro(ParametroConstantes.USAR_VERSAO_SIMPLIFICADA, "true", "true, false"));

			parametros.add(new Parametro(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS, "3", "2, 3"));

			//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
			parametros.add(new Parametro(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL,
					ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI,
					ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI + ", "
							+ ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG));

			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_II, "true", "true, false"));

			//4 (16 TIMES), 6 (28 a 32 TIMES)
			parametros.add(new Parametro(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R));

			parametros.add(new Parametro(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE,
					ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM,
					ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_E_SEMANA_A_SEMANA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_NAO_MARCAR_PARAM));
			
			parametros.add(new Parametro(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS, "3", "1, 2, 3, 4"));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.CALCULAR_PROBABILIDADES, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA, "true", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.USAR_APOSTAS_ESPORTIVAS, "false", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.GERAR_TRANSFERENCIA_INICIO_TEMPORADA, "false", "false, true"));
			
			parametros.add(new Parametro(ParametroConstantes.GERAR_MUDANCA_CLUBE_NIVEL, "true", "false, true"));

			/*parametros.add(
					new Parametro(ParametroConstantes.ESCALACAO_PADRAO, ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132,
							ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_4222 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_41212));*/

			parametroRepository.saveAll(parametros);
		}
	}
}
