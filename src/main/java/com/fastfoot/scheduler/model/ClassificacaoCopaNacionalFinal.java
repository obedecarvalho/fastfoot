package com.fastfoot.scheduler.model;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;

public enum ClassificacaoCopaNacionalFinal {
	NAO_PARTICIPOU,//99
	CN_OITAVAS_FINAL,//16
	CN_QUARTAS_FINAL,//8
	CN_SEMI_FINAL,//4
	CN_VICE_CAMPEAO,//2
	CN_CAMPEAO,//1
	CNII_OITAVAS_FINAL,//16
	CNII_QUARTAS_FINAL,//8
	CNII_SEMI_FINAL,//4
	CNII_VICE_CAMPEAO,//2
	CNII_CAMPEAO;//1

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
