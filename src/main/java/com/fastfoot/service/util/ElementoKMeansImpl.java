package com.fastfoot.service.util;

import java.util.Arrays;

public class ElementoKMeansImpl<T> implements ElementoKMeans {
	
	private T elemento;
	
	private Double[] atributos;
	
	public ElementoKMeansImpl(T elemento) {
		this.elemento = elemento;
	}
	
	public ElementoKMeansImpl(T elemento, Double[] atributos) {
		this.elemento = elemento;
		this.atributos = atributos;
	}
	
	public ElementoKMeansImpl(T elemento, int numeroAtributos) {
		this.elemento = elemento;
		this.atributos = new Double[numeroAtributos];
	}

	@Override
	public Double[] getAtributos() {
		return atributos;
	}

	public T getElemento() {
		return elemento;
	}

	public void setElemento(T elemento) {
		this.elemento = elemento;
	}

	public void setAtributos(Double[] atributos) {
		this.atributos = atributos;
	}

	@Override
	public String toString() {
		return "ElementoKMeansImpl [elemento=" + elemento + ", atributos=" + Arrays.toString(atributos) + "]";
	}

}
