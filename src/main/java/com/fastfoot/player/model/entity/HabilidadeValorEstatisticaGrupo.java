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

import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
@Table(indexes = { @Index(columnList = "id_temporada, id_habilidade_valor") })
public class HabilidadeValorEstatisticaGrupo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeValorEstatisticaGrupoSequence")
	@SequenceGenerator(name = "habilidadeValorEstatisticaGrupoSequence", sequenceName = "habilidade_valor_estatistica_grupo_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_habilidade_valor")
	private HabilidadeValor habilidadeValor;

	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

	private Integer quantidadeUso;
	
	private Integer quantidadeUsoVencedor;
	
	private Boolean amistoso;

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

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	
	public Double getPorcAcerto() {
		return Double.valueOf(quantidadeUsoVencedor)/quantidadeUso;
	}

	public Boolean getAmistoso() {
		return amistoso;
	}

	public void setAmistoso(Boolean amistoso) {
		this.amistoso = amistoso;
	}

}
