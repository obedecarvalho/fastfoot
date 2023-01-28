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

import com.fastfoot.player.model.HabilidadeGrupo;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class HabilidadeGrupoValor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeGrupoValorSequence")
	@SequenceGenerator(name = "habilidadeGrupoValorSequence", sequenceName = "habilidade_grupo_valor_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	private HabilidadeGrupo habilidadeGrupo;

	private Double valor;

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

	public HabilidadeGrupo getHabilidadeGrupo() {
		return habilidadeGrupo;
	}

	public void setHabilidadeGrupo(HabilidadeGrupo habilidadeGrupo) {
		this.habilidadeGrupo = habilidadeGrupo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "HabilidadeGrupoValor [grupo=" + habilidadeGrupo + ", valor=" + valor + "]";
	}

}
