package com.fastfoot.scheduler.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;

@Entity
@Table(indexes = { @Index(columnList = "id_semana")})
public class RodadaAmistosa implements RodadaJogavel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rodadaAmistosaSequence")	
	@SequenceGenerator(name = "rodadaAmistosaSequence", sequenceName = "rodada_amistosa_seq")
	private Long id;
	
	private Integer numero;
	
	/*@ManyToOne
	@JoinColumn(name = "id_campeonato")
	private Campeonato campeonato;*/

	/*@ManyToOne
	@JoinColumn(name = "id_grupo_campeonato")
	private GrupoCampeonato grupoCampeonato;*/
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;

	@Transient
	private List<PartidaAmistosaResultado> partidas;

	public RodadaAmistosa() {

	}
	
	public RodadaAmistosa(Integer numero) {
		this.numero = numero;
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

	/*public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}*/

	/*public GrupoCampeonato getGrupoCampeonato() {
		return grupoCampeonato;
	}

	public void setGrupoCampeonato(GrupoCampeonato grupoCampeonato) {
		this.grupoCampeonato = grupoCampeonato;
	}*/

	@Override
	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	@Override
	public boolean isUltimaRodadaPontosCorridos() {
		/*if(getCampeonato() != null && getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
			return true;
		}
		if (getGrupoCampeonato() != null && getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
			return true;
		}*/
		return false;
	}
	
	@Override
	public CampeonatoJogavel getCampeonatoJogavel() {
		/*if (campeonato != null) return campeonato;
		if (grupoCampeonato != null) return grupoCampeonato.getCampeonato();*/
		return null;
	}

	@Override
	public String toString() {
		return "RodadaAmistosa [sem=" + (semana != null ? semana.getNumero() : "") + "]";
	}
}
