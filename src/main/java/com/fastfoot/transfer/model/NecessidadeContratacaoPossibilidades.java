package com.fastfoot.transfer.model;

import java.util.List;

import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;

public class NecessidadeContratacaoPossibilidades {
	
	private NecessidadeContratacaoClube necessidadeContratacaoClube;
	
	private List<DisponivelNegociacao> possiveisContratacoes;

	public NecessidadeContratacaoClube getNecessidadeContratacaoClube() {
		return necessidadeContratacaoClube;
	}

	public void setNecessidadeContratacaoClube(NecessidadeContratacaoClube necessidadeContratacaoClube) {
		this.necessidadeContratacaoClube = necessidadeContratacaoClube;
	}

	public List<DisponivelNegociacao> getPossiveisContratacoes() {
		return possiveisContratacoes;
	}

	public void setPossiveisContratacoes(List<DisponivelNegociacao> possiveisContratacoes) {
		this.possiveisContratacoes = possiveisContratacoes;
	}

}
