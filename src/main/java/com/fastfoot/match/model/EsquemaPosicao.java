package com.fastfoot.match.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.player.model.entity.Jogador;

public class EsquemaPosicao {

	private Jogador mandante;
	
	private Jogador visitante;
	
	private Jogador goleiro;

	private Integer numero;

	private List<EsquemaTransicao> transicoesMandante;
	
	private List<EsquemaTransicao> transicoesVisitante;
	
	private Double probabilidadeArremateForaMandante;
	
	private Double probabilidadeArremateForaVisitante;
	
	public EsquemaPosicao() {
		this.transicoesMandante = new ArrayList<EsquemaTransicao>();
		this.transicoesVisitante = new ArrayList<EsquemaTransicao>();
	}
	
	public EsquemaPosicao(Integer numero, Jogador goleiro) {
		super();
		this.goleiro = goleiro;
		this.numero = numero;
		this.transicoesMandante = new ArrayList<EsquemaTransicao>();
		this.transicoesVisitante = new ArrayList<EsquemaTransicao>();
	}

	/*public EsquemaPosicao(Integer numero, Jogador mandante, Jogador visitante) {
		super();
		this.mandante = mandante;
		this.visitante = visitante;
		this.numero = numero;
		this.transicoesMandante = new ArrayList<EsquemaTransicao>();
		this.transicoesVisitante = new ArrayList<EsquemaTransicao>();
	}*/
	
	public EsquemaPosicao(Integer numero, Jogador mandante, Jogador visitante, Double probabilidadeArremateForaMandante,
			Double probabilidadeArremateForaVisitante) {
		super();
		this.mandante = mandante;
		this.visitante = visitante;
		this.numero = numero;
		this.transicoesMandante = new ArrayList<EsquemaTransicao>();
		this.transicoesVisitante = new ArrayList<EsquemaTransicao>();
		this.probabilidadeArremateForaMandante = probabilidadeArremateForaMandante;
		this.probabilidadeArremateForaVisitante = probabilidadeArremateForaVisitante;
	}

	public Jogador getMandante() {
		return mandante;
	}

	public void setMandante(Jogador mandante) {
		this.mandante = mandante;
	}

	public Jogador getVisitante() {
		return visitante;
	}

	public void setVisitante(Jogador visitante) {
		this.visitante = visitante;
	}

	public List<EsquemaTransicao> getTransicoesMandante() {
		return transicoesMandante;
	}

	public void setTransicoesMandante(List<EsquemaTransicao> transicoesMandante) {
		this.transicoesMandante = transicoesMandante;
	}

	public List<EsquemaTransicao> getTransicoesVisitante() {
		return transicoesVisitante;
	}

	public void setTransicoesVisitante(List<EsquemaTransicao> transicoesVisitante) {
		this.transicoesVisitante = transicoesVisitante;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Jogador getGoleiro() {
		return goleiro;
	}

	public void setGoleiro(Jogador goleiro) {
		this.goleiro = goleiro;
	}

	public Double getProbabilidadeArremateForaMandante() {
		return probabilidadeArremateForaMandante;
	}

	public void setProbabilidadeArremateForaMandante(Double probabilidadeArremateForaMandante) {
		this.probabilidadeArremateForaMandante = probabilidadeArremateForaMandante;
	}

	public Double getProbabilidadeArremateForaVisitante() {
		return probabilidadeArremateForaVisitante;
	}

	public void setProbabilidadeArremateForaVisitante(Double probabilidadeArremateForaVisitante) {
		this.probabilidadeArremateForaVisitante = probabilidadeArremateForaVisitante;
	}
	
	public Double getProbabilidadeArremateFora(boolean posseMandante) {
		if (posseMandante) {
			return probabilidadeArremateForaMandante;
		} else {
			return probabilidadeArremateForaVisitante;
		}
	}

	public void addTransicaoMandante(EsquemaTransicao esquemaTransicao) {
		/*if (transicoesMandante == null) {
			transicoesMandante = new ArrayList<EsquemaTransicao>();
		}*/
		transicoesMandante.add(esquemaTransicao);
	}

	public void addTransicaoVisitante(EsquemaTransicao esquemaTransicao) {
		/*if (transicoesVisitante == null) {
			transicoesVisitante = new ArrayList<EsquemaTransicao>();
		}*/
		transicoesVisitante.add(esquemaTransicao);
	}

	@Override
	public String toString() {
		return "EsquemaPosicao [" + numero + "]";
	}
}
