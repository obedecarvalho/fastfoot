package com.fastfoot.scheduler.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
//@Table(indexes = { @Index(columnList = "id_rodada_amistosa") })
public class PartidaAmistosaResultado implements PartidaResultadoJogavel {

	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaSequence")	
	@SequenceGenerator(name = "partidaSequence", sequenceName = "partida_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_rodada_amistosa")
	private RodadaAmistosa rodada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_mandante")
	private Clube clubeMandante;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_visitante")
	private Clube clubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;
	
	private Boolean partidaJogada;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_estatisticas")
	private PartidaEstatisticas partidaEstatisticas;

	@Transient
	private EscalacaoClube escalacaoMandante;

	@Transient
	private EscalacaoClube escalacaoVisitante;

	public PartidaAmistosaResultado() {
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
	public void setGolsVisitante(Integer golsVisitante) {
		this.golsVisitante = golsVisitante;
	}

	@Override
	public RodadaAmistosa getRodada() {
		return rodada;
	}

	public void setRodada(RodadaAmistosa rodada) {
		this.rodada = rodada;
	}

	@Override
	public PartidaEstatisticas getPartidaEstatisticas() {
		return partidaEstatisticas;
	}

	@Override
	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas) {
		this.partidaEstatisticas = partidaEstatisticas;
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

	@JsonIgnore
	@Override
	public boolean isAmistoso() {
		return true;
	}
	
	//###	METODOS AUXILIARES	###

	@JsonIgnore
	@Override
	public Clube getClubeVencedor() {
		//if (golsMandante == null || golsVisitante == null) return null;//Partida nao realizada
		if (partidaJogada && golsMandante > golsVisitante) return clubeMandante;
		if (partidaJogada && golsVisitante > golsMandante) return clubeVisitante;
		return null;//Empate
	}

	@JsonIgnore
	@Override
	public Clube getClubePerdedor() {
		//if (golsMandante == null || golsVisitante == null) return null;//Partida nao realizada
		if (partidaJogada && golsMandante < golsVisitante) return clubeMandante;
		if (partidaJogada && golsVisitante < golsMandante) return clubeVisitante;
		return null;//Empate
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
	@Override
	public boolean isDisputarPenalts() {
		return false;
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
		return "PartidaAmistosaResultado [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " " + golsMandante
				+ " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
	}

}
