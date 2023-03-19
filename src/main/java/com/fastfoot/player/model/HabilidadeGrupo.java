package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.List;

public enum HabilidadeGrupo {

	DEFESA(new Habilidade[] {Habilidade.MARCACAO, Habilidade.DESARME, Habilidade.INTERCEPTACAO, Habilidade.CABECEIO, Habilidade.FORCA}),

	CONCLUSAO(new Habilidade[] {Habilidade.FINALIZACAO, Habilidade.CABECEIO}),

	CRIACAO(new Habilidade[] {Habilidade.ARMACAO, Habilidade.CRUZAMENTO, Habilidade.PASSE}),

	POSSE_BOLA(new Habilidade[] {Habilidade.DOMINIO, Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.PASSE}),

	QUEBRA_LINHA(new Habilidade[] {Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.FORCA, Habilidade.POSICIONAMENTO}),

	GOLEIRO(new Habilidade[] {Habilidade.REFLEXO, Habilidade.JOGO_AEREO}),
	;
	
	private Habilidade[] habilidades;
	
	private HabilidadeGrupo(Habilidade[] habilidades) {
		this.habilidades = habilidades;
	}

	public Habilidade[] getHabilidades() {
		return habilidades;
	}
	
	public int[] getHabilidadesOrdinal() {
		return Arrays.stream(habilidades).mapToInt(h -> h.ordinal()).toArray();
	}

	public static List<HabilidadeGrupo> getAll() {
		return Arrays.asList(
				new HabilidadeGrupo[] { HabilidadeGrupo.CONCLUSAO, HabilidadeGrupo.CRIACAO, HabilidadeGrupo.DEFESA,
						HabilidadeGrupo.GOLEIRO, HabilidadeGrupo.POSSE_BOLA, HabilidadeGrupo.QUEBRA_LINHA });
	}
}
