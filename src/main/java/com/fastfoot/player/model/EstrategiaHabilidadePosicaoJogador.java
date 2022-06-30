package com.fastfoot.player.model;

import java.util.List;

public interface EstrategiaHabilidadePosicaoJogador {

	public List<Habilidade> getHabilidadesEspecificas();

	public List<Habilidade> getHabilidadesEspecificasEletivas();

	public List<Habilidade> getHabilidadesComum();

	public List<Habilidade> getHabilidadesOutros();

	public Integer getNumHabEspEletivas();

}
