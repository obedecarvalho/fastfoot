package com.fastfoot.player.model;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaHabilidadeVolante implements EstrategiaHabilidadePosicaoJogador {

	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS;
	
	protected static final List<Habilidade> HABILIDADES_ESPECIFICAS_ELETIVAS;
	
	protected static final List<Habilidade> HABILIDADES_COMUM;
	
	protected static final List<Habilidade> HABILIDADES_COMUNS_ELETIVAS;
	
	protected static final List<Habilidade> HABILIDADES_OUTROS;

	protected static final Integer NUM_HAB_ESP_ELETIVAS = 2;
	
	protected static final Integer NUM_HAB_COMUNS_ELETIVAS = 3;
	
	static {
		HABILIDADES_ESPECIFICAS = new ArrayList<Habilidade>();
		HABILIDADES_ESPECIFICAS_ELETIVAS = new ArrayList<Habilidade>();
		HABILIDADES_COMUM = new ArrayList<Habilidade>();
		HABILIDADES_COMUNS_ELETIVAS = new ArrayList<Habilidade>();
		HABILIDADES_OUTROS = new ArrayList<Habilidade>();
		
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.PASSE);
		
		HABILIDADES_COMUNS_ELETIVAS.add(Habilidade.FINALIZACAO);
		HABILIDADES_OUTROS.add(Habilidade.CRUZAMENTO);
		HABILIDADES_COMUNS_ELETIVAS.add(Habilidade.ARMACAO);
		
		HABILIDADES_COMUNS_ELETIVAS.add(Habilidade.CABECEIO);
		
		HABILIDADES_ESPECIFICAS.add(Habilidade.MARCACAO);
		HABILIDADES_ESPECIFICAS.add(Habilidade.DESARME);
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.INTERCEPTACAO);
		
		HABILIDADES_ESPECIFICAS_ELETIVAS.add(Habilidade.VELOCIDADE);
		HABILIDADES_OUTROS.add(Habilidade.DRIBLE);
		HABILIDADES_COMUNS_ELETIVAS.add(Habilidade.FORCA);
		
		HABILIDADES_COMUNS_ELETIVAS.add(Habilidade.POSICIONAMENTO);
		HABILIDADES_COMUM.add(Habilidade.DOMINIO);
		
		HABILIDADES_OUTROS.add(Habilidade.REFLEXO);
		HABILIDADES_OUTROS.add(Habilidade.JOGO_AEREO);
	}

	@Override
	public List<Habilidade> getHabilidadesEspecificas() {
		return HABILIDADES_ESPECIFICAS;
	}

	@Override
	public List<Habilidade> getHabilidadesEspecificasEletivas() {
		return HABILIDADES_ESPECIFICAS_ELETIVAS;
	}

	@Override
	public List<Habilidade> getHabilidadesComum() {
		return HABILIDADES_COMUM;
	}
	
	@Override
	public List<Habilidade> getHabilidadesComunsEletivas() {
		return HABILIDADES_COMUNS_ELETIVAS;
	}

	@Override
	public List<Habilidade> getHabilidadesOutros() {
		return HABILIDADES_OUTROS;
	}

	@Override
	public Integer getNumHabEspEletivas() {
		return NUM_HAB_ESP_ELETIVAS;
	}
	
	@Override
	public Integer getNumHabComunsEletivas() {
		return NUM_HAB_COMUNS_ELETIVAS;
	}

	private static EstrategiaHabilidadeVolante INSTANCE;
	
	protected EstrategiaHabilidadeVolante() {

	}
	
	public static EstrategiaHabilidadeVolante getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EstrategiaHabilidadeVolante();
		}
		return INSTANCE;
	}

}
