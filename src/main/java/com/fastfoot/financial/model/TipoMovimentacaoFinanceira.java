package com.fastfoot.financial.model;

import com.fastfoot.model.Constantes;

public enum TipoMovimentacaoFinanceira {

	/*
	 * ENTRADAS	1XX
	 * SAIDAS	2XX
	 * CAIXA	3XX
	 * ATIVO	4XX
	 */

	//Entradas
	ENTRADA_PATROCINIO(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 1),
	ENTRADA_INGRESSOS(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 2),
	ENTRADA_PREMIACAO(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 3),
	ENTRADA_INGRESSOS_AMISTOSOS(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 4),
	ENTRADA_SOCIO_TORCEDOR(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 5),//TODO: implementar logica
	ENTRADA_DIREITOS_TRANSMISSAO(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 6),//TODO: implementar logica
	ENTRADA_VENDA_JOGADOR(Constantes.MOV_FINAN_ID_BASE_ENTRADA + 7),

	//Saidas
	SAIDA_SALARIO_JOGADOR(Constantes.MOV_FINAN_ID_BASE_SAIDA + 1),
	SAIDA_PREMIACAO_JOGADORES(Constantes.MOV_FINAN_ID_BASE_SAIDA + 2),
	SAIDA_MANUTENCAO_ESTADIO(Constantes.MOV_FINAN_ID_BASE_SAIDA + 3),//TODO: implementar logica
	SAIDA_TREINAMENTO_ESPECIFICO(Constantes.MOV_FINAN_ID_BASE_SAIDA + 4),//TODO: implementar logica
	SAIDA_SALARIO_EQUIPE_TECNICA(Constantes.MOV_FINAN_ID_BASE_SAIDA + 5),//TODO: implementar logica
	SAIDA_OLHEIRO(Constantes.MOV_FINAN_ID_BASE_SAIDA + 6),//TODO: implementar logica
	SAIDA_COMPRA_JOGADOR(Constantes.MOV_FINAN_ID_BASE_SAIDA + 7),
	SAIDA_JUROS_BANCARIOS(Constantes.MOV_FINAN_ID_BASE_SAIDA + 8),
	SAIDA_SALARIO_TREINADOR(Constantes.MOV_FINAN_ID_BASE_SAIDA + 9),//TODO: implementar logica

	//Caixa
	CAIXA_INICIAL(Constantes.MOV_FINAN_ID_BASE_CAIXA + 1),
	CAIXA_INICIO_TEMPORADA(Constantes.MOV_FINAN_ID_BASE_CAIXA + 2),
	CAIXA_FIM_TEMPORADA(Constantes.MOV_FINAN_ID_BASE_CAIXA + 3),
	CAIXA_VARIACAO_TEMPORADA(Constantes.MOV_FINAN_ID_BASE_CAIXA + 4),

	//Ativo
	ATIVO_VALOR_ELENCO(Constantes.MOV_FINAN_ID_BASE_ATIVO + 1),
	;

	private Integer id;

	private TipoMovimentacaoFinanceira(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
