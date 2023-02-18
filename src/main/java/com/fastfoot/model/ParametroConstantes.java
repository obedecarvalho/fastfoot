package com.fastfoot.model;

public class ParametroConstantes {
	
	//true, false
	public static final String USAR_VERSAO_SIMPLIFICADA = "USAR_VERSAO_SIMPLIFICADA";

	//2, 3
	public static final String NUMERO_CAMPEONATOS_CONTINENTAIS = "NUMERO_CAMPEONATOS_CONTINENTAIS";

	//SEGUNDO_MELHOR_GRUPO, MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR
	public static final String ESTRATEGIA_PROMOTOR_CONTINENTAL = "ESTRATEGIA_PROMOTOR_CONTINENTAL";

	public static final String ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_SEG = "SEGUNDO_MELHOR_GRUPO";

	public static final String ESTRATEGIA_PROMOTOR_CONTINENTAL_PARAM_ELI = "MELHOR_ELIMINADO_CAMPEONATO_SUPERIOR";

	//true, false
	public static final String JOGAR_COPA_NACIONAL_II = "JOGAR_COPA_NACIONAL_II";

	//4 (16 TIMES), 5 (20, 22 ou 24 TIMES), 6 (28, 30 ou 32 TIMES)
	public static final String NUMERO_RODADAS_COPA_NACIONAL = "NUMERO_RODADAS_COPA_NACIONAL";

	public static final String NUMERO_RODADAS_COPA_NACIONAL_PARAM_4R = "4 (16 TIMES)";

	public static final String NUMERO_RODADAS_COPA_NACIONAL_PARAM_6R = "6 (28, 30 ou 32 TIMES)";

	public static final String NUMERO_RODADAS_COPA_NACIONAL_PARAM_5R = "5 (20, 22 ou 24 TIMES)";

	public static final String MARCAR_AMISTOSOS_AUTOMATICAMENTE = "MARCAR_AMISTOSOS_AUTOMATICAMENTE";
	
	public static final String MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_PARAM = "INICIO_TEMPORADA";
	
	public static final String MARCAR_AMISTOSOS_AUTOMATICAMENTE_SEMANA_A_SEMANA_PARAM = "SEMANA_A_SEMANA";
	
	public static final String MARCAR_AMISTOSOS_AUTOMATICAMENTE_INICIO_TEMPORADA_E_SEMANA_A_SEMANA_PARAM = "INICIO_TEMPORADA_E_SEMANA_A_SEMANA";
	
	public static final String MARCAR_AMISTOSOS_AUTOMATICAMENTE_NAO_MARCAR_PARAM = "NAO_MARCAR";

	//3, 2, 4
	public static final String NUMERO_CLUBES_REBAIXADOS = "NUMERO_CLUBES_REBAIXADOS";
	
	//true, false
	public static final String JOGAR_CONTINENTAL_III_REDUZIDO = "JOGAR_CONTINENTAL_III_REDUZIDO";
	
	//true, false
	//Para o caso de haver CIII Reduzido, 32 clubes jogar a copa nacional (ao invés de 30)
	public static final String JOGAR_COPA_NACIONAL_COMPLETA = "JOGAR_COPA_NACIONAL_COMPLETA";

	//true, false
	public static final String JOGAR_SUPERCOPA = "JOGAR_SUPERCOPA";//TODO: implementar logica
		
	//true, false
	public static final String CALCULAR_PROBABILIDADES = "CALCULAR_PROBABILIDADES";
	
	/*public static final String ESCALACAO_PADRAO = "ESCALACAO_PADRAO";

	public static final String ESCALACAO_PADRAO_PARAM_41212 = "4-1-2-1-2";

	public static final String ESCALACAO_PADRAO_PARAM_4132 = "4-1-3-2";

	public static final String ESCALACAO_PADRAO_PARAM_4222 = "4-2-2-2";*/
	
	public static final String USAR_APOSTAS_ESPORTIVAS = "USAR_APOSTAS_ESPORTIVAS";
	
	public static final String DISPUTAR_TERCEIRO_LUGAR = "DISPUTAR_TERCEIRO_LUGAR";//TODO: implementar logica (conferir ClubeRankingUtil)
}
