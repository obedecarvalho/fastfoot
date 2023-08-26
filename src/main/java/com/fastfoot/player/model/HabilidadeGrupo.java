package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.List;

public enum HabilidadeGrupo implements HabilidadeJogavel {

	DEFESA(new Habilidade[] {Habilidade.MARCACAO, Habilidade.DESARME, Habilidade.INTERCEPTACAO/*, Habilidade.CABECEIO, Habilidade.FORCA*/}, TipoHabilidadeAcao.REACAO),

	CONCLUSAO(new Habilidade[] {Habilidade.FINALIZACAO, Habilidade.CABECEIO}, TipoHabilidadeAcao.ACAO_FIM),//ARREMATE

	CRIACAO(new Habilidade[] {Habilidade.ARMACAO, Habilidade.CRUZAMENTO, Habilidade.PASSE}, TipoHabilidadeAcao.ACAO_FIM),

	POSSE_BOLA(new Habilidade[] {Habilidade.DOMINIO, /*Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.PASSE*/ Habilidade.POSICIONAMENTO}, TipoHabilidadeAcao.ACAO_INICIAL),

	QUEBRA_LINHA(new Habilidade[] {Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.FORCA, Habilidade.POSICIONAMENTO}, TipoHabilidadeAcao.ACAO_MEIO),

	GOLEIRO(new Habilidade[] {Habilidade.REFLEXO, Habilidade.JOGO_AEREO}, TipoHabilidadeAcao.REACAO_GOLEIRO),

	FORA(null, null),
	;
	
	private Habilidade[] habilidades;

	private TipoHabilidadeAcao tipoHabilidade;
	
	private HabilidadeGrupo(Habilidade[] habilidades, TipoHabilidadeAcao tipoHabilidade) {
		this.tipoHabilidade = tipoHabilidade;
		this.habilidades = habilidades;
	}

	public Habilidade[] getHabilidades() {
		return habilidades;
	}
	
	public int[] getHabilidadesOrdinal() {
		return Arrays.stream(habilidades).mapToInt(h -> h.ordinal()).toArray();
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

	@Override
	public String getDescricao() {
		return this.name();
	}

	public static List<HabilidadeGrupo> getAll() {
		return Arrays.asList(
				new HabilidadeGrupo[] { HabilidadeGrupo.CONCLUSAO, HabilidadeGrupo.CRIACAO, HabilidadeGrupo.DEFESA,
						HabilidadeGrupo.GOLEIRO, HabilidadeGrupo.POSSE_BOLA, HabilidadeGrupo.QUEBRA_LINHA });
	}

}
