package com.fastfoot.scheduler.model;

public class OrdemClassificacaoGeral {
	
	public static final OrdemClassificacaoGeral[] ORDEM = {
			
			new OrdemClassificacaoGeral(1, ClassificacaoContinental.C_CAMPEAO),//CI
			new OrdemClassificacaoGeral(2, ClassificacaoNacional.N_1),//CI
			new OrdemClassificacaoGeral(3, ClassificacaoContinental.CII_CAMPEAO),//CI
			new OrdemClassificacaoGeral(4, ClassificacaoNacional.N_2),//CI
			new OrdemClassificacaoGeral(5, ClassificacaoCopaNacional.CN_CAMPEAO),//CI ou CII
			new OrdemClassificacaoGeral(6, ClassificacaoNacional.N_3),//CI ou CII
			new OrdemClassificacaoGeral(7, ClassificacaoNacional.N_4),//CI ou CII
			new OrdemClassificacaoGeral(8, ClassificacaoContinental.CIII_CAMPEAO),//CII
			new OrdemClassificacaoGeral(9, ClassificacaoNacional.N_5),//CII ou CIII
			new OrdemClassificacaoGeral(10, ClassificacaoNacional.N_6),//CII ou CIII
			new OrdemClassificacaoGeral(11, ClassificacaoNacional.N_7),//CII ou CIII ou Nada
			new OrdemClassificacaoGeral(12, ClassificacaoNacional.N_8),//CII ou CIII ou Nada
			new OrdemClassificacaoGeral(13, ClassificacaoCopaNacional.CNII_CAMPEAO),//CIII ou Nada
			new OrdemClassificacaoGeral(16, ClassificacaoNacional.N_9),//CIII ou Nada
			new OrdemClassificacaoGeral(17, ClassificacaoNacional.N_10),//CIII ou Nada
			new OrdemClassificacaoGeral(18, ClassificacaoNacional.N_11),//CIII ou Nada
			new OrdemClassificacaoGeral(19, ClassificacaoNacional.N_12),//CIII ou Nada
			new OrdemClassificacaoGeral(20, ClassificacaoNacional.N_13),
			new OrdemClassificacaoGeral(21, ClassificacaoNacional.NII_1),
			new OrdemClassificacaoGeral(22, ClassificacaoNacional.NII_2),
			new OrdemClassificacaoGeral(23, ClassificacaoNacional.NII_3),
			new OrdemClassificacaoGeral(24, ClassificacaoNacional.N_14),
			new OrdemClassificacaoGeral(25, ClassificacaoNacional.N_15),
			new OrdemClassificacaoGeral(26, ClassificacaoNacional.N_16),
			new OrdemClassificacaoGeral(27, ClassificacaoNacional.NII_4),
			new OrdemClassificacaoGeral(28, ClassificacaoNacional.NII_5),
			new OrdemClassificacaoGeral(29, ClassificacaoNacional.NII_6),
			new OrdemClassificacaoGeral(30, ClassificacaoNacional.NII_7),
			new OrdemClassificacaoGeral(31, ClassificacaoNacional.NII_8),
			new OrdemClassificacaoGeral(32, ClassificacaoNacional.NII_9),
			new OrdemClassificacaoGeral(33, ClassificacaoNacional.NII_10),
			new OrdemClassificacaoGeral(34, ClassificacaoNacional.NII_11),
			new OrdemClassificacaoGeral(35, ClassificacaoNacional.NII_12),
			new OrdemClassificacaoGeral(36, ClassificacaoNacional.NII_13),
			new OrdemClassificacaoGeral(37, ClassificacaoNacional.NII_14),
			new OrdemClassificacaoGeral(38, ClassificacaoNacional.NII_15),
			new OrdemClassificacaoGeral(39, ClassificacaoNacional.NII_16)

	};
	
	public static final OrdemClassificacaoGeral[] ORDEM_GLOBAL = {
			new OrdemClassificacaoGeral(1, ClassificacaoContinental.C_CAMPEAO),//101
			new OrdemClassificacaoGeral(2, ClassificacaoNacional.N_1),
			new OrdemClassificacaoGeral(3, ClassificacaoContinental.CII_CAMPEAO),//201
			new OrdemClassificacaoGeral(4, ClassificacaoNacional.N_2),
			new OrdemClassificacaoGeral(5, ClassificacaoCopaNacional.CN_CAMPEAO),//101
			new OrdemClassificacaoGeral(6, ClassificacaoNacional.N_3),
			new OrdemClassificacaoGeral(7, ClassificacaoNacional.N_4),
			new OrdemClassificacaoGeral(8, ClassificacaoContinental.CIII_CAMPEAO),//301
			new OrdemClassificacaoGeral(9, ClassificacaoNacional.N_5),
			new OrdemClassificacaoGeral(10, ClassificacaoNacional.N_6),
			new OrdemClassificacaoGeral(11, ClassificacaoNacional.N_7),
			new OrdemClassificacaoGeral(12, ClassificacaoNacional.N_8),
			new OrdemClassificacaoGeral(13, ClassificacaoCopaNacional.CNII_CAMPEAO),//201
			new OrdemClassificacaoGeral(14, ClassificacaoContinental.C_VICE_CAMPEAO),//102
			new OrdemClassificacaoGeral(15, ClassificacaoContinental.CII_VICE_CAMPEAO),//202
			new OrdemClassificacaoGeral(16, ClassificacaoCopaNacional.CN_VICE_CAMPEAO),//102
			new OrdemClassificacaoGeral(17, ClassificacaoContinental.CIII_VICE_CAMPEAO),//302
			new OrdemClassificacaoGeral(18, ClassificacaoContinental.C_SEMI_FINAL),//104
			new OrdemClassificacaoGeral(19, ClassificacaoContinental.CII_SEMI_FINAL),//204
			new OrdemClassificacaoGeral(20, ClassificacaoCopaNacional.CNII_VICE_CAMPEAO),//202
			new OrdemClassificacaoGeral(21, ClassificacaoCopaNacional.CN_SEMI_FINAL),//104
			new OrdemClassificacaoGeral(22, ClassificacaoNacional.N_9),
			new OrdemClassificacaoGeral(23, ClassificacaoNacional.N_10),
			new OrdemClassificacaoGeral(24, ClassificacaoNacional.N_11),
			new OrdemClassificacaoGeral(25, ClassificacaoNacional.N_12),
			new OrdemClassificacaoGeral(26, ClassificacaoNacional.N_13),
			new OrdemClassificacaoGeral(27, ClassificacaoContinental.CIII_SEMI_FINAL),//304
			new OrdemClassificacaoGeral(28, ClassificacaoNacional.NII_1),
			new OrdemClassificacaoGeral(29, ClassificacaoNacional.NII_2),
			new OrdemClassificacaoGeral(30, ClassificacaoNacional.NII_3),
			new OrdemClassificacaoGeral(31, ClassificacaoNacional.N_14),
			new OrdemClassificacaoGeral(32, ClassificacaoNacional.N_15),
			new OrdemClassificacaoGeral(33, ClassificacaoNacional.N_16),
			new OrdemClassificacaoGeral(27, ClassificacaoCopaNacional.CNII_SEMI_FINAL),//204
			new OrdemClassificacaoGeral(34, ClassificacaoNacional.NII_4),
			new OrdemClassificacaoGeral(35, ClassificacaoNacional.NII_5),
			new OrdemClassificacaoGeral(36, ClassificacaoNacional.NII_6),
			new OrdemClassificacaoGeral(37, ClassificacaoNacional.NII_7),
			new OrdemClassificacaoGeral(38, ClassificacaoNacional.NII_8),
			new OrdemClassificacaoGeral(39, ClassificacaoNacional.NII_9),
			new OrdemClassificacaoGeral(40, ClassificacaoNacional.NII_10),
			new OrdemClassificacaoGeral(41, ClassificacaoNacional.NII_11),
			new OrdemClassificacaoGeral(42, ClassificacaoNacional.NII_12),
			new OrdemClassificacaoGeral(43, ClassificacaoNacional.NII_13),
			new OrdemClassificacaoGeral(44, ClassificacaoNacional.NII_14),
			new OrdemClassificacaoGeral(45, ClassificacaoNacional.NII_15),
			new OrdemClassificacaoGeral(46, ClassificacaoNacional.NII_16),
			new OrdemClassificacaoGeral(47, ClassificacaoContinental.C_QUARTAS_FINAL),//108
			new OrdemClassificacaoGeral(48, ClassificacaoContinental.CII_QUARTAS_FINAL),//208
			new OrdemClassificacaoGeral(49, ClassificacaoCopaNacional.CN_QUARTAS_FINAL),//108
			new OrdemClassificacaoGeral(50, ClassificacaoContinental.CIII_QUARTAS_FINAL),//308
			new OrdemClassificacaoGeral(51, ClassificacaoCopaNacional.CNII_QUARTAS_FINAL),//208
			new OrdemClassificacaoGeral(52, ClassificacaoContinental.C_FASE_GRUPOS),//116
			new OrdemClassificacaoGeral(53, ClassificacaoContinental.CII_FASE_GRUPOS),//216
			new OrdemClassificacaoGeral(54, ClassificacaoCopaNacional.CN_OITAVAS_FINAL),//116
			new OrdemClassificacaoGeral(55, ClassificacaoContinental.CIII_FASE_GRUPOS),//316
			new OrdemClassificacaoGeral(56, ClassificacaoCopaNacional.CNII_OITAVAS_FINAL)//216
	};

	private Integer ordem;

	private ClassificacaoNacional classificacaoNacional;
	
	private ClassificacaoContinental classificacaoContinental;
	
	private ClassificacaoCopaNacional classificacaoCopaNacional;
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoNacional classificacao) {
		this.ordem = ordem;
		this.classificacaoNacional = classificacao;
	}
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoContinental classificacao) {
		this.ordem = ordem;
		this.classificacaoContinental = classificacao;
	}
	
	public OrdemClassificacaoGeral(Integer ordem, ClassificacaoCopaNacional classificacao) {
		this.ordem = ordem;
		this.classificacaoCopaNacional = classificacao;
	}

	public ClassificacaoNacional getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacional classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoContinental getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinental classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public ClassificacaoCopaNacional getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacional classificacaoCopaNacional) {
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
