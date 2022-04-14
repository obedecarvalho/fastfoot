package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.CampeonatoJogavel;

public class CampeonatoDTO {

	private Long idCampeonato;
	
	private String nomeCampeonato;
	
	private String nivelCampeonato;
	
	public Long getIdCampeonato() {
		return idCampeonato;
	}

	public void setIdCampeonato(Long idCampeonato) {
		this.idCampeonato = idCampeonato;
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
	
	public static List<CampeonatoDTO> convertToDTO(List<? extends CampeonatoJogavel> campeonatos) {
		return campeonatos.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}

	public static CampeonatoDTO convertToDTO(CampeonatoJogavel campeonatoJogavel) {
		CampeonatoDTO dto = new CampeonatoDTO();
		dto.setIdCampeonato(campeonatoJogavel.getId());
		dto.setNivelCampeonato(campeonatoJogavel.getNivelCampeonato().name());

		//dto.setNomeCampeonato(campeonatoJogavel.getNome());
		dto.setNomeCampeonato((campeonatoJogavel.getLiga() != null ? campeonatoJogavel.getLiga().name() + "-" : "")
				+ campeonatoJogavel.getNivelCampeonato().name());
		return dto;
	}
}
