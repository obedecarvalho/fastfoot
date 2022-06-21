package com.fastfoot.player.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.player.model.CelulaDesenvolvimento;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador"), 
		//@Index(columnList = "id_grupo_desenvolvimento"),
		//@Index(columnList = "id_grupo_desenvolvimento, ativo"), 
		@Index(columnList = "celula_desenvolvimento, ativo") })
public class GrupoDesenvolvimentoJogador {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupoDesenvolvimentoJogadorSequence")
	@SequenceGenerator(name = "grupoDesenvolvimentoJogadorSequence", sequenceName = "grupo_desenvolvimento_jogador_seq")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	/*@ManyToOne
	@JoinColumn(name = "id_grupo_desenvolvimento")
	private GrupoDesenvolvimento grupoDesenvolvimento;*/

	@Column(name = "ativo")
	private Boolean ativo;

	@Column(name = "celula_desenvolvimento")
	private CelulaDesenvolvimento celulaDesenvolvimento;

	private Integer qtdeExecAno;
	
	public GrupoDesenvolvimentoJogador() {
	
	}

	/*public GrupoDesenvolvimentoJogador(GrupoDesenvolvimento grupoDesenvolvimento, Jogador jogador, Boolean ativo) {
		super();
		this.grupoDesenvolvimento = grupoDesenvolvimento;
		this.jogador = jogador;
		this.ativo = ativo;
	}*/

	public GrupoDesenvolvimentoJogador(CelulaDesenvolvimento celulaDesenvolvimento, Jogador jogador, Boolean ativo) {
		super();
		this.celulaDesenvolvimento = celulaDesenvolvimento;
		this.jogador = jogador;
		this.ativo = ativo;
		this.qtdeExecAno = 0;
	}

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

	/*public GrupoDesenvolvimento getGrupoDesenvolvimento() {
		return grupoDesenvolvimento;
	}

	public void setGrupoDesenvolvimento(GrupoDesenvolvimento grupoDesenvolvimento) {
		this.grupoDesenvolvimento = grupoDesenvolvimento;
	}*/

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public CelulaDesenvolvimento getCelulaDesenvolvimento() {
		return celulaDesenvolvimento;
	}

	public void setCelulaDesenvolvimento(CelulaDesenvolvimento celulaDesenvolvimento) {
		this.celulaDesenvolvimento = celulaDesenvolvimento;
	}

	public Integer getQtdeExecAno() {
		return qtdeExecAno;
	}

	public void setQtdeExecAno(Integer qtdeExecAno) {
		this.qtdeExecAno = qtdeExecAno;
	}
}
