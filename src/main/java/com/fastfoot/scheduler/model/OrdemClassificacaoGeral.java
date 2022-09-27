package com.fastfoot.scheduler.model;

public class OrdemClassificacaoGeral {
	
	public static final OrdemClassificacaoGeral[] ORDEM = {
			
			new OrdemClassificacaoGeral(1, ClassificacaoContinentalFinal.C_CAMPEAO),//CI
			new OrdemClassificacaoGeral(2, ClassificacaoNacionalFinal.N_1),//CI
			new OrdemClassificacaoGeral(3, ClassificacaoContinentalFinal.CII_CAMPEAO),//CI
			new OrdemClassificacaoGeral(4, ClassificacaoNacionalFinal.N_2),//CI
			new OrdemClassificacaoGeral(5, ClassificacaoCopaNacionalFinal.CN_CAMPEAO),//CI ou CII
			new OrdemClassificacaoGeral(6, ClassificacaoNacionalFinal.N_3),//CI ou CII
			new OrdemClassificacaoGeral(7, ClassificacaoNacionalFinal.N_4),//CI ou CII
			new OrdemClassificacaoGeral(8, ClassificacaoContinentalFinal.CIII_CAMPEAO),//CII
			new OrdemClassificacaoGeral(9, ClassificacaoNacionalFinal.N_5),//CII ou CIII
			new OrdemClassificacaoGeral(10, ClassificacaoNacionalFinal.N_6),//CII ou CIII
			new OrdemClassificacaoGeral(11, ClassificacaoNacionalFinal.N_7),//CII ou CIII ou Nada
			new OrdemClassificacaoGeral(12, ClassificacaoNacionalFinal.N_8),//CII ou CIII ou Nada
			new OrdemClassificacaoGeral(13, ClassificacaoCopaNacionalFinal.CNII_CAMPEAO),//CIII ou Nada
			new OrdemClassificacaoGeral(16, ClassificacaoNacionalFinal.N_9),//CIII ou Nada
			new OrdemClassificacaoGeral(17, ClassificacaoNacionalFinal.N_10),//CIII ou Nada
			new OrdemClassificacaoGeral(18, ClassificacaoNacionalFinal.N_11),//CIII ou Nada
			new OrdemClassificacaoGeral(19, ClassificacaoNacionalFinal.N_12),//CIII ou Nada
			new OrdemClassificacaoGeral(20, ClassificacaoNacionalFinal.N_13),
			new OrdemClassificacaoGeral(21, ClassificacaoNacionalFinal.NII_1),
			new OrdemClassificacaoGeral(22, ClassificacaoNacionalFinal.NII_2),
			new OrdemClassificacaoGeral(23, ClassificacaoNacionalFinal.NII_3),
			new OrdemClassificacaoGeral(24, ClassificacaoNacionalFinal.N_14),
			new OrdemClassificacaoGeral(25, ClassificacaoNacionalFinal.N_15),
			new OrdemClassificacaoGeral(26, ClassificacaoNacionalFinal.N_16),
			new OrdemClassificacaoGeral(27, ClassificacaoNacionalFinal.NII_4),
			new OrdemClassificacaoGeral(28, ClassificacaoNacionalFinal.NII_5),
			new OrdemClassificacaoGeral(29, ClassificacaoNacionalFinal.NII_6),
			new OrdemClassificacaoGeral(30, ClassificacaoNacionalFinal.NII_7),
			new OrdemClassificacaoGeral(31, ClassificacaoNacionalFinal.NII_8),
			new OrdemClassificacaoGeral(32, ClassificacaoNacionalFinal.NII_9),
			new OrdemClassificacaoGeral(33, ClassificacaoNacionalFinal.NII_10),
			new OrdemClassificacaoGeral(34, ClassificacaoNacionalFinal.NII_11),
			new OrdemClassificacaoGeral(35, ClassificacaoNacionalFinal.NII_12),
			new OrdemClassificacaoGeral(36, ClassificacaoNacionalFinal.NII_13),
			new OrdemClassificacaoGeral(37, ClassificacaoNacionalFinal.NII_14),
			new OrdemClassificacaoGeral(38, ClassificacaoNacionalFinal.NII_15),
			new OrdemClassificacaoGeral(39, ClassificacaoNacionalFinal.NII_16)

	};
	
	public static final OrdemClassificacaoGeral[] ORDEM_GLOBAL = {
			new OrdemClassificacaoGeral(1, ClassificacaoContinentalFinal.C_CAMPEAO),
			new OrdemClassificacaoGeral(2, ClassificacaoNacionalFinal.N_1),
			new OrdemClassificacaoGeral(3, ClassificacaoContinentalFinal.CII_CAMPEAO),
			new OrdemClassificacaoGeral(4, ClassificacaoNacionalFinal.N_2),
			new OrdemClassificacaoGeral(5, ClassificacaoCopaNacionalFinal.CN_CAMPEAO),
			new OrdemClassificacaoGeral(6, ClassificacaoNacionalFinal.N_3),
			new OrdemClassificacaoGeral(7, ClassificacaoNacionalFinal.N_4),
			new OrdemClassificacaoGeral(8, ClassificacaoContinentalFinal.CIII_CAMPEAO),
			new OrdemClassificacaoGeral(9, ClassificacaoNacionalFinal.N_5),
			new OrdemClassificacaoGeral(10, ClassificacaoNacionalFinal.N_6),
			new OrdemClassificacaoGeral(11, ClassificacaoNacionalFinal.N_7),
			new OrdemClassificacaoGeral(12, ClassificacaoNacionalFinal.N_8),
			new OrdemClassificacaoGeral(13, ClassificacaoCopaNacionalFinal.CNII_CAMPEAO),
			new OrdemClassificacaoGeral(14, ClassificacaoContinentalFinal.C_VICE_CAMPEAO),
			new OrdemClassificacaoGeral(15, ClassificacaoContinentalFinal.CII_VICE_CAMPEAO),
			new OrdemClassificacaoGeral(16, ClassificacaoCopaNacionalFinal.CN_VICE_CAMPEAO),
			new OrdemClassificacaoGeral(17, ClassificacaoContinentalFinal.CIII_VICE_CAMPEAO),
			new OrdemClassificacaoGeral(18, ClassificacaoContinentalFinal.C_SEMI_FINAL),
			new OrdemClassificacaoGeral(19, ClassificacaoContinentalFinal.CII_SEMI_FINAL),
			new OrdemClassificacaoGeral(20, ClassificacaoCopaNacionalFinal.CNII_VICE_CAMPEAO),
			new OrdemClassificacaoGeral(21, ClassificacaoCopaNacionalFinal.CN_SEMI_FINAL),
			new OrdemClassificacaoGeral(22, ClassificacaoNacionalFinal.N_9),
			new OrdemClassificacaoGeral(23, ClassificacaoNacionalFinal.N_10),
			new OrdemClassificacaoGeral(24, ClassificacaoNacionalFinal.N_11),
			new OrdemClassificacaoGeral(25, ClassificacaoNacionalFinal.N_12),
			new OrdemClassificacaoGeral(26, ClassificacaoNacionalFinal.N_13),
			new OrdemClassificacaoGeral(27, ClassificacaoContinentalFinal.CIII_SEMI_FINAL),
			new OrdemClassificacaoGeral(28, ClassificacaoNacionalFinal.NII_1),
			new OrdemClassificacaoGeral(29, ClassificacaoNacionalFinal.NII_2),
			new OrdemClassificacaoGeral(30, ClassificacaoNacionalFinal.NII_3),
			new OrdemClassificacaoGeral(31, ClassificacaoNacionalFinal.N_14),
			new OrdemClassificacaoGeral(32, ClassificacaoNacionalFinal.N_15),
			new OrdemClassificacaoGeral(33, ClassificacaoNacionalFinal.N_16),
			new OrdemClassificacaoGeral(34, ClassificacaoNacionalFinal.NII_4),
			new OrdemClassificacaoGeral(35, ClassificacaoNacionalFinal.NII_5),
			new OrdemClassificacaoGeral(36, ClassificacaoNacionalFinal.NII_6),
			new OrdemClassificacaoGeral(37, ClassificacaoNacionalFinal.NII_7),
			new OrdemClassificacaoGeral(38, ClassificacaoNacionalFinal.NII_8),
			new OrdemClassificacaoGeral(39, ClassificacaoNacionalFinal.NII_9),
			new OrdemClassificacaoGeral(40, ClassificacaoNacionalFinal.NII_10),
			new OrdemClassificacaoGeral(41, ClassificacaoNacionalFinal.NII_11),
			new OrdemClassificacaoGeral(42, ClassificacaoNacionalFinal.NII_12),
			new OrdemClassificacaoGeral(43, ClassificacaoNacionalFinal.NII_13),
			new OrdemClassificacaoGeral(44, ClassificacaoNacionalFinal.NII_14),
			new OrdemClassificacaoGeral(45, ClassificacaoNacionalFinal.NII_15),
			new OrdemClassificacaoGeral(46, ClassificacaoNacionalFinal.NII_16)
	};

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
