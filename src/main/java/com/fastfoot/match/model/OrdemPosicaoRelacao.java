package com.fastfoot.match.model;

import com.fastfoot.player.model.Posicao;

public enum OrdemPosicaoRelacao {
	GOLEIRO(new Posicao[] {Posicao.GOLEIRO, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA}),
	LATERAL(new Posicao[] {Posicao.LATERAL, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	ZAGUEIRO(new Posicao[] {Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA, Posicao.GOLEIRO}),
	VOLANTE(new Posicao[] {Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	MEIA(new Posicao[] {Posicao.MEIA, Posicao.ATACANTE, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	ATACANTE(new Posicao[] {Posicao.ATACANTE, Posicao.MEIA, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	;
	
	private Posicao[] ordemPosicaoRelacao;
	
	private OrdemPosicaoRelacao(Posicao[] ordemPosicaoRelacao) {
		this.ordemPosicaoRelacao = ordemPosicaoRelacao;
	}

	public Posicao[] getOrdemPosicaoRelacao() {
		return ordemPosicaoRelacao;
	}

	public static OrdemPosicaoRelacao getByPosicao(Posicao posicao) {
		for (OrdemPosicaoRelacao ordem : values()) {
			if (ordem.getOrdemPosicaoRelacao()[0].equals(posicao)) {
				return ordem;
			}
		}
		return null;
	}

}
