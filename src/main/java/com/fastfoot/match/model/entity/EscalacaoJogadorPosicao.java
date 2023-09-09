package com.fastfoot.match.model.entity;

import java.util.Objects;

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
import com.fastfoot.match.model.StatusEscalacaoJogador;
import com.fastfoot.player.model.entity.Jogador;

@Entity
@Table(indexes = { @Index(columnList = "id_clube, ativo"), @Index(columnList = "id_jogador, ativo")})
public class EscalacaoJogadorPosicao {
	
	/*
	 * TODO: Fazer escalacao apenas para clube gerenciado
	 * TODO: salvar a escalação automatica feita antes da partida?
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escalacaoJogadorPosicaoSequence")
	@SequenceGenerator(name = "escalacaoJogadorPosicaoSequence", sequenceName = "escalacao_jogador_posicao_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	private EscalacaoPosicao escalacaoPosicao;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;//TODO: usar o de EscalacaoClubePartida
	
	private Boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "id_escalacao_clube")
	private EscalacaoClube escalacaoClube;
	
	private StatusEscalacaoJogador statusEscalacaoJogador;

	public EscalacaoJogadorPosicao() {

	}

	public EscalacaoJogadorPosicao(Clube clube, EscalacaoClube escalacaoClube, EscalacaoPosicao escalacaoPosicao,
			Jogador jogador, Boolean ativo, StatusEscalacaoJogador statusEscalacaoJogador) {
		super();
		this.escalacaoPosicao = escalacaoPosicao;
		this.jogador = jogador;
		this.clube = clube;
		this.ativo = ativo;
		this.escalacaoClube = escalacaoClube;
		this.statusEscalacaoJogador = statusEscalacaoJogador;
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

	public StatusEscalacaoJogador getStatusEscalacaoJogador() {
		return statusEscalacaoJogador;
	}

	public void setStatusEscalacaoJogador(StatusEscalacaoJogador statusEscalacaoJogador) {
		this.statusEscalacaoJogador = statusEscalacaoJogador;
	}

	public EscalacaoClube getEscalacaoClubePartida() {
		return escalacaoClube;
	}

	public void setEscalacaoClubePartida(EscalacaoClube escalacaoClubePartida) {
		this.escalacaoClube = escalacaoClubePartida;
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
		EscalacaoJogadorPosicao other = (EscalacaoJogadorPosicao) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "EscalacaoJogadorPosicao [jogador=" + jogador + ", escalacaoPosicao=" + escalacaoPosicao + "]";
	}
}
