package com.fastfoot.player.model;

public enum ValorTransferenciaHabilidadeGrupo {

	DEFESA(HabilidadeGrupo.DEFESA, 1.15),
	CONCLUSAO(HabilidadeGrupo.CONCLUSAO, 1.275),
	CRIACAO(HabilidadeGrupo.CRIACAO, 1.225),
	POSSE_BOLA(HabilidadeGrupo.POSSE_BOLA, 1.075),
	QUEBRA_LINHA(HabilidadeGrupo.QUEBRA_LINHA, 1.20),
	GOLEIRO(HabilidadeGrupo.GOLEIRO, 1.3),
	;

	private Double peso;

	private HabilidadeGrupo habilidade;

	private ValorTransferenciaHabilidadeGrupo(HabilidadeGrupo habilidade, Double peso) {
		this.habilidade = habilidade;
		this.peso = peso;
	}

	public Double getPeso() {
		return peso;
	}

	public HabilidadeGrupo getHabilidade() {
		return habilidade;
	}
}
