package com.fastfoot.player.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
@Table(indexes = { @Index(columnList = "id_habilidade_valor") })
public class HabilidadeValorEstatisticaGrupo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeValorEstatisticaGrupoSequence")
	@SequenceGenerator(name = "habilidadeValorEstatisticaGrupoSequence", sequenceName = "habilidade_valor_estatistica_grupo_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_habilidade_valor")
	private HabilidadeValor habilidadeValor;
	
	/*@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;*/
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;

	private Integer quantidadeUso;
	
	private Integer quantidadeUsoVencedor;
	
	//private Double porcAcerto;

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

	/*public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}*/

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
		return new Double(quantidadeUsoVencedor)/quantidadeUso;
	}

	/*public Double getPorcAcerto() {
		return porcAcerto;
	}


	public void setPorcAcerto(Double porcAcerto) {
		this.porcAcerto = porcAcerto;
	}*/
}
