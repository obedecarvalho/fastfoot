package com.fastfoot.player.model;

import java.util.Objects;

import javax.persistence.Transient;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ElementoRoleta;

public class HabilidadeValor implements ElementoRoleta {

	private Long id;

	private Jogador jogador;
	
	private Habilidade habilidade;

	private Integer valor;
	
	private Integer potencialDesenvolvimento;
	
	@Transient
	private HabilidadeAcao habilidadeAcao;

	public HabilidadeValor(HabilidadeAcao habilidadeAcao, Integer valor) {
		super();
		this.habilidadeAcao = habilidadeAcao;
		this.valor = valor;
	}

	public HabilidadeAcao getHabilidadeAcao() {
		return habilidadeAcao;
	}

	public void setHabilidadeAcao(HabilidadeAcao habilidadeAcao) {
		this.habilidadeAcao = habilidadeAcao;
	}

	@Override
	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
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

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	public Integer getPotencialDesenvolvimento() {
		return potencialDesenvolvimento;
	}

	public void setPotencialDesenvolvimento(Integer potencialDesenvolvimento) {
		this.potencialDesenvolvimento = potencialDesenvolvimento;
	}
	
	//TODO: melhorar hashCode

	@Override
	public int hashCode() {
		return Objects.hash(habilidadeAcao, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HabilidadeValor other = (HabilidadeValor) obj;
		return Objects.equals(habilidadeAcao, other.habilidadeAcao) && valor == other.valor;
	}
}
