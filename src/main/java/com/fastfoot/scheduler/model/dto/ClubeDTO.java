package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.club.model.entity.Clube;

public class ClubeDTO {

	private Integer idClube;
	
	private String nomeClube;

	public Integer getIdClube() {
		return idClube;
	}

	public void setIdClube(Integer idClube) {
		this.idClube = idClube;
	}

	public String getNomeClube() {
		return nomeClube;
	}

	public void setNomeClube(String nomeClube) {
		this.nomeClube = nomeClube;
	}
	
	public static List<ClubeDTO> convertToDTO(List<Clube> clube) {
		return clube.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}
	
	public static ClubeDTO convertToDTO(Clube clube){
		ClubeDTO dto = new ClubeDTO();
		dto.setIdClube(clube.getId());
		dto.setNomeClube(clube.getNome());
		return dto;
	}
}
