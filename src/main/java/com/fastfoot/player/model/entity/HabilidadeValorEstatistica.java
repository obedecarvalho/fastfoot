package com.fastfoot.player.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.fastfoot.player.model.HabilidadeValorJogavelEstatistica;
import com.fastfoot.scheduler.model.entity.Semana;

@Entity
@Table(indexes = { @Index(columnList = "id_semana, id_habilidade_valor") })
public class HabilidadeValorEstatistica implements HabilidadeValorJogavelEstatistica {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeValorEstatisticaSequence")
	@SequenceGenerator(name = "habilidadeValorEstatisticaSequence", sequenceName = "habilidade_valor_estatistica_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_habilidade_valor")
	private HabilidadeValor habilidadeValor;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;

	private Integer quantidadeUso;
	
	private Integer quantidadeUsoVencedor;
	
	private Boolean amistoso;
	
	public HabilidadeValorEstatistica() {
		quantidadeUso = 0;
		quantidadeUsoVencedor = 0;
	}

	public HabilidadeValorEstatistica(HabilidadeValor habilidadeValor) {
		this.quantidadeUso = 0;
		this.quantidadeUsoVencedor = 0;
		this.habilidadeValor = habilidadeValor;
	}
	
	public HabilidadeValorEstatistica(HabilidadeValor habilidadeValor, Semana semana, Boolean amistoso) {
		quantidadeUso = 0;
		quantidadeUsoVencedor = 0;
		this.habilidadeValor = habilidadeValor;
		this.semana = semana;
		this.amistoso = amistoso;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HabilidadeValor getHabilidadeValor() {
		return habilidadeValor;
	}

	public void setHabilidadeValor(HabilidadeValor habilidadeValor) {
		this.habilidadeValor = habilidadeValor;
	}

	public Integer getQuantidadeUso() {
		return quantidadeUso;
	}

	public void setQuantidadeUso(Integer quantidadeUso) {
		this.quantidadeUso = quantidadeUso;
	}

	public Integer getQuantidadeUsoVencedor() {
		return quantidadeUsoVencedor;
	}

	public void setQuantidadeUsoVencedor(Integer quantidadeUsoVencedor) {
		this.quantidadeUsoVencedor = quantidadeUsoVencedor;
	}
	
	@Override
	public void incrementarQuantidadeUso() {
		this.quantidadeUso++;
	}

	@Override
	public void incrementarQuantidadeUsoVencedor() {
		this.quantidadeUsoVencedor++;
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public Boolean getAmistoso() {
		return amistoso;
	}

	public void setAmistoso(Boolean amistoso) {
		this.amistoso = amistoso;
	}

	@Override
	public String toString() {
		return "HabilidadeValorEstatistica [habilidadeValor=" + habilidadeValor + ", qtdeUso=" + quantidadeUso
				+ ", qtdeUsoVencedor=" + quantidadeUsoVencedor + "]";
	}

}
