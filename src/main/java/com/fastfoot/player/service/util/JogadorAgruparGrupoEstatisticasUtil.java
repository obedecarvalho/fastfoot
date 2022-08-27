package com.fastfoot.player.service.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;

@Deprecated
public class JogadorAgruparGrupoEstatisticasUtil {

	public static Map<Jogador, Map<Habilidade, List<PartidaLance>>> agruparEstatisticas(List<PartidaLance> lances) {

		Map<Jogador, Map<Habilidade, List<PartidaLance>>> jogadorEstatisticas = lances.stream().collect(Collectors
				.groupingBy(PartidaLance::getJogador, Collectors.groupingBy(PartidaLance::getHabilidadeUsada)));
		
		//Para calcular quantidade de uso: jogadorEstatisticas.get(Jogador).get(Habilidade).size()
		//Para calcular quantidade de uso vencedor: jogadorEstatisticas.get(Jogador).get(Habilidade)stream().filter(e -> e.getVencedor()).count()
		
		return jogadorEstatisticas;

	}
}
