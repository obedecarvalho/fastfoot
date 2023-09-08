package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
public class Clube implements ElementoRoleta {
	
	@Id
	private Long id;
	
	private String nome;
	
	private Integer forcaGeral;

	private Integer forcaGeralAtual;
	
	private ClubeNivel nivelNacional;
	
	private ClubeNivel nivelInternacional;
	
	private String logo;
	
	@ManyToOne
	@JoinColumn(name = "id_liga_jogo")
	private LigaJogo ligaJogo;

	@Transient
	@JsonIgnore
	private Integer valorN;
	
	public Clube() {
		super();
	}
	
	public Clube(Long id) {
		super();
		this.id = id;
	}

	/*public Clube(Long id, Liga liga, ClubeNivel nivelNacional, ClubeNivel nivelInternacional, String nome, String logo) {
		super();
		this.id = id;
		this.nome = nome;
		this.nivelNacional = nivelNacional;
		this.forcaGeral = nivelNacional.getForcaGeral();
		//this.liga = liga;
		this.nivelInternacional = nivelInternacional;
		this.logo = logo;
	}*/
	
	public Clube(Long id, LigaJogo ligaJogo, ClubeNivel nivelNacional, ClubeNivel nivelInternacional, String nome, String logo) {
		super();
		this.id = id;
		this.nome = nome;
		this.nivelNacional = nivelNacional;
		this.forcaGeral = nivelNacional.getForcaGeral();//TODO
		this.ligaJogo = ligaJogo;
		this.nivelInternacional = nivelInternacional;
		this.logo = logo;
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

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public void setForcaGeral(Integer overhall) {
		this.forcaGeral = overhall;
	}

	public ClubeNivel getNivelNacional() {
		return nivelNacional;
	}

	public void setNivelNacional(ClubeNivel nivelNacional) {
		this.nivelNacional = nivelNacional;
	}

	public Integer getForcaGeralAtual() {
		return forcaGeralAtual;
	}

	public void setForcaGeralAtual(Integer forcaGeralAtual) {
		this.forcaGeralAtual = forcaGeralAtual;
	}

	public ClubeNivel getNivelInternacional() {
		return nivelInternacional;
	}

	public void setNivelInternacional(ClubeNivel nivelInternacional) {
		this.nivelInternacional = nivelInternacional;
	}

	public LigaJogo getLigaJogo() {
		return ligaJogo;
	}

	public void setLigaJogo(LigaJogo ligaJogo) {
		this.ligaJogo = ligaJogo;
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
		return "Clube [nome=" + nome + ", liga=" + ligaJogo.getLiga().name() + "]";
	}

	@Override
	@JsonIgnore
	public Double getValorAsDouble() {
		return new Double(getValor());
	}

	@Override
	@JsonIgnore
	public Double getValorNAsDouble() {
		return new Double(getValorN());
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
