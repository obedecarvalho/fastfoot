package com.fastfoot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.Parametro;

@Service
public class PreCarregarParametrosService {
	
	/*@Autowired
	private ParametroRepository parametroRepository;*/

	/*public void preCarregarParametros() {
		
		parametroRepository.deleteAll();

		if (parametroRepository.count() == 0) {

			List<Parametro> parametros = inicializarParametrosPadrao(null);

			parametroRepository.saveAll(parametros);
		}
	}*/

	public List<Parametro> inicializarParametrosPadrao(Jogo jogo) {
			List<Parametro> parametros = new ArrayList<Parametro>();
			
			//parametros.add(new Parametro(ParametroConstantes.USAR_VERSAO_SIMPLIFICADA, "true", "true, false", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA,
					ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_HABILIDADE_GRUPO,
					String.format("%s, %s, %s",
							ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_FORCA_GERAL,
							ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_HABILIDADE,
							ParametroConstantes.METODO_CALCULO_VALOR_TRANSFERENCIA_PARAM_HABILIDADE_GRUPO),
					jogo));

			parametros.add(new Parametro(ParametroConstantes.NUMERO_CAMPEONATOS_CONTINENTAIS, "3", "2, 3", jogo));

			//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
			parametros.add(new Parametro(ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL,
					ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI,
					ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI + ", "
							+ ParametroConstantes.ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG, jogo));

			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_II, "true", "true, false", jogo));

			//4 (16 TIMES), 6 (28 a 32 TIMES)
			parametros.add(new Parametro(ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R, 
					ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R + ", "
							+ ParametroConstantes.NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R, jogo));

			parametros.add(new Parametro(ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE,
					ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM,
					ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_E_SEMANA_A_SEMANA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_PARAM + ", "
							+ ParametroConstantes.MARCAR_AMISTOSOS_AUTOMATICAMENTE_NAO_MARCAR_PARAM, jogo));
			
			parametros.add(new Parametro(ParametroConstantes.NUMERO_CLUBES_REBAIXADOS, "3", "1, 2, 3, 4", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_CONTINENTAL_III_REDUZIDO, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.CALCULAR_PROBABILIDADES, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.JOGAR_COPA_NACIONAL_COMPLETA_32_TIMES, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.USAR_APOSTAS_ESPORTIVAS, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.GERAR_TRANSFERENCIA_INICIO_TEMPORADA, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.GERAR_MUDANCA_CLUBE_NIVEL, "true", "false, true", jogo));
			
			parametros.add(new Parametro(ParametroConstantes.METODO_CALCULO_PROBABILIDADE,
					ParametroConstantes.METODO_CALCULO_PROBABILIDADE_FORCA_GERAL_PARAM,
					String.format("%s, %s, %s, %s, %s, %s",
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_POISSON_PARAM,
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_PARAM,
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_ESTATISTICA_FINALIZACAO_DEFESA_PARAM,
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_GRUPO_PARAM,
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_SIMULAR_PARTIDA_HABILIDADE_PARAM,
							ParametroConstantes.METODO_CALCULO_PROBABILIDADE_FORCA_GERAL_PARAM), jogo));

			/*parametros.add(
					new Parametro(ParametroConstantes.ESCALACAO_PADRAO, ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132,
							ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_4222 + ", "
									+ ParametroConstantes.ESCALACAO_PADRAO_PARAM_41212));*/

		return parametros;
	}
}
