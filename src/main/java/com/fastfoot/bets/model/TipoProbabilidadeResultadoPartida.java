package com.fastfoot.bets.model;

public enum TipoProbabilidadeResultadoPartida {
	ZERO,
	SIMULAR_PARTIDA,//1 --50,97%
	ESTATISTICAS_FINALIZACAO,//2 --40,89%
	ESTATISTICAS_FINALIZACAO_DEFESA,//3 --41,66%
	ESTATISTICAS_FINALIZACAO_POISSON,//4 --42,03%
	SIMULAR_PARTIDA_HABILIDADE_GRUPO,//5 --48,20%
	FORCA_GERAL,//6 --45,89%
	;
}
