package com.fastfoot.scheduler.model.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaDisputaPenalties;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
//@Table(indexes = { @Index(columnList = "id_rodada_eliminatoria")})
public class PartidaEliminatoriaResultado implements PartidaResultadoJogavel {
	
	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaSequence")	
	@SequenceGenerator(name = "partidaSequence", sequenceName = "partida_seq")
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
	@JsonIgnore
	private PartidaEliminatoriaResultado proximaPartida;

	@JsonIgnore
	@Column(name = "classifica_a_mandante")
	private Boolean classificaAMandante;
	
	private Boolean partidaJogada;
	
	@OneToOne
	@JoinColumn(name = "id_partida_estatisticas")
	private PartidaEstatisticas partidaEstatisticas;
	
	@OneToOne
	@JoinColumn(name = "id_partida_torcida")
	private PartidaTorcida partidaTorcida;
	
	@OneToOne
	@JoinColumn(name = "id_partida_probabilidade_resultado")
	private PartidaProbabilidadeResultado partidaProbabilidadeResultado;
	
	//private Integer golsMandantePenalts;
	
	//private Integer golsVisitantePenalts;
	
	@OneToOne
	@JoinColumn(name = "id_partida_disputa_penalties")
	private PartidaDisputaPenalties partidaDisputaPenalties;

	@Transient
	private EscalacaoClube escalacaoMandante;

	@Transient
	private EscalacaoClube escalacaoVisitante;

	public PartidaEliminatoriaResultado() {
		this.golsMandante = 0;
		this.golsVisitante = 0;
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

	@Override
	public Boolean getPartidaJogada() {
		return partidaJogada;
	}

	@Override
	public void setPartidaJogada(Boolean partidaJogada) {
		this.partidaJogada = partidaJogada;
	}

	@Override
	public PartidaEstatisticas getPartidaEstatisticas() {
		return partidaEstatisticas;
	}

	@Override
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas) {
		this.partidaEstatisticas = partidaEstatisticas;
	}

	/*
	public Integer getGolsMandantePenalts() {
		return golsMandantePenalts;
	}

	public void setGolsMandantePenalts(Integer golsMandantePenalts) {
		this.golsMandantePenalts = golsMandantePenalts;
	}

	public Integer getGolsVisitantePenalts() {
		return golsVisitantePenalts;
	}

	public void setGolsVisitantePenalts(Integer golsVisitantePenalts) {
		this.golsVisitantePenalts = golsVisitantePenalts;
	}
	*/

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

	@Override
	public EscalacaoClube getEscalacaoMandante() {
		return escalacaoMandante;
	}

	@Override
	public void setEscalacaoMandante(EscalacaoClube escalacaoMandante) {
		this.escalacaoMandante = escalacaoMandante;
	}

	@Override
	public EscalacaoClube getEscalacaoVisitante() {
		return escalacaoVisitante;
	}

	@Override
	public void setEscalacaoVisitante(EscalacaoClube escalacaoVisitante) {
		this.escalacaoVisitante = escalacaoVisitante;
	}

	public PartidaTorcida getPartidaTorcida() {
		return partidaTorcida;
	}

	public void setPartidaTorcida(PartidaTorcida partidaTorcida) {
		this.partidaTorcida = partidaTorcida;
	}

	public PartidaProbabilidadeResultado getPartidaProbabilidadeResultado() {
		return partidaProbabilidadeResultado;
	}

	public void setPartidaProbabilidadeResultado(PartidaProbabilidadeResultado partidaProbabilidadeResultado) {
		this.partidaProbabilidadeResultado = partidaProbabilidadeResultado;
	}

	public PartidaDisputaPenalties getPartidaDisputaPenalties() {
		return partidaDisputaPenalties;
	}

	public void setPartidaDisputaPenalties(PartidaDisputaPenalties partidaDisputaPenalties) {
		this.partidaDisputaPenalties = partidaDisputaPenalties;
	}

	@JsonIgnore
	@Override
	public boolean isAmistoso() {
		return false;
	}
	
	//###	METODOS AUXILIARES	###

	@JsonIgnore
	@Override
	public Clube getClubeVencedor() {
		if (partidaJogada) {
			if (golsMandante > golsVisitante) return clubeMandante;
			if (golsVisitante > golsMandante) return clubeVisitante;		
			if (golsMandante == golsVisitante && getGolsMandantePenalties() > getGolsVisitantePenalties()) return clubeMandante;
			if (golsVisitante == golsMandante && getGolsVisitantePenalties() > getGolsMandantePenalties()) return clubeVisitante;
		}
		return null;
	}

	@JsonIgnore
	@Override
	public Clube getClubePerdedor() {
		if (partidaJogada) {
			if (golsMandante < golsVisitante) return clubeMandante;
			if (golsVisitante < golsMandante) return clubeVisitante;
			if (golsMandante == golsVisitante && getGolsMandantePenalties() < getGolsVisitantePenalties()) return clubeMandante;
			if (golsVisitante == golsMandante && getGolsVisitantePenalties() < getGolsMandantePenalties()) return clubeVisitante;
		}
		return null;
	}
	
	@JsonIgnore
	@Override
	public boolean isResultadoEmpatado() {
		return partidaJogada && (golsMandante == golsVisitante);
	}
	
	@JsonIgnore
	@Override
	public boolean isMandanteVencedor() {
		return partidaJogada && (golsMandante > golsVisitante);
	}
	
	@JsonIgnore
	@Override
	public boolean isVisitanteVencedor() {
		return partidaJogada && (golsMandante < golsVisitante);
	}
	
	@JsonIgnore
	public boolean isMandanteEliminado() {
		if (partidaJogada && golsMandante < golsVisitante) return true;
		if (partidaJogada && golsMandante == golsVisitante && getGolsMandantePenalties() < getGolsVisitantePenalties()) return true;
		return false;
	}
	
	@JsonIgnore
	public boolean isVisitanteEliminado() {
		if (partidaJogada && golsMandante > golsVisitante) return true;
		if (partidaJogada && golsMandante == golsVisitante && getGolsMandantePenalties() > getGolsVisitantePenalties()) return true;
		return false;
	}
	
	private Integer getGolsMandantePenalties() {
		return partidaDisputaPenalties.getGolsMandantePenalties();
	}
	
	private Integer getGolsVisitantePenalties() {
		return partidaDisputaPenalties.getGolsVisitantePenalties();
	}
	
	@JsonIgnore
	@Override
	public boolean isDisputarPenalties() {
		return true;
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
	public void incrementarFinalizacaoDefendida(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarFinalizacaoDefendida(posseBolaMandante);
	}

	@Override
	public void incrementarFinalizacaoFora(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarFinalizacaoFora(posseBolaMandante);
	}

	@Override
	public void incrementarLance(boolean posseBolaMandante) {
		partidaEstatisticas.incrementarLance(posseBolaMandante);
	}
	
	@Override
	public NivelCampeonato getNivelCampeonato() {
		return rodada.getNivelCampeonato();
	}

	@Override
	public String toString() {
		if (getGolsMandantePenalties() != null) {
			return "PartidaEliminatoria [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " "
					+ golsMandante + " (" + getGolsMandantePenalties() + ") " + " x " + " (" + getGolsVisitantePenalties() + ") "
					+ golsVisitante + " " + clubeVisitante.getNome() + "]";
		} else {
			return "PartidaEliminatoria [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " "
					+ golsMandante + " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
		}
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
		PartidaEliminatoriaResultado other = (PartidaEliminatoriaResultado) obj;
		return Objects.equals(id, other.id);
	}
}
