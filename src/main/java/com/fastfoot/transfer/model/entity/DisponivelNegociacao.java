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
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.TipoNegociacao;

@Entity
@Table(indexes = { @Index(columnList = "id_clube") })
public class DisponivelNegociacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disponivelNegociacaoSequence")
	@SequenceGenerator(name = "disponivelNegociacaoSequence", sequenceName = "disponivel_negociacao_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private TipoNegociacao tipoNegociacao;
	
	private Boolean ativo;

	public DisponivelNegociacao() {

	}

	public DisponivelNegociacao(Temporada temporada, Clube clube, Jogador jogador, TipoNegociacao tipoNegociacao, Boolean ativo) {
		super();
		this.temporada = temporada;
		this.clube = clube;
		this.jogador = jogador;
		this.tipoNegociacao = tipoNegociacao;
		this.ativo = ativo;
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

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public TipoNegociacao getTipoNegociacao() {
		return tipoNegociacao;
	}

	public void setTipoNegociacao(TipoNegociacao tipoNegociacao) {
		this.tipoNegociacao = tipoNegociacao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
