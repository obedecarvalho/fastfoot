package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.HabilidadeAcaoJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.Jogador;

public interface Esquema {

	/*public List<HabilidadeValor> getHabilidadesValorAcaoMeioFimJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoMeioFimJogadorPosicaoAtualPosse();*/

	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelAcaoMeioFimJogadorPosicaoAtualPosse(Boolean agrupado);

	/*public List<HabilidadeValor> getHabilidadesValorJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorJogadorPosicaoAtualPosse();*/

	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelJogadorPosicaoAtualPosse(Boolean agrupado);

	/*public List<HabilidadeValor> getHabilidadesValorAcaoFimJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorAcaoFimJogadorPosicaoAtualPosse();*/
	
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelAcaoFimJogadorPosicaoAtualPosse(Boolean agrupado);

	/*public List<HabilidadeValor> getHabilidadesValorJogadorPosicaoAtualSemPosse(List<HabilidadeAcao> habilidades);

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValorJogadorPosicaoAtualSemPosse(List<HabilidadeGrupoAcao> habilidades);*/
	
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavelJogadorPosicaoAtualSemPosse(Boolean agrupado, List<? extends HabilidadeAcaoJogavel> habilidades);

	public void inverterPosse();

	public List<EsquemaTransicao> getTransicoesPosse();
	
	/*public List<HabilidadeValor> getHabilidadesValor(List<HabilidadeAcao> habilidades);

	public List<HabilidadeGrupoValor> getHabilidadesGrupoValor(List<HabilidadeGrupoAcao> habilidades);*/
	
	public List<? extends HabilidadeValorJogavel> getHabilidadesValorJogavel(Boolean agrupado, List<? extends HabilidadeAcaoJogavel> habilidades);

	public EsquemaPosicao getGoleiroPosse();

	public EsquemaPosicao getGoleiroSemPosse();

	public EsquemaPosicao getPosicaoAtual();

	public void setPosicaoAtual(EsquemaPosicao posicaoAtual);

	public Boolean getPosseBolaMandante();

	public Jogador getJogadorPosse();
	
	public Jogador getJogadorSemPosse();

	//public Double getProbabilidadeArremateForaPosicaoPosse();
	
	public List<EsquemaPosicao> getPosicoes();
	
	public EsquemaPosicao getGoleiroVisitante();
	
	public EsquemaPosicao getGoleiroMandante();
	
	public EscalacaoClube getEscalacaoClubeMandante();
	
	public EscalacaoClube getEscalacaoClubeVisitante();
}
