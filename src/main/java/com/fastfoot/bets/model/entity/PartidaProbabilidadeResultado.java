package com.fastfoot.bets.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Entity
public class PartidaProbabilidadeResultado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaProbabilidadeResultadoSequence")	
	@SequenceGenerator(name = "partidaProbabilidadeResultadoSequence", sequenceName = "partida_probabilidade_resultado_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;
	
	private Double probabilidadeVitoriaMandante;
	
	private Double probabilidadeVitoriaVisitante;
	
	private Double probabilidadeEmpate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartidaResultado getPartidaResultado() {
		return partidaResultado;
	}

	public void setPartidaResultado(PartidaResultado partidaResultado) {
		this.partidaResultado = partidaResultado;
	}

	public PartidaEliminatoriaResultado getPartidaEliminatoriaResultado() {
		return partidaEliminatoriaResultado;
	}

	public void setPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado) {
		this.partidaEliminatoriaResultado = partidaEliminatoriaResultado;
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

	public PartidaResultadoJogavel getPartidaResultadoJogavel() {
		PartidaResultadoJogavel partidaResultadoJogavel = null;

		if (partidaResultado != null) {
			partidaResultadoJogavel = partidaResultado;
		} else if (partidaEliminatoriaResultado != null) {
			partidaResultadoJogavel = partidaEliminatoriaResultado;
		}

		return partidaResultadoJogavel;
	}

	@Override
	public String toString() {
		return "PartidaProbabilidadeResultado [Partida=" + getPartidaResultadoJogavel()
				+ ", probVitMand=" + probabilidadeVitoriaMandante + ", probVitVis="
				+ probabilidadeVitoriaVisitante + ", probEmp=" + probabilidadeEmpate + "]";
	}
}
