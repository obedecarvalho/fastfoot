package com.fastfoot.player.model.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.player.model.CelulaDesenvolvimento;

@Deprecated
public class GrupoDesenvolvimentoJogador {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupoDesenvolvimentoJogadorSequence")
	@SequenceGenerator(name = "grupoDesenvolvimentoJogadorSequence", sequenceName = "grupo_desenvolvimento_jogador_seq")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	@Column(name = "ativo")
	private Boolean ativo;

	@Column(name = "celula_desenvolvimento")
	private CelulaDesenvolvimento celulaDesenvolvimento;

	private Integer qtdeExecAno;
	
	private Integer qtdeExec;
	
	public GrupoDesenvolvimentoJogador() {
	
	}

	public GrupoDesenvolvimentoJogador(CelulaDesenvolvimento celulaDesenvolvimento, Jogador jogador, Boolean ativo) {
		super();
		this.celulaDesenvolvimento = celulaDesenvolvimento;
		this.jogador = jogador;
		this.ativo = ativo;
		this.qtdeExecAno = 0;
		this.qtdeExec = 0;
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

	public Integer getQtdeExec() {
		return qtdeExec;
	}

	public void setQtdeExec(Integer qtdeExec) {
		this.qtdeExec = qtdeExec;
	}
}
