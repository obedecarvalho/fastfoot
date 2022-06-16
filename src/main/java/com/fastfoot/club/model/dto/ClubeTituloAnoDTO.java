package com.fastfoot.club.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.NivelCampeonato;

public class ClubeTituloAnoDTO {
	
	private String clubeNome;

	private String nivelCampeonato;
	
	private Integer ano;
	
	public ClubeTituloAnoDTO() {

	}

	public ClubeTituloAnoDTO(String clubeNome, String nivelCampeonato, Integer ano) {
		this.clubeNome = clubeNome;
		this.nivelCampeonato = nivelCampeonato;
		this.ano = ano;
	}

	public String getClubeNome() {
		return clubeNome;
	}

	public void setClubeNome(String clubeNome) {
		this.clubeNome = clubeNome;
	}

	public String getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(String nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public static List<ClubeTituloAnoDTO> convertToDTO(List<Clube> clubes, NivelCampeonato nivelCampeonato, Integer ano) {
		return clubes.stream().map(p -> convertToDTO(p, nivelCampeonato, ano)).collect(Collectors.toList());
	}

	public static ClubeTituloAnoDTO convertToDTO(Clube clube, NivelCampeonato nivelCampeonato, Integer ano) {
		return new ClubeTituloAnoDTO(clube.getNome(), nivelCampeonato.name(), ano);
	}
}
