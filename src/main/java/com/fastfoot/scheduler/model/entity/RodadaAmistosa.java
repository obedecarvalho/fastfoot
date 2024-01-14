package com.fastfoot.scheduler.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.NivelCampeonatoAttributeConverter;
import com.fastfoot.scheduler.model.RodadaJogavel;

@Entity
//@Table(indexes = { @Index(columnList = "id_semana")})
public class RodadaAmistosa implements RodadaJogavel {

	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rodadaSequence")	
	@SequenceGenerator(name = "rodadaSequence", sequenceName = "rodada_seq")
	private Long id;
	
	private Integer numero;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	@Convert(converter = NivelCampeonatoAttributeConverter.class)
	private NivelCampeonato nivelCampeonato;

	@Transient
	private List<PartidaAmistosaResultado> partidas;

	public RodadaAmistosa() {

	}
	
	public RodadaAmistosa(Integer numero, NivelCampeonato nivelCampeonato) {
		this.numero = numero;
		this.nivelCampeonato = nivelCampeonato;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public List<PartidaAmistosaResultado> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<PartidaAmistosaResultado> partidas) {
		this.partidas = partidas;
	}
	
	public void addAllPartidas(List<PartidaAmistosaResultado> partidas) {
		if (this.partidas == null) {
			this.partidas = new ArrayList<PartidaAmistosaResultado>();
		}
		this.partidas.addAll(partidas);
	}

	@Override
	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	@Override
	public boolean isUltimaRodadaPontosCorridos() {
		return false;
	}
	
	@JsonIgnore
	@Override
	public CampeonatoJogavel getCampeonatoJogavel() {
		return null;
	}
	
	@Override
	public NivelCampeonato getNivelCampeonato() {
		return getCampeonatoJogavel() != null ? getCampeonatoJogavel().getNivelCampeonato() : this.nivelCampeonato;
	}
	
	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	@Override
	public boolean isAmistoso() {
		return true;
	}

	@Override
	public String toString() {
		return "RodadaAmistosa [sem=" + (semana != null ? semana.getNumero() : "") + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RodadaAmistosa other = (RodadaAmistosa) obj;
		return Objects.equals(id, other.id);
	}
}
