package com.fastfoot.probability.model;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Classificacao;

public class ClassificacaoProbabilidade implements Comparable<ClassificacaoProbabilidade> {

	private Clube clube;
	
	private Integer pontos;
	
	private Integer vitorias;
	
	private Integer saldoGols;
	
	private Integer golsPro;

	private Integer posicao;

	private Integer numJogos;
	
	private Double probabilidadeVitoria;
	
	private Double probabilidadeEmpate;
	
	private Double probabilidadeDerrota;
	
	public ClassificacaoProbabilidade() {

	}

	public ClassificacaoProbabilidade(Clube clube) {
		super();
		this.clube = clube;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getPontos() {
		return pontos;
	}

	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}

	public Integer getVitorias() {
		return vitorias;
	}

	public void setVitorias(Integer vitorias) {
		this.vitorias = vitorias;
	}

	public Integer getSaldoGols() {
		return saldoGols;
	}

	public void setSaldoGols(Integer saldoGols) {
		this.saldoGols = saldoGols;
	}

	public Integer getGolsPro() {
		return golsPro;
	}

	public void setGolsPro(Integer golsPro) {
		this.golsPro = golsPro;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public Integer getNumJogos() {
		return numJogos;
	}

	public void setNumJogos(Integer numJogos) {
		this.numJogos = numJogos;
	}

	public Integer getEmpates() {
		return getPontos() - (3* getVitorias());
	}

	public Integer getDerrotas() {
		return getNumJogos() - getVitorias() - getEmpates(); 
	}
	
	public Integer getGolsContra() {
		return getGolsPro() - getSaldoGols();
	}

	@Override
	public int compareTo(ClassificacaoProbabilidade o) {

		if (o.getPontos() < getPontos()) return -1;
		if (o.getPontos() > getPontos()) return 1;

		if (o.getVitorias() < getVitorias()) return -1;
		if (o.getVitorias() > getVitorias()) return 1;

		if (o.getSaldoGols() < getSaldoGols()) return -1;
		if (o.getSaldoGols() > getSaldoGols()) return 1;

		if (o.getGolsPro() < getGolsPro()) return -1;
		if (o.getGolsPro() > getGolsPro()) return 1;

		return 0;
	}

	public static ClassificacaoProbabilidade criar(Classificacao c) {
		ClassificacaoProbabilidade cp = new ClassificacaoProbabilidade();
		
		cp.setClube(c.getClube());
		
		cp.setNumJogos(c.getNumJogos());
		
		cp.setPontos(c.getPontos());
		
		cp.setPosicao(c.getPosicao());
		
		cp.setVitorias(c.getVitorias());
		
		cp.setSaldoGols(c.getSaldoGols());
		
		cp.setGolsPro(c.getGolsPro());
		
		cp.calcularProbabilidades();
		
		return cp;
	}
	
	public Double getProbabilidadeVitoria() {
		return probabilidadeVitoria;
	}

	public void setProbabilidadeVitoria(Double probabilidadeVitoria) {
		this.probabilidadeVitoria = probabilidadeVitoria;
	}

	public Double getProbabilidadeEmpate() {
		return probabilidadeEmpate;
	}

	public void setProbabilidadeEmpate(Double probabilidadeEmpate) {
		this.probabilidadeEmpate = probabilidadeEmpate;
	}

	public Double getProbabilidadeDerrota() {
		return probabilidadeDerrota;
	}

	public void setProbabilidadeDerrota(Double probabilidadeDerrota) {
		this.probabilidadeDerrota = probabilidadeDerrota;
	}

	public void calcularProbabilidades() {
		setProbabilidadeVitoria(getVitorias().doubleValue()/getNumJogos());
		setProbabilidadeEmpate(getEmpates().doubleValue()/getNumJogos());
		setProbabilidadeDerrota(getDerrotas().doubleValue()/getNumJogos());
	}
}
