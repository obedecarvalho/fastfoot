package com.fastfoot.scheduler.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.RodadaJogavel;

@Entity
/*@Table(indexes = { @Index(columnList = "id_semana"), @Index(columnList = "id_campeonato"),
		@Index(columnList = "id_grupo_campeonato") })*/
public class Rodada implements RodadaJogavel {

	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rodadaSequence")	
	@SequenceGenerator(name = "rodadaSequence", sequenceName = "rodada_seq")
	private Long id;
	
	private Integer numero;
	
	@ManyToOne
	@JoinColumn(name = "id_campeonato")
	private Campeonato campeonato;

	@ManyToOne
	@JoinColumn(name = "id_grupo_campeonato")
	private GrupoCampeonato grupoCampeonato;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;

	@Transient
	private List<PartidaResultado> partidas;

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
	public List<PartidaResultado> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<PartidaResultado> partidas) {
		this.partidas = partidas;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public GrupoCampeonato getGrupoCampeonato() {
		return grupoCampeonato;
	}

	public void setGrupoCampeonato(GrupoCampeonato grupoCampeonato) {
		this.grupoCampeonato = grupoCampeonato;
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
		if(getCampeonato() != null && getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
			return true;
		}
		if (getGrupoCampeonato() != null && getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
			return true;
		}
		return false;
	}
	
	@JsonIgnore
	@Override
	public CampeonatoJogavel getCampeonatoJogavel() {
		if (campeonato != null) return campeonato;
		if (grupoCampeonato != null) return grupoCampeonato.getCampeonato();
		return null;
	}
	
	@Override
	public NivelCampeonato getNivelCampeonato() {
		return getCampeonatoJogavel() != null ? getCampeonatoJogavel().getNivelCampeonato() : null;
	}
	
	@Override
	public boolean isAmistoso() {
		return false;
	}

	@Override
	public String toString() {
		return "Rodada [" + numero + ", sem=" + (semana != null ? semana.getNumero() : "") + (campeonato != null ? ", (NAC)" : ", (CONT)") + "]";
	}
}
