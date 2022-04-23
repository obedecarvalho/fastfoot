package com.fastfoot.match.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;

@Entity
public class PartidaLance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partidaLanceSequence")	
	@SequenceGenerator(name = "partidaLanceSequence", sequenceName = "partida_lance_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_resumo")
	private PartidaResumo partidaResumo;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Habilidade habilidadeUsada;
	
	private Boolean vencedor;

	private Integer ordem;

	private Boolean acao;

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

	public Boolean getVencedor() {
		return vencedor;
	}

	public void setVencedor(Boolean vencedor) {
		this.vencedor = vencedor;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getAcao() {
		return acao;
	}

	public void setAcao(Boolean acao) {
		this.acao = acao;
	}

}
