package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticas;
import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;

public class JogadorGrupoEstatisticasFactory {

	public static void agruparEstatisticas(PartidaEstatisticas partidaEstatisticas) {

		List<JogadorGrupoEstatisticas> grupos = new ArrayList<JogadorGrupoEstatisticas>();

		Map<Jogador, Map<Habilidade, List<JogadorEstatisticas>>> jogadorEstatisticas = partidaEstatisticas.getJogadorEstatisticas()
				.stream().collect(Collectors.groupingBy(JogadorEstatisticas::getJogador, Collectors.groupingBy(JogadorEstatisticas::getHabilidadeUsada)));

		for (Jogador jogador : jogadorEstatisticas.keySet()) {
			grupos.addAll(agruparEstatisticas(partidaEstatisticas, jogador, jogadorEstatisticas.get(jogador)));
		}

		partidaEstatisticas.setGrupoEstatisticas(grupos);
	}
	
	private static List<JogadorGrupoEstatisticas> agruparEstatisticas(PartidaEstatisticas partidaEstatisticas, Jogador jogador, Map<Habilidade,List<JogadorEstatisticas>> habilidadesEstatisticas) {
		List<JogadorGrupoEstatisticas> estatisticasGrupos = new ArrayList<JogadorGrupoEstatisticas>();
		JogadorGrupoEstatisticas jogadorGrupoEstatisticas = null;
		for (Habilidade habilidade : habilidadesEstatisticas.keySet()) {
			List<JogadorEstatisticas> estatisticasHabilidade = habilidadesEstatisticas.get(habilidade);
			jogadorGrupoEstatisticas = new JogadorGrupoEstatisticas();
			jogadorGrupoEstatisticas.setPartidaEstatisticas(partidaEstatisticas);
			jogadorGrupoEstatisticas.setJogador(jogador);
			jogadorGrupoEstatisticas.setHabilidadeUsada(habilidade);
			jogadorGrupoEstatisticas.setQuantidadeUso(estatisticasHabilidade.size());
			jogadorGrupoEstatisticas.setQuantidadeUsoVencedor((int) estatisticasHabilidade.stream().filter(e -> e.getVencedor()).count());			
			estatisticasGrupos.add(jogadorGrupoEstatisticas);
		}

		return estatisticasGrupos;
	}

}
