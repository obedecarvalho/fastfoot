package com.fastfoot.scheduler.model;

public class OrdemClassificacaoGeral {
	
	private Integer ordem;

	private ClassificacaoNacionalFinal classificacaoNacional;
	
	private ClassificacaoContinentalFinal classificacaoContinental;
	
	private ClassificacaoCopaNacionalFinal classificacaoCopaNacional;
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoNacionalFinal classificacao) {
		this.ordem = ordem;
		this.classificacaoNacional = classificacao;
	}
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoContinentalFinal classificacao) {
		this.ordem = ordem;
		this.classificacaoContinental = classificacao;
	}
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoCopaNacionalFinal classificacao) {
		this.ordem = ordem;
		this.classificacaoCopaNacional = classificacao;
	}

	public ClassificacaoNacionalFinal getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacionalFinal classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoContinentalFinal getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinentalFinal classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public ClassificacaoCopaNacionalFinal getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public boolean isNacional() {
		return classificacaoNacional != null;
	}
	
	public boolean isCopaNacional() {
		return classificacaoCopaNacional != null;
	}
	
	public boolean isContinental() {
		return classificacaoContinental != null;
	}
}
