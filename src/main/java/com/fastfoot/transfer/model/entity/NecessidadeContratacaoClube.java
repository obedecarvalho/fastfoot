package com.fastfoot.transfer.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.NivelAdequacao;

@Entity
@Table(indexes = { @Index(columnList = "id_clube") })
public class NecessidadeContratacaoClube {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "necessidadeContratacaoClubeSequence")
	@SequenceGenerator(name = "necessidadeContratacaoClubeSequence", sequenceName = "necessidade_contratacao_clube_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Posicao posicao;
	
	private NivelAdequacao nivelAdequacaoMin;
	
	private NivelAdequacao nivelAdequacaoMax;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private Boolean necessidadeSatisfeita;
	
	private Integer idadeMaxima;
	
	private Boolean necessidadePrioritaria;
	
	public NecessidadeContratacaoClube() {

	}

	public NecessidadeContratacaoClube(Temporada temporada, Clube clube, Posicao posicao,
			NivelAdequacao nivelAdequacaoMax, NivelAdequacao nivelAdequacaoMin, Boolean necessidadeSatisfeita, Boolean necessidadePrioritaria) {
		super();
		this.temporada = temporada;
		this.clube = clube;
		this.posicao = posicao;
		this.nivelAdequacaoMin = nivelAdequacaoMin;
		this.nivelAdequacaoMax = nivelAdequacaoMax;
		this.necessidadeSatisfeita = necessidadeSatisfeita;
		this.necessidadePrioritaria = necessidadePrioritaria;
	}
	
	public NecessidadeContratacaoClube(Temporada temporada, Clube clube, Posicao posicao,
			NivelAdequacao nivelAdequacaoMax, NivelAdequacao nivelAdequacaoMin, Boolean necessidadeSatisfeita, Integer idadeMaxima) {
		super();
		this.temporada = temporada;
		this.clube = clube;
		this.posicao = posicao;
		this.nivelAdequacaoMin = nivelAdequacaoMin;
		this.nivelAdequacaoMax = nivelAdequacaoMax;
		this.necessidadeSatisfeita = necessidadeSatisfeita;
		this.idadeMaxima = idadeMaxima;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public NivelAdequacao getNivelAdequacaoMin() {
		return nivelAdequacaoMin;
	}

	public void setNivelAdequacaoMin(NivelAdequacao nivelAdequacaoMin) {
		this.nivelAdequacaoMin = nivelAdequacaoMin;
	}

	public NivelAdequacao getNivelAdequacaoMax() {
		return nivelAdequacaoMax;
	}

	public void setNivelAdequacaoMax(NivelAdequacao nivelAdequacaoMax) {
		this.nivelAdequacaoMax = nivelAdequacaoMax;
	}

	public Boolean isNecessidadeSatisfeita() {
		return necessidadeSatisfeita;
	}

	public void setNecessidadeSatisfeita(Boolean necessidadeSatisfeita) {
		this.necessidadeSatisfeita = necessidadeSatisfeita;
	}

	public Integer getIdadeMaxima() {
		return idadeMaxima;
	}

	public void setIdadeMaxima(Integer idadeMaxima) {
		this.idadeMaxima = idadeMaxima;
	}

	public Boolean getNecessidadeSatisfeita() {
		return necessidadeSatisfeita;
	}

	public Boolean getNecessidadePrioritaria() {
		return necessidadePrioritaria;
	}

	public void setNecessidadePrioritaria(Boolean necessidadePrioritaria) {
		this.necessidadePrioritaria = necessidadePrioritaria;
	}

}
