package com.fastfoot.player.model;

public enum Habilidade {
	
	NULL("NULL", null),

	PASSE("Passe", TipoHabilidade.ACAO_FIM),//1
	FINALIZACAO("Finalizacao", TipoHabilidade.ACAO_FIM),//2
	CRUZAMENTO("Cruzamento", TipoHabilidade.ACAO_FIM),//3
	ARMACAO("Armacao", TipoHabilidade.ACAO_FIM),//4

	MARCACAO("Marcacao", TipoHabilidade.REACAO),//5
	DESARME("Desarme", TipoHabilidade.REACAO),//6
	INTERCEPTACAO("Interceptacao", TipoHabilidade.REACAO),//7

	VELOCIDADE("Velocidade", TipoHabilidade.ACAO_MEIO),//8
	DIBLE("Dible", TipoHabilidade.ACAO_MEIO),//9
	FORCA("Forca", TipoHabilidade.ACAO_MEIO),//10

	CABECEIO("Cabeceio", TipoHabilidade.ACAO_INICIO_FIM),//11
	POSICIONAMENTO("Posicionamento", TipoHabilidade.ACAO_INICIO_MEIO),//12
	DOMINIO("Dominio", TipoHabilidade.ACAO_INICIAL),//13

	REFLEXO("Reflexo", TipoHabilidade.REACAO_GOLEIRO),//14
	JOGO_AEREO("Jogo Aereo", TipoHabilidade.REACAO_GOLEIRO),//15
	
	ERRO("ERRO", null),
	FORA("FORA", null);

	private String descricao;
	
	private TipoHabilidade tipoHabilidade;
	
	private Habilidade(String descricao, TipoHabilidade tipoHabilidade) {
		this.descricao = descricao;
		this.tipoHabilidade = tipoHabilidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoHabilidade getTipoHabilidade() {
		return tipoHabilidade;
	}

	public void setTipoHabilidade(TipoHabilidade tipoHabilidade) {
		this.tipoHabilidade = tipoHabilidade;
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
}
