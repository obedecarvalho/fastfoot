package com.fastfoot.transfer.model;

import java.util.Objects;

import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;

public class PossivelTrocaJogador {
	
	private PropostaTransferenciaJogador propostaJogador1;
	
	private PropostaTransferenciaJogador propostaJogador2;

	public PossivelTrocaJogador() {

	}

	public PossivelTrocaJogador(PropostaTransferenciaJogador propostaJogador1,
			PropostaTransferenciaJogador propostaJogador2) {
		
		if (propostaJogador1.getValorTransferencia() >= propostaJogador2.getValorTransferencia()) {
			this.propostaJogador1 = propostaJogador1;
			this.propostaJogador2 = propostaJogador2;
		} else {
			this.propostaJogador1 = propostaJogador2;
			this.propostaJogador2 = propostaJogador1;
		}
	}

	public PropostaTransferenciaJogador getPropostaJogador1() {
		return propostaJogador1;
	}

	public void setPropostaJogador1(PropostaTransferenciaJogador propostaJogador1) {
		this.propostaJogador1 = propostaJogador1;
	}

	public PropostaTransferenciaJogador getPropostaJogador2() {
		return propostaJogador2;
	}

	public void setPropostaJogador2(PropostaTransferenciaJogador propostaJogador2) {
		this.propostaJogador2 = propostaJogador2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(propostaJogador1, propostaJogador2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PossivelTrocaJogador other = (PossivelTrocaJogador) obj;
		return Objects.equals(propostaJogador1, other.propostaJogador1)
				&& Objects.equals(propostaJogador2, other.propostaJogador2);
	}

	@Override
	public String toString() {
		return "PossivelTrocaJogador [propostaJogador1=" + propostaJogador1 + ", propostaJogador2=" + propostaJogador2
				+ "]";
	}

}
