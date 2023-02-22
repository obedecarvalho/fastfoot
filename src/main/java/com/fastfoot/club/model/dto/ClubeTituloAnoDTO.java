package com.fastfoot.club.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.NivelCampeonato;

public class ClubeTituloAnoDTO {
	
	//private String clubeNome;
	
	private Clube clube;

	//private String nivelCampeonato;
	
	private NivelCampeonato nivelCampeonato;
	
	private Integer ano;
	
	public ClubeTituloAnoDTO() {

	}

	public ClubeTituloAnoDTO(Clube clube, NivelCampeonato nivelCampeonato, Integer ano) {
		this.clube = clube;
		this.nivelCampeonato = nivelCampeonato;
		this.ano = ano;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClubeNome(Clube clube) {
		this.clube = clube;
	}

	public NivelCampeonato getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
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
		return new ClubeTituloAnoDTO(clube, nivelCampeonato, ano);
	}
}
