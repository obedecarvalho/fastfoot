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
public class JogadorEstatisticasAmistososTemporada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEstatisticasAmistososTemporadaSequence")	
	@SequenceGenerator(name = "jogadorEstatisticasAmistososTemporadaSequence", sequenceName = "jogador_estatisticas_amistosos_temporada_seq")
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
	
	private Integer numeroJogosAmistosos;
	
	private Integer golsMarcadosAmistosos;
	
	private Integer assistenciasAmistosos;
	
	//private Integer numeroJogos;
	
	//private Integer golsMarcados;
	
	//private Integer assistencias;
	
	private Integer finalizacoesDefendidasAmistosos;
	
	private Integer finalizacoesForaAmistosos;
	
	private Integer faltasAmistosos;//TODO: implementar lógica

	//TODO: estatisticas goleiro: gols tomados, defesas

	public JogadorEstatisticasAmistososTemporada() {
		
	}
	
	public JogadorEstatisticasAmistososTemporada(Jogador jogador, Temporada temporada, Clube clube) {
		this.jogador = jogador;
		this.temporada = temporada;
		//this.numeroJogos = 0;
		//this.golsMarcados = 0;
		this.finalizacoesDefendidasAmistosos = 0;
		this.finalizacoesForaAmistosos = 0;
		this.numeroJogosAmistosos = 0;
		this.golsMarcadosAmistosos = 0;
		this.faltasAmistosos = 0;
		//this.assistencias = 0;
		this.assistenciasAmistosos = 0;
		this.clube = clube;
	}
	
	public boolean isEmpty() {
		return //this.numeroJogos == 0 && 
				//this.golsMarcados == 0 && 
				//this.finalizacoesDefendidas == 0 && 
				//this.finalizacoesFora == 0 && 				
				//this.golsMarcadosAmistosos == 0 && 
				//this.faltas == 0 && 
				//this.assistencias == 0 && 
				//this.assistenciasAmistosos == 0 &&
				this.numeroJogosAmistosos == 0;
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

	/*public Integer getNumeroJogos() {
		return numeroJogos;
	}

	public void setNumeroJogos(Integer numeroJogos) {
		this.numeroJogos = numeroJogos;
	}*/

	/*public Integer getGolsMarcados() {
		return golsMarcados;
	}

	public void setGolsMarcados(Integer golsMarcados) {
		this.golsMarcados = golsMarcados;
	}*/
	
	/*public void incrementarGolsMarcados() {
		this.golsMarcados++;
	}*/

	public Integer getFaltasAmistosos() {
		return faltasAmistosos;
	}

	public void setFaltasAmistosos(Integer faltas) {
		this.faltasAmistosos = faltas;
	}

	public Integer getFinalizacoesDefendidasAmistosos() {
		return finalizacoesDefendidasAmistosos;
	}

	public void setFinalizacoesDefendidasAmistosos(Integer finalizacoesDefendidas) {
		this.finalizacoesDefendidasAmistosos = finalizacoesDefendidas;
	}
	
	public void incrementarFinalizacoesDefendidasAmistosos() {
		this.finalizacoesDefendidasAmistosos++;
	}

	public Integer getFinalizacoesForaAmistosos() {
		return finalizacoesForaAmistosos;
	}

	public void setFinalizacoesForaAmistosos(Integer finalizacoesFora) {
		this.finalizacoesForaAmistosos = finalizacoesFora;
	}
	
	public void incrementarFinalizacoesForaAmistosos() {
		this.finalizacoesForaAmistosos++;
	}

	public Integer getNumeroJogosAmistosos() {
		return numeroJogosAmistosos;
	}

	public void setNumeroJogosAmistosos(Integer numeroJogosAmistosos) {
		this.numeroJogosAmistosos = numeroJogosAmistosos;
	}

	public Integer getGolsMarcadosAmistosos() {
		return golsMarcadosAmistosos;
	}

	public void setGolsMarcadosAmistosos(Integer golsMarcadosAmistosos) {
		this.golsMarcadosAmistosos = golsMarcadosAmistosos;
	}
	
	public void incrementarGolsMarcadosAmistosos() {
		this.golsMarcadosAmistosos++;
	}

	/*public Integer getAssistencias() {
		return assistencias;
	}

	public void setAssistencias(Integer assistencias) {
		this.assistencias = assistencias;
	}*/

	public Integer getAssistenciasAmistosos() {
		return assistenciasAmistosos;
	}

	public void setAssistenciasAmistosos(Integer assistenciasAmistosos) {
		this.assistenciasAmistosos = assistenciasAmistosos;
	}
	
	/*public void incrementarAssistencias() {
		this.assistencias++;
	}*/

	public void incrementarAssistenciasAmistosos() {
		this.assistenciasAmistosos++;
	}

	@Override
	public String toString() {
		return "JogadorEstatisticasAmistososTemporada [jogador=" + jogador + ", numeroJogos="
				+ numeroJogosAmistosos + ", golsMarcados=" + golsMarcadosAmistosos + ", assistencias=" + assistenciasAmistosos + "]";
	}

}
