package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class TreinadorTituloRanking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treinadorTituloRankingSequence")	
	@SequenceGenerator(name = "treinadorTituloRankingSequence", sequenceName = "treinador_titulo_ranking_seq")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_treinador")
	private Treinador treinador;
	
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
	
	public TreinadorTituloRanking() {

	}
	
	public TreinadorTituloRanking(Treinador treinador) {
		this.treinador = treinador;
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

	public Treinador getTreinador() {
		return treinador;
	}

	public void setTreinador(Treinador treinador) {
		this.treinador = treinador;
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

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
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
		TreinadorTituloRanking other = (TreinadorTituloRanking) obj;
		return Objects.equals(id, other.id);
	}

}
