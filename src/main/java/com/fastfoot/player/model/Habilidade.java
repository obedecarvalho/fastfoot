package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Habilidade {
	
	public static final Habilidade PASSE = new Habilidade(1, "Passe", TipoHabilidade.ACAO_FIM);
	public static final Habilidade FINALIZACAO = new Habilidade(2, "Finalizacao", TipoHabilidade.ACAO_FIM);
	public static final Habilidade CRUZAMENTO = new Habilidade(3, "Cruzamento", TipoHabilidade.ACAO_FIM);
	public static final Habilidade ARMACAO = new Habilidade(4, "Armacao", TipoHabilidade.ACAO_FIM);

	public static final Habilidade MARCACAO = new Habilidade(5, "Marcacao", TipoHabilidade.REACAO);
	public static final Habilidade DESARME = new Habilidade(6, "Desarme", TipoHabilidade.REACAO);
	public static final Habilidade INTERCEPTACAO = new Habilidade(7, "Interceptacao", TipoHabilidade.REACAO);

	public static final Habilidade VELOCIDADE = new Habilidade(8, "Velocidade", TipoHabilidade.ACAO_MEIO);
	public static final Habilidade DIBLE = new Habilidade(9, "Dible", TipoHabilidade.ACAO_MEIO);
	public static final Habilidade FORCA = new Habilidade(10, "Forca", TipoHabilidade.ACAO_MEIO);
	
	public static final Habilidade CABECEIO = new Habilidade(11, "Cabeceio", TipoHabilidade.ACAO_INICIO_FIM);
	public static final Habilidade POSICIONAMENTO = new Habilidade(12, "Posicionamento", TipoHabilidade.ACAO_INICIO_MEIO);
	public static final Habilidade DOMINIO = new Habilidade(13, "Dominio", TipoHabilidade.ACAO_INICIAL);
	
	public static final Habilidade REFLEXO = new Habilidade(14, "Reflexo", TipoHabilidade.REACAO_GOLEIRO);
	public static final Habilidade JOGO_AEREO = new Habilidade(15, "Jogo Aereo", TipoHabilidade.REACAO_GOLEIRO);

	public static final Habilidade NULL = new Habilidade(16, "NULL", null);
	

	static {
		//--> Reação
		//==> Ação Subsequente
		
		//Passe AF --> Marcacao ==> {Dominio}?
		PASSE.setPossiveisReacoes(Arrays.asList(MARCACAO));
		PASSE.setAcoesSubsequentes(Arrays.asList(DOMINIO));
		
		//Finalizacao AF --> Marcacao
		FINALIZACAO.setPossiveisReacoes(Arrays.asList(MARCACAO));
		FINALIZACAO.setReacaoGoleiro(REFLEXO);
		
		//Cruzamento AF --> Marcacao ==> {Cabeceio}
		CRUZAMENTO.setPossiveisReacoes(Arrays.asList(MARCACAO));
		CRUZAMENTO.setAcoesSubsequentes(Arrays.asList(CABECEIO));
		
		//Armacao AF --> Marcacao ==> {Posicionamento}
		ARMACAO.setPossiveisReacoes(Arrays.asList(MARCACAO));
		ARMACAO.setAcoesSubsequentes(Arrays.asList(POSICIONAMENTO));
		
		//Cabeceio AF --> Cabeceio 
		CABECEIO.setPossiveisReacoes(Arrays.asList(CABECEIO));
		CABECEIO.setReacaoGoleiro(JOGO_AEREO);
		
		/*
		//Marcacao R
		MARCACAO.setPossiveisReacoes(Arrays.asList(null));
		
		//Desarme R
		DESARME.setPossiveisReacoes(Arrays.asList(null));
		
		//Interceptacao R
		INTERCEPTACAO.setPossiveisReacoes(Arrays.asList(null));
		*/
		
		//Velocidade AM --> {Desarme, Velocidade}
		VELOCIDADE.setPossiveisReacoes(Arrays.asList(DESARME, VELOCIDADE));

		
		//Dible AM --> Desarme
		DIBLE.setPossiveisReacoes(Arrays.asList(DESARME));

		
		//Força AM --> {Desarme, Força}?
		FORCA.setPossiveisReacoes(Arrays.asList(DESARME, FORCA));

		
		//Posicionamento AI --> Interceptacao  
		POSICIONAMENTO.setPossiveisReacoes(Arrays.asList(INTERCEPTACAO));

		
		//Dominio(?) AI --> Interceptacao 
		DOMINIO.setPossiveisReacoes(Arrays.asList(INTERCEPTACAO));

	}
	
	private Integer id;

	private String descricao;
	
	private List<Habilidade> possiveisReacoes;
	
	private List<Habilidade> acoesSubsequentes;
	
	private Habilidade reacaoGoleiro;
	
	private TipoHabilidade tipoHabilidade;
	
	
	
	public Habilidade(Integer id, String descricao, TipoHabilidade tipoHabilidade) {
		super();
		this.descricao = descricao;
		this.tipoHabilidade = tipoHabilidade;
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Habilidade> getPossiveisReacoes() {
		return possiveisReacoes;
	}

	public void setPossiveisReacoes(List<Habilidade> possiveisReacoes) {
		this.possiveisReacoes = possiveisReacoes;
	}

	public List<Habilidade> getAcoesSubsequentes() {
		return acoesSubsequentes;
	}

	public void setAcoesSubsequentes(List<Habilidade> acoesSubsequentes) {
		this.acoesSubsequentes = acoesSubsequentes;
	}

	public TipoHabilidade getTipoHabilidade() {
		return tipoHabilidade;
	}

	public void setTipoHabilidade(TipoHabilidade tipoHabilidade) {
		this.tipoHabilidade = tipoHabilidade;
	}

	public Habilidade getReacaoGoleiro() {
		return reacaoGoleiro;
	}

	public void setReacaoGoleiro(Habilidade reacaoGoleiro) {
		this.reacaoGoleiro = reacaoGoleiro;
	}

	public boolean contemAcoesSubsequentes() {
		return acoesSubsequentes != null;
	}
	
	public boolean isAcaoFim() {
		return TipoHabilidade.ACAO_FIM.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoMeio() {
		return TipoHabilidade.ACAO_MEIO.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoInicial() {
		return TipoHabilidade.ACAO_INICIAL.equals(getTipoHabilidade());
	}
	
	public boolean isReacao() {
		return TipoHabilidade.REACAO.equals(getTipoHabilidade());
	}

	public boolean isReacaoGoleiro() {
		return TipoHabilidade.REACAO_GOLEIRO.equals(getTipoHabilidade());
	}

	public boolean isAcaoInicioFim() {
		return TipoHabilidade.ACAO_INICIO_FIM.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoInicioMeio() {
		return TipoHabilidade.ACAO_INICIO_MEIO.equals(getTipoHabilidade());
	}
	
	public boolean isExigeGoleiro() {
		return reacaoGoleiro != null;
	}

	@Override
	public String toString() {
		return "Hab [" + descricao + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Habilidade other = (Habilidade) obj;
		return Objects.equals(id, other.id);
	}

}
