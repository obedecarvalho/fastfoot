package com.fastfoot.match.model;

import java.util.List;

import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.HabilidadeGrupoAcao;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public interface Esquema {

	public List<HabilidadeValor> getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoAcaoMeioFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesAcaoFimJogadorPosicaoAtualPosse();

	public List<HabilidadeGrupoValor> getHabilidadesGrupoAcaoFimJogadorPosicaoAtualPosse();

	public List<HabilidadeValor> getHabilidadesJogadorPosicaoAtualSemPosse(List<HabilidadeAcao> habilidades);

	public List<HabilidadeGrupoValor> getHabilidadesGrupoJogadorPosicaoAtualSemPosse(List<HabilidadeGrupoAcao> habilidades);

	public void inverterPosse();

	public List<EsquemaTransicao> getTransicoesPosse();
	
	public List<HabilidadeValor> getHabilidades(List<HabilidadeAcao> habilidades);

	public List<HabilidadeGrupoValor> getHabilidadesGrupo(List<HabilidadeGrupoAcao> habilidades);

	public EsquemaPosicao getGoleiroPosse();

	public EsquemaPosicao getGoleiroSemPosse();

	public EsquemaPosicao getPosicaoAtual();

	public void setPosicaoAtual(EsquemaPosicao posicaoAtual);

	public Boolean getPosseBolaMandante();

	public Jogador getJogadorPosse();
	
	public Jogador getJogadorSemPosse();

	public Double getProbabilidadeArremateForaPosicaoPosse();
	
	public List<EsquemaPosicao> getPosicoes();
	
	public EsquemaPosicao getGoleiroVisitante();
	
	public EsquemaPosicao getGoleiroMandante();
	
	public EscalacaoClube getEscalacaoClubeMandante();
	
	public EscalacaoClube getEscalacaoClubeVisitante();
}
