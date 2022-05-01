package com.fastfoot.player.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.fastfoot.player.model.HabilidadeAcao;
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

	private Integer idade;

	private Integer forcaGeral;
	
	@Transient
	private List<HabilidadeValor> habilidades;
	
	@Transient
	private List<HabilidadeValor> habilidadesAcaoFim;
	
	@Transient
	private List<HabilidadeValor> habilidadesAcaoMeioFim;

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

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public List<HabilidadeValor> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<HabilidadeValor> habilidadeValor) {
		this.habilidades = habilidadeValor;
	}

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public void setForcaGeral(Integer forcaGeral) {
		this.forcaGeral = forcaGeral;
	}

	@Override
	public String toString() {
		return "Jogador [posicao=" + posicao + ", numero=" + numero + "]";
	}
	
	//###	METODOS AUXILIARES	###
	
	public void addHabilidade(HabilidadeValor habilidadeValor) {
		if (getHabilidades() == null) {
			setHabilidades(new ArrayList<HabilidadeValor>());
		}
		getHabilidades().add(habilidadeValor);
	}

	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades) {
		return getHabilidades().stream().filter(hv -> habilidades.contains(hv.getHabilidadeAcao())).collect(Collectors.toList());
	}

	public List<HabilidadeValor> getHabilidadesAcaoFimValor() {
		if (habilidadesAcaoFim == null) {
			habilidadesAcaoFim = getHabilidades(Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO,
					HabilidadeAcao.CRUZAMENTO, HabilidadeAcao.ARMACAO));
		}
		return habilidadesAcaoFim; 
	}

	public List<HabilidadeValor> getHabilidadesAcaoMeioFimValor() {
		if (habilidadesAcaoMeioFim == null) {
			habilidadesAcaoMeioFim = getHabilidades(
					Arrays.asList(HabilidadeAcao.PASSE, HabilidadeAcao.FINALIZACAO, HabilidadeAcao.CRUZAMENTO,
							HabilidadeAcao.ARMACAO, HabilidadeAcao.VELOCIDADE, HabilidadeAcao.DIBLE, HabilidadeAcao.FORCA));
		}
		return habilidadesAcaoMeioFim;
	}
}
