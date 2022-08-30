package com.fastfoot.transfer.model.dto;

import com.fastfoot.player.model.Posicao;

public class JogadorAlvoDTO {
	
	private Long idJogador;
	
	private Integer idClube;
	
	private Integer forcaGeralJogador;
	
	private Posicao posicao;
	
	private Boolean titular;
	
	private Boolean disponivelNegociacao;

	public Long getIdJogador() {
		return idJogador;
	}

	public void setIdJogador(Long idJogador) {
		this.idJogador = idJogador;
	}

	public Integer getIdClube() {
		return idClube;
	}

	public void setIdClube(Integer idClube) {
		this.idClube = idClube;
	}

	public Integer getForcaGeralJogador() {
		return forcaGeralJogador;
	}

	public void setForcaGeralJogador(Integer forcaGeralJogador) {
		this.forcaGeralJogador = forcaGeralJogador;
	}

	public Boolean isTitular() {
		return titular;
	}

	public void setTitular(Boolean titular) {
		this.titular = titular;
	}

	public Boolean isDisponivelNegociacao() {
		return disponivelNegociacao;
	}

	public void setDisponivelNegociacao(Boolean disponivelNegociacao) {
		this.disponivelNegociacao = disponivelNegociacao;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

}
