package com.fastfoot.match.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraEntrada;
import com.fastfoot.match.model.entity.PartidaTorcida;

public class PartidaTorcidaSalvarDTO {
	
	private List<MovimentacaoFinanceiraEntrada> movimentacaoFinanceiraEntradas;
	
	private List<PartidaTorcida> partidaTorcidaList;
	
	public PartidaTorcidaSalvarDTO() {
		this.movimentacaoFinanceiraEntradas = new ArrayList<MovimentacaoFinanceiraEntrada>();
		this.partidaTorcidaList = new ArrayList<PartidaTorcida>();
	}

	public List<MovimentacaoFinanceiraEntrada> getMovimentacaoFinanceiraEntradas() {
		return movimentacaoFinanceiraEntradas;
	}

	public void setMovimentacaoFinanceiraEntradas(List<MovimentacaoFinanceiraEntrada> movimentacaoFinanceiraEntradas) {
		this.movimentacaoFinanceiraEntradas = movimentacaoFinanceiraEntradas;
	}

	public List<PartidaTorcida> getPartidaTorcidaList() {
		return partidaTorcidaList;
	}

	public void setPartidaTorcidaList(List<PartidaTorcida> partidaTorcidaList) {
		this.partidaTorcidaList = partidaTorcidaList;
	}
	
	public void addMovimentacaoFinanceiraEntrada(MovimentacaoFinanceiraEntrada mfe) {
		this.movimentacaoFinanceiraEntradas.add(mfe);
	}

	public void addPartidaTorcida(PartidaTorcida partidaTorcida) {
		this.partidaTorcidaList.add(partidaTorcida);
	}
}
