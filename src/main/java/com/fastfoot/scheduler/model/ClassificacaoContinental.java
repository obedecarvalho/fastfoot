package com.fastfoot.scheduler.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public enum ClassificacaoContinental {
	NAO_PARTICIPOU,//-1
	//###	Continental	###
	C_FASE_GRUPOS,//116
	C_QUARTAS_FINAL,//108
	C_SEMI_FINAL,//104
	C_VICE_CAMPEAO,//102
	C_CAMPEAO,//101
	//###	Continental II	###
	CII_FASE_GRUPOS,//216
	CII_QUARTAS_FINAL,//208
	CII_SEMI_FINAL,//204
	CII_VICE_CAMPEAO,//202
	CII_CAMPEAO,//201
	//###	Continental III	###
	CIII_FASE_GRUPOS,//316
	CIII_QUARTAS_FINAL,//308
	CIII_SEMI_FINAL,//304
	CIII_VICE_CAMPEAO,//302
	CIII_CAMPEAO;//301
	
	public static ClassificacaoContinental getClassificacao(NivelCampeonato nivel, RodadaEliminatoria rodada, Boolean vencedor) {
		
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				if (vencedor) {
					return C_CAMPEAO;
				} else {
					return C_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				return C_SEMI_FINAL;
			} else if (rodada.getNumero() == 4) {
				return C_QUARTAS_FINAL;
			}
		}
		
		if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				if (vencedor) {
					return CII_CAMPEAO;
				} else {
					return CII_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				return CII_SEMI_FINAL;
			} else if (rodada.getNumero() == 4) {
				return CII_QUARTAS_FINAL;
			}
		}
		
		if (NivelCampeonato.CONTINENTAL_III.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				if (vencedor) {
					return CIII_CAMPEAO;
				} else {
					return CIII_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				return CIII_SEMI_FINAL;
			} else if (rodada.getNumero() == 4) {
				return CIII_QUARTAS_FINAL;
			}
		}
		
		return NAO_PARTICIPOU;
	}

	@Deprecated
	public static ClassificacaoContinental getClassificacao(NivelCampeonato nivel, RodadaEliminatoria rodada, Clube clube) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubeVencedor().equals(clube)) return C_CAMPEAO;
					if (p.getClubePerdedor().equals(clube)) return C_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return C_SEMI_FINAL;
				}
			} else if (rodada.getNumero() == 4) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return C_QUARTAS_FINAL;
				}
			}
		}

		if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubeVencedor().equals(clube)) return CII_CAMPEAO;
					if (p.getClubePerdedor().equals(clube)) return CII_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CII_SEMI_FINAL;
				}
			} else if (rodada.getNumero() == 4) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CII_QUARTAS_FINAL;
				}
			}
		}

		if (NivelCampeonato.CONTINENTAL_III.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubeVencedor().equals(clube)) return CIII_CAMPEAO;
					if (p.getClubePerdedor().equals(clube)) return CIII_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CIII_SEMI_FINAL;
				}
			} else if (rodada.getNumero() == 4) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CIII_QUARTAS_FINAL;
				}
			}
		}

		return NAO_PARTICIPOU;
	}

	public static ClassificacaoContinental getClassificacaoCampeao(NivelCampeonato nivel) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			return C_CAMPEAO;
		} else if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			return CII_CAMPEAO;
		} else if (NivelCampeonato.CONTINENTAL_III.equals(nivel)) {
			return CIII_CAMPEAO;
		}
		return NAO_PARTICIPOU;
	}

	public static ClassificacaoContinental getClassificacaoFaseGrupo(NivelCampeonato nivel) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			return C_FASE_GRUPOS;
		} else if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			return CII_FASE_GRUPOS;
		} else if (NivelCampeonato.CONTINENTAL_III.equals(nivel)) {
			return CIII_FASE_GRUPOS;
		}
		return NAO_PARTICIPOU;
	}
}
