package com.fastfoot.probability.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;

public class ClubeProbabilidadeDTO {
	
	private String nomeClube;
	
	private Integer probabilidadeCampeao;
	
	private Integer probabilidadeRebaixamento;
	
	private Integer probabilidadeAcesso;

	private Integer probabilidadeClassificacaoContinental;
	
	private Integer probabilidadeClassificacaoCopaNacional;
	
	private ClassificacaoDTO classificacao;

	public String getNomeClube() {
		return nomeClube;
	}

	public void setNomeClube(String nomeClube) {
		this.nomeClube = nomeClube;
	}

	public Integer getProbabilidadeCampeao() {
		return probabilidadeCampeao;
	}

	public void setProbabilidadeCampeao(Integer probabilidadeCampeao) {
		this.probabilidadeCampeao = probabilidadeCampeao;
	}

	public Integer getProbabilidadeRebaixamento() {
		return probabilidadeRebaixamento;
	}

	public void setProbabilidadeRebaixamento(Integer probabilidadeRebaixamento) {
		this.probabilidadeRebaixamento = probabilidadeRebaixamento;
	}

	public Integer getProbabilidadeAcesso() {
		return probabilidadeAcesso;
	}

	public void setProbabilidadeAcesso(Integer probabilidadeAcesso) {
		this.probabilidadeAcesso = probabilidadeAcesso;
	}

	public Integer getProbabilidadeClassificacaoContinental() {
		return probabilidadeClassificacaoContinental;
	}

	public void setProbabilidadeClassificacaoContinental(Integer probabilidadeClassificacaoContinental) {
		this.probabilidadeClassificacaoContinental = probabilidadeClassificacaoContinental;
	}

	public Integer getProbabilidadeClassificacaoCopaNacional() {
		return probabilidadeClassificacaoCopaNacional;
	}

	public void setProbabilidadeClassificacaoCopaNacional(Integer probabilidadeClassificacaoCopaNacional) {
		this.probabilidadeClassificacaoCopaNacional = probabilidadeClassificacaoCopaNacional;
	}

	public ClassificacaoDTO getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoDTO classificacao) {
		this.classificacao = classificacao;
	}

	public static List<ClubeProbabilidadeDTO> convertToDTO(List<ClubeProbabilidade> partidas) {
		return partidas.stream().map(p -> convertToDTO(p)).collect(Collectors.toList());
	}

	public static ClubeProbabilidadeDTO convertToDTO(ClubeProbabilidade cp) {
		ClubeProbabilidadeDTO dto = new ClubeProbabilidadeDTO();
		
		dto.setNomeClube(cp.getClube().getNome());
		
		dto.setProbabilidadeAcesso(cp.getProbabilidadeAcesso());
		
		dto.setProbabilidadeCampeao(cp.getProbabilidadeCampeao());
		
		dto.setProbabilidadeClassificacaoContinental(cp.getProbabilidadeClassificacaoContinental());
		
		dto.setProbabilidadeClassificacaoCopaNacional(cp.getProbabilidadeClassificacaoCopaNacional());
		
		dto.setProbabilidadeRebaixamento(cp.getProbabilidadeRebaixamento());
		
		return dto;
	}
}
