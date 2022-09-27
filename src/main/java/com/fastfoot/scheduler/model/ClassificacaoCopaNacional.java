package com.fastfoot.scheduler.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public enum ClassificacaoCopaNacional {
	NAO_PARTICIPOU,//-1
	CN_OITAVAS_FINAL,//116
	CN_QUARTAS_FINAL,//108
	CN_SEMI_FINAL,//104
	CN_VICE_CAMPEAO,//102
	CN_CAMPEAO,//101
	CNII_OITAVAS_FINAL,//216
	CNII_QUARTAS_FINAL,//208
	CNII_SEMI_FINAL,//204
	CNII_VICE_CAMPEAO,//202
	CNII_CAMPEAO;//201

	public static ClassificacaoCopaNacional getClassificacao(NivelCampeonato nivel, RodadaEliminatoria rodada, Clube clube) {
		if (NivelCampeonato.COPA_NACIONAL.equals(nivel)) {
			if (rodada.getNumero() == 6) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubeVencedor().equals(clube)) return CN_CAMPEAO;
					if (p.getClubePerdedor().equals(clube)) return CN_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 5) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CN_SEMI_FINAL;
				}
			} else if (rodada.getNumero() == 4) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CN_QUARTAS_FINAL;
				}
			} else if (rodada.getNumero() == 3) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CN_OITAVAS_FINAL;
				}
			}
		}

		if (NivelCampeonato.COPA_NACIONAL_II.equals(nivel)) {
			if (rodada.getNumero() == 4) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubeVencedor().equals(clube)) return CNII_CAMPEAO;
					if (p.getClubePerdedor().equals(clube)) return CNII_VICE_CAMPEAO;
				}
			} else if (rodada.getNumero() == 3) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CNII_SEMI_FINAL;
				}
			} else if (rodada.getNumero() == 2) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CNII_QUARTAS_FINAL;
				}
			} else if (rodada.getNumero() == 1) {
				for (PartidaEliminatoriaResultado p : rodada.getPartidas()) {
					if (p.getClubePerdedor().equals(clube)) return CNII_OITAVAS_FINAL;
				}
			}
		}
		return NAO_PARTICIPOU;
	}

	public static ClassificacaoCopaNacional getClassificacaoCampeao(NivelCampeonato nivel) {
		if (NivelCampeonato.COPA_NACIONAL.equals(nivel)) {
			return CN_CAMPEAO;
		} else if (NivelCampeonato.COPA_NACIONAL_II.equals(nivel)) {
			return CNII_CAMPEAO;
		}
		return NAO_PARTICIPOU;
	}
}
