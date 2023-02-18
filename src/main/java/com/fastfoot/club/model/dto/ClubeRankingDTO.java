package com.fastfoot.club.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.ClubeRanking;

public class ClubeRankingDTO {

	private String clubeNome;
	
	private Integer ano;
	
	private String classificacaoNacional;
	
	private String classificacaoCopaNacional;
	
	private String classificacaoContinental;

	private Integer posicaoGeral;
	
	public String getClubeNome() {
		return clubeNome;
	}

	public void setClubeNome(String clubeNome) {
		this.clubeNome = clubeNome;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(String classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public String getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(String classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public String getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(String classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public Integer getPosicaoGeral() {
		return posicaoGeral;
	}

	public void setPosicaoGeral(Integer posicaoGeral) {
		this.posicaoGeral = posicaoGeral;
	}

	public static List<ClubeRankingDTO> convertToDTO(List<ClubeRanking> clube) {
		return clube.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}

	public static ClubeRankingDTO convertToDTO(ClubeRanking clubeRanking) {
		ClubeRankingDTO dto = new ClubeRankingDTO();
		
		dto.setClubeNome(clubeRanking.getClube().getNome());
		dto.setAno(clubeRanking.getAno());
		dto.setPosicaoGeral(clubeRanking.getPosicaoGeral());
		dto.setClassificacaoNacional(clubeRanking.getClassificacaoNacional().name());
		dto.setClassificacaoCopaNacional(clubeRanking.getClassificacaoCopaNacional().name());
		dto.setClassificacaoContinental(clubeRanking.getClassificacaoContinental().name());
		
		return dto;
	}
}
