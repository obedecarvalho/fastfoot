package com.fastfoot.player.model;

public enum ModoDesenvolvimentoJogador {

	REGULAR	(150, 0.26d, 0.31d, 0.36d, 0.41d, 0.46d, 0.51d, 0.56d, 0.62d, 0.68d, 0.74d, 0.80d, 0.85d, 0.90d, 0.95d, 1.00d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d),
	PRECOCE	(300, 0.26d, 0.33d, 0.40d, 0.47d, 0.54d, 0.61d, 0.68d, 0.73d, 0.78d, 0.83d, 0.88d, 0.91d, 0.94d, 0.97d, 1.00d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d),
	TARDIO	(0, 0.24d, 0.28d, 0.32d, 0.36d, 0.40d, 0.44d, 0.48d, 0.54d, 0.60d, 0.66d, 0.72d, 0.79d, 0.86d, 0.93d, 1.00d, 1.00d, 0.95d, 0.90d, 0.85d, 0.77d, 0.69d, 0.61d),
	;
	
	private Double[] valorAjuste;
	
	private Integer numeroMinimoMinutos;
	
	private ModoDesenvolvimentoJogador(Integer numeroMinimoMinutos, Double... valorAjuste) {
		this.valorAjuste = valorAjuste;
		this.numeroMinimoMinutos = numeroMinimoMinutos;
	}

	public Double[] getValorAjuste() {
		return valorAjuste;
	}

	public Integer getNumeroMinimoMinutos() {
		return numeroMinimoMinutos;
	}
}
