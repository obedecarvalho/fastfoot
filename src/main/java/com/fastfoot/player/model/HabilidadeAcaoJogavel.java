package com.fastfoot.player.model;

import java.util.List;

public interface HabilidadeAcaoJogavel {

	public boolean contemAcoesSubsequentes();
	
	public List<? extends HabilidadeAcaoJogavel> getAcoesSubsequentes();
	
	public List<? extends HabilidadeAcaoJogavel> getPossiveisReacoes();
	
	public boolean isExigeGoleiro();

	public boolean isAcaoFim();

	public boolean isAcaoMeio();

	public boolean isAcaoInicial();

	public boolean isReacao();

	public boolean isReacaoGoleiro();

	public boolean isAcaoInicioFim();

	public boolean isAcaoInicioMeio();
	
	public HabilidadeAcaoJogavel getReacaoGoleiro();

}
