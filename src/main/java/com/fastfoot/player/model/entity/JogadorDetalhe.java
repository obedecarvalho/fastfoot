package com.fastfoot.player.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;

@Entity
//@Table(indexes = { @Index(columnList = "id_jogador") })
public class JogadorDetalhe {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorDetalheSequence")	
	@SequenceGenerator(name = "jogadorDetalheSequence", sequenceName = "jogador_detalhe_seq")
	private Long id;
	
	@OneToOne(mappedBy = "jogadorDetalhe")
	private Jogador jogador;
	
	private ModoDesenvolvimentoJogador modoDesenvolvimentoJogador;
	
	@Transient
	private JogadorEstatisticasTemporada estatisticasTemporadaAtual;
	
	@Transient
	private JogadorEstatisticasTemporada estatisticasAmistososTemporadaAtual;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public ModoDesenvolvimentoJogador getModoDesenvolvimentoJogador() {
		return modoDesenvolvimentoJogador;
	}

	public void setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador modoDesenvolvimentoJogador) {
		this.modoDesenvolvimentoJogador = modoDesenvolvimentoJogador;
	}

	public JogadorEstatisticasTemporada getEstatisticasTemporadaAtual() {
		return estatisticasTemporadaAtual;
	}

	public void setEstatisticasTemporadaAtual(JogadorEstatisticasTemporada estatisticasTemporadaAtual) {
		this.estatisticasTemporadaAtual = estatisticasTemporadaAtual;
	}

	public JogadorEstatisticasTemporada getEstatisticasAmistososTemporadaAtual() {
		return estatisticasAmistososTemporadaAtual;
	}

	public void setEstatisticasAmistososTemporadaAtual(JogadorEstatisticasTemporada estatisticasAmistososTemporadaAtual) {
		this.estatisticasAmistososTemporadaAtual = estatisticasAmistososTemporadaAtual;
	}

	@JsonIgnore
	@Transient
	public Clube getClube() {
		return jogador.getClube();
	}

	@Override
	public int hashCode() {
		return Objects.hash(jogador);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JogadorDetalhe other = (JogadorDetalhe) obj;
		return Objects.equals(jogador, other.jogador);
	}

}
