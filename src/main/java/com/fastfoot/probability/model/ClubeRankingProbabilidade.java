package com.fastfoot.probability.model;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;

public class ClubeRankingProbabilidade {

	private Clube clube;
	
	private ClassificacaoNacionalFinal classificacaoNacional;
	
	private ClassificacaoCopaNacionalFinal classificacaoCopaNacional;
	
	private ClassificacaoContinentalFinal classificacaoContinental;

	private Integer posicaoGeral;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public ClassificacaoNacionalFinal getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacionalFinal classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoCopaNacionalFinal getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public ClassificacaoContinentalFinal getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinentalFinal classificacaoContinental) {
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
