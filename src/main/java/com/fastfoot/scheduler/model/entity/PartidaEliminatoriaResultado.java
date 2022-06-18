package com.fastfoot.scheduler.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
@Table(indexes = { @Index(columnList = "id_rodada_eliminatoria")})
public class PartidaEliminatoriaResultado implements PartidaResultadoJogavel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaEliminatoriaResultadoSequence")	
	@SequenceGenerator(name = "partidaEliminatoriaResultadoSequence", sequenceName = "partida_eliminatoria_resultado_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_rodada_eliminatoria")
	private RodadaEliminatoria rodada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_mandante")
	private Clube clubeMandante;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_visitante")
	private Clube clubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;
	
	@ManyToOne
	@JoinColumn(name = "id_proxima_partida")
	private PartidaEliminatoriaResultado proximaPartida;

	private Boolean classificaAMandante;
	
	private Integer finalizacacoesForaMandante;
	
	private Integer finalizacacoesForaVisitante;
	
	private Integer finalizacacoesDefendidasMandante;
	
	private Integer finalizacacoesDefendidasVisitante;
	
	private Integer lancesMandante;
	
	private Integer lancesVisitante;
	
	private Boolean partidaJogada;

	public PartidaEliminatoriaResultado() {
		this.golsMandante = 0;
		this.golsVisitante = 0;
		this.finalizacacoesForaMandante = 0;
		this.finalizacacoesForaVisitante = 0;
		this.finalizacacoesDefendidasMandante = 0;
		this.finalizacacoesDefendidasVisitante = 0;
		this.lancesMandante = 0;
		this.lancesVisitante = 0;
		this.partidaJogada = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public RodadaEliminatoria getRodada() {
		return rodada;
	}

	public void setRodada(RodadaEliminatoria rodada) {
		this.rodada = rodada;
	}

	@Override
	public Clube getClubeMandante() {
		return clubeMandante;
	}

	public void setClubeMandante(Clube clubeMandante) {
		this.clubeMandante = clubeMandante;
	}

	@Override
	public Clube getClubeVisitante() {
		return clubeVisitante;
	}

	public void setClubeVisitante(Clube clubeVisitante) {
		this.clubeVisitante = clubeVisitante;
	}

	@Override
	public Integer getGolsMandante() {
		return golsMandante;
	}

	@Override
	public void setGolsMandante(Integer golsMandante) {
		this.golsMandante = golsMandante;
	}

	@Override
	public Integer getGolsVisitante() {
		return golsVisitante;
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

	@Override
	public Boolean getPartidaJogada() {
		return partidaJogada;
	}

	@Override
	public void setPartidaJogada(Boolean partidaJogada) {
		this.partidaJogada = partidaJogada;
	}

	@Override
	public void setGolsVisitante(Integer golsVisitante) {
		this.golsVisitante = golsVisitante;
	}

	public PartidaEliminatoriaResultado getProximaPartida() {
		return proximaPartida;
	}

	public void setProximaPartida(PartidaEliminatoriaResultado proximaPartida) {
		this.proximaPartida = proximaPartida;
	}
	
	public Boolean getClassificaAMandante() {
		return classificaAMandante;
	}

	public void setClassificaAMandante(Boolean classificaAMandante) {
		this.classificaAMandante = classificaAMandante;
	}
	
	//###	METODOS AUXILIARES	###

	@Override
	public Clube getClubeVencedor() {
		if (golsMandante >= golsVisitante) return clubeMandante;
		if (golsVisitante > golsMandante) return clubeVisitante;
		//TODO: penalts
		return null;
	}

	@Override
	public Clube getClubePerdedor() {
		if (golsMandante < golsVisitante) return clubeMandante;
		if (golsVisitante <= golsMandante) return clubeVisitante;
		//TODO: penalts
		return null;
	}

	@Override
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.finalizacacoesDefendidasMandante++;
		} else {
			this.finalizacacoesDefendidasVisitante++;
		}
	}

	@Override
	public void incrementarFinalizacaoFora(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.finalizacacoesForaMandante++;
		} else {
			this.finalizacacoesForaVisitante++;
		}
	}

	@Override
	public void incrementarLance(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.lancesMandante++;
		} else {
			this.lancesVisitante++;
		}
	}

	@Override
	public void incrementarGol(boolean posseBolaMandante) {
		if (posseBolaMandante) {
			this.golsMandante++;
		} else {
			this.golsVisitante++;
		}
	}

	@Override
	public String toString() {
		return "PartidaEliminatoria [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " "
				+ golsMandante + " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
	}
}
