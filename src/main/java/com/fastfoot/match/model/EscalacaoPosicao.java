package com.fastfoot.match.model;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.player.model.Posicao;

public enum EscalacaoPosicao {
	NULL(false, "NULL", null),
	P_GOL(true, "GOL", OrdemPosicaoRelacao.GOLEIRO),
	P_ZD(true, "ZD", OrdemPosicaoRelacao.ZAGUEIRO),
	P_ZE(true, "ZE", OrdemPosicaoRelacao.ZAGUEIRO),
	P_LD(true, "LD", OrdemPosicaoRelacao.LATERAL),
	P_LE(true, "LE", OrdemPosicaoRelacao.LATERAL),
	P_VD(true, "VD", OrdemPosicaoRelacao.VOLANTE),
	P_VE(true, "VE", OrdemPosicaoRelacao.VOLANTE),	
	P_MD(true, "MD", OrdemPosicaoRelacao.MEIA),
	P_ME(true, "ME", OrdemPosicaoRelacao.MEIA),
	P_AD(true, "AD", OrdemPosicaoRelacao.ATACANTE),
	P_AE(true, "AE", OrdemPosicaoRelacao.ATACANTE),
	//Suprentes
	P_RES_1(false, "RES_1", OrdemPosicaoRelacao.GOLEIRO), //GOL
	P_RES_2(false, "RES_2", OrdemPosicaoRelacao.ZAGUEIRO), //ZAG
	P_RES_3(false, "RES_3", OrdemPosicaoRelacao.VOLANTE), //VOL
	P_RES_4(false, "RES_4", OrdemPosicaoRelacao.LATERAL), //LAT
	P_RES_5(false, "RES_5", OrdemPosicaoRelacao.MEIA), //MEI
	P_RES_6(false, "RES_6", OrdemPosicaoRelacao.ATACANTE), //ATA
	P_SUPLENTE(false, "SUP", null);
	
	private boolean titular;
	
	private String descricao;
	
	private OrdemPosicaoRelacao ordemPosicaoRelacao;
	
	private EscalacaoPosicao (boolean titular, String descricao, OrdemPosicaoRelacao ordemPosicaoRelacao) {
		this.titular = titular;
		this.descricao = descricao;
		this.ordemPosicaoRelacao = ordemPosicaoRelacao;
	}
	
	public static List<EscalacaoPosicao> getEscalacaoTitulares() {
		return Arrays.asList(P_GOL, P_LD, P_ZD, P_ZE, P_LE, P_VD, P_VE, P_MD, P_ME, P_AD, P_AE);
	}

	public boolean isTitular() {
		return titular;
	}

	public String getDescricao() {
		return descricao;
	}

	public Posicao[] getOrdemPosicaoParaEscalar() {
		return ordemPosicaoRelacao.getOrdemPosicaoRelacao();
	}

}
