package com.fastfoot.player.model.entity;

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
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
@Table(indexes = { @Index(columnList = "id_temporada, id_jogador") })
public class JogadorEstatisticasTemporada {

	//TODO: roubadas de bola
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEstatisticasTemporadaSequence")	
	@SequenceGenerator(name = "jogadorEstatisticasTemporadaSequence", sequenceName = "jogador_estatisticas_temporada_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	private Boolean amistoso;
	
	private Integer numeroJogos;//Numero total de jogos
	
	private Integer numeroJogosTitular;
	
	private Integer numeroMinutosJogados;
	
	//###	JOGADOR LINHA	###
	
	private Integer golsMarcados;
	
	private Integer assistencias;
	
	private Integer finalizacoesDefendidas;
	
	private Integer finalizacoesFora;
	
	private Integer faltas;//TODO: implementar lógica
	
	//###	GOLEIRO	###
	
	private Integer golsSofridos;
	
	private Integer goleiroFinalizacoesDefendidas;
	
	//###	DISPUTA PENALTS	###
	
	private Integer numeroRodadaDisputaPenalties;
	
	private Integer golsDisputaPenalties;
	
	private Integer golsPerdidosDisputaPenalties;
	
	private Integer defesasDisputaPenalties;

	private Integer golsSofridosDisputaPenalties;

	public JogadorEstatisticasTemporada() {
		
	}
	
	public JogadorEstatisticasTemporada(Jogador jogador, Temporada temporada, Clube clube, Boolean amistoso) {
		this.jogador = jogador;
		this.temporada = temporada;
		this.clube = clube;
		this.amistoso = amistoso;
		
		this.numeroJogos = 0;
		this.numeroJogosTitular = 0;
		this.numeroMinutosJogados = 0;
		
		this.golsMarcados = 0;
		this.assistencias = 0;
		this.finalizacoesDefendidas = 0;
		this.finalizacoesFora = 0;
		this.faltas = 0;
		
		this.golsSofridos = 0;
		this.goleiroFinalizacoesDefendidas = 0;
		
		this.numeroRodadaDisputaPenalties = 0;
		this.golsDisputaPenalties = 0;
		this.golsPerdidosDisputaPenalties = 0;
		this.defesasDisputaPenalties = 0;
		this.golsSofridosDisputaPenalties = 0;
	}
	
	public JogadorEstatisticasTemporada(Jogador jogador) {
		this.jogador = jogador;
		
		this.numeroJogos = 0;
		this.numeroJogosTitular = 0;
		this.numeroMinutosJogados = 0;
		
		this.golsMarcados = 0;
		this.assistencias = 0;
		this.finalizacoesDefendidas = 0;
		this.finalizacoesFora = 0;
		this.faltas = 0;
		
		this.golsSofridos = 0;
		this.goleiroFinalizacoesDefendidas = 0;
		
		this.numeroRodadaDisputaPenalties = 0;
		this.golsDisputaPenalties = 0;
		this.golsPerdidosDisputaPenalties = 0;
		this.defesasDisputaPenalties = 0;
		this.golsSofridosDisputaPenalties = 0;
	}
	
	public boolean isEmpty() {
		return this.numeroJogos == 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getNumeroJogos() {
		return numeroJogos;
	}

	public void setNumeroJogos(Integer numeroJogos) {
		this.numeroJogos = numeroJogos;
	}

	public Integer getGolsMarcados() {
		return golsMarcados;
	}

	public void setGolsMarcados(Integer golsMarcados) {
		this.golsMarcados = golsMarcados;
	}
	
	public void incrementarGolsMarcados() {
		this.golsMarcados++;
	}

	public Integer getFaltas() {
		return faltas;
	}

	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	public Integer getFinalizacoesDefendidas() {
		return finalizacoesDefendidas;
	}

	public void setFinalizacoesDefendidas(Integer finalizacoesDefendidas) {
		this.finalizacoesDefendidas = finalizacoesDefendidas;
	}
	
	public void incrementarFinalizacoesDefendidas() {
		this.finalizacoesDefendidas++;
	}

	public Integer getFinalizacoesFora() {
		return finalizacoesFora;
	}

	public void setFinalizacoesFora(Integer finalizacoesFora) {
		this.finalizacoesFora = finalizacoesFora;
	}
	
	public void incrementarFinalizacoesFora() {
		this.finalizacoesFora++;
	}

	public Boolean getAmistoso() {
		return amistoso;
	}

	public void setAmistoso(Boolean amistoso) {
		this.amistoso = amistoso;
	}

	public Integer getNumeroJogosTitular() {
		return numeroJogosTitular;
	}

	public void setNumeroJogosTitular(Integer numeroJogosTitular) {
		this.numeroJogosTitular = numeroJogosTitular;
	}

	public Integer getGolsSofridos() {
		return golsSofridos;
	}
	
	public void incrementarGolsSofridos() {
		this.golsSofridos++;
	}

	public void setGolsSofridos(Integer golsSofridos) {
		this.golsSofridos = golsSofridos;
	}

	public Integer getGoleiroFinalizacoesDefendidas() {
		return goleiroFinalizacoesDefendidas;
	}

	public void setGoleiroFinalizacoesDefendidas(Integer goleiroFinalizacoesDefendidas) {
		this.goleiroFinalizacoesDefendidas = goleiroFinalizacoesDefendidas;
	}
	
	public void incrementarGoleiroFinalizacoesDefendidas() {
		this.goleiroFinalizacoesDefendidas++;
	}

	public Integer getNumeroRodadaDisputaPenalties() {
		return numeroRodadaDisputaPenalties;
	}

	public void setNumeroRodadaDisputaPenalties(Integer numeroRodadaDisputaPenalties) {
		this.numeroRodadaDisputaPenalties = numeroRodadaDisputaPenalties;
	}
	
	public void incrementarNumeroRodadaDisputaPenalties() {
		this.numeroRodadaDisputaPenalties++;
	}
	
	public void incrementarGolsDisputaPenalties() {
		this.golsDisputaPenalties++;
	}

	public Integer getGolsDisputaPenalties() {
		return golsDisputaPenalties;
	}

	public void setGolsDisputaPenalties(Integer golsDisputaPenalties) {
		this.golsDisputaPenalties = golsDisputaPenalties;
	}
	
	public void incrementarGolsPerdidosDisputaPenalties() {
		this.golsPerdidosDisputaPenalties++;
	}

	public Integer getGolsPerdidosDisputaPenalties() {
		return golsPerdidosDisputaPenalties;
	}

	public void setGolsPerdidosDisputaPenalties(Integer golsPerdidosDisputaPenalties) {
		this.golsPerdidosDisputaPenalties = golsPerdidosDisputaPenalties;
	}
	
	public void incrementarDefesasDisputaPenalties() {
		this.defesasDisputaPenalties++;
	}

	public Integer getDefesasDisputaPenalties() {
		return defesasDisputaPenalties;
	}

	public void setDefesasDisputaPenalties(Integer defesasDisputaPenalties) {
		this.defesasDisputaPenalties = defesasDisputaPenalties;
	}
	
	public void incrementarGolsSofridosDisputaPenalties() {
		this.golsSofridosDisputaPenalties++;
	}

	public Integer getGolsSofridosDisputaPenalties() {
		return golsSofridosDisputaPenalties;
	}

	public void setGolsSofridosDisputaPenalties(Integer golsSofridosDisputaPenalties) {
		this.golsSofridosDisputaPenalties = golsSofridosDisputaPenalties;
	}

	public Integer getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(Integer assistencias) {
		this.assistencias = assistencias;
	}
	
	public void incrementarAssistencias() {
		this.assistencias++;
	}

	public Integer getNumeroMinutosJogados() {
		return numeroMinutosJogados;
	}

	public void setNumeroMinutosJogados(Integer numeroMinutosJogados) {
		this.numeroMinutosJogados = numeroMinutosJogados;
	}

	@Override
	public String toString() {
		return "JogadorEstatisticasTemporada [jogador=" + jogador + ", numeroJogos=" + numeroJogos +

				(!jogador.getPosicao().isGoleiro() ?

						(", golsMarcados=" + golsMarcados + ", assistencias=" + assistencias
								+ ", finalizacoesDefendidas=" + finalizacoesDefendidas + ", finalizacoesFora="
								+ finalizacoesFora)
						: "")
				+

				(jogador.getPosicao().isGoleiro()
						? (", golsSofridos=" + golsSofridos + ", goleiroFinalizacoesDefendidas="
								+ goleiroFinalizacoesDefendidas)
						: "")
				+ ", amistoso=" + amistoso + "]";
	}

}
