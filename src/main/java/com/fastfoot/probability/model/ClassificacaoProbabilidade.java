package com.fastfoot.probability.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public class ClassificacaoProbabilidade implements Comparable<ClassificacaoProbabilidade> {

	/*@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificacaoSequence")	
	@SequenceGenerator(name = "classificacaoSequence", sequenceName = "classificacao_seq")
	private Long id;*/
	
	/*@ManyToOne
	@JoinColumn(name = "id_clube")*/
	private Clube clube;
	
	/*@ManyToOne
	@JoinColumn(name = "id_campeonato")*/
	//private Campeonato campeonato;

	/*@ManyToOne
	@JoinColumn(name = "id_grupo_campeonato")
	private GrupoCampeonato grupoCampeonato;*/
	
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

	public ClassificacaoProbabilidade(Clube clube/*, Campeonato campeonato, GrupoCampeonato grupoCampeonato*/) {
		super();
		this.clube = clube;
		//this.campeonato = campeonato;
		//this.grupoCampeonato = grupoCampeonato;
	}

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

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

	/*public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}*/

	/*public GrupoCampeonato getGrupoCampeonato() {
		return grupoCampeonato;
	}

	public void setGrupoCampeonato(GrupoCampeonato grupoCampeonato) {
		this.grupoCampeonato = grupoCampeonato;
	}*/

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

	/*public CampeonatoJogavel getCampeonatoJogavel() {
		if (campeonato != null) return campeonato;
		//if (grupoCampeonato != null) return grupoCampeonato.getCampeonato();
		return null;
	}*/

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

	/*@Override
	public String toString() {
		return "Classificacao [" + clube.getNome() + ", pos=" + posicao 
				+ ", P=" + pontos + ", V=" + vitorias + ", SG=" + saldoGols + ", GP=" + golsPro
				+ (campeonato != null ? ", (NAC)" : ", (CONT)")
				 + "]";
	}*/

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
