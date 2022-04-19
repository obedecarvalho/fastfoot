package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HabilidadeAcao {
	
	public static final HabilidadeAcao PASSE = new HabilidadeAcao(1, Habilidade.PASSE, TipoHabilidade.ACAO_FIM);
	public static final HabilidadeAcao FINALIZACAO = new HabilidadeAcao(2, Habilidade.FINALIZACAO, TipoHabilidade.ACAO_FIM);
	public static final HabilidadeAcao CRUZAMENTO = new HabilidadeAcao(3, Habilidade.CRUZAMENTO, TipoHabilidade.ACAO_FIM);
	public static final HabilidadeAcao ARMACAO = new HabilidadeAcao(4, Habilidade.ARMACAO, TipoHabilidade.ACAO_FIM);

	public static final HabilidadeAcao MARCACAO = new HabilidadeAcao(5, Habilidade.MARCACAO, TipoHabilidade.REACAO);
	public static final HabilidadeAcao DESARME = new HabilidadeAcao(6, Habilidade.DESARME, TipoHabilidade.REACAO);
	public static final HabilidadeAcao INTERCEPTACAO = new HabilidadeAcao(7, Habilidade.INTERCEPTACAO, TipoHabilidade.REACAO);

	public static final HabilidadeAcao VELOCIDADE = new HabilidadeAcao(8, Habilidade.VELOCIDADE, TipoHabilidade.ACAO_MEIO);
	public static final HabilidadeAcao DIBLE = new HabilidadeAcao(9, Habilidade.DIBLE, TipoHabilidade.ACAO_MEIO);
	public static final HabilidadeAcao FORCA = new HabilidadeAcao(10, Habilidade.FORCA, TipoHabilidade.ACAO_MEIO);
	
	public static final HabilidadeAcao CABECEIO = new HabilidadeAcao(11, Habilidade.CABECEIO, TipoHabilidade.ACAO_INICIO_FIM);
	public static final HabilidadeAcao POSICIONAMENTO = new HabilidadeAcao(12, Habilidade.POSICIONAMENTO, TipoHabilidade.ACAO_INICIO_MEIO);
	public static final HabilidadeAcao DOMINIO = new HabilidadeAcao(13, Habilidade.DOMINIO, TipoHabilidade.ACAO_INICIAL);
	
	public static final HabilidadeAcao REFLEXO = new HabilidadeAcao(14, Habilidade.REFLEXO, TipoHabilidade.REACAO_GOLEIRO);
	public static final HabilidadeAcao JOGO_AEREO = new HabilidadeAcao(15, Habilidade.JOGO_AEREO, TipoHabilidade.REACAO_GOLEIRO);

	public static final HabilidadeAcao NULL = new HabilidadeAcao(16, Habilidade.NULL, null);
	
	public static final Map<Habilidade, HabilidadeAcao> HAB_HABACAO = new HashMap<Habilidade, HabilidadeAcao>();
	

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

		HAB_HABACAO.put(Habilidade.PASSE, PASSE);
		HAB_HABACAO.put(Habilidade.FINALIZACAO, FINALIZACAO);
		HAB_HABACAO.put(Habilidade.CRUZAMENTO, CRUZAMENTO);
		HAB_HABACAO.put(Habilidade.ARMACAO, ARMACAO);
		HAB_HABACAO.put(Habilidade.MARCACAO, MARCACAO);
		HAB_HABACAO.put(Habilidade.DESARME, DESARME);
		HAB_HABACAO.put(Habilidade.INTERCEPTACAO, INTERCEPTACAO);
		HAB_HABACAO.put(Habilidade.VELOCIDADE, VELOCIDADE);
		HAB_HABACAO.put(Habilidade.DIBLE, DIBLE);
		HAB_HABACAO.put(Habilidade.FORCA, FORCA);
		HAB_HABACAO.put(Habilidade.CABECEIO, CABECEIO);
		HAB_HABACAO.put(Habilidade.POSICIONAMENTO, POSICIONAMENTO);
		HAB_HABACAO.put(Habilidade.DOMINIO, DOMINIO);
		HAB_HABACAO.put(Habilidade.REFLEXO, REFLEXO);
		HAB_HABACAO.put(Habilidade.JOGO_AEREO, JOGO_AEREO);
		HAB_HABACAO.put(Habilidade.NULL, NULL);
	}
	
	//private Integer id;
	
	private Habilidade habilidade;

	//private String descricao;
	
	private List<HabilidadeAcao> possiveisReacoes;
	
	private List<HabilidadeAcao> acoesSubsequentes;
	
	private HabilidadeAcao reacaoGoleiro;
	
	private TipoHabilidade tipoHabilidade;

	public HabilidadeAcao(Integer id, Habilidade habilidade, TipoHabilidade tipoHabilidade) {
		super();
		//this.descricao = descricao;
		this.habilidade = habilidade;
		this.tipoHabilidade = tipoHabilidade;
		//this.id = id;
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

	public TipoHabilidade getTipoHabilidade() {
		return tipoHabilidade;
	}

	public void setTipoHabilidade(TipoHabilidade tipoHabilidade) {
		this.tipoHabilidade = tipoHabilidade;
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

	/*public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}*/

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	@Override
	public String toString() {
		return "Hab [" + getDescricao() + "]";
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

	/*@Override
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
		HabilidadeAcao other = (HabilidadeAcao) obj;
		return Objects.equals(id, other.id);
	}*/

}
