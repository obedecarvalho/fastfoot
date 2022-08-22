package com.fastfoot.player.service.util;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.player.model.EstrategiaHabilidadeAtacante;
import com.fastfoot.player.model.EstrategiaHabilidadeGoleiro;
import com.fastfoot.player.model.EstrategiaHabilidadeLateral;
import com.fastfoot.player.model.EstrategiaHabilidadeMeia;
import com.fastfoot.player.model.EstrategiaHabilidadeVolante;
import com.fastfoot.player.model.EstrategiaHabilidadeZagueiro;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;

public class JogadorCalcularForcaUtil {

	// G
	//private static final List<Habilidade> HAB_ESP_GOL = Arrays.asList(Habilidade.REFLEXO, Habilidade.JOGO_AEREO);

	// Z
	/*private static final List<Habilidade> HAB_ESP_ZAG = Arrays.asList(Habilidade.CABECEIO, Habilidade.MARCACAO,
			Habilidade.DESARME, Habilidade.INTERCEPTACAO, Habilidade.FORCA);*/

	// L
	/*private static final List<Habilidade> HAB_ESP_LAT = Arrays.asList(Habilidade.PASSE, Habilidade.MARCACAO,
			Habilidade.DESARME, Habilidade.INTERCEPTACAO, Habilidade.VELOCIDADE);*/

	// V
	/*private static final List<Habilidade> HAB_ESP_VOL = Arrays.asList(Habilidade.PASSE, Habilidade.MARCACAO,
			Habilidade.DESARME, Habilidade.INTERCEPTACAO);*/

	// ML
	/*private static final List<Habilidade> HAB_ESP_MEI_LAT = Arrays.asList(Habilidade.PASSE, Habilidade.FINALIZACAO,
			Habilidade.CRUZAMENTO, Habilidade.ARMACAO, Habilidade.VELOCIDADE, Habilidade.DRIBLE, Habilidade.DOMINIO);*/

	// M
	/*private static final List<Habilidade> HAB_ESP_MEI = Arrays.asList(Habilidade.PASSE, Habilidade.FINALIZACAO,
			Habilidade.CRUZAMENTO, Habilidade.ARMACAO, Habilidade.DRIBLE, Habilidade.POSICIONAMENTO, Habilidade.DOMINIO);*/

	// A
	/*private static final List<Habilidade> HAB_ESP_ATA = Arrays.asList(Habilidade.FINALIZACAO, Habilidade.CABECEIO,
			Habilidade.DRIBLE, Habilidade.FORCA, Habilidade.POSICIONAMENTO);*/

	/*@Deprecated
	public static void calcular(Jogador jogador) {
		if (Posicao.GOLEIRO.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_GOL);
		} else if (Posicao.ZAGUEIRO.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_ZAG);
		} else if (Posicao.VOLANTE.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_VOL);
		} else if (Posicao.LATERAL.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_LAT);
		/*} else if (Posicao.MEIA_LATERAL.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_MEI_LAT);* /
		} else if (Posicao.MEIA.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_MEI);
		} else if (Posicao.ATACANTE.equals(jogador.getPosicao())) {
			calcular(jogador, HAB_ESP_ATA);
		}
	}*/
	
	/*private static void calcular(Jogador jogador, List<Habilidade> habilidadesEspecificas) {
		jogador.setForcaGeral((new Long(Math
				.round(jogador.getHabilidades().stream().filter(h -> habilidadesEspecificas.contains(h.getHabilidade()))
						.mapToInt(HabilidadeValor::getValor).average().getAsDouble())))
				.intValue());
	}*/

	public static void calcularForcaGeral(Jogador jogador) {
		jogador.setForcaGeral((new Double(jogador.getHabilidades().stream().filter(h -> h.isHabilidadeEspecifica())
				.mapToInt(h -> h.getValor()).average().getAsDouble())).intValue());
	}

	public static void calcular(Jogador jogador) {
		/*if (Posicao.GOLEIRO.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeGoleiro.getInstance()).intValue());
		} else if (Posicao.ZAGUEIRO.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeZagueiro.getInstance()).intValue());
		} else if (Posicao.VOLANTE.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeVolante.getInstance()).intValue());
		} else if (Posicao.LATERAL.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeLateral.getInstance()).intValue());
		} else if (Posicao.MEIA.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeMeia.getInstance()).intValue());
		} else if (Posicao.ATACANTE.equals(jogador.getPosicao())) {
			jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, EstrategiaHabilidadeAtacante.getInstance()).intValue());
		}*/

		jogador.setForcaGeral(JogadorFactory.calcularForcaGeral(jogador, JogadorFactory.getEstrategiaPosicaoJogador(jogador.getPosicao())).intValue());
	}
}
