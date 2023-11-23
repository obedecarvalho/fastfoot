package com.fastfoot.player.service.util;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;

public class JogadorCalcularForcaUtil {

	public static void calcularForcaGeral(Jogador jogador) {
		jogador.setForcaGeral((new Double(jogador.getHabilidadesValor().stream().filter(h -> h.isHabilidadeEspecifica())
				.mapToDouble(h -> h.getValorTotal()).average().getAsDouble())).intValue());
	}

	public static void calcular(Jogador jogador) {
		jogador.setForcaGeral(JogadorFactory
				.calcularForcaGeral(jogador, JogadorFactory.getEstrategiaPosicaoJogador(jogador.getPosicao()))
				.intValue());
	}
}
