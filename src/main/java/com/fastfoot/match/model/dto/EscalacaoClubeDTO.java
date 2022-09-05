package com.fastfoot.match.model.dto;

import java.util.List;

public class EscalacaoClubeDTO {
	
	private List<EscalacaoJogadorDTO> escalacaoTitular;
	
	private List<EscalacaoJogadorDTO> escalacaoReserva;
	
	private List<EscalacaoJogadorDTO> escalacaoSuplente;

	public List<EscalacaoJogadorDTO> getEscalacaoTitular() {
		return escalacaoTitular;
	}

	public void setEscalacaoTitular(List<EscalacaoJogadorDTO> escalacaoTitular) {
		this.escalacaoTitular = escalacaoTitular;
	}

	public List<EscalacaoJogadorDTO> getEscalacaoReserva() {
		return escalacaoReserva;
	}

	public void setEscalacaoReserva(List<EscalacaoJogadorDTO> escalacaoReserva) {
		this.escalacaoReserva = escalacaoReserva;
	}

	public List<EscalacaoJogadorDTO> getEscalacaoSuplente() {
		return escalacaoSuplente;
	}

	public void setEscalacaoSuplente(List<EscalacaoJogadorDTO> escalacaoSuplente) {
		this.escalacaoSuplente = escalacaoSuplente;
	}

}
