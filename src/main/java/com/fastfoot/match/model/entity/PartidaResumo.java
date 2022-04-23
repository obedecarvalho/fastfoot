package com.fastfoot.match.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Entity
public class PartidaResumo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaResumoSequence")	
	@SequenceGenerator(name = "partidaResumoSequence", sequenceName = "partida_resumo_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;
	
	private Integer finalizacacoesForaMandante;
	
	private Integer finalizacacoesForaVisitante;
	
	private Integer finalizacacoesDefendidasMandante;
	
	private Integer finalizacacoesDefendidasVisitante;
	
	private Integer lancesMandante;
	
	private Integer lancesVisitante;
	
	@Transient
	private List<PartidaLance> partidaLances;

	@Transient
	private List<JogadorGrupoEstatisticas> grupoEstatisticas;

	public PartidaResumo() {
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
	
	public List<PartidaLance> getPartidaLances() {
		return partidaLances;
	}

	public void setPartidaLances(List<PartidaLance> partidaLances) {
		this.partidaLances = partidaLances;
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
	
	public List<JogadorGrupoEstatisticas> getGrupoEstatisticas() {
		return grupoEstatisticas;
	}

	public void setGrupoEstatisticas(List<JogadorGrupoEstatisticas> grupoEstatisticas) {
		this.grupoEstatisticas = grupoEstatisticas;
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

	public void setPartidaResultadoJogavel(PartidaResultadoJogavel partidaResultado) {
		if (partidaResultado instanceof PartidaResultado) {
			setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}
	}
	
	public void addPartidaLance(PartidaLance partidaLance) {
		if (this.partidaLances == null) {
			this.partidaLances = new ArrayList<PartidaLance>(); 
		}
		this.partidaLances.add(partidaLance);
	}
}
