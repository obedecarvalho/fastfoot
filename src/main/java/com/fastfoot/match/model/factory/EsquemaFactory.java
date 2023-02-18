package com.fastfoot.match.model.factory;

import java.util.List;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;

public abstract class EsquemaFactory {
	
	protected static final Integer PESO_LATERAL = 75;
	
	protected static final Integer PESO_AVANCAR = 100;
	
	protected static final Integer PESO_AVANCAR_DUPLO = 66;//TODO: Implementar logica aumentar dificuldade
	
	protected static final Integer PESO_RECUAR = 50;
	
	protected static final Integer PESO_RECUAR_DUPLO = 33;//TODO: Implementar logica aumentar dificuldade

	protected static final Double PROBABILIDADE_ARREMATE_FORA_ZAG = 1.50d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_LAT = 1.50d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_VOL = 1.50d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_MEI = 1.10d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_ATA = 1.10d;
	
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_LINHA_2 = 1.50d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_LINHA_3 = 1.50d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_LINHA_4 = 1.10d;
	
	protected static final Double PROBABILIDADE_ARREMATE_FORA_LINHA_5 = 1.10d;
	
	public abstract Esquema gerarEsquemaEscalacao(List<EscalacaoJogadorPosicao> mandantes,
			List<EscalacaoJogadorPosicao> visitantes, JogadorApoioCriacao jogadorApoioCriacaoMandante,
			JogadorApoioCriacao jogadorApoioCriacaoVisitante);

	protected static void addTransicaoMandante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoMandante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoMandante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
	
	protected static void addTransicaoVisitante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoVisitante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoVisitante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
}
