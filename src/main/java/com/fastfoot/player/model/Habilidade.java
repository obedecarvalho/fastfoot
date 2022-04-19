package com.fastfoot.player.model;

public enum Habilidade {
	
	NULL("NULL", null),

	PASSE("Passe", TipoHabilidade.ACAO_FIM),
	FINALIZACAO("Finalizacao", TipoHabilidade.ACAO_FIM),
	CRUZAMENTO("Cruzamento", TipoHabilidade.ACAO_FIM),
	ARMACAO("Armacao", TipoHabilidade.ACAO_FIM),

	MARCACAO("Marcacao", TipoHabilidade.REACAO),
	DESARME("Desarme", TipoHabilidade.REACAO),
	INTERCEPTACAO("Interceptacao", TipoHabilidade.REACAO),

	VELOCIDADE("Velocidade", TipoHabilidade.ACAO_MEIO),
	DIBLE("Dible", TipoHabilidade.ACAO_MEIO),
	FORCA("Forca", TipoHabilidade.ACAO_MEIO),

	CABECEIO("Cabeceio", TipoHabilidade.ACAO_INICIO_FIM),
	POSICIONAMENTO("Posicionamento", TipoHabilidade.ACAO_INICIO_MEIO),
	DOMINIO("Dominio", TipoHabilidade.ACAO_INICIAL),

	REFLEXO("Reflexo", TipoHabilidade.REACAO_GOLEIRO),
	JOGO_AEREO("Jogo Aereo", TipoHabilidade.REACAO_GOLEIRO);

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
