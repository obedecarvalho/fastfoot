package com.fastfoot.match.model.entity;

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
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.player.model.entity.Jogador;

@Entity
@Table(indexes = { @Index(columnList = "id_clube"), @Index(columnList = "id_jogador") })
public class EscalacaoJogadorPosicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escalacaoJogadorPosicaoSequence")
	@SequenceGenerator(name = "escalacaoJogadorPosicaoSequence", sequenceName = "escalacao_jogador_posicao_seq")
	private Long id;

	/*@ManyToOne
	@JoinColumn(name = "id_escalacao")
	private Escalacao escalacao;*/
	
	//@OneToOne
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	private EscalacaoPosicao escalacaoPosicao;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Boolean ativo;
	
	//TODO: temporada??
	
	//TODO: semana??

	public EscalacaoJogadorPosicao() {

	}

	/*public EscalacaoJogadorPosicao(Escalacao escalacao, EscalacaoPosicao escalacaoPosicao, Jogador jogador) {
		super();
		this.escalacaoPosicao = escalacaoPosicao;
		this.jogador = jogador;
		//this.escalacao = escalacao;
	}*/
	
	public EscalacaoJogadorPosicao(Clube clube, EscalacaoPosicao escalacaoPosicao, Jogador jogador, Boolean ativo) {
		super();
		this.escalacaoPosicao = escalacaoPosicao;
		this.jogador = jogador;
		this.clube = clube;
		this.ativo = ativo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EscalacaoPosicao getEscalacaoPosicao() {
		return escalacaoPosicao;
	}

	public void setEscalacaoPosicao(EscalacaoPosicao escalacaoPosicao) {
		this.escalacaoPosicao = escalacaoPosicao;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	/*public Escalacao getEscalacao() {
		return escalacao;
	}

	public void setEscalacao(Escalacao escalacao) {
		this.escalacao = escalacao;
	}*/

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
