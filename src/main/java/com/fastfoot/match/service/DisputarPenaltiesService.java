package com.fastfoot.match.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class DisputarPenaltiesService {

	private static final Integer NUMERO_PENALTIES_DISPUTAR = 5;
	
	private static final Integer[] PESO_FINALIZACAO_PENALTY = new Integer[] {5, 1};//70% de chance de acertar
	
	public void disputarPenalties(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltiesHabilidadeValorJogavel((PartidaEliminatoriaResultado) partidaResultado, esquema,
				JogadorFactory.getComparatorHabilidadeFinalizacao(), Habilidade.FINALIZACAO, Habilidade.REFLEXO, false);
	}

	public void disputarPenaltiesHabilidadeGrupo(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltiesHabilidadeValorJogavel((PartidaEliminatoriaResultado) partidaResultado, esquema,
				JogadorFactory.getComparatorHabilidadeGrupoConclusao(), HabilidadeGrupo.CONCLUSAO,
				HabilidadeGrupo.GOLEIRO, true);
	}

	protected void disputarPenaltiesHabilidadeValorJogavel(PartidaEliminatoriaResultado partidaResultado, Esquema esquema,
			Comparator<Jogador> ordemBatedores, HabilidadeJogavel habilidadeBatedor,
			HabilidadeJogavel habilidadeGoleiro, Boolean agrupado) {

		List<Jogador> jogadoresMandantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getMandante).filter(j -> j != null)
				.collect(Collectors.toList());
		List<Jogador> jogadoresVisitantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getVisitante).filter(j -> j != null)
				.collect(Collectors.toList());
		
		jogadoresMandantes.add(esquema.getGoleiroMandante().getGoleiro());
		jogadoresVisitantes.add(esquema.getGoleiroVisitante().getGoleiro());
		
		Jogador goleiroMandante = esquema.getGoleiroMandante().getGoleiro();
		Jogador goleiroVisitante = esquema.getGoleiroVisitante().getGoleiro();
		
		Collections.sort(jogadoresMandantes, ordemBatedores);
		Collections.sort(jogadoresVisitantes, ordemBatedores);
		
		HabilidadeValorJogavel habilidadeGoleiroMandante = goleiroMandante.getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeGoleiro);
		HabilidadeValorJogavel habilidadeGoleiroVisitante = goleiroVisitante.getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeGoleiro);

		int penaltiesCertosMandantes = 0, penaltiesCertosVisitante = 0,
				penaltiesDisputadosMandante = 0, penaltiesDisputadosVisitante = 0,
				penaltiesDefendidosMandante = 0, penaltiesDefendidosVisitante = 0;
		
		//int i = NUMERO_PENALTS_DISPUTAR;
		int i = 0, rodadasDisputadas = 0;
		while ((rodadasDisputadas < NUMERO_PENALTIES_DISPUTAR
					&& !fimAntecipadoDisputaPenalties(penaltiesCertosMandantes, penaltiesCertosVisitante, penaltiesDisputadosMandante, penaltiesDisputadosVisitante))
				|| (rodadasDisputadas >= NUMERO_PENALTIES_DISPUTAR && penaltiesCertosMandantes == penaltiesCertosVisitante)) {
			//Mandante
			if (disputarPenaltyHabilidadeValorJogavel(jogadoresMandantes.get(i), goleiroVisitante, 
					jogadoresMandantes.get(i).getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeBatedor), 
					habilidadeGoleiroVisitante)) {
				penaltiesCertosMandantes++;
			} else {
				penaltiesDefendidosVisitante++;
			}
			penaltiesDisputadosMandante++;

			if (rodadasDisputadas < NUMERO_PENALTIES_DISPUTAR
					&& fimAntecipadoDisputaPenalties(penaltiesCertosMandantes, penaltiesCertosVisitante, penaltiesDisputadosMandante, penaltiesDisputadosVisitante)) {
				continue;
			}

			//Visitante
			if (disputarPenaltyHabilidadeValorJogavel(jogadoresVisitantes.get(i), goleiroMandante, 
					jogadoresVisitantes.get(i).getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeBatedor), 
					habilidadeGoleiroMandante)) {
				penaltiesCertosVisitante++;
			} else {
				penaltiesDefendidosMandante++;
			}
			penaltiesDisputadosVisitante++;


			i = (i + 1) % 11;
			rodadasDisputadas++;
		}

		partidaResultado.getPartidaDisputaPenalties().setGolsMandantePenalties(penaltiesCertosMandantes);
		partidaResultado.getPartidaDisputaPenalties().setGolsVisitantePenalties(penaltiesCertosVisitante);
		partidaResultado.getPartidaDisputaPenalties().setNumeroPenaltiesBatidosMandante(penaltiesDisputadosMandante);
		partidaResultado.getPartidaDisputaPenalties().setNumeroPenaltiesBatidosVisitante(penaltiesDisputadosVisitante);
		partidaResultado.getPartidaDisputaPenalties().setPenaltiesDefendidosMandante(penaltiesDefendidosMandante);
		partidaResultado.getPartidaDisputaPenalties().setPenaltiesDefendidosVisitante(penaltiesDefendidosVisitante);
		//partidaResultado.getPartidaDisputaPenalties().setPenaltiesForaMandante(null);
		//partidaResultado.getPartidaDisputaPenalties().setPenaltiesForaVisitante(null);

	}
	
	protected Boolean disputarPenaltyHabilidadeValorJogavel(Jogador batedor, Jogador goleiro,
			HabilidadeValorJogavel hvJogLinha, HabilidadeValorJogavel hvGoleiro) {//TODO: criar penalt para fora
		
		batedor.getJogadorEstatisticasSemana().incrementarNumeroRodadaDisputaPenalties();
		goleiro.getJogadorEstatisticasSemana().incrementarNumeroRodadaDisputaPenalties();

		boolean acertou = false;
		if (RoletaUtil.isPrimeiroVencedorNPonderado(hvJogLinha, hvGoleiro, PESO_FINALIZACAO_PENALTY)) {
			acertou = true;//penaltsCertosMandantes++;
			
			batedor.getJogadorEstatisticasSemana().incrementarGolsDisputaPenalties();
			goleiro.getJogadorEstatisticasSemana().incrementarGolsSofridosDisputaPenalties();
		} else {
			batedor.getJogadorEstatisticasSemana().incrementarGolsPerdidosDisputaPenalties();
			goleiro.getJogadorEstatisticasSemana().incrementarDefesasDisputaPenalties();
		}

		return acertou;
	}

	private boolean fimAntecipadoDisputaPenalties(int penaltiesCertosMandantes, int penaltiesCertosVisitante,
			int penaltiesDisputadosMandantes, int penaltiesDisputadosVisitante) {
		if (penaltiesCertosMandantes > penaltiesCertosVisitante) {
			return (penaltiesCertosMandantes - penaltiesCertosVisitante) > (NUMERO_PENALTIES_DISPUTAR - penaltiesDisputadosVisitante);
		}
		if (penaltiesCertosVisitante > penaltiesCertosMandantes) {
			return (penaltiesCertosVisitante - penaltiesCertosMandantes) > (NUMERO_PENALTIES_DISPUTAR - penaltiesDisputadosMandantes);
		}
		return false;
	}
}
