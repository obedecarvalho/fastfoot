package com.fastfoot.transfer.model.dto;

import java.util.List;

import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;

public class TransferenciaConcluidaDTO {

	private PropostaTransferenciaJogador propostaAceita;
	
	private List<PropostaTransferenciaJogador> propostasRejeitar;
	
	private DisponivelNegociacao disponivelNegociacao;
	
	public TransferenciaConcluidaDTO() {

	}

	public TransferenciaConcluidaDTO(PropostaTransferenciaJogador propostaAceita,
			List<PropostaTransferenciaJogador> propostasRejeitar, DisponivelNegociacao disponivelNegociacao) {
		this.propostaAceita = propostaAceita;
		this.propostasRejeitar = propostasRejeitar;
		this.disponivelNegociacao = disponivelNegociacao;
	}

	public PropostaTransferenciaJogador getPropostaAceita() {
		return propostaAceita;
	}

	public void setPropostaAceita(PropostaTransferenciaJogador propostaAceita) {
		this.propostaAceita = propostaAceita;
	}

	public List<PropostaTransferenciaJogador> getPropostasRejeitar() {
		return propostasRejeitar;
	}

	public void setPropostasRejeitar(List<PropostaTransferenciaJogador> propostasRejeitar) {
		this.propostasRejeitar = propostasRejeitar;
	}

	public DisponivelNegociacao getDisponivelNegociacao() {
		return disponivelNegociacao;
	}

	public void setDisponivelNegociacao(DisponivelNegociacao disponivelNegociacao) {
		this.disponivelNegociacao = disponivelNegociacao;
	}

}
