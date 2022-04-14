package com.fastfoot.scheduler.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.Rodada;

public class PartidaResultadoDTO {

	private Integer numeroRodada;
	
	private Integer numeroSemana;
	
	private Integer anoTemporada;
	
	private Integer numeroSemanaAtualTemporada;
	
	private Boolean temporadaAtual;
	
	private String nomeClubeMandante;
	
	private String nomeClubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;
	
	private String nomeCampeonato;
	
	private String nivelCampeonato;
	
	private Integer numeroGrupoCampeonato;
		
	public Integer getNumeroRodada() {
		return numeroRodada;
	}

	public void setNumeroRodada(Integer numeroRodada) {
		this.numeroRodada = numeroRodada;
	}

	public Integer getNumeroSemana() {
		return numeroSemana;
	}

	public void setNumeroSemana(Integer numeroSemana) {
		this.numeroSemana = numeroSemana;
	}

	public Integer getAnoTemporada() {
		return anoTemporada;
	}

	public void setAnoTemporada(Integer anoTemporada) {
		this.anoTemporada = anoTemporada;
	}

	public Integer getNumeroSemanaAtualTemporada() {
		return numeroSemanaAtualTemporada;
	}

	public void setNumeroSemanaAtualTemporada(Integer numeroSemanaAtualTemporada) {
		this.numeroSemanaAtualTemporada = numeroSemanaAtualTemporada;
	}

	public Boolean getTemporadaAtual() {
		return temporadaAtual;
	}

	public void setTemporadaAtual(Boolean temporadaAtual) {
		this.temporadaAtual = temporadaAtual;
	}

	public String getNomeClubeMandante() {
		return nomeClubeMandante;
	}

	public void setNomeClubeMandante(String nomeClubeMandante) {
		this.nomeClubeMandante = nomeClubeMandante;
	}

	public String getNomeClubeVisitante() {
		return nomeClubeVisitante;
	}

	public void setNomeClubeVisitante(String nomeClubeVisitante) {
		this.nomeClubeVisitante = nomeClubeVisitante;
	}

	public Integer getGolsMandante() {
		return golsMandante;
	}

	public void setGolsMandante(Integer golsMandante) {
		this.golsMandante = golsMandante;
	}

	public Integer getGolsVisitante() {
		return golsVisitante;
	}

	public void setGolsVisitante(Integer golsVisitante) {
		this.golsVisitante = golsVisitante;
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

	public Integer getNumeroGrupoCampeonato() {
		return numeroGrupoCampeonato;
	}

	public void setNumeroGrupoCampeonato(Integer numeroGrupoCampeonato) {
		this.numeroGrupoCampeonato = numeroGrupoCampeonato;
	}
	
	public static List<PartidaResultadoDTO> convertToDTO(List<? extends PartidaResultadoJogavel> partidas) {
		return partidas.stream().map(p -> convertToDTO(p)).collect(Collectors.toList());
	}

	public static PartidaResultadoDTO convertToDTO(PartidaResultadoJogavel pr) {
		
		PartidaResultadoDTO dto = new PartidaResultadoDTO();

		if (pr.getClubeMandante() != null) {
			dto.setNomeClubeMandante(pr.getClubeMandante().getNome());
		}
		if (pr.getClubeVisitante() != null) {
			dto.setNomeClubeVisitante(pr.getClubeVisitante().getNome());
		}
		dto.setGolsMandante(pr.getGolsMandante());
		dto.setGolsVisitante(pr.getGolsVisitante());

		dto.setNumeroRodada(pr.getRodada().getNumero());

		dto.setNumeroSemana(pr.getRodada().getSemana().getNumero());

		dto.setTemporadaAtual(pr.getRodada().getSemana().getTemporada().getAtual());
		dto.setAnoTemporada(pr.getRodada().getSemana().getTemporada().getAno());
		dto.setNumeroSemanaAtualTemporada(pr.getRodada().getSemana().getTemporada().getSemanaAtual());

		dto.setNivelCampeonato(pr.getRodada().getCampeonatoJogavel().getNivelCampeonato().name());
		dto.setNomeCampeonato(pr.getRodada().getCampeonatoJogavel().getNome());

		if (pr.getRodada() instanceof Rodada && ((Rodada)pr.getRodada()).getGrupoCampeonato() != null) {
			dto.setNumeroGrupoCampeonato(((Rodada)pr.getRodada()).getGrupoCampeonato().getNumero());
		}

		return dto;
	}
}
