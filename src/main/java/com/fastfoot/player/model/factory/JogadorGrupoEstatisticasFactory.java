package com.fastfoot.player.model.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorGrupoEstatisticas;

public class JogadorGrupoEstatisticasFactory {

	public static void agruparEstatisticas(PartidaResumo partidaEstatisticas) {

		List<JogadorGrupoEstatisticas> grupos = new ArrayList<JogadorGrupoEstatisticas>();

		Map<Jogador, Map<Habilidade, List<PartidaLance>>> jogadorEstatisticas = partidaEstatisticas.getPartidaLances()
				.stream().collect(Collectors.groupingBy(PartidaLance::getJogador, Collectors.groupingBy(PartidaLance::getHabilidadeUsada)));

		for (Jogador jogador : jogadorEstatisticas.keySet()) {
			grupos.addAll(agruparEstatisticas(partidaEstatisticas, jogador, jogadorEstatisticas.get(jogador)));
		}

		partidaEstatisticas.setGrupoEstatisticas(grupos);
	}
	
	private static List<JogadorGrupoEstatisticas> agruparEstatisticas(PartidaResumo partidaEstatisticas, Jogador jogador, Map<Habilidade,List<PartidaLance>> habilidadesEstatisticas) {
		List<JogadorGrupoEstatisticas> estatisticasGrupos = new ArrayList<JogadorGrupoEstatisticas>();
		JogadorGrupoEstatisticas jogadorGrupoEstatisticas = null;
		for (Habilidade habilidade : habilidadesEstatisticas.keySet()) {
			List<PartidaLance> estatisticasHabilidade = habilidadesEstatisticas.get(habilidade);
			jogadorGrupoEstatisticas = new JogadorGrupoEstatisticas();
			jogadorGrupoEstatisticas.setPartidaResumo(partidaEstatisticas);
			jogadorGrupoEstatisticas.setJogador(jogador);
			jogadorGrupoEstatisticas.setHabilidadeUsada(habilidade);
			jogadorGrupoEstatisticas.setQuantidadeUso(estatisticasHabilidade.size());
			jogadorGrupoEstatisticas.setQuantidadeUsoVencedor((int) estatisticasHabilidade.stream().filter(e -> e.getVencedor()).count());			
			estatisticasGrupos.add(jogadorGrupoEstatisticas);
		}

		return estatisticasGrupos;
	}

}
