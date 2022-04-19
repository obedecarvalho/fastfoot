package com.fastfoot.player.model;

public enum Habilidade {
	
	NULL("NULL"),
	PASSE("Passe"),
	FINALIZACAO("Finalizacao"),
	CRUZAMENTO("Cruzamento"),
	ARMACAO("Armacao"),
	MARCACAO("Marcacao"),
	DESARME("Desarme"),
	INTERCEPTACAO("Interceptacao"),
	VELOCIDADE("Velocidade"),
	DIBLE("Dible"),
	FORCA("Forca"),
	CABECEIO("Cabeceio"),
	POSICIONAMENTO("Posicionamento"),
	DOMINIO("Dominio"),
	REFLEXO("Reflexo"),
	JOGO_AEREO("Jogo Aereo");

	private String descricao;
	
	private TipoHabilidade tipoHabilidade;
	
	private Habilidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
