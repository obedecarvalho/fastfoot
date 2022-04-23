package com.fastfoot.player.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.player.model.Habilidade;

/**
 * 
 * @author obede
 *
 * https://shekhargulati.com/2020/05/11/improving-spring-data-jpa-hibernate-bulk-insert-performance-by-more-than-100-times/
 */

@Entity
public class JogadorGrupoEstatisticas {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEstatisticasSequence")	
	@SequenceGenerator(name = "jogadorEstatisticasSequence", sequenceName = "jogador_estatisticas_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_resumo")
	private PartidaResumo partidaResumo;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Habilidade habilidadeUsada;

	private Integer quantidadeUso;
	
	private Integer quantidadeUsoVencedor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartidaResumo getPartidaResumo() {
		return partidaResumo;
	}

	public void setPartidaResumo(PartidaResumo partidaResumo) {
		this.partidaResumo = partidaResumo;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Habilidade getHabilidadeUsada() {
		return habilidadeUsada;
	}

	public void setHabilidadeUsada(Habilidade habilidadeUsada) {
		this.habilidadeUsada = habilidadeUsada;
	}

	public Integer getQuantidadeUso() {
		return quantidadeUso;
	}

	public void setQuantidadeUso(Integer quantidadeUso) {
		this.quantidadeUso = quantidadeUso;
	}

	public Integer getQuantidadeUsoVencedor() {
		return quantidadeUsoVencedor;
	}

	public void setQuantidadeUsoVencedor(Integer quantidadeUsoVencedor) {
		this.quantidadeUsoVencedor = quantidadeUsoVencedor;
	}

}
