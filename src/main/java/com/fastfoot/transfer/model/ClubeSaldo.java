package com.fastfoot.transfer.model;

import com.fastfoot.club.model.entity.Clube;

public class ClubeSaldo {
	
	private Clube clube;
	
	private Double saldo;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public synchronized boolean atualizarSaldo(Double valor) {

		if (saldo >= valor) {
			saldo -= valor;
			return true;
		}

		return false;
	}

}
