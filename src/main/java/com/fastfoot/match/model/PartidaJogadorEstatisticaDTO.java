package com.fastfoot.match.model;

import java.util.ArrayList;
import java.util.List;

import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;

public class PartidaJogadorEstatisticaDTO {
	
	private List<HabilidadeValorEstatistica> habilidadeValorEstatistica;
	
	private List<JogadorEstatisticasTemporada> jogadorEstatisticasTemporada;
	
	//private List<JogadorEstatisticasAmistososTemporada> jogadorEstatisticasAmistososTemporada;

	public PartidaJogadorEstatisticaDTO() {
		this.habilidadeValorEstatistica = new ArrayList<HabilidadeValorEstatistica>();
		this.jogadorEstatisticasTemporada = new ArrayList<JogadorEstatisticasTemporada>();
		//this.jogadorEstatisticasAmistososTemporada = new ArrayList<JogadorEstatisticasAmistososTemporada>();
	}

	public List<HabilidadeValorEstatistica> getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}

	public List<JogadorEstatisticasTemporada> getJogadorEstatisticasTemporada() {
		return jogadorEstatisticasTemporada;
	}
	
	public void adicionarHabilidadeValorEstatistica(List<HabilidadeValorEstatistica> habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica.addAll(habilidadeValorEstatistica);
	}
	
	public void adicionarJogadorEstatisticasTemporada(List<JogadorEstatisticasTemporada> jogadorEstatisticasTemporada) {
		this.jogadorEstatisticasTemporada.addAll(jogadorEstatisticasTemporada);
	}

	/*public List<JogadorEstatisticasAmistososTemporada> getJogadorEstatisticasAmistososTemporada() {
		return jogadorEstatisticasAmistososTemporada;
	}
	
	public void adicionarJogadorEstatisticasAmistososTemporada(
			List<JogadorEstatisticasAmistososTemporada> jogadorEstatisticasAmistososTemporada) {
		this.jogadorEstatisticasAmistososTemporada.addAll(jogadorEstatisticasAmistososTemporada);
	}*/
}
