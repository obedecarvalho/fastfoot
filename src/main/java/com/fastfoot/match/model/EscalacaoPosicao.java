package com.fastfoot.match.model;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.player.model.Posicao;

public enum EscalacaoPosicao {
	NULL(false, "NULL", new Posicao[] {}),
	P_1(true, "GOL", new Posicao[] {Posicao.GOLEIRO, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA}),
	P_2(true, "LD", new Posicao[] {Posicao.LATERAL, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	P_3(true, "ZD", new Posicao[] {Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA, Posicao.GOLEIRO}),
	P_4(true, "ZE", new Posicao[] {Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA, Posicao.GOLEIRO}),
	P_6(true, "LE", new Posicao[] {Posicao.LATERAL, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	P_8(true, "VD", new Posicao[] {Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	P_5(true, "VE", new Posicao[] {Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}),
	P_7(true, "MD", new Posicao[] {Posicao.MEIA, Posicao.ATACANTE, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	P_10(true, "ME", new Posicao[] {Posicao.MEIA, Posicao.ATACANTE, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	P_9(true, "AD", new Posicao[] {Posicao.ATACANTE, Posicao.MEIA, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	P_11(true, "AE", new Posicao[] {Posicao.ATACANTE, Posicao.MEIA, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}),
	//Suprentes
	P_12(false, "RES", new Posicao[] {Posicao.GOLEIRO, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA}), //GOL
	P_13(false, "RES", new Posicao[] {Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.LATERAL, Posicao.ATACANTE, Posicao.MEIA, Posicao.GOLEIRO}), //ZAG
	P_15(false, "RES", new Posicao[] {Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}), //VOL
	P_16(false, "RES", new Posicao[] {Posicao.LATERAL, Posicao.ZAGUEIRO, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE, Posicao.GOLEIRO}), //LAT
	P_17(false, "RES", new Posicao[] {Posicao.MEIA, Posicao.ATACANTE, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}), //MEI
	P_19(false, "RES", new Posicao[] {Posicao.ATACANTE, Posicao.MEIA, Posicao.LATERAL, Posicao.VOLANTE, Posicao.ZAGUEIRO, Posicao.GOLEIRO}), //ATA
	P_SUPLENTE(false, "SUP", new Posicao[] {});
	
	private boolean titular;
	
	private String descricao;
	
	private Posicao[] ordemPosicaoParaEscalar;
	
	private EscalacaoPosicao (boolean titular, String descricao, Posicao[] ordemPosicaoParaEscalar) {
		this.titular = titular;
		this.descricao = descricao;
		this.ordemPosicaoParaEscalar = ordemPosicaoParaEscalar;
	}
	
	public static List<EscalacaoPosicao> getEscalacaoTitulares() {
		return Arrays.asList(P_1, P_2, P_3, P_4, P_6, P_5, P_8, P_7, P_10, P_9, P_11);
	}

	public boolean isTitular() {
		return titular;
	}

	public String getDescricao() {
		return descricao;
	}

	public Posicao[] getOrdemPosicaoParaEscalar() {
		return ordemPosicaoParaEscalar;
	}

}
