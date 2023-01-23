package com.fastfoot.match.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.JogadorEstatisticaSemana;

public class PartidaJogadorEstatisticaDTO {

	private List<HabilidadeValorEstatistica> habilidadeValorEstatistica;

	private List<JogadorEstatisticaSemana> jogadorEstatisticasSemana;

	public PartidaJogadorEstatisticaDTO() {
		this.habilidadeValorEstatistica = new ArrayList<HabilidadeValorEstatistica>();
		this.jogadorEstatisticasSemana = new ArrayList<JogadorEstatisticaSemana>();
	}

	public List<HabilidadeValorEstatistica> getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}
	
	public void adicionarHabilidadeValorEstatistica(List<HabilidadeValorEstatistica> habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica.addAll(habilidadeValorEstatistica);
	}

	public List<JogadorEstatisticaSemana> getJogadorEstatisticasSemana() {
		return jogadorEstatisticasSemana;
	}

	public void adicionarJogadorEstatisticaSemana(List<JogadorEstatisticaSemana> jogadorEstatisticaSemana) {
		this.jogadorEstatisticasSemana.addAll(jogadorEstatisticaSemana);
	}

}
