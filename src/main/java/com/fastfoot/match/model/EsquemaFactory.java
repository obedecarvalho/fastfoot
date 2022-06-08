package com.fastfoot.match.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fastfoot.player.model.entity.Jogador;

public abstract class EsquemaFactory {
	
	protected static final Integer PESO_LATERAL = 75;
	
	protected static final Integer PESO_AVANCAR = 100;
	
	protected static final Integer PESO_RECUAR = 50;

	protected static void ordenarJogadores(List<Jogador> jogadores) {
		Collections.sort(jogadores, new Comparator<Jogador>() {

			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o1.getNumero().compareTo(o2.getNumero());
			}
		});
	}

	protected static void addTransicaoMandante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoMandante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoMandante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
	
	protected static void addTransicaoVisitante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoVisitante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoVisitante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
}
