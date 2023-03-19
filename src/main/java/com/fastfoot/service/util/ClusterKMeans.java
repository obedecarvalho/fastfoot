package com.fastfoot.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClusterKMeans {

	private Integer id;
	
	private Double centroide[];
	
	private List<ElementoKMeans> elementos;
	
	private String label;
	
	public ClusterKMeans(Integer id, Double centroide[]) {
		this.id = id;
		this.centroide = centroide;
		this.elementos = new ArrayList<ElementoKMeans>();
	}
	
	public ClusterKMeans(Integer id, int numeroAtributos) {
		this.id = id;
		this.centroide = new Double[numeroAtributos];
		this.elementos = new ArrayList<ElementoKMeans>();
	}
	
	public ClusterKMeans(Integer id, String label, Double centroide[]) {
		this.id = id;
		this.centroide = centroide;
		this.label = label;
		this.elementos = new ArrayList<ElementoKMeans>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double[] getCentroide() {
		return centroide;
	}

	public void setCentroide(Double[] centroide) {
		this.centroide = centroide;
	}

	public List<ElementoKMeans> getElementos() {
		return elementos;
	}

	public void setElementos(List<ElementoKMeans> elementos) {
		this.elementos = elementos;
	}
	
	public void addElemento(ElementoKMeans elemento) {
		this.elementos.add(elemento);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "ClusterKMeans [id=" + id + ", label=" + label + ", centroide=" + Arrays.toString(centroide) + "]";
	}

}
