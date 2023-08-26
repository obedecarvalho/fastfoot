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
public class DisputarPenaltsService {//TODO: Penalty e Penalties

	private static final Integer NUMERO_PENALTS_DISPUTAR = 5;
	
	public void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsHabilidadeValorJogavel(partidaResultado, esquema,
				JogadorFactory.getComparatorHabilidadeFinalizacao(), Habilidade.FINALIZACAO, Habilidade.REFLEXO, false);
	}

	public void disputarPenaltsHabilidadeGrupo(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsHabilidadeValorJogavel(partidaResultado, esquema,
				JogadorFactory.getComparatorHabilidadeGrupoConclusao(), HabilidadeGrupo.CONCLUSAO,
				HabilidadeGrupo.GOLEIRO, true);
	}

	protected void disputarPenaltsHabilidadeValorJogavel(PartidaResultadoJogavel partidaResultado, Esquema esquema,
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

		int penaltsCertosMandantes = 0, penaltsCertosVisitante = 0;
		
		//int i = NUMERO_PENALTS_DISPUTAR;
		int i = 0;
		while ((i < NUMERO_PENALTS_DISPUTAR) 
				|| (i >= NUMERO_PENALTS_DISPUTAR && penaltsCertosMandantes == penaltsCertosVisitante)) {
			//Mandante
			if (disputarPenaltyHabilidadeValorJogavel(jogadoresMandantes.get(i), goleiroVisitante, 
					jogadoresMandantes.get(i).getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeBatedor), 
					goleiroVisitante.getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeGoleiro))) {
				penaltsCertosMandantes++;
			}
			//Visitante
			if (disputarPenaltyHabilidadeValorJogavel(jogadoresVisitantes.get(i), goleiroMandante, 
					jogadoresVisitantes.get(i).getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeBatedor), 
					goleiroMandante.getHabilidadeValorJogavelByHabilidade(agrupado, habilidadeGoleiro))) {
				penaltsCertosVisitante++;
			}
			
			i = (i + 1) % 11;
		}

		((PartidaEliminatoriaResultado) partidaResultado).setGolsMandantePenalts(penaltsCertosMandantes);
		((PartidaEliminatoriaResultado) partidaResultado).setGolsVisitantePenalts(penaltsCertosVisitante);

	}
	
	protected Boolean disputarPenaltyHabilidadeValorJogavel(Jogador batedor, Jogador goleiro,
			HabilidadeValorJogavel hvJogLinha, HabilidadeValorJogavel hvGoleiro) {//TODO: criar penalt para fora
		
		batedor.getJogadorEstatisticasSemana().incrementarNumeroRodadaDisputaPenalt();
		goleiro.getJogadorEstatisticasSemana().incrementarNumeroRodadaDisputaPenalt();

		boolean acertou = false;
		if (RoletaUtil.isPrimeiroVencedorN(hvJogLinha, hvGoleiro)) {
			acertou = true;//penaltsCertosMandantes++;
			
			batedor.getJogadorEstatisticasSemana().incrementarGolsDisputaPenalt();
			goleiro.getJogadorEstatisticasSemana().incrementarGolsSofridosDisputaPenalt();
		} else {
			batedor.getJogadorEstatisticasSemana().incrementarGolsPerdidosDisputaPenalt();
			goleiro.getJogadorEstatisticasSemana().incrementarDefesasDisputaPenalt();
		}

		return acertou;
	}
}
