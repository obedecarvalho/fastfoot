package com.fastfoot.match.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class DisputarPenaltsService {
	
	private static final Comparator<Jogador> COMPARATOR;
	
	private static final Integer NUMERO_PENALTS_DISPUTAR = 5;
	
	static {
		COMPARATOR = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o2.getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO).getValorTotal()
						.compareTo(o1.getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO).getValorTotal());// Reverso
			}
		};
	}

	public void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		//TODO: criar penalt para fora

		List<Jogador> jogadoresMandantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getMandante).filter(j -> j != null)
				.collect(Collectors.toList());
		List<Jogador> jogadoresVisitantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getVisitante).filter(j -> j != null)
				.collect(Collectors.toList());
		
		jogadoresMandantes.add(esquema.getGoleiroMandante().getGoleiro());
		jogadoresVisitantes.add(esquema.getGoleiroVisitante().getGoleiro());
		
		Jogador goleiroMandante = esquema.getGoleiroMandante().getGoleiro();
		Jogador goleiroVisitante = esquema.getGoleiroVisitante().getGoleiro();
		
		Collections.sort(jogadoresMandantes, COMPARATOR);
		Collections.sort(jogadoresVisitantes, COMPARATOR);

		HabilidadeValor hvJogLinha = null, hvGoleiro = null;
		int penaltsCertosMandantes = 0, penaltsCertosVisitante = 0;
		for (int i = 0; i < NUMERO_PENALTS_DISPUTAR; i++) {
			
			//Mandante
			hvJogLinha = jogadoresMandantes.get(i).getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO);
			hvGoleiro = goleiroVisitante.getHabilidadeValorByHabilidade(Habilidade.REFLEXO);
			
			jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();
			goleiroVisitante.getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();

			if (RoletaUtil.isPrimeiroVencedorN(hvJogLinha, hvGoleiro)) {
				penaltsCertosMandantes++;
				
				jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarGolsDisputaPenalt();
				goleiroVisitante.getJogadorEstatisticaSemana().incrementarGolsSofridosDisputaPenalt();
			} else {
				jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarGolsPerdidosDisputaPenalt();
				goleiroVisitante.getJogadorEstatisticaSemana().incrementarDefesasDisputaPenalt();
			}

			//Visitante
			hvJogLinha = jogadoresVisitantes.get(i).getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO);
			hvGoleiro = goleiroMandante.getHabilidadeValorByHabilidade(Habilidade.REFLEXO);
			
			jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();
			goleiroMandante.getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();

			if (RoletaUtil.isPrimeiroVencedorN(hvJogLinha, hvGoleiro)) {
				penaltsCertosVisitante++;
				
				jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarGolsDisputaPenalt();
				goleiroMandante.getJogadorEstatisticaSemana().incrementarGolsSofridosDisputaPenalt();
			} else {
				jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarGolsPerdidosDisputaPenalt();
				goleiroMandante.getJogadorEstatisticaSemana().incrementarDefesasDisputaPenalt();
			}
		}
		
		int i = NUMERO_PENALTS_DISPUTAR;
		while (penaltsCertosMandantes == penaltsCertosVisitante) {
			//Mandante
			hvJogLinha = jogadoresMandantes.get(i).getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO);
			hvGoleiro = goleiroVisitante.getHabilidadeValorByHabilidade(Habilidade.REFLEXO);
			
			jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();
			goleiroVisitante.getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();

			if (RoletaUtil.isPrimeiroVencedorN(hvJogLinha, hvGoleiro)) {
				penaltsCertosMandantes++;
				
				jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarGolsDisputaPenalt();
				goleiroVisitante.getJogadorEstatisticaSemana().incrementarGolsSofridosDisputaPenalt();
			} else {
				jogadoresMandantes.get(i).getJogadorEstatisticaSemana().incrementarGolsPerdidosDisputaPenalt();
				goleiroVisitante.getJogadorEstatisticaSemana().incrementarDefesasDisputaPenalt();
			}

			//Visitante
			hvJogLinha = jogadoresVisitantes.get(i).getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO);
			hvGoleiro = goleiroMandante.getHabilidadeValorByHabilidade(Habilidade.REFLEXO);
			
			jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();
			goleiroMandante.getJogadorEstatisticaSemana().incrementarNumeroRodadaDisputaPenalt();

			if (RoletaUtil.isPrimeiroVencedorN(hvJogLinha, hvGoleiro)) {
				penaltsCertosVisitante++;
				
				jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarGolsDisputaPenalt();
				goleiroMandante.getJogadorEstatisticaSemana().incrementarGolsSofridosDisputaPenalt();
			} else {
				jogadoresVisitantes.get(i).getJogadorEstatisticaSemana().incrementarGolsPerdidosDisputaPenalt();
				goleiroMandante.getJogadorEstatisticaSemana().incrementarDefesasDisputaPenalt();
			}
			
			i = (i + 1) % 11;
		}

		((PartidaEliminatoriaResultado) partidaResultado).setGolsMandantePenalts(penaltsCertosMandantes);
		((PartidaEliminatoriaResultado) partidaResultado).setGolsVisitantePenalts(penaltsCertosVisitante);

	}
}
