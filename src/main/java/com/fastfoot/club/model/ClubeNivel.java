package com.fastfoot.club.model;

import com.fastfoot.service.PreCarregarClubeService;

public enum ClubeNivel {
	
	NIVEL_A(PreCarregarClubeService.FORCA_NIVEL_1, 1, 2, 0),
	NIVEL_B(PreCarregarClubeService.FORCA_NIVEL_2, 3, 5, 0),
	NIVEL_C(PreCarregarClubeService.FORCA_NIVEL_3, 6, 9, 0),
	NIVEL_D(PreCarregarClubeService.FORCA_NIVEL_4, 10, 14, 0),
	NIVEL_E(PreCarregarClubeService.FORCA_NIVEL_5, 15, 20, 0),
	NIVEL_F(PreCarregarClubeService.FORCA_NIVEL_6, 21, 26, 0),
	NIVEL_G(PreCarregarClubeService.FORCA_NIVEL_7, 27, 32, 0);
	
	private Integer forcaGeral;
	
	private Integer clubeRankingMin;
	
	private Integer clubeRankingMax;
	
	private Integer tamanhoTorcida;
	
	/*
	 * Consulta Util:
		select c.forca_geral, max(cr.posicao_geral), min(cr.posicao_geral), 
			avg(cr.posicao_geral), percentile_disc(0.5) within group (order by cr.posicao_geral) as mediana
		from clube_ranking cr
		inner join clube c on c.id = cr.id_clube
		where cr.id_temporada is not null
		group by c.forca_geral
	 */
	
	private ClubeNivel(Integer forcaGeral, Integer clubeRankingMin, Integer clubeRankingMax, Integer tamanhoTorcida) {
		this.forcaGeral = forcaGeral;
		this.clubeRankingMin = clubeRankingMin;
		this.clubeRankingMax = clubeRankingMax;
		this.tamanhoTorcida = tamanhoTorcida;
	}

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public Integer getClubeRankingMin() {
		return clubeRankingMin;
	}

	public Integer getClubeRankingMax() {
		return clubeRankingMax;
	}

	public Integer getTamanhoTorcida() {
		return tamanhoTorcida;
	}
}