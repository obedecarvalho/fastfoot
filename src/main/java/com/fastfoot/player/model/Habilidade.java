package com.fastfoot.player.model;

import java.util.Arrays;
import java.util.List;

public enum Habilidade implements HabilidadeJogavel {
	
	/**
	 	create table habilidade_descricao (
        	id int4, dsc text
		);
		
		
		INSERT INTO habilidade_descricao (id, dsc) VALUES (1, 'Passe');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (2, 'Finalizacao');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (3, 'Cruzamento');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (4, 'Armacao');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (5, 'Marcacao');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (6, 'Desarme');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (7, 'Interceptacao');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (8, 'Velocidade');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (9, 'Dible');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (10, 'Forca');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (11, 'Cabeceio');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (12, 'Posicionamento');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (13, 'Dominio');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (14, 'Reflexo');
		INSERT INTO habilidade_descricao (id, dsc) VALUES (15, 'Jogo Aereo');
	 */
	
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

	@Override
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
	
	public static List<Habilidade> getAll(){
		return Arrays.asList(new Habilidade[] { Habilidade.PASSE, Habilidade.FINALIZACAO, Habilidade.CRUZAMENTO,
				Habilidade.ARMACAO, Habilidade.MARCACAO, Habilidade.DESARME, Habilidade.INTERCEPTACAO,
				Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.FORCA, Habilidade.CABECEIO,
				Habilidade.POSICIONAMENTO, Habilidade.DOMINIO, Habilidade.REFLEXO, Habilidade.JOGO_AEREO });
	}
}
