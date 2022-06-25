package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HabilidadeAcao {
	
	public static final HabilidadeAcao PASSE = new HabilidadeAcao(Habilidade.PASSE);
	public static final HabilidadeAcao FINALIZACAO = new HabilidadeAcao(Habilidade.FINALIZACAO);
	public static final HabilidadeAcao CRUZAMENTO = new HabilidadeAcao(Habilidade.CRUZAMENTO);
	public static final HabilidadeAcao ARMACAO = new HabilidadeAcao(Habilidade.ARMACAO);

	public static final HabilidadeAcao MARCACAO = new HabilidadeAcao(Habilidade.MARCACAO);
	public static final HabilidadeAcao DESARME = new HabilidadeAcao(Habilidade.DESARME);
	public static final HabilidadeAcao INTERCEPTACAO = new HabilidadeAcao(Habilidade.INTERCEPTACAO);

	public static final HabilidadeAcao VELOCIDADE = new HabilidadeAcao(Habilidade.VELOCIDADE);
	public static final HabilidadeAcao DRIBLE = new HabilidadeAcao(Habilidade.DRIBLE);
	public static final HabilidadeAcao FORCA = new HabilidadeAcao(Habilidade.FORCA);
	
	public static final HabilidadeAcao CABECEIO = new HabilidadeAcao(Habilidade.CABECEIO);
	public static final HabilidadeAcao POSICIONAMENTO = new HabilidadeAcao(Habilidade.POSICIONAMENTO);
	public static final HabilidadeAcao DOMINIO = new HabilidadeAcao(Habilidade.DOMINIO);
	
	public static final HabilidadeAcao REFLEXO = new HabilidadeAcao(Habilidade.REFLEXO);
	public static final HabilidadeAcao JOGO_AEREO = new HabilidadeAcao(Habilidade.JOGO_AEREO);

	public static final HabilidadeAcao NULL = new HabilidadeAcao(Habilidade.NULL);
	
	public static final Map<Habilidade, HabilidadeAcao> HABILIDADE_ACAO = new HashMap<Habilidade, HabilidadeAcao>();
	

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

		
		//Drible AM --> Desarme
		DRIBLE.setPossiveisReacoes(Arrays.asList(DESARME));

		
		//Força AM --> {Desarme, Força}?
		FORCA.setPossiveisReacoes(Arrays.asList(DESARME, FORCA));

		
		//Posicionamento AI --> Interceptacao  
		POSICIONAMENTO.setPossiveisReacoes(Arrays.asList(INTERCEPTACAO));//POSICIONAMENTO_DEFENSIVO

		
		//Dominio(?) AI --> Interceptacao 
		DOMINIO.setPossiveisReacoes(Arrays.asList(INTERCEPTACAO));

		HABILIDADE_ACAO.put(Habilidade.PASSE, PASSE);
		HABILIDADE_ACAO.put(Habilidade.FINALIZACAO, FINALIZACAO);
		HABILIDADE_ACAO.put(Habilidade.CRUZAMENTO, CRUZAMENTO);
		HABILIDADE_ACAO.put(Habilidade.ARMACAO, ARMACAO);
		HABILIDADE_ACAO.put(Habilidade.MARCACAO, MARCACAO);
		HABILIDADE_ACAO.put(Habilidade.DESARME, DESARME);
		HABILIDADE_ACAO.put(Habilidade.INTERCEPTACAO, INTERCEPTACAO);
		HABILIDADE_ACAO.put(Habilidade.VELOCIDADE, VELOCIDADE);
		HABILIDADE_ACAO.put(Habilidade.DRIBLE, DRIBLE);
		HABILIDADE_ACAO.put(Habilidade.FORCA, FORCA);
		HABILIDADE_ACAO.put(Habilidade.CABECEIO, CABECEIO);
		HABILIDADE_ACAO.put(Habilidade.POSICIONAMENTO, POSICIONAMENTO);
		HABILIDADE_ACAO.put(Habilidade.DOMINIO, DOMINIO);
		HABILIDADE_ACAO.put(Habilidade.REFLEXO, REFLEXO);
		HABILIDADE_ACAO.put(Habilidade.JOGO_AEREO, JOGO_AEREO);
		HABILIDADE_ACAO.put(Habilidade.NULL, NULL);
	}

	private Habilidade habilidade;

	private List<HabilidadeAcao> possiveisReacoes;
	
	private List<HabilidadeAcao> acoesSubsequentes;
	
	private HabilidadeAcao reacaoGoleiro;

	public HabilidadeAcao(Habilidade habilidade) {
		super();
		this.habilidade = habilidade;
	}

	public String getDescricao() {
		return habilidade.getDescricao();
	}

	public List<HabilidadeAcao> getPossiveisReacoes() {
		return possiveisReacoes;
	}

	public void setPossiveisReacoes(List<HabilidadeAcao> possiveisReacoes) {
		this.possiveisReacoes = possiveisReacoes;
	}

	public List<HabilidadeAcao> getAcoesSubsequentes() {
		return acoesSubsequentes;
	}

	public void setAcoesSubsequentes(List<HabilidadeAcao> acoesSubsequentes) {
		this.acoesSubsequentes = acoesSubsequentes;
	}

	public HabilidadeAcao getReacaoGoleiro() {
		return reacaoGoleiro;
	}

	public void setReacaoGoleiro(HabilidadeAcao reacaoGoleiro) {
		this.reacaoGoleiro = reacaoGoleiro;
	}

	public boolean contemAcoesSubsequentes() {
		return acoesSubsequentes != null;
	}

	public boolean isExigeGoleiro() {
		return reacaoGoleiro != null;
	}

	public boolean isAcaoFim() {
		return habilidade.isAcaoFim();
	}

	public boolean isAcaoMeio() {
		return habilidade.isAcaoMeio();
	}

	public boolean isAcaoInicial() {
		return habilidade.isAcaoInicial();
	}

	public boolean isReacao() {
		return habilidade.isReacao();
	}

	public boolean isReacaoGoleiro() {
		return habilidade.isReacaoGoleiro();
	}

	public boolean isAcaoInicioFim() {
		return habilidade.isAcaoInicioFim();
	}

	public boolean isAcaoInicioMeio() {
		return habilidade.isAcaoInicioMeio();
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	@Override
	public String toString() {
		return "HabAcao [" + getDescricao() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(habilidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HabilidadeAcao other = (HabilidadeAcao) obj;
		return habilidade == other.habilidade;
	}

}
