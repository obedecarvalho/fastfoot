package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public interface Esquema {

	public List<HabilidadeValor> getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesAcaoFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualSemPosse(List<HabilidadeAcao> habilidades);

	public void inverterPosse();

	public List<EsquemaTransicao> getTransicoesPosse();
	
	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades);

	public EsquemaPosicao getGoleiroPosse();

	public EsquemaPosicao getGoleiroSemPosse();

	public EsquemaPosicao getPosicaoAtual();

	public void setPosicaoAtual(EsquemaPosicao posicaoAtual);

	public Boolean getPosseBolaMandante();

	public Jogador getJogadorPosse();
	
	public Jogador getJogadorSemPosse();

	public Double getProbabilidadeArremateForaPosicaoPosse();
}
