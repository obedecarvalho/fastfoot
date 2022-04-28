package com.fastfoot.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Parametro {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String nome;
	
	private String valor;
	
	private String descricao;
	
	private String valorMaximo;
	
	private String valorMinimo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(String valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public String getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(String valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

}
