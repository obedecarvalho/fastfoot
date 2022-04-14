package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;

public interface Esquema {

	public List<HabilidadeValor> getHabilidadeValorAcaoMeioFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadeValorJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadeValorAcaoFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadeValorJogadorPosicaoAtualSemPosse(List<Habilidade> habilidades);

	public void inverterPosse();

	public List<EsquemaTransicao> getTransicoesPosse();
	
	public List<HabilidadeValor> getHabilidadeValor(List<Habilidade> habilidades);

	public EsquemaPosicao getGoleiroPosse();

	public EsquemaPosicao getGoleiroSemPosse();

	public EsquemaPosicao getPosicaoAtual();

	public void setPosicaoAtual(EsquemaPosicao posicaoAtual);

	public Boolean getPosseBolaMandante();
}
