package com.fastfoot.player.model;

public enum Habilidade {
	
	NULL("NULL", null),

	PASSE("Passe", TipoHabilidadeAcao.ACAO_FIM),//1
	FINALIZACAO("Finalizacao", TipoHabilidadeAcao.ACAO_FIM),//2
	CRUZAMENTO("Cruzamento", TipoHabilidadeAcao.ACAO_FIM),//3
	ARMACAO("Armacao", TipoHabilidadeAcao.ACAO_FIM),//4

	MARCACAO("Marcacao", TipoHabilidadeAcao.REACAO),//5
	DESARME("Desarme", TipoHabilidadeAcao.REACAO),//6
	INTERCEPTACAO("Interceptacao", TipoHabilidadeAcao.REACAO),//7	//POSICIONAMENTO_DEFENSIVO

	VELOCIDADE("Velocidade", TipoHabilidadeAcao.ACAO_MEIO),//8	//ACAO_MEIO_REACAO
	DRIBLE("Drible", TipoHabilidadeAcao.ACAO_MEIO),//9
	FORCA("Forca", TipoHabilidadeAcao.ACAO_MEIO),//10			//ACAO_MEIO_REACAO

	CABECEIO("Cabeceio", TipoHabilidadeAcao.ACAO_INICIO_FIM),//11
	POSICIONAMENTO("Posicionamento", TipoHabilidadeAcao.ACAO_INICIO_MEIO),//12
	DOMINIO("Dominio", TipoHabilidadeAcao.ACAO_INICIAL),//13

	REFLEXO("Reflexo", TipoHabilidadeAcao.REACAO_GOLEIRO),//14
	JOGO_AEREO("Jogo Aereo", TipoHabilidadeAcao.REACAO_GOLEIRO),//15
	
	ERRO("ERRO", null),
	FORA("FORA", null);

	private String descricao;
	
	private TipoHabilidadeAcao tipoHabilidade;
	
	private Habilidade(String descricao, TipoHabilidadeAcao tipoHabilidade) {
		this.descricao = descricao;
		this.tipoHabilidade = tipoHabilidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoHabilidadeAcao getTipoHabilidade() {
		return tipoHabilidade;
	}

	public void setTipoHabilidade(TipoHabilidadeAcao tipoHabilidade) {
		this.tipoHabilidade = tipoHabilidade;
	}
	
	public boolean isAcaoFim() {
		return TipoHabilidadeAcao.ACAO_FIM.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoMeio() {
		return TipoHabilidadeAcao.ACAO_MEIO.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoInicial() {
		return TipoHabilidadeAcao.ACAO_INICIAL.equals(getTipoHabilidade());
	}
	
	public boolean isReacao() {
		return TipoHabilidadeAcao.REACAO.equals(getTipoHabilidade());
	}

	public boolean isReacaoGoleiro() {
		return TipoHabilidadeAcao.REACAO_GOLEIRO.equals(getTipoHabilidade());
	}

	public boolean isAcaoInicioFim() {
		return TipoHabilidadeAcao.ACAO_INICIO_FIM.equals(getTipoHabilidade());
	}
	
	public boolean isAcaoInicioMeio() {
		return TipoHabilidadeAcao.ACAO_INICIO_MEIO.equals(getTipoHabilidade());
	}
}
