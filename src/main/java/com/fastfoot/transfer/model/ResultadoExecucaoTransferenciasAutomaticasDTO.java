package com.fastfoot.transfer.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;

public class ResultadoExecucaoTransferenciasAutomaticasDTO {
	
	private List<NecessidadeContratacaoClube> necessidadeContratacaoClubes;
	
	private List<DisponivelNegociacao> disponivelNegociacao;
	
	private List<PropostaTransferenciaJogador> propostaTransferenciaJogadores;
	
	private List<Jogador> jogadores;

	private List<MovimentacaoFinanceira> movimentacoesFinanceiras;
	
	private List<Contrato> contratos;
	
	private List<Contrato> contratosInativar;

	public ResultadoExecucaoTransferenciasAutomaticasDTO() {
		this.jogadores = new ArrayList<Jogador>();
		this.movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();
		this.propostaTransferenciaJogadores = new ArrayList<PropostaTransferenciaJogador>();
		this.contratos = new ArrayList<Contrato>();
		this.contratosInativar = new ArrayList<Contrato>();
	}

	public List<NecessidadeContratacaoClube> getNecessidadeContratacaoClubes() {
		return necessidadeContratacaoClubes;
	}

	public void setNecessidadeContratacaoClubes(List<NecessidadeContratacaoClube> necessidadeContratacaoClubes) {
		this.necessidadeContratacaoClubes = necessidadeContratacaoClubes;
	}

	public List<DisponivelNegociacao> getDisponivelNegociacao() {
		return disponivelNegociacao;
	}

	public void setDisponivelNegociacao(List<DisponivelNegociacao> disponivelNegociacao) {
		this.disponivelNegociacao = disponivelNegociacao;
	}

	public List<PropostaTransferenciaJogador> getPropostaTransferenciaJogadores() {
		return propostaTransferenciaJogadores;
	}

	public void setPropostaTransferenciaJogadores(List<PropostaTransferenciaJogador> propostas) {
		this.propostaTransferenciaJogadores = propostas;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadoresSalvar) {
		this.jogadores = jogadoresSalvar;
	}

	public List<MovimentacaoFinanceira> getMovimentacoesFinanceiras() {
		return movimentacoesFinanceiras;
	}

	public void setMovimentacoesFinanceiras(List<MovimentacaoFinanceira> movimentacoesFinanceiras) {
		this.movimentacoesFinanceiras = movimentacoesFinanceiras;
	}

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public List<Contrato> getContratosInativar() {
		return contratosInativar;
	}

	public void setContratosInativar(List<Contrato> contratosInativar) {
		this.contratosInativar = contratosInativar;
	}

}
