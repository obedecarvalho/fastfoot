package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.entity.Semana;

public class SemanaDTO {

	private Integer numero;

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public static List<SemanaDTO> convertToDTO(List<Semana> semanas) {
		return semanas.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}
	
	public static SemanaDTO convertToDTO(Semana s) {
		SemanaDTO dto = new SemanaDTO();
		dto.setNumero(s.getNumero());
		return dto;
	}
}
