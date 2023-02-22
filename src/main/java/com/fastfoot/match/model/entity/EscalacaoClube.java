package com.fastfoot.match.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Entity
public class EscalacaoClube {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "escalacaoClubeSequence")
	@SequenceGenerator(name = "escalacaoClubeSequence", sequenceName = "escalacao_clube_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_amistosa")
	private PartidaAmistosaResultado partidaAmistosaResultado;
	
	@JsonIgnore
	@OneToMany(mappedBy = "escalacaoClube", fetch = FetchType.LAZY)
	private List<EscalacaoJogadorPosicao> listEscalacaoJogadorPosicao;
	
	public EscalacaoClube() {

	}
	
	public EscalacaoClube(Clube c, PartidaResultadoJogavel partida, Boolean ativo) {
		this.clube = c;
		if (partida instanceof PartidaResultado) {
			this.partidaResultado = (PartidaResultado) partida;
		} else if (partida instanceof PartidaEliminatoriaResultado) {
			this.partidaEliminatoriaResultado = (PartidaEliminatoriaResultado) partida;
		} else if (partida instanceof PartidaAmistosaResultado) {
			this.partidaAmistosaResultado = (PartidaAmistosaResultado) partida;
		}
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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public PartidaResultado getPartidaResultado() {
		return partidaResultado;
	}

	public void setPartidaResultado(PartidaResultado partidaResultado) {
		this.partidaResultado = partidaResultado;
	}

	public PartidaEliminatoriaResultado getPartidaEliminatoriaResultado() {
		return partidaEliminatoriaResultado;
	}

	public void setPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado) {
		this.partidaEliminatoriaResultado = partidaEliminatoriaResultado;
	}

	public PartidaAmistosaResultado getPartidaAmistosaResultado() {
		return partidaAmistosaResultado;
	}

	public void setPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado) {
		this.partidaAmistosaResultado = partidaAmistosaResultado;
	}

	public List<EscalacaoJogadorPosicao> getListEscalacaoJogadorPosicao() {
		return listEscalacaoJogadorPosicao;
	}

	public void setListEscalacaoJogadorPosicao(List<EscalacaoJogadorPosicao> listEscalacaoJogadorPosicao) {
		this.listEscalacaoJogadorPosicao = listEscalacaoJogadorPosicao;
	}

}
