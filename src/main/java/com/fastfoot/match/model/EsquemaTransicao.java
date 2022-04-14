package com.fastfoot.match.model;

import com.fastfoot.service.util.ElementoRoleta;

public class EsquemaTransicao implements ElementoRoleta {

	private EsquemaPosicao posInicial;
	
	private EsquemaPosicao posFinal;
	
	private Integer peso;
	
	public EsquemaTransicao(EsquemaPosicao posInicial, EsquemaPosicao posFinal, Integer peso) {
		super();
		this.posInicial = posInicial;
		this.posFinal = posFinal;
		this.peso = peso;
	}

	public EsquemaPosicao getPosInicial() {
		return posInicial;
	}

	public void setPosInicial(EsquemaPosicao posInicial) {
		this.posInicial = posInicial;
	}

	public void setPosFinal(EsquemaPosicao posFinal) {
		this.posFinal = posFinal;
	}

	public EsquemaPosicao getPosFinal() {
		return posFinal;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public int getValor() {
		return peso;
	}

	@Override
	public String toString() {
		return "EsquemaTransicao [pI=" + posInicial + ", pF=" + posFinal + ", peso=" + peso + "]";
	}

}
