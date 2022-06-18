package com.fastfoot.club.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ClubeTituloRanking {

	@Id
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Integer titulosNacional;
	
	private Integer titulosNacionalII;
	
	private Integer titulosCopaNacional;
	
	private Integer titulosCopaNacionalII;
	
	private Integer titulosContinental;
	
	private Integer titulosContinentalII;
	
	private Integer titulosContinentalIII;
	
	private Integer pontuacao;//TODO
	
	public ClubeTituloRanking() {

	}
	
	public ClubeTituloRanking(Integer id, Clube clube) {
		this.id = id;
		this.clube = clube;
		this.titulosNacional = 0;	
		this.titulosNacionalII = 0;	
		this.titulosCopaNacional = 0;	
		this.titulosCopaNacionalII = 0;	
		this.titulosContinental = 0;	
		this.titulosContinentalII = 0;
		this.titulosContinentalIII = 0;
		this.pontuacao = 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getTitulosNacional() {
		return titulosNacional;
	}

	public void setTitulosNacional(Integer titulosNacional) {
		this.titulosNacional = titulosNacional;
	}

	public Integer getTitulosNacionalII() {
		return titulosNacionalII;
	}

	public void setTitulosNacionalII(Integer titulosNacionalII) {
		this.titulosNacionalII = titulosNacionalII;
	}

	public Integer getTitulosCopaNacional() {
		return titulosCopaNacional;
	}

	public void setTitulosCopaNacional(Integer titulosCopaNacional) {
		this.titulosCopaNacional = titulosCopaNacional;
	}

	public Integer getTitulosCopaNacionalII() {
		return titulosCopaNacionalII;
	}

	public void setTitulosCopaNacionalII(Integer titulosCopaNacionalII) {
		this.titulosCopaNacionalII = titulosCopaNacionalII;
	}

	public Integer getTitulosContinental() {
		return titulosContinental;
	}

	public void setTitulosContinental(Integer titulosContinental) {
		this.titulosContinental = titulosContinental;
	}

	public Integer getTitulosContinentalII() {
		return titulosContinentalII;
	}

	public void setTitulosContinentalII(Integer titulosContinentalII) {
		this.titulosContinentalII = titulosContinentalII;
	}

	public Integer getTitulosContinentalIII() {
		return titulosContinentalIII;
	}

	public void setTitulosContinentalIII(Integer titulosContinentalIII) {
		this.titulosContinentalIII = titulosContinentalIII;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public void calcularPontuacao() {
		pontuacao = titulosContinental * 30 + titulosContinentalII * 20 + titulosContinentalIII * 10;
		pontuacao += titulosCopaNacional * 20 + titulosCopaNacionalII * 7;
		pontuacao += titulosNacional * 25 + titulosNacionalII * 8;
	}
}
