package com.fastfoot.player.model;

import java.util.List;

public interface EstrategiaHabilidadePosicaoJogador {

	public List<Habilidade> getHabilidadesEspecificas();

	public List<Habilidade> getHabilidadesEspecificasEletivas();

	public List<Habilidade> getHabilidadesComum();
	
	public List<Habilidade> getHabilidadesComunsEletivas();

	public List<Habilidade> getHabilidadesOutros();
	
	public List<Habilidade> getHabilidadesCoringa();

	public Integer getNumHabEspEletivas();

	public Integer getNumHabComunsEletivas();
	
	public Integer getNumHabCoringaSelecionadoEspecifica();
	
	public Integer getNumHabCoringaSelecionadoComum();

}
