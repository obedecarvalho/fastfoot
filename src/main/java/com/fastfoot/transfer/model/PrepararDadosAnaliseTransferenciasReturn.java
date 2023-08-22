package com.fastfoot.transfer.model;

import java.util.List;

import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;

public class PrepararDadosAnaliseTransferenciasReturn {

	private List<NecessidadeContratacaoClube> necessidadeContratacaoClubes;
	
	private List<DisponivelNegociacao> disponivelNegociacao;

	public List<NecessidadeContratacaoClube> getNecessidadeContratacaoClubes() {
		return necessidadeContratacaoClubes;
	}

	public List<DisponivelNegociacao> getDisponivelNegociacao() {
		return disponivelNegociacao;
	}

	public PrepararDadosAnaliseTransferenciasReturn(List<NecessidadeContratacaoClube> necessidadeContratacaoClubes,
			List<DisponivelNegociacao> disponivelNegociacao) {
		super();
		this.necessidadeContratacaoClubes = necessidadeContratacaoClubes;
		this.disponivelNegociacao = disponivelNegociacao;
	}
}
