package com.fastfoot.player.model;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaHabilidadeLateral implements EstrategiaHabilidadePosicaoJogador {

	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS;
	
	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS_ELETIVAS;
	
	protected static final List<Habilidade> HABILIDADES_COMUM;
	
	protected static final List<Habilidade> HABILIDADES_OUTROS;

	protected static final Integer NUM_HAB_ESP_ELETIVAS = 1;
	
	static {
		HABILIDADES_ESPECIFICAS = new ArrayList<Habilidade>();
		HABILIDADES_ESPECIFICAS_ELETIVAS = new ArrayList<Habilidade>();
		HABILIDADES_COMUM = new ArrayList<Habilidade>();
		HABILIDADES_OUTROS = new ArrayList<Habilidade>();
		
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.PASSE);
		
		HABILIDADES_OUTROS.add(Habilidade.FINALIZACAO);
		HABILIDADES_ESPECIFICAS.add(Habilidade.CRUZAMENTO);
		HABILIDADES_OUTROS.add(Habilidade.ARMACAO);
		
		HABILIDADES_OUTROS.add(Habilidade.CABECEIO);
		
		HABILIDADES_ESPECIFICAS.add(Habilidade.MARCACAO);
		HABILIDADES_ESPECIFICAS.add(Habilidade.DESARME);
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.INTERCEPTACAO);
		
		HABILIDADES_ESPECIFICAS.add(Habilidade.VELOCIDADE);
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.DRIBLE);
		HABILIDADES_OUTROS.add(Habilidade.FORCA);
		
		HABILIDADES_OUTROS.add(Habilidade.POSICIONAMENTO);
		HABILIDADES_COMUM.add(Habilidade.DOMINIO);
		
		HABILIDADES_OUTROS.add(Habilidade.REFLEXO);
		HABILIDADES_OUTROS.add(Habilidade.JOGO_AEREO);
	}

	public List<Habilidade> getHabilidadesEspecificas() {
		return HABILIDADES_ESPECIFICAS;
	}

	public List<Habilidade> getHabilidadesEspecificasEletivas() {
		return HABILIDADES_ESPECIFICAS_ELETIVAS;
	}

	public List<Habilidade> getHabilidadesComum() {
		return HABILIDADES_COMUM;
	}

	public List<Habilidade> getHabilidadesOutros() {
		return HABILIDADES_OUTROS;
	}

	public Integer getNumHabEspEletivas() {
		return NUM_HAB_ESP_ELETIVAS;
	}
	
	private static EstrategiaHabilidadeLateral INSTANCE;
	
	protected EstrategiaHabilidadeLateral() {

	}
	
	public static EstrategiaHabilidadeLateral getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EstrategiaHabilidadeLateral();
		}
		return INSTANCE;
	}

}
