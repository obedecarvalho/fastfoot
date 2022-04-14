package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public enum ClassificacaoCopaNacionalFinal {
	NAO_PARTICIPOU,
	CN_OITAVAS_FINAL,
	CN_QUARTAS_FINAL,
	CN_SEMI_FINAL,
	CN_VICE_CAMPEAO,
	CN_CAMPEAO,
	CNII_OITAVAS_FINAL,
	CNII_QUARTAS_FINAL,
	CNII_SEMI_FINAL,
	CNII_VICE_CAMPEAO,
	CNII_CAMPEAO;

	public static ClassificacaoCopaNacionalFinal getClassificacao(NivelCampeonato nivel, RodadaEliminatoria rodada, Clube clube) {
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

	public static ClassificacaoCopaNacionalFinal getClassificacaoCampeao(NivelCampeonato nivel) {
		if (NivelCampeonato.COPA_NACIONAL.equals(nivel)) {
			return CN_CAMPEAO;
		} else if (NivelCampeonato.COPA_NACIONAL_II.equals(nivel)) {
			return CNII_CAMPEAO;
		}
		return NAO_PARTICIPOU;
	}
}
