package com.fastfoot.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Parametro {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametroSequence")	
	@SequenceGenerator(name = "parametroSequence", sequenceName = "parametro_seq")
	private Long id;
	
	private String nome;
	
	private String valor;
	
	private String descricao;

	//Valores separados por ,
	private String possiveisValores;
	
	@ManyToOne
	@JoinColumn(name = "id_jogo")
	private Jogo jogo;

	public Parametro() {

	}
	
	public Parametro(String nome, String valor, String possiveisValores, Jogo jogo) {
		this.nome = nome;
		this.valor = valor;
		this.possiveisValores = possiveisValores;
		this.jogo = jogo;
	}
	
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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPossiveisValores() {
		return possiveisValores;
	}

	public void setPossiveisValores(String possiveisValores) {
		this.possiveisValores = possiveisValores;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public boolean validar() {
		String[] valores = getPossiveisValores().split(",");
		for (String v : valores) {
			if (valor.equals(v)) {
				return true;
			}
		}
		return false;
	}
}
