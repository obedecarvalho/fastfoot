package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
//@Table(indexes = { @Index(columnList = "liga") })
public class Clube implements ElementoRoleta {
	
	@Id
	private Integer id;
	
	private String nome;
	
	private Integer forcaGeral;
	
	private Liga liga;

	private Integer forcaGeralAtual;
	
	private ClubeNivel clubeNivel;

	@Transient
	private Integer valorN;
	
	public Clube() {
		super();
	}
	
	public Clube(Integer id) {
		super();
		this.id = id;
	}

	/*public Clube(Integer id, Liga liga, Integer forcaGeral, String nome) {
		super();
		this.id = id;
		this.nome = nome;
		this.forcaGeral = forcaGeral;
		this.liga = liga;
	}*/
	
	public Clube(Integer id, Liga liga, ClubeNivel clubeNivel, String nome) {
		super();
		this.id = id;
		this.nome = nome;
		this.clubeNivel = clubeNivel;
		this.forcaGeral = clubeNivel.getForcaGeral();
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

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public void setForcaGeral(Integer overhall) {
		this.forcaGeral = overhall;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public ClubeNivel getClubeNivel() {
		return clubeNivel;
	}

	public void setClubeNivel(ClubeNivel clubeNivel) {
		this.clubeNivel = clubeNivel;
	}

	public Integer getForcaGeralAtual() {
		return forcaGeralAtual;
	}

	public void setForcaGeralAtual(Integer forcaGeralAtual) {
		this.forcaGeralAtual = forcaGeralAtual;
	}

	@Override
	public Integer getValor() {
		return forcaGeralAtual;
	}

	@Override
	public Integer getValorN() {
		if (valorN == null) {
			valorN = (int) Math.pow(forcaGeralAtual, Constantes.ROLETA_N_POWER);
		}
		return valorN;
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
	
	@Override
	public String toString() {
		return "Clube [nome=" + nome + ", liga=" + liga.name() + "]";
	}

}
