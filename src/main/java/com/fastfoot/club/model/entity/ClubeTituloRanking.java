package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ClubeTituloRanking {

	@Id
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Integer titulosNacional;
	
	@Column(name = "titulos_nacional_ii")
	private Integer titulosNacionalII;
	
	private Integer titulosCopaNacional;
	
	@Column(name = "titulos_copa_nacional_ii")
	private Integer titulosCopaNacionalII;
	
	private Integer titulosContinental;
	
	@Column(name = "titulos_continental_ii")
	private Integer titulosContinentalII;
	
	@Column(name = "titulos_continental_iii")
	private Integer titulosContinentalIII;
	
	private Integer pontuacao;
	
	public ClubeTituloRanking() {

	}
	
	public ClubeTituloRanking(Long id, Clube clube) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public void incrementartitulosNacional(){
		this.titulosNacional++;
	}

	public void incrementartitulosNacionalII(){
		this.titulosNacionalII++;
	}

	public void incrementartitulosCopaNacional(){
		this.titulosCopaNacional++;
	}

	public void incrementartitulosCopaNacionalII(){
		this.titulosCopaNacionalII++;
	}

	public void incrementartitulosContinental(){
		this.titulosContinental++;
	}

	public void incrementartitulosContinentalII(){
		this.titulosContinentalII++;
	}

	public void incrementartitulosContinentalIII(){
		this.titulosContinentalIII++;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClubeTituloRanking other = (ClubeTituloRanking) obj;
		return Objects.equals(id, other.id);
	}

}
