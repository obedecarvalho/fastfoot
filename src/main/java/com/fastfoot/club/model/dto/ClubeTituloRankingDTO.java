package com.fastfoot.club.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.ClubeTituloRanking;

public class ClubeTituloRankingDTO {
	
	private String clubeNome;
	
	private Integer titulosNacional;
	
	private Integer titulosNacionalII;
	
	private Integer titulosCopaNacional;
	
	private Integer titulosCopaNacionalII;
	
	private Integer titulosContinental;
	
	private Integer titulosContinentalII;
	
	private Integer titulosContinentalIII;
	
	private Integer pontuacao;
	
	public String getClubeNome() {
		return clubeNome;
	}

	public void setClubeNome(String clubeNome) {
		this.clubeNome = clubeNome;
	}

	public Integer getTitulosNacional() {
		return titulosNacional;
	}

	public void setTitulosNacional(Integer titulosNacional) {
		this.titulosNacional = titulosNacional;
	}

	public Integer getTitulosNacionalII() {
		return titulosNacionalII;
	}

	public void setTitulosNacionalII(Integer titulosNacionalII) {
		this.titulosNacionalII = titulosNacionalII;
	}

	public Integer getTitulosCopaNacional() {
		return titulosCopaNacional;
	}

	public void setTitulosCopaNacional(Integer titulosCopaNacional) {
		this.titulosCopaNacional = titulosCopaNacional;
	}

	public Integer getTitulosCopaNacionalII() {
		return titulosCopaNacionalII;
	}

	public void setTitulosCopaNacionalII(Integer titulosCopaNacionalII) {
		this.titulosCopaNacionalII = titulosCopaNacionalII;
	}

	public Integer getTitulosContinental() {
		return titulosContinental;
	}

	public void setTitulosContinental(Integer titulosContinental) {
		this.titulosContinental = titulosContinental;
	}

	public Integer getTitulosContinentalII() {
		return titulosContinentalII;
	}

	public void setTitulosContinentalII(Integer titulosContinentalII) {
		this.titulosContinentalII = titulosContinentalII;
	}

	public Integer getTitulosContinentalIII() {
		return titulosContinentalIII;
	}

	public void setTitulosContinentalIII(Integer titulosContinentalIII) {
		this.titulosContinentalIII = titulosContinentalIII;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public static List<ClubeTituloRankingDTO> convertToDTO(List<ClubeTituloRanking> partidas) {
		return partidas.stream().map(p -> convertToDTO(p)).collect(Collectors.toList());
	}
	
	public static ClubeTituloRankingDTO convertToDTO(ClubeTituloRanking pr) {
		
		ClubeTituloRankingDTO dto = new ClubeTituloRankingDTO();
		
		dto.setClubeNome(pr.getClube().getNome());
		
		dto.setTitulosContinental(pr.getTitulosContinental());
		dto.setTitulosContinentalII(pr.getTitulosContinentalII());
		dto.setTitulosContinentalIII(pr.getTitulosContinentalIII());
		
		dto.setTitulosCopaNacional(pr.getTitulosCopaNacional());
		dto.setTitulosCopaNacionalII(pr.getTitulosCopaNacionalII());
		
		dto.setTitulosNacional(pr.getTitulosNacional());
		dto.setTitulosNacionalII(pr.getTitulosNacionalII());
		
		dto.setPontuacao(pr.getPontuacao());
		
		return dto;
	}
}
