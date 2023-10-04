package com.fastfoot.model;

public class Constantes {

	//###	TEMPORADA	###
	
	public static final Integer ANO_INICIAL = 2023;
	
	/** Numero de semanas na temporada */
	public static final Integer NUM_SEMANAS = 25;
	
	//###	PARTIDAS	###
	public static final Integer PTOS_VITORIA = 3;
	public static final Integer PTOS_EMPATE = 1;
	
	//###	LIGAS	###
	
	public static final Integer NRO_LIGAS = 4;
	
	public static final Integer NRO_CLUBES_POR_LIGA = 32;
	
	//###	CAMPEONATO NACIONAL	###
	
	/** Numero de clubes que disputam o campeonato nacional por divisao */
	public static final Integer NRO_CLUBE_CAMP_NACIONAL = 16;
	
	/** Numero divisoes campeonato nacional */
	public static final Integer NRO_DIVISOES = 2;
	
	public static final Integer NRO_RODADAS_CAMP_NACIONAL = 15;
	
	//###	COPA NACIONAL	###
	
	public static final Integer NRO_RODADAS_CP_NAC = 6;
	
	public static final Integer NRO_CLUBES_CP_NAC_II = 16;
	
	public static final Integer SEMANA_PROMOCAO_CNII = 6;
	
	//###	PROMOTOR	###
	/** Semana que o promotor deve executar */
	public static final Integer SEMANA_PROMOCAO_CONTINENTAL = 12;
	
	
	//###	CONTINENTAL	###
	
	/** Numero de clube a se classificar para Continental */
	public static final Integer NRO_CLUBES_POR_LIGA_CONT = 4;
	
	/** Total de clubes classificados para fase final */
	public static final Integer NRO_CLUBES_FASE_FINAL = 8;
	
	/** Numero de grupos */
	public static final Integer NRO_GRUPOS_CONT = 4;
	
	/** Número de clubes por grupo */
	public static final Integer NRO_CLUBES_GRUPOS = 4;
	
	public static final Integer NRO_PARTIDAS_FASE_GRUPOS = 3;
	
	//###	AMISTOSOS	###
	/** Numero de grupos */
	public static final Integer NRO_GRUPOS_AMISTOSOS = 4;
	
	/** Número de clubes por grupo */
	public static final Integer NRO_CLUBES_GRUPOS_AMISTOSOS = 4;

	//###	ROLETA	###
	public static final Integer ROLETA_N_POWER = 2;

	//###	TRANSFERENCIA	###
	public static final Integer PESO_DIFERENCA_JOGADOR_CLUBE_TRANSFERENCIA = 1000;
	
	public static final Integer NUMERO_MINIMO_JOGADORES_LINHA = 15;//TODO: implementar lógica
	
	public static final Integer NUMERO_MINIMO_GOLEIROS = 2;
	
	//###	SALARIO		###
	/** Porcentagem do Valor de Transferencia que seja pago como salário semanal para o Jogador */
	public static final Double PORC_VALOR_JOG_SALARIO_SEMANAL = 0.004;
	
	//###	ENERGIA JOGADOR	###
	public static final Integer ENERGIA_INICIAL = 100;
	
	public static final Integer CONSUMO_PARCIAL_ENERGIA_FIXA = 6;

	public static final Integer CONSUMO_PARCIAL_ENERGIA_PARTIDA = 2;
	
	public static final Integer CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO = 3;

	public static final Integer RECUPERACAO_ENERGIA_SEMANAL = 12;
	
	public static final Integer RECUPERACAO_ENERGIA_SEMANAL_ATE_23 = RECUPERACAO_ENERGIA_SEMANAL + 10;
	
	public static final Integer RECUPERACAO_ENERGIA_SEMANAL_24_A_32 = RECUPERACAO_ENERGIA_SEMANAL + 7;
	
	public static final Integer RECUPERACAO_ENERGIA_SEMANAL_APOS_33 = RECUPERACAO_ENERGIA_SEMANAL + 4;
	
	public static final Integer ENERGIA_MINIMA_JOGAR = 30;
	
	//###	SUBSTITUICOES	###
	public static final Integer NUMERO_MAX_SUBSTITUICOES = 5;
	
	//###	FINANCAS	###
	public static final Double PORC_JUROS_BANCARIOS_SEMANAL = 0.0038;
	
	//###	CONTRATOS	###
	public static final Integer NUMERO_ANO_MAX_CONTRATO_PADRAO = 3;
	
	public static final Integer NUMERO_ANO_MIN_CONTRATO_PADRAO = 1;
	
	public static final Integer NUMERO_ANO_MAX_CONTRATO_PADRAO_JOGADOR_JOVEM = 5;
	
	//###	JOGADOR	###
	public static final Integer IDADE_MAX_JOGADOR_JOVEM = 23;

	//###	MOV FINANCEIRA	###
	public static final Integer MOV_FINAN_ID_BASE_ENTRADA = 100;

	public static final Integer MOV_FINAN_ID_BASE_SAIDA = 200;

	public static final Integer MOV_FINAN_ID_BASE_CAIXA = 300;

	public static final Integer MOV_FINAN_ID_BASE_ATIVO = 400;
	
	//###	TREINADOR	###
	public static final Integer POSICAO_MAX_RANKING_NIVEL_6 = 28;
	
	public static final Integer POSICAO_MAX_RANKING_NIVEL_7 = 30;
	//###	/TREINADOR	###
}
