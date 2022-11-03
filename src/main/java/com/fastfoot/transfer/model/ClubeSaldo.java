package com.fastfoot.transfer.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.service.util.FormatadorUtil;

public class ClubeSaldo {
	
	private Clube clube;
	
	private Double saldo;
	
	private Double previsaoSaidaSalarios;
	
	private Double previsaoEntradaIngressos;
	
	private Double movimentacoesTransferenciaVenda;
	
	private Double movimentacoesTransferenciaCompra;

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
	
	public Double getPrevisaoSaidaSalarios() {
		return previsaoSaidaSalarios;
	}

	public void setPrevisaoSaidaSalarios(Double previsaoSaidaSalarios) {
		this.previsaoSaidaSalarios = previsaoSaidaSalarios;
	}

	public Double getPrevisaoEntradaIngressos() {
		return previsaoEntradaIngressos;
	}

	public void setPrevisaoEntradaIngressos(Double previsaoEntradaIngressos) {
		this.previsaoEntradaIngressos = previsaoEntradaIngressos;
	}

	public Double getMovimentacoesTransferenciaVenda() {
		return movimentacoesTransferenciaVenda;
	}

	public void setMovimentacoesTransferenciaVenda(Double movimentacoesTransferenciaVenda) {
		this.movimentacoesTransferenciaVenda = movimentacoesTransferenciaVenda;
	}

	public Double getMovimentacoesTransferenciaCompra() {
		return movimentacoesTransferenciaCompra;
	}

	public void setMovimentacoesTransferenciaCompra(Double movimentacoesTransferenciaCompra) {
		this.movimentacoesTransferenciaCompra = movimentacoesTransferenciaCompra;
	}

	public Double getSaldoPrevisto() {
		return saldo + previsaoEntradaIngressos + movimentacoesTransferenciaVenda - movimentacoesTransferenciaCompra - previsaoSaidaSalarios;
	}

	public synchronized boolean deduzirSaldo(Double valor) {

		if (saldo >= valor) {
			saldo -= valor;
			return true;
		}

		return false;
	}
	
	public synchronized void incrementarSaldo(Double valor) {
		if (0 < valor) {
			saldo += valor;

		}
	}
	
	/**
	 * 
	 * @param valorTransferencia Valor absoluto da Transferencia
	 * @param valorSalario Valor absoluto do Salario
	 * @return
	 */
	public synchronized boolean inserirCompra(Double valorTransferencia, Double valorSalario) {

		if (getSaldoPrevisto() >= (valorTransferencia + valorSalario)) {
			movimentacoesTransferenciaCompra += valorTransferencia;
			previsaoSaidaSalarios += valorSalario;
			return true;
		}

		return false;
	}
	
	/**
	 * 
	 * @param valorTransferencia Valor absoluto da Transferencia
	 * @param valorSalario Valor absoluto do Salario
	 */
	public synchronized void inserirVenda(Double valorTransferencia, Double valorSalario) {
		movimentacoesTransferenciaVenda += valorTransferencia;
		previsaoSaidaSalarios -= valorSalario;
	}

	@Override
	public String toString() {
		return "ClubeSaldo [clube=" + clube.getId() + ", getSaldoPrevisto()=" + FormatadorUtil.formatarDecimal(getSaldoPrevisto()) + "]";
	}

}
