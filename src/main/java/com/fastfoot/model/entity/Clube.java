package com.fastfoot.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fastfoot.model.Liga;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
public class Clube implements ElementoRoleta {
	
	@Id
	private Integer id;
	
	private String nome;
	
	private Integer overhall;
	
	private Liga liga;
	
	public Clube() {
		super();
	}
	
	public Clube(Integer id) {
		super();
		this.id = id;
	}

	public Clube(Integer id, Liga liga, Integer overhall, String nome) {
		super();
		this.id = id;
		this.nome = nome;
		this.overhall = overhall;
		this.liga = liga;
	}

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

	public Integer getOverhall() {
		return overhall;
	}

	public void setOverhall(Integer overhall) {
		this.overhall = overhall;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	@Override
	public String toString() {
		return "Clube [nome=" + nome + ", liga=" + liga.name() + "]";
	}

	@Override
	public Integer getValor() {
		return overhall;
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
		Clube other = (Clube) obj;
		return Objects.equals(id, other.id);
	}

}
