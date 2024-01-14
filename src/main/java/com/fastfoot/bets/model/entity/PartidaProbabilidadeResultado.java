package com.fastfoot.bets.model.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
public class PartidaProbabilidadeResultado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaProbabilidadeResultadoSequence")	
	@SequenceGenerator(name = "partidaProbabilidadeResultadoSequence", sequenceName = "partida_probabilidade_resultado_seq")
	private Long id;
	
	private Double probabilidadeVitoriaMandante;
	
	private Double probabilidadeVitoriaVisitante;
	
	private Double probabilidadeEmpate;
	
	private TipoProbabilidadeResultadoPartida tipoProbabilidadeResultadoPartida;
	
	@Transient
	private PartidaResultadoJogavel partida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getProbabilidadeVitoriaMandante() {
		return probabilidadeVitoriaMandante;
	}

	public void setProbabilidadeVitoriaMandante(Double probabilidadeVitoriaMandante) {
		this.probabilidadeVitoriaMandante = probabilidadeVitoriaMandante;
	}

	public Double getProbabilidadeVitoriaVisitante() {
		return probabilidadeVitoriaVisitante;
	}

	public void setProbabilidadeVitoriaVisitante(Double probabilidadeVitoriaVisitante) {
		this.probabilidadeVitoriaVisitante = probabilidadeVitoriaVisitante;
	}

	public Double getProbabilidadeEmpate() {
		return probabilidadeEmpate;
	}

	public void setProbabilidadeEmpate(Double probabilidadeEmpate) {
		this.probabilidadeEmpate = probabilidadeEmpate;
	}

	public PartidaResultadoJogavel getPartida() {
		return partida;
	}

	public void setPartida(PartidaResultadoJogavel partida) {
		this.partida = partida;
	}

	public TipoProbabilidadeResultadoPartida getTipoProbabilidadeResultadoPartida() {
		return tipoProbabilidadeResultadoPartida;
	}

	public void setTipoProbabilidadeResultadoPartida(TipoProbabilidadeResultadoPartida tipoProbabilidadeResultadoPartida) {
		this.tipoProbabilidadeResultadoPartida = tipoProbabilidadeResultadoPartida;
	}

	/*public PartidaResultadoJogavel getPartidaResultadoJogavel() {
		PartidaResultadoJogavel partidaResultadoJogavel = null;

		if (partidaResultado != null) {
			partidaResultadoJogavel = partidaResultado;
		} else if (partidaEliminatoriaResultado != null) {
			partidaResultadoJogavel = partidaEliminatoriaResultado;
		}

		return partidaResultadoJogavel;
	}*/

	@Override
	public String toString() {
		return "PartidaProbabilidadeResultado ["
				+ "probVitMand=" + probabilidadeVitoriaMandante + ", probVitVis="
				+ probabilidadeVitoriaVisitante + ", probEmp=" + probabilidadeEmpate + "]";
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
		PartidaProbabilidadeResultado other = (PartidaProbabilidadeResultado) obj;
		return Objects.equals(id, other.id);
	}
}
