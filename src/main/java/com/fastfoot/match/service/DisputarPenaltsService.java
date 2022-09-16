package com.fastfoot.match.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Service
public class DisputarPenaltsService {
	
	private static final Comparator<Jogador> COMPARATOR;
	
	static {
		COMPARATOR = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				
				return o2.getHabilidadeValorByHabilidade(Arrays.asList(Habilidade.FINALIZACAO)).get(0).getValorTotal()
						.compareTo(o1.getHabilidadeValorByHabilidade(Arrays.asList(Habilidade.FINALIZACAO)).get(0)
								.getValorTotal());//Reverso
			}
		};
	}

	public void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		//TODO: considerar substituiçoes

		List<Jogador> jogadoresMandantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getMandante)
				.collect(Collectors.toList());
		List<Jogador> jogadoresVisitantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getVisitante)
				.collect(Collectors.toList());
		
		jogadoresMandantes.add(esquema.getGoleiroMandante().getGoleiro());
		jogadoresVisitantes.add(esquema.getGoleiroVisitante().getGoleiro());
		
		Collections.sort(jogadoresMandantes, COMPARATOR);
		Collections.sort(jogadoresVisitantes, COMPARATOR);
		
		//TODO
		
	}
}
