package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public enum ClassificacaoContinentalFinal {
	NAO_PARTICIPOU,
	//###	Continental	###
	C_FASE_GRUPOS,
	C_QUARTAS_FINAL,
	C_SEMI_FINAL,
	C_VICE_CAMPEAO,
	C_CAMPEAO,
	//###	Continental II	###
	CII_FASE_GRUPOS,
	CII_QUARTAS_FINAL,
	CII_SEMI_FINAL,
	CII_VICE_CAMPEAO,
	CII_CAMPEAO;

	public static ClassificacaoContinentalFinal getClassificacao(NivelCampeonato nivel, RodadaEliminatoria rodada, Clube clube) {
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
		return NAO_PARTICIPOU;
	}

	public static ClassificacaoContinentalFinal getClassificacaoCampeao(NivelCampeonato nivel) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			return C_CAMPEAO;
		} else if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			return CII_CAMPEAO;
		}
		return NAO_PARTICIPOU;
	}

	public static ClassificacaoContinentalFinal getClassificacaoFaseGrupo(NivelCampeonato nivel) {
		if (NivelCampeonato.CONTINENTAL.equals(nivel)) {
			return C_FASE_GRUPOS;
		} else if (NivelCampeonato.CONTINENTAL_II.equals(nivel)) {
			return CII_FASE_GRUPOS;
		}
		return NAO_PARTICIPOU;
	}
}
