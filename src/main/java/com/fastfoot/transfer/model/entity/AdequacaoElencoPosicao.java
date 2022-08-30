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

@Deprecated
@Entity
@Table(indexes = { @Index(columnList = "id_clube") })
public class AdequacaoElencoPosicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adequacaoElencoPosicaoSequence")
	@SequenceGenerator(name = "adequacaoElencoPosicaoSequence", sequenceName = "adequacao_elenco_posicao_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Posicao posicao;
	
	private Double adequacao;

	public AdequacaoElencoPosicao() {

	}
	
	public AdequacaoElencoPosicao(Clube clube, Posicao posicao) {
		this.clube = clube;
		this.posicao = posicao;
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

	public Double getAdequacao() {
		return adequacao;
	}

	public void setAdequacao(Double adequacao) {
		this.adequacao = adequacao;
	}

}
