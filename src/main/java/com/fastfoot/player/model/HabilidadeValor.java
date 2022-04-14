package com.fastfoot.player.model;

import java.util.Objects;

import com.fastfoot.service.util.ElementoRoleta;

public class HabilidadeValor implements ElementoRoleta {

	private Habilidade habilidade;

	private int valor;

	public HabilidadeValor(Habilidade habilidade, Integer valor) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	@Override
	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	//TODO: melhorar hashCode
	
	@Override
	public int hashCode() {
		return Objects.hash(habilidade, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HabilidadeValor other = (HabilidadeValor) obj;
		return Objects.equals(habilidade, other.habilidade) && valor == other.valor;
	}
}
