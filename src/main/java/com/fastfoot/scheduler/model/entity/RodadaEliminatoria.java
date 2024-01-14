package com.fastfoot.scheduler.model.entity;

import java.util.List;
import java.util.Objects;

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
import com.fastfoot.scheduler.model.RodadaJogavel;

@Entity
/*@Table(indexes = { @Index(columnList = "id_semana"), @Index(columnList = "id_campeonato_eliminatorio"),
		@Index(columnList = "id_campeonato_misto") })*/
public class RodadaEliminatoria implements RodadaJogavel {

	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rodadaSequence")	
	@SequenceGenerator(name = "rodadaSequence", sequenceName = "rodada_seq")
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
	@JsonIgnore
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

	@Override
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
	
	@JsonIgnore
	@Override
	public CampeonatoJogavel getCampeonatoJogavel() {
		if (campeonatoEliminatorio != null) return campeonatoEliminatorio;
		if (campeonatoMisto != null) return campeonatoMisto;
		return null;
	}
	
	@Override
	public NivelCampeonato getNivelCampeonato() {
		return getCampeonatoJogavel() != null ? getCampeonatoJogavel().getNivelCampeonato() : null;
	}

	@Override
	public String toString() {
		return "RodadaEliminatoria [" + numero + ", sem=" + semana.getNumero()
				+ (campeonatoEliminatorio != null ? ", (CN)" : ", (CONT)") + "]";
	}

	@Override
	public boolean isUltimaRodadaPontosCorridos() {
		return false;
	}

	@Override
	public boolean isAmistoso() {
		return false;
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
		RodadaEliminatoria other = (RodadaEliminatoria) obj;
		return Objects.equals(id, other.id);
	}
}
