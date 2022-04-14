package com.fastfoot.player.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.Posicao;

@Entity
public class Jogador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorSequence")	
	@SequenceGenerator(name = "jogadorSequence", sequenceName = "jogador_seq")
	private Long id;

	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Posicao posicao;
	
	private Integer numero;
	
	//Ação Fim
	
	private Integer valorPasse;

	private Integer valorFinalizacao;

	private Integer valorCruzamento;

	private Integer valorArmacao;

	private Integer valorCabeceio;

	//Reação

	private Integer valorMarcacao;

	private Integer valorDesarme;

	private Integer valorInterceptacao;

	//Ação Meio

	private Integer valorVelocidade;

	private Integer valorDible;

	private Integer valorForca;
	
	//Ação Inicio

	private Integer valorPosicionamento;

	private Integer valorDominio;
	
	//Goleiro
	
	private Integer valorReflexo;
	
	private Integer valorJogoAereo;

	@Transient
	private List<HabilidadeValor> habilidadeValor;

	@Transient
	private List<HabilidadeValor> habilidadeAcaoFimValor;

	@Transient
	private List<HabilidadeValor> habilidadeAcaoMeioFimValor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public Integer getValorPasse() {
		return valorPasse;
	}

	public void setValorPasse(Integer valorPasse) {
		this.valorPasse = valorPasse;
	}

	public Integer getValorFinalizacao() {
		return valorFinalizacao;
	}

	public void setValorFinalizacao(Integer valorFinalizacao) {
		this.valorFinalizacao = valorFinalizacao;
	}

	public Integer getValorCruzamento() {
		return valorCruzamento;
	}

	public void setValorCruzamento(Integer valorCruzamento) {
		this.valorCruzamento = valorCruzamento;
	}

	public Integer getValorArmacao() {
		return valorArmacao;
	}

	public void setValorArmacao(Integer valorArmacao) {
		this.valorArmacao = valorArmacao;
	}

	public Integer getValorCabeceio() {
		return valorCabeceio;
	}

	public void setValorCabeceio(Integer valorCabeceio) {
		this.valorCabeceio = valorCabeceio;
	}

	public Integer getValorMarcacao() {
		return valorMarcacao;
	}

	public void setValorMarcacao(Integer valorMarcacao) {
		this.valorMarcacao = valorMarcacao;
	}

	public Integer getValorDesarme() {
		return valorDesarme;
	}

	public void setValorDesarme(Integer valorDesarme) {
		this.valorDesarme = valorDesarme;
	}

	public Integer getValorInterceptacao() {
		return valorInterceptacao;
	}

	public void setValorInterceptacao(Integer valorInterceptacao) {
		this.valorInterceptacao = valorInterceptacao;
	}

	public Integer getValorVelocidade() {
		return valorVelocidade;
	}

	public void setValorVelocidade(Integer valorVelocidade) {
		this.valorVelocidade = valorVelocidade;
	}

	public Integer getValorDible() {
		return valorDible;
	}

	public void setValorDible(Integer valorDible) {
		this.valorDible = valorDible;
	}

	public Integer getValorForca() {
		return valorForca;
	}

	public void setValorForca(Integer valorForca) {
		this.valorForca = valorForca;
	}

	public Integer getValorPosicionamento() {
		return valorPosicionamento;
	}

	public void setValorPosicionamento(Integer valorPosicionamento) {
		this.valorPosicionamento = valorPosicionamento;
	}

	public Integer getValorDominio() {
		return valorDominio;
	}

	public void setValorDominio(Integer valorDominio) {
		this.valorDominio = valorDominio;
	}

	public Integer getValorReflexo() {
		return valorReflexo;
	}

	public void setValorReflexo(Integer valorReflexo) {
		this.valorReflexo = valorReflexo;
	}

	public Integer getValorJogoAereo() {
		return valorJogoAereo;
	}

	public void setValorJogoAereo(Integer valorJogoAereo) {
		this.valorJogoAereo = valorJogoAereo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
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

	@Override
	public String toString() {
		return "Jogador [posicao=" + posicao + ", numero=" + numero + "]";
	}
	
	//###	METODOS AUXILIARES	###

	public List<HabilidadeValor> getHabilidadeValor(List<Habilidade> habilidades) {
		return getHabilidadeValor().stream().filter(hv -> habilidades.contains(hv.getHabilidade())).collect(Collectors.toList());
	}
	
	public List<HabilidadeValor> getHabilidadeValor() {
		if (habilidadeValor == null) {
			habilidadeValor = new ArrayList<HabilidadeValor>();
			
			habilidadeValor.add(new HabilidadeValor(Habilidade.PASSE, getValorPasse()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.FINALIZACAO, getValorFinalizacao()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.CRUZAMENTO, getValorCruzamento()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.ARMACAO, getValorArmacao()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.CABECEIO, getValorCabeceio()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.MARCACAO, getValorMarcacao()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.DESARME, getValorDesarme()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.INTERCEPTACAO, getValorInterceptacao()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.VELOCIDADE, getValorVelocidade()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.DIBLE, getValorDible()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.FORCA, getValorForca()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.POSICIONAMENTO, getValorPosicionamento()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.DOMINIO, getValorDominio()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.REFLEXO, getValorReflexo()));
			habilidadeValor.add(new HabilidadeValor(Habilidade.JOGO_AEREO, getValorJogoAereo()));
		}
		return habilidadeValor;
	}

	public List<HabilidadeValor> getHabilidadeAcaoFimValor() {
		if (habilidadeAcaoFimValor == null) {
			habilidadeAcaoFimValor = new ArrayList<HabilidadeValor>();
			
			habilidadeAcaoFimValor.add(new HabilidadeValor(Habilidade.PASSE, getValorPasse()));
			habilidadeAcaoFimValor.add(new HabilidadeValor(Habilidade.FINALIZACAO, getValorFinalizacao()));
			habilidadeAcaoFimValor.add(new HabilidadeValor(Habilidade.CRUZAMENTO, getValorCruzamento()));
			habilidadeAcaoFimValor.add(new HabilidadeValor(Habilidade.ARMACAO, getValorArmacao()));
		}
		return habilidadeAcaoFimValor;
	}

	public List<HabilidadeValor> getHabilidadeAcaoMeioFimValor() {
		if (habilidadeAcaoMeioFimValor == null) {
			habilidadeAcaoMeioFimValor = new ArrayList<HabilidadeValor>();
			
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.PASSE, getValorPasse()));
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.FINALIZACAO, getValorFinalizacao()));
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.CRUZAMENTO, getValorCruzamento()));
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.ARMACAO, getValorArmacao()));
			
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.VELOCIDADE, getValorVelocidade()));
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.DIBLE, getValorDible()));
			habilidadeAcaoMeioFimValor.add(new HabilidadeValor(Habilidade.FORCA, getValorForca()));
		}
		return habilidadeAcaoMeioFimValor;
	}
}
