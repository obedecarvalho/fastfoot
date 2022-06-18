package com.fastfoot.match.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.club.model.entity.Clube;

@Entity
@Table(indexes = { @Index(columnList = "id_clube") })
public class Escalacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escalacaoSequence")
	@SequenceGenerator(name = "escalacaoSequence", sequenceName = "escalacao_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;

	@OneToMany(mappedBy = "escalacaoPosicao", fetch = FetchType.LAZY)
	private List<EscalacaoJogadorPosicao> jogadorPosicoes;

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

	public List<EscalacaoJogadorPosicao> getJogadorPosicoes() {
		return jogadorPosicoes;
	}

	public void setJogadorPosicoes(List<EscalacaoJogadorPosicao> jogadorPosicoes) {
		this.jogadorPosicoes = jogadorPosicoes;
	}

	public void addJogadorPosicao(EscalacaoJogadorPosicao jogadorPosicao) {
		this.jogadorPosicoes.add(jogadorPosicao);
	}
}
