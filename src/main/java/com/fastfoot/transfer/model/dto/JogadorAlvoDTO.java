package com.fastfoot.transfer.model.dto;

import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.service.util.ElementoRoleta;

public class JogadorAlvoDTO implements ElementoRoleta {
	
	private Long idJogador;
	
	private Integer idClube;
	
	private Integer forcaGeralJogador;
	
	private Integer forcaGeralClube;
	
	private Integer idade;
	
	private Double valorTransferencia;
	
	private Posicao posicao;
	
	private Boolean titular;
	
	private Boolean disponivelNegociacao;
	
	private Integer rankTransferencia;

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

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Double getValorTransferencia() {
		return valorTransferencia;
	}

	public void setValorTransferencia(Double valorTransferencia) {
		this.valorTransferencia = valorTransferencia;
	}

	public Boolean getTitular() {
		return titular;
	}

	public Boolean getDisponivelNegociacao() {
		return disponivelNegociacao;
	}

	public Integer getRankTransferencia() {
		return rankTransferencia;
	}

	public void setRankTransferencia(Integer rankTransferencia) {
		this.rankTransferencia = rankTransferencia;
	}
	
	public Integer getForcaGeralClube() {
		return forcaGeralClube;
	}

	public void setForcaGeralClube(Integer forcaGeralClube) {
		this.forcaGeralClube = forcaGeralClube;
	}

	public void calcularRankTransferencia() {//TODO: melhorar calculo
		if (rankTransferencia == null) {
			Double rankTransferencia = valorTransferencia;

			if (titular == null) titular = false;
			if (disponivelNegociacao == null) disponivelNegociacao = false;

			if (titular && disponivelNegociacao) {
				//Nada
			} else if (titular) {
				rankTransferencia = rankTransferencia / 1.1;
			} else if (disponivelNegociacao) {
				rankTransferencia = rankTransferencia / 0.9;
			}
			
			this.rankTransferencia = rankTransferencia.intValue();
		}
	}

	@Override
	public Integer getValor() {
		//return rankTransferencia;
		return Constantes.PESO_DIFERENCA_JOGADOR_CLUBE_TRANSFERENCIA / ((forcaGeralClube - forcaGeralJogador) + 1);
	}

	@Override
	public Integer getValorN() {
		//return rankTransferencia;
		return new Double(Constantes.PESO_DIFERENCA_JOGADOR_CLUBE_TRANSFERENCIA
				/ (Math.pow((forcaGeralClube - forcaGeralJogador), 2) + 1)).intValue();
	}

}
