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

import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;

@Entity
public class RodadaEliminatoria implements RodadaJogavel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rodadaEliminatoriaSequence")	
	@SequenceGenerator(name = "rodadaEliminatoriaSequence", sequenceName = "rodada_eliminatoria_seq")
	private Long id;
	
	private Integer numero;
	
	@ManyToOne
	@JoinColumn(name = "id_campeonato_eliminatorio")
	private CampeonatoEliminatorio campeonatoEliminatorio;

	@ManyToOne
	@JoinColumn(name = "id_campeonato_misto")
	private CampeonatoMisto campeonatoMisto;
	
	@ManyToOne
	@JoinColumn(name = "id_proxima_rodada")
	private RodadaEliminatoria proximaRodada;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	@Transient
	private List<PartidaEliminatoriaResultado> partidas;

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

	public CampeonatoEliminatorio getCampeonatoEliminatorio() {
		return campeonatoEliminatorio;
	}

	public void setCampeonatoEliminatorio(CampeonatoEliminatorio campeonatoEliminatorio) {
		this.campeonatoEliminatorio = campeonatoEliminatorio;
	}

	public List<PartidaEliminatoriaResultado> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<PartidaEliminatoriaResultado> partidas) {
		this.partidas = partidas;
	}

	public RodadaEliminatoria getProximaRodada() {
		return proximaRodada;
	}

	public void setProximaRodada(RodadaEliminatoria proximaRodada) {
		this.proximaRodada = proximaRodada;
	}

	public CampeonatoMisto getCampeonatoMisto() {
		return campeonatoMisto;
	}

	public void setCampeonatoMisto(CampeonatoMisto campeonatoMisto) {
		this.campeonatoMisto = campeonatoMisto;
	}

	@Override
	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}
	
	@Override
	public CampeonatoJogavel getCampeonatoJogavel() {
		if (campeonatoEliminatorio != null) return campeonatoEliminatorio;
		if (campeonatoMisto != null) return campeonatoMisto;
		return null;
	}

	@Override
	public String toString() {
		return "RodadaEliminatoria [" + numero + ", sem=" + semana.getNumero() + (campeonatoEliminatorio != null ? ", (CN)" : ", (CONT)" ) + "]";
	}

	@Override
	public boolean isUltimaRodadaPontosCorridos() {
		return false;
	}

}
