package com.fastfoot.player.service.util;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;

public class JogadorCalcularForcaUtil {

	public static void calcularForcaGeral(Jogador jogador) {
		jogador.setForcaGeral((new Double(jogador.getHabilidades().stream().filter(h -> h.isHabilidadeEspecifica())
				.mapToInt(h -> h.getValor()).average().getAsDouble())).intValue());//TODO: usar valorTotal
	}

	public static void calcular(Jogador jogador) {
		jogador.setForcaGeral(JogadorFactory
				.calcularForcaGeral(jogador, JogadorFactory.getEstrategiaPosicaoJogador(jogador.getPosicao()))
				.intValue());
	}
}
