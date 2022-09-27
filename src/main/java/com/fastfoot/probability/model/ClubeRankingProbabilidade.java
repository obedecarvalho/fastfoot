package com.fastfoot.probability.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;

public class ClubeRankingProbabilidade {

	private Clube clube;
	
	private ClassificacaoNacional classificacaoNacional;
	
	private ClassificacaoCopaNacional classificacaoCopaNacional;
	
	private ClassificacaoContinental classificacaoContinental;

	private Integer posicaoGeral;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public ClassificacaoNacional getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacional classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoCopaNacional getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacional classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public ClassificacaoContinental getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinental classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public Integer getPosicaoGeral() {
		return posicaoGeral;
	}

	public void setPosicaoGeral(Integer posicaoGeral) {
		this.posicaoGeral = posicaoGeral;
	}

	public boolean isBetweenP1AndP4() {
		return posicaoGeral >= 1 && posicaoGeral <= 4; 
	}
	
	public boolean isBetweenP5AndP8() {
		return posicaoGeral >= 5 && posicaoGeral <= 8; 
	}
	
	public boolean isBetweenP9AndP12() {
		return posicaoGeral >= 9 && posicaoGeral <= 12; 
	}
	
	public boolean isBetweenP9AndP10() {
		return posicaoGeral >= 9 && posicaoGeral <= 10; 
	}

	@Override
	public String toString() {
		return "ClubeRanking [" + clube.getNome() + ", pos=" + posicaoGeral + ", rkNac="
				+ (classificacaoNacional != null ? classificacaoNacional.name() : "") + ", rkCN="
				+ (classificacaoCopaNacional != null ? classificacaoCopaNacional.name() : "") + ", rkCont="
				+ (classificacaoContinental != null ? classificacaoContinental.name() : "") + "]";
	}
}
