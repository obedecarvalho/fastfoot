package com.fastfoot.club.model;

import com.fastfoot.service.PreCarregarClubeService;

public enum ClubeNivel {
	
	/**
	 	create table clube_nivel_info (
        	clube_nivel int4, tamanho_torcida int4
		);
		
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (0, 60000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (1, 50000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (2, 42000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (3, 36000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (4, 30000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (5, 25000);
		INSERT INTO clube_nivel_info (clube_nivel, tamanho_torcida) VALUES (6, 20000);

	 */
	
	NIVEL_A(PreCarregarClubeService.FORCA_NIVEL_1, 1, 2, 60000, 30000000.0d),
	NIVEL_B(PreCarregarClubeService.FORCA_NIVEL_2, 3, 5, 50000, 25000000.0d),
	NIVEL_C(PreCarregarClubeService.FORCA_NIVEL_3, 6, 9, 42000, 21000000.0d),
	NIVEL_D(PreCarregarClubeService.FORCA_NIVEL_4, 10, 14, 36000, 17500000.0d),
	NIVEL_E(PreCarregarClubeService.FORCA_NIVEL_5, 15, 20, 30000, 14500000.0d),
	NIVEL_F(PreCarregarClubeService.FORCA_NIVEL_6, 21, 26, 25000, 12000000.0d),
	NIVEL_G(PreCarregarClubeService.FORCA_NIVEL_7, 27, 32, 20000, 10000000.0d);
	
	private Integer forcaGeral;
	
	private Integer clubeRankingMin;
	
	private Integer clubeRankingMax;
	
	private Integer tamanhoTorcida;
	
	private Double caixaInicial;
	
	/*
	 * Consulta Util:
		select c.forca_geral, max(cr.posicao_geral), min(cr.posicao_geral), 
			avg(cr.posicao_geral), percentile_disc(0.5) within group (order by cr.posicao_geral) as mediana
		from clube_ranking cr
		inner join clube c on c.id = cr.id_clube
		where cr.id_temporada is not null
		group by c.forca_geral
	 */
	
	private ClubeNivel(Integer forcaGeral, Integer clubeRankingMin, Integer clubeRankingMax, Integer tamanhoTorcida,
			Double caixaInicial) {
		this.forcaGeral = forcaGeral;
		this.clubeRankingMin = clubeRankingMin;
		this.clubeRankingMax = clubeRankingMax;
		this.tamanhoTorcida = tamanhoTorcida;
		this.caixaInicial = caixaInicial;
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

	public Double getCaixaInicial() {
		return caixaInicial;
	}
}
