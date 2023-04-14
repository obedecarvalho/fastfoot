package com.fastfoot.match.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.JogadorEnergia;
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;

public class PartidaJogadorEstatisticaDTO {

	private List<HabilidadeValorEstatistica> habilidadeValorEstatistica;

	private List<JogadorEstatisticasSemana> jogadorEstatisticasSemana;
	
	private List<PartidaLance> partidaLances;
	
	private List<JogadorEnergia> jogadorEnergias;

	public PartidaJogadorEstatisticaDTO() {
		this.habilidadeValorEstatistica = new ArrayList<HabilidadeValorEstatistica>();
		this.jogadorEstatisticasSemana = new ArrayList<JogadorEstatisticasSemana>();
		this.partidaLances = new ArrayList<PartidaLance>();
		this.jogadorEnergias = new ArrayList<JogadorEnergia>();
	}

	public List<HabilidadeValorEstatistica> getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}
	
	public void adicionarHabilidadeValorEstatistica(List<HabilidadeValorEstatistica> habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica.addAll(habilidadeValorEstatistica);
	}

	public List<JogadorEstatisticasSemana> getJogadorEstatisticasSemana() {
		return jogadorEstatisticasSemana;
	}

	public void adicionarJogadorEstatisticaSemana(List<JogadorEstatisticasSemana> jogadorEstatisticaSemana) {
		this.jogadorEstatisticasSemana.addAll(jogadorEstatisticaSemana);
	}

	public List<PartidaLance> getPartidaLances() {
		return partidaLances;
	}

	public void adicionarPartidaLance(List<PartidaLance> partidaLances) {
		this.partidaLances.addAll(partidaLances);
	}

	public List<JogadorEnergia> getJogadorEnergias() {
		return jogadorEnergias;
	}

	public void adicionarJogadorEnergias(List<JogadorEnergia> jogadorEstatisticaSemana) {
		this.jogadorEnergias.addAll(jogadorEstatisticaSemana);
	}
}
