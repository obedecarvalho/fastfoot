package com.fastfoot.service.util;

import com.fastfoot.model.Constantes;

public class DistribuicaoPoissonElemento implements ElementoRoleta {

	private Integer valorDescritivo;
	
	private Double valorProbabilidade;
	
	private Double valorProbabilidadeN;

	public Integer getValorDescritivo() {
		return valorDescritivo;
	}

	public void setValorDescritivo(Integer valorDescritivo) {
		this.valorDescritivo = valorDescritivo;
	}

	public Double getValorProbabilidade() {
		return valorProbabilidade;
	}

	public void setValorProbabilidade(Double valorProbabilidade) {
		this.valorProbabilidade = valorProbabilidade;
	}

	@Override
	public String toString() {
		return "DistribuicaoPoissonElemento [valor=" + valorDescritivo + ", prob=" + valorProbabilidade + "]";
	}

	@Override
	public Integer getValorN() {
		if (valorProbabilidadeN == null) {
			valorProbabilidadeN = new Double(Math.pow(valorProbabilidade, Constantes.ROLETA_N_POWER));
		}
		return valorProbabilidadeN.intValue();
	}

	@Override
	public Integer getValor() {
		return getValorProbabilidade().intValue();
	}

	@Override
	public Double getValorAsDouble() {
		return getValorProbabilidade();
	}

	@Override
	public Double getValorNAsDouble() {
		if (valorProbabilidadeN == null) {
			valorProbabilidadeN = new Double(Math.pow(valorProbabilidade, Constantes.ROLETA_N_POWER));
		}
		return valorProbabilidadeN;
	}

}
