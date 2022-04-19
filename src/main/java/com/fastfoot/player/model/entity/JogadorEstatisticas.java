package com.fastfoot.player.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeAcao;

@Entity
public class JogadorEstatisticas {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jogadorEstatisticasSequence")	
	@SequenceGenerator(name = "jogadorEstatisticasSequence", sequenceName = "jogador_estatisticas_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_estatisticas")
	private PartidaEstatisticas partidaEstatisticas;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;

	private Habilidade habilidadeUsada;
	
	//private Integer idHabilidadeUsada;
	
	private Boolean vencedor;

	private Integer ordem;

	private Boolean acao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartidaEstatisticas getPartidaEstatisticas() {
		return partidaEstatisticas;
	}

	public void setPartidaEstatisticas(PartidaEstatisticas partidaEstatisticas) {
		this.partidaEstatisticas = partidaEstatisticas;
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
		//this.idHabilidadeUsada = habilidadeUsada.getId();
	}

	public Boolean getVencedor() {
		return vencedor;
	}

	public void setVencedor(Boolean vencedor) {
		this.vencedor = vencedor;
	}

	/*public Integer getIdHabilidadeUsada() {
		return idHabilidadeUsada;
	}

	public void setIdHabilidadeUsada(Integer idHabilidadeUsada) {
		this.idHabilidadeUsada = idHabilidadeUsada;
	}*/

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
