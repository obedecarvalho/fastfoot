package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HabilidadeGrupoAcao implements HabilidadeAcaoJogavel {
	
	public static final HabilidadeGrupoAcao DEFESA = new HabilidadeGrupoAcao(HabilidadeGrupo.DEFESA);
	
	public static final HabilidadeGrupoAcao CONCLUSAO = new HabilidadeGrupoAcao(HabilidadeGrupo.CONCLUSAO);
	
	public static final HabilidadeGrupoAcao CRIACAO = new HabilidadeGrupoAcao(HabilidadeGrupo.CRIACAO);
	
	public static final HabilidadeGrupoAcao POSSE_BOLA = new HabilidadeGrupoAcao(HabilidadeGrupo.POSSE_BOLA);

	public static final HabilidadeGrupoAcao QUEBRA_LINHA = new HabilidadeGrupoAcao(HabilidadeGrupo.QUEBRA_LINHA);

	public static final HabilidadeGrupoAcao GOLEIRO = new HabilidadeGrupoAcao(HabilidadeGrupo.GOLEIRO);

	public static final Map<HabilidadeGrupo, HabilidadeGrupoAcao> HABILIDADE_GRUPO_ACAO = new HashMap<HabilidadeGrupo, HabilidadeGrupoAcao>();
	

	static {
		//--> Reação
		//==> Ação Subsequente
		
		//CONCLUSAO	AF --> DEFESA
		CONCLUSAO.setPossiveisReacoes(Arrays.asList(DEFESA));
		CONCLUSAO.setReacaoGoleiro(GOLEIRO);
		
		//CRIACAO	AF --> DEFESA	==> POSSE_BOLA
		CRIACAO.setPossiveisReacoes(Arrays.asList(DEFESA));
		CRIACAO.setAcoesSubsequentes(Arrays.asList(POSSE_BOLA));
		
		//QUEBRA_LINHA	AM --> DEFESA
		QUEBRA_LINHA.setPossiveisReacoes(Arrays.asList(DEFESA));
		
		//POSSE_BOLA	AI --> DEFESA
		POSSE_BOLA.setPossiveisReacoes(Arrays.asList(DEFESA));
		
		//DEFESA	R
		
		//GOLEIRO	RG

		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.DEFESA, DEFESA);
		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.CONCLUSAO, CONCLUSAO);
		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.CRIACAO, CRIACAO);
		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.POSSE_BOLA, POSSE_BOLA);
		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.QUEBRA_LINHA, QUEBRA_LINHA);
		HABILIDADE_GRUPO_ACAO.put(HabilidadeGrupo.GOLEIRO, GOLEIRO);
	}

	private HabilidadeGrupo habilidadeGrupo;

	private List<HabilidadeGrupoAcao> possiveisReacoes;
	
	private List<HabilidadeGrupoAcao> acoesSubsequentes;
	
	private HabilidadeGrupoAcao reacaoGoleiro;

	public HabilidadeGrupoAcao(HabilidadeGrupo habilidadeGrupo) {
		this.habilidadeGrupo = habilidadeGrupo;
	}

	/*
	 * public String getDescricao() { return habilidade.getDescricao(); }
	 */

	@Override
	public List<HabilidadeGrupoAcao> getPossiveisReacoes() {
		return possiveisReacoes;
	}

	public void setPossiveisReacoes(List<HabilidadeGrupoAcao> possiveisReacoes) {
		this.possiveisReacoes = possiveisReacoes;
	}

	@Override
	public List<HabilidadeGrupoAcao> getAcoesSubsequentes() {
		return acoesSubsequentes;
	}

	public void setAcoesSubsequentes(List<HabilidadeGrupoAcao> acoesSubsequentes) {
		this.acoesSubsequentes = acoesSubsequentes;
	}

	@Override
	public HabilidadeGrupoAcao getReacaoGoleiro() {
		return reacaoGoleiro;
	}

	public void setReacaoGoleiro(HabilidadeGrupoAcao reacaoGoleiro) {
		this.reacaoGoleiro = reacaoGoleiro;
	}

	@Override
	public boolean contemAcoesSubsequentes() {
		return acoesSubsequentes != null;
	}

	@Override
	public boolean isExigeGoleiro() {
		return reacaoGoleiro != null;
	}

	@Override
	public boolean isAcaoFim() {
		return habilidadeGrupo.isAcaoFim();
	}

	@Override
	public boolean isAcaoMeio() {
		return habilidadeGrupo.isAcaoMeio();
	}

	@Override
	public boolean isAcaoInicial() {
		return habilidadeGrupo.isAcaoInicial();
	}

	@Override
	public boolean isReacao() {
		return habilidadeGrupo.isReacao();
	}

	@Override
	public boolean isReacaoGoleiro() {
		return habilidadeGrupo.isReacaoGoleiro();
	}

	@Override
	public boolean isAcaoInicioFim() {
		return habilidadeGrupo.isAcaoInicioFim();
	}

	@Override
	public boolean isAcaoInicioMeio() {
		return habilidadeGrupo.isAcaoInicioMeio();
	}

	public HabilidadeGrupo getHabilidadeGrupo() {
		return habilidadeGrupo;
	}

	public void setHabilidadeGrupo(HabilidadeGrupo habilidadeGrupo) {
		this.habilidadeGrupo = habilidadeGrupo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(habilidadeGrupo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HabilidadeGrupoAcao other = (HabilidadeGrupoAcao) obj;
		return habilidadeGrupo == other.habilidadeGrupo;
	}

	public String getDescricao() {
		return habilidadeGrupo.getDescricao();
	}

	@Override
	public String toString() {
		return "HabGrupAcao [" + getDescricao() + "]";
	}
}
