package com.fastfoot.match.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.match.model.entity.PartidaTorcida;

@Deprecated
public class PartidaTorcidaSalvarDTO {
	
	private List<MovimentacaoFinanceira> movimentacaoFinanceira;
	
	private List<PartidaTorcida> partidaTorcidaList;
	
	public PartidaTorcidaSalvarDTO() {
		this.movimentacaoFinanceira = new ArrayList<MovimentacaoFinanceira>();
		this.partidaTorcidaList = new ArrayList<PartidaTorcida>();
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public void setMovimentacaoFinanceira(List<MovimentacaoFinanceira> movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

	public List<PartidaTorcida> getPartidaTorcidaList() {
		return partidaTorcidaList;
	}

	public void setPartidaTorcidaList(List<PartidaTorcida> partidaTorcidaList) {
		this.partidaTorcidaList = partidaTorcidaList;
	}
	
	public void addMovimentacaoFinanceira(MovimentacaoFinanceira mfe) {
		this.movimentacaoFinanceira.add(mfe);
	}

	public void addPartidaTorcida(PartidaTorcida partidaTorcida) {
		this.partidaTorcidaList.add(partidaTorcida);
	}
}
