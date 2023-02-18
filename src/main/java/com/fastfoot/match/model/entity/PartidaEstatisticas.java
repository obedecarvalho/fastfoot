package com.fastfoot.match.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity
public class PartidaEstatisticas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaEstatisticasSequence")	
	@SequenceGenerator(name = "partidaEstatisticasSequence", sequenceName = "partida_estatisticas_seq")
	private Long id;
	
	private Integer finalizacacoesForaMandante;
	
	private Integer finalizacacoesForaVisitante;
	
	private Integer finalizacacoesDefendidasMandante;
	
	private Integer finalizacacoesDefendidasVisitante;
	
	private Integer lancesMandante;
	
	private Integer lancesVisitante;
	
	public PartidaEstatisticas() {
		this.finalizacacoesForaMandante = 0;
		this.finalizacacoesForaVisitante = 0;
		this.finalizacacoesDefendidasMandante = 0;
		this.finalizacacoesDefendidasVisitante = 0;
		this.lancesMandante = 0;
		this.lancesVisitante = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFinalizacacoesForaMandante() {
		return finalizacacoesForaMandante;
	}

	public void setFinalizacacoesForaMandante(Integer finalizacacoesForaMandante) {
		this.finalizacacoesForaMandante = finalizacacoesForaMandante;
	}

	public Integer getFinalizacacoesForaVisitante() {
		return finalizacacoesForaVisitante;
	}

	public void setFinalizacacoesForaVisitante(Integer finalizacacoesForaVisitante) {
		this.finalizacacoesForaVisitante = finalizacacoesForaVisitante;
	}

	public Integer getFinalizacacoesDefendidasMandante() {
		return finalizacacoesDefendidasMandante;
	}

	public void setFinalizacacoesDefendidasMandante(Integer finalizacacoesDefendidasMandante) {
		this.finalizacacoesDefendidasMandante = finalizacacoesDefendidasMandante;
	}

	public Integer getFinalizacacoesDefendidasVisitante() {
		return finalizacacoesDefendidasVisitante;
	}

	public void setFinalizacacoesDefendidasVisitante(Integer finalizacacoesDefendidasVisitante) {
		this.finalizacacoesDefendidasVisitante = finalizacacoesDefendidasVisitante;
	}

	public Integer getLancesMandante() {
		return lancesMandante;
	}

	public void setLancesMandante(Integer lancesMandante) {
		this.lancesMandante = lancesMandante;
	}

	public Integer getLancesVisitante() {
		return lancesVisitante;
	}

	public void setLancesVisitante(Integer lancesVisitante) {
		this.lancesVisitante = lancesVisitante;
	}
	
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.finalizacacoesDefendidasMandante++;
		} else {
			this.finalizacacoesDefendidasVisitante++;
		}
	}

	public void incrementarFinalizacaoFora(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.finalizacacoesForaMandante++;
		} else {
			this.finalizacacoesForaVisitante++;
		}
	}

	public void incrementarLance(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.lancesMandante++;
		} else {
			this.lancesVisitante++;
		}
	}
	
	@Transient
	public double getPosseBolaMandante() {
		return lancesMandante.doubleValue() / (lancesMandante + lancesVisitante);
	}
	
	@Transient
	public double getPosseBolaVisitante() {
		return lancesVisitante.doubleValue() / (lancesMandante + lancesVisitante);
	}

	@Override
	public String toString() {
		return "PartidaEstatisticas [lancesMandante=" + lancesMandante + ", lancesVisitante=" + lancesVisitante
				+ ", finalizacacoesForaMandante=" + finalizacacoesForaMandante + ", finalizacacoesForaVisitante="
				+ finalizacacoesForaVisitante + ", finalizacacoesDefendidasMandante=" + finalizacacoesDefendidasMandante
				+ ", finalizacacoesDefendidasVisitante=" + finalizacacoesDefendidasVisitante + "]";
	}

}
