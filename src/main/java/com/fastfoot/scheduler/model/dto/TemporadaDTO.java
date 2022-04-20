package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.entity.Temporada;

public class TemporadaDTO {
	
	private Integer ano;
	
	private Integer semanaAtual;
	
	private Boolean atual;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getSemanaAtual() {
		return semanaAtual;
	}

	public void setSemanaAtual(Integer semanaAtual) {
		this.semanaAtual = semanaAtual;
	}
	
	public Boolean getAtual() {
		return atual;
	}

	public void setAtual(Boolean atual) {
		this.atual = atual;
	}

	public static List<TemporadaDTO> convertToDTO(List<Temporada> temporadas) {
		return temporadas.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}

	public static TemporadaDTO convertToDTO(Temporada t){
		TemporadaDTO dto = new TemporadaDTO();
		dto.setAno(t.getAno());
		dto.setSemanaAtual(t.getSemanaAtual());		
		dto.setAtual(t.getAtual());
		return dto;
	}

}
