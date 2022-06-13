package com.fastfoot.probability.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.scheduler.model.dto.ClassificacaoDTO;

public class ClubeProbabilidadeDTO {
	
	private String nomeClube;
	
	private String probabilidadeCampeao;
	
	private String probabilidadeRebaixamento;
	
	private String probabilidadeAcesso;

	private String probabilidadeClassificacaoContinental;
	
	private String probabilidadeClassificacaoCopaNacional;
	
	private ClassificacaoDTO classificacao;

	public String getNomeClube() {
		return nomeClube;
	}

	public void setNomeClube(String nomeClube) {
		this.nomeClube = nomeClube;
	}

	public String getProbabilidadeCampeao() {
		return probabilidadeCampeao;
	}

	public void setProbabilidadeCampeao(String probabilidadeCampeao) {
		this.probabilidadeCampeao = probabilidadeCampeao;
	}

	public String getProbabilidadeRebaixamento() {
		return probabilidadeRebaixamento;
	}

	public void setProbabilidadeRebaixamento(String probabilidadeRebaixamento) {
		this.probabilidadeRebaixamento = probabilidadeRebaixamento;
	}

	public String getProbabilidadeAcesso() {
		return probabilidadeAcesso;
	}

	public void setProbabilidadeAcesso(String probabilidadeAcesso) {
		this.probabilidadeAcesso = probabilidadeAcesso;
	}

	public String getProbabilidadeClassificacaoContinental() {
		return probabilidadeClassificacaoContinental;
	}

	public void setProbabilidadeClassificacaoContinental(String probabilidadeClassificacaoContinental) {
		this.probabilidadeClassificacaoContinental = probabilidadeClassificacaoContinental;
	}

	public String getProbabilidadeClassificacaoCopaNacional() {
		return probabilidadeClassificacaoCopaNacional;
	}

	public void setProbabilidadeClassificacaoCopaNacional(String probabilidadeClassificacaoCopaNacional) {
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
		
		dto.setProbabilidadeAcesso(cp.getProbabilidadeAcessoDescricao());
		
		dto.setProbabilidadeCampeao(cp.getProbabilidadeCampeaoDescricao());
		
		dto.setProbabilidadeClassificacaoContinental(cp.getProbabilidadeClassificacaoContinentalDescricao());
		
		dto.setProbabilidadeClassificacaoCopaNacional(cp.getProbabilidadeClassificacaoCopaNacionalDescricao());
		
		dto.setProbabilidadeRebaixamento(cp.getProbabilidadeRebaixamentoDescricao());
		
		return dto;
	}
}
