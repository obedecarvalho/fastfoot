package com.fastfoot.player.model;

public enum ValorTransferenciaHabilidade {

	ZERO(Habilidade.NULL, 0.0),

	PASSE(Habilidade.PASSE, 1.15),//1
	FINALIZACAO(Habilidade.FINALIZACAO, 1.30),//2
	CRUZAMENTO(Habilidade.CRUZAMENTO, 1.15),//3
	ARMACAO(Habilidade.ARMACAO, 1.25),//4
	MARCACAO(Habilidade.MARCACAO, 1.0),//5
	DESARME(Habilidade.DESARME, 1.0),//6
	INTERCEPTACAO(Habilidade.INTERCEPTACAO, 1.0),//7
	VELOCIDADE(Habilidade.VELOCIDADE, 1.25),//8
	DRIBLE(Habilidade.DRIBLE, 1.25),//9
	FORCA(Habilidade.FORCA, 1.15),//10
	CABECEIO(Habilidade.CABECEIO, 1.25),//11
	POSICIONAMENTO(Habilidade.POSICIONAMENTO, 1.15),//12
	DOMINIO(Habilidade.DOMINIO, 1.0),//13
	REFLEXO(Habilidade.REFLEXO, 1.5),//14
	JOGO_AEREO(Habilidade.JOGO_AEREO, 1.5),//15
	;

	private Double peso;

	private Habilidade habilidade;

	private ValorTransferenciaHabilidade(Habilidade habilidade, Double peso) {
		this.habilidade = habilidade;
		this.peso = peso;
	}

	public Double getPeso() {
		return peso;
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

}
