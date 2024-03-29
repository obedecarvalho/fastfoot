package com.fastfoot.transfer.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.TipoNegociacao;

@Entity
//@Table(indexes = { @Index(columnList = "id_clube") })
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

	@Override
	public String toString() {
		return "DisponivelNegociacao [clube=" + clube + ", jogador=" + jogador + ", tipoNegociacao=" + tipoNegociacao
				+ ", ativo=" + ativo + "]";
	}

}
