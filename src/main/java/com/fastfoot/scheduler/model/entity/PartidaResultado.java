package com.fastfoot.scheduler.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Entity
//@Table(indexes = { @Index(columnList = "id_rodada")})
public class PartidaResultado implements PartidaResultadoJogavel {
	
	@Id //Sequence compartilhada com entidades equivalentes
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaSequence")	
	@SequenceGenerator(name = "partidaSequence", sequenceName = "partida_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_rodada")
	private Rodada rodada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_mandante")
	private Clube clubeMandante;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_visitante")
	private Clube clubeVisitante;
	
	private Integer golsMandante;
	
	private Integer golsVisitante;

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

	@Transient
	private EscalacaoClube escalacaoMandante;

	@Transient
	private EscalacaoClube escalacaoVisitante;
	
	public PartidaResultado() {
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

	@Override
	public Rodada getRodada() {
		return rodada;
	}

	public void setRodada(Rodada rodada) {
		this.rodada = rodada;
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

	@JsonIgnore
	@Override
	public boolean isAmistoso() {
		return false;
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
	public boolean isDisputarPenalties() {
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
		return "Partida [rod=" + rodada.getNumero() + ", " + clubeMandante.getNome() + " " + golsMandante
				+ " x " + golsVisitante + " " + clubeVisitante.getNome() + "]";
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
		PartidaResultado other = (PartidaResultado) obj;
		return Objects.equals(id, other.id);
	}
}
