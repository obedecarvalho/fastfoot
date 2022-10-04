package com.fastfoot.player.service.util;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;

public class HabilidadeValorGrupoUtil {
	
	private static final List<Habilidade> HAB_DEFESA = Arrays.asList(Habilidade.MARCACAO, Habilidade.DESARME,
			Habilidade.CABECEIO, Habilidade.INTERCEPTACAO);// Habilidade.VELOCIDADE, Habilidade.FORCA
	
	private static final List<Habilidade> HAB_ARREMATE = Arrays.asList(Habilidade.FINALIZACAO, Habilidade.CABECEIO);

	private static final List<Habilidade> HAB_CRIACAO = Arrays.asList(Habilidade.ARMACAO, Habilidade.CRUZAMENTO);// Habilidade.PASSE

	private static final List<Habilidade> HAB_POSSE_BOLA = Arrays.asList(Habilidade.PASSE, Habilidade.DOMINIO);

	private static final List<Habilidade> HAB_QUEBRA_LINHA = Arrays.asList(Habilidade.VELOCIDADE, Habilidade.DRIBLE,
			Habilidade.FORCA, Habilidade.POSICIONAMENTO);
	
	public static Double getHabilidadeValorGrupoDefesa(Jogador j) {
		return j.getHabilidadeValorByHabilidade(HAB_DEFESA).stream().mapToDouble(hv -> hv.getValorTotal()).average()
				.getAsDouble();
	}

	public static Double getHabilidadeValorGrupoArremate(Jogador j) {
		return j.getHabilidadeValorByHabilidade(HAB_ARREMATE).stream().mapToDouble(hv -> hv.getValorTotal()).average()
				.getAsDouble();
	}

	public static Double getHabilidadeValorGrupoCriacao(Jogador j) {
		return j.getHabilidadeValorByHabilidade(HAB_CRIACAO).stream().mapToDouble(hv -> hv.getValorTotal()).average()
				.getAsDouble();
	}

	public static Double getHabilidadeValorGrupoPosseBola(Jogador j) {
		return j.getHabilidadeValorByHabilidade(HAB_POSSE_BOLA).stream().mapToDouble(hv -> hv.getValorTotal()).average()
				.getAsDouble();
	}

	public static Double getHabilidadeValorGrupoQuebraLinha(Jogador j) {
		return j.getHabilidadeValorByHabilidade(HAB_QUEBRA_LINHA).stream().mapToDouble(hv -> hv.getValorTotal())
				.average().getAsDouble();
	}

}
