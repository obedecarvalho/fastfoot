package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.model.entity.Jogo;

@Entity
public class Treinador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treinadorSequence")	
	@SequenceGenerator(name = "treinadorSequence", sequenceName = "treinador_seq")
	private Long id;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_jogo")
	private Jogo jogo;

	private Boolean ativo;
	
	@JsonIgnore
	@OneToOne(mappedBy = "treinador")
	private Clube clube;
	
	@JsonIgnore
	@Transient
	private TreinadorTituloRanking treinadorTituloRanking;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public TreinadorTituloRanking getTreinadorTituloRanking() {
		return treinadorTituloRanking;
	}

	public void setTreinadorTituloRanking(TreinadorTituloRanking treinadorTituloRanking) {
		this.treinadorTituloRanking = treinadorTituloRanking;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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
		Treinador other = (Treinador) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Treinador [id=" + id + ", nome=" + nome + "]";
	}

}
