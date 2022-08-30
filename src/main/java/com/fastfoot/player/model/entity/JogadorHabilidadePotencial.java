package com.fastfoot.player.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Deprecated
public class JogadorHabilidadePotencial {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorHabilidadeValorSequence")
	@SequenceGenerator(name = "jogadorHabilidadeValorSequence", sequenceName = "jogador_habilidade_valor_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Integer potencialPasse;

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

	public Integer getPotencialPasse() {
		return potencialPasse;
	}

	public void setPotencialPasse(Integer potencialPasse) {
		this.potencialPasse = potencialPasse;
	}
	
}
