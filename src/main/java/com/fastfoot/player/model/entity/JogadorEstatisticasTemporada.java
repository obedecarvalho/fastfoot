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

	//TODO: estatisticas goleiro: gols tomados, defesas
	//TODO: num jogos titular e reserva separados
	
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
	
	private Integer numeroJogos;
	
	private Integer golsMarcados;
	
	private Integer assistencias;
	
	private Integer finalizacoesDefendidas;
	
	private Integer finalizacoesFora;
	
	private Integer faltas;//TODO: implementar lógica

	public JogadorEstatisticasTemporada() {
		
	}
	
	public JogadorEstatisticasTemporada(Jogador jogador, Temporada temporada, Clube clube) {
		this.jogador = jogador;
		this.temporada = temporada;
		this.numeroJogos = 0;
		this.golsMarcados = 0;
		this.finalizacoesDefendidas = 0;
		this.finalizacoesFora = 0;
		this.faltas = 0;
		this.assistencias = 0;
		this.clube = clube;
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

	public Integer getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(Integer assistencias) {
		this.assistencias = assistencias;
	}
	
	public void incrementarAssistencias() {
		this.assistencias++;
	}

	@Override
	public String toString() {
		return "JogadorEstatisticasTemporada [jogador=" + jogador + ", numeroJogos="
				+ numeroJogos + ", golsMarcados=" + golsMarcados + ", assistencias=" + assistencias + "]";
	}

}
