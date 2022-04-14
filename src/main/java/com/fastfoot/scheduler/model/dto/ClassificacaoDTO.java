package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.entity.Classificacao;

public class ClassificacaoDTO {

	private String nomeClube;
	
	private String nomeCampeonato;
		
	private String nivelCampeonato;
	
	private Integer posicao;
	
	private Integer pontos;
	
	private Integer vitorias;
	
	private Integer empates;
	
	private Integer derrotas;
	
	private Integer saldoGols;
	
	private Integer golsPro;
	
	private Integer golsContra;
	
	private Integer numJogos;
	
	private Integer numeroGrupoCampeonato;

	public String getNomeClube() {
		return nomeClube;
	}

	public void setNomeClube(String nomeClube) {
		this.nomeClube = nomeClube;
	}

	public String getNomeCampeonato() {
		return nomeCampeonato;
	}

	public void setNomeCampeonato(String nomeCampeonato) {
		this.nomeCampeonato = nomeCampeonato;
	}

	public String getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(String nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
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

	public Integer getEmpates() {
		return empates;
	}

	public void setEmpates(Integer empates) {
		this.empates = empates;
	}

	public Integer getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(Integer derrotas) {
		this.derrotas = derrotas;
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

	public Integer getGolsContra() {
		return golsContra;
	}

	public void setGolsContra(Integer golsContra) {
		this.golsContra = golsContra;
	}

	public Integer getNumJogos() {
		return numJogos;
	}

	public void setNumJogos(Integer numJogos) {
		this.numJogos = numJogos;
	}
	
	public Integer getNumeroGrupoCampeonato() {
		return numeroGrupoCampeonato;
	}

	public void setNumeroGrupoCampeonato(Integer numeroGrupoCampeonato) {
		this.numeroGrupoCampeonato = numeroGrupoCampeonato;
	}

	public static List<ClassificacaoDTO> convertToDTO(List<? extends Classificacao> partidas) {
		return partidas.stream().map(p -> convertToDTO(p)).collect(Collectors.toList());
	}
	
	public static ClassificacaoDTO convertToDTO(Classificacao pr) {

		ClassificacaoDTO dto = new ClassificacaoDTO();

		dto.setNomeClube(pr.getClube().getNome());
		dto.setNomeCampeonato(pr.getCampeonatoJogavel().getNome());
		dto.setNivelCampeonato(pr.getCampeonatoJogavel().getNivelCampeonato().name());		
		dto.setPosicao(pr.getPosicao());
		dto.setPontos(pr.getPontos());
		dto.setVitorias(pr.getVitorias());
		dto.setEmpates(pr.getEmpates());
		dto.setDerrotas(pr.getDerrotas());
		dto.setSaldoGols(pr.getSaldoGols());
		dto.setGolsPro(pr.getGolsPro());
		dto.setGolsContra(pr.getGolsContra());
		dto.setNumJogos(pr.getNumJogos());
		
		if (pr.getGrupoCampeonato() != null) {
			dto.setNumeroGrupoCampeonato(pr.getGrupoCampeonato().getNumero());
		}

		return dto;
	}
}
