package com.fastfoot.probability.model;

import java.util.List;
import java.util.Map;

import com.fastfoot.model.entity.Clube;

public class ClubeProbabilidade {
	
	private Clube clube;
	
	private Map<Integer, List<ClassificacaoProbabilidade>> posicaoQtde;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Map<Integer, List<ClassificacaoProbabilidade>> getPosicaoQtde() {
		return posicaoQtde;
	}

	public void setPosicaoQtde(Map<Integer, List<ClassificacaoProbabilidade>> posicaoQtde) {
		this.posicaoQtde = posicaoQtde;
	}

}
