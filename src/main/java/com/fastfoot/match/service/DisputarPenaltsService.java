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
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class DisputarPenaltsService {//TODO: Penalty e Penalties
	
	private static final Comparator<Jogador> COMPARATOR_FINALIZACAO;

	private static final Comparator<Jogador> COMPARATOR_CONCLUSAO;
	
	private static final Integer NUMERO_PENALTS_DISPUTAR = 5;
	
	static {
		COMPARATOR_FINALIZACAO = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o2.getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO).getValorTotal()
						.compareTo(o1.getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO).getValorTotal());// Reverso
			}
		};

		COMPARATOR_CONCLUSAO = new Comparator<Jogador>() {
			
			@Override
			public int compare(Jogador o1, Jogador o2) {
				return o2.getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo.CONCLUSAO).getValorTotal()
						.compareTo(o1.getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo.CONCLUSAO).getValorTotal());// Reverso
			}
		};
	}

	public void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {

		List<Jogador> jogadoresMandantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getMandante).filter(j -> j != null)
				.collect(Collectors.toList());
		List<Jogador> jogadoresVisitantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getVisitante).filter(j -> j != null)
				.collect(Collectors.toList());
		
		jogadoresMandantes.add(esquema.getGoleiroMandante().getGoleiro());
		jogadoresVisitantes.add(esquema.getGoleiroVisitante().getGoleiro());
		
		Jogador goleiroMandante = esquema.getGoleiroMandante().getGoleiro();
		Jogador goleiroVisitante = esquema.getGoleiroVisitante().getGoleiro();
		
		Collections.sort(jogadoresMandantes, COMPARATOR_FINALIZACAO);
		Collections.sort(jogadoresVisitantes, COMPARATOR_FINALIZACAO);

		int penaltsCertosMandantes = 0, penaltsCertosVisitante = 0;
		for (int i = 0; i < NUMERO_PENALTS_DISPUTAR; i++) {
			//Mandante
			if (disputarPenalty(jogadoresMandantes.get(i), goleiroVisitante)) {
				penaltsCertosMandantes++;
			}
			//Visitante
			if (disputarPenalty(jogadoresVisitantes.get(i), goleiroMandante)) {
				penaltsCertosVisitante++;
			}
		}
		
		int i = NUMERO_PENALTS_DISPUTAR;
		while (penaltsCertosMandantes == penaltsCertosVisitante) {
			//Mandante
			if (disputarPenalty(jogadoresMandantes.get(i), goleiroVisitante)) {
				penaltsCertosMandantes++;
			}
			//Visitante
			if (disputarPenalty(jogadoresVisitantes.get(i), goleiroMandante)) {
				penaltsCertosVisitante++;
			}
			
			i = (i + 1) % 11;
		}

		((PartidaEliminatoriaResultado) partidaResultado).setGolsMandantePenalts(penaltsCertosMandantes);
		((PartidaEliminatoriaResultado) partidaResultado).setGolsVisitantePenalts(penaltsCertosVisitante);

	}

	private Boolean disputarPenalty(Jogador batedor, Jogador goleiro) {//TODO: criar penalt para fora
		HabilidadeValor hvJogLinha = batedor.getHabilidadeValorByHabilidade(Habilidade.FINALIZACAO);
		HabilidadeValor hvGoleiro = goleiro.getHabilidadeValorByHabilidade(Habilidade.REFLEXO);

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

	public void disputarPenaltsHabilidadeGrupo(PartidaResultadoJogavel partidaResultado, Esquema esquema) {

		List<Jogador> jogadoresMandantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getMandante).filter(j -> j != null)
				.collect(Collectors.toList());
		List<Jogador> jogadoresVisitantes = esquema.getPosicoes().stream().map(EsquemaPosicao::getVisitante).filter(j -> j != null)
				.collect(Collectors.toList());
		
		jogadoresMandantes.add(esquema.getGoleiroMandante().getGoleiro());
		jogadoresVisitantes.add(esquema.getGoleiroVisitante().getGoleiro());
		
		Jogador goleiroMandante = esquema.getGoleiroMandante().getGoleiro();
		Jogador goleiroVisitante = esquema.getGoleiroVisitante().getGoleiro();
		
		Collections.sort(jogadoresMandantes, COMPARATOR_CONCLUSAO);
		Collections.sort(jogadoresVisitantes, COMPARATOR_CONCLUSAO);

		int penaltsCertosMandantes = 0, penaltsCertosVisitante = 0;
		for (int i = 0; i < NUMERO_PENALTS_DISPUTAR; i++) {
			//Mandante
			if (disputarPenaltyHabilidadeGrupo(jogadoresMandantes.get(i), goleiroVisitante)) {
				penaltsCertosMandantes++;
			}
			//Visitante
			if (disputarPenaltyHabilidadeGrupo(jogadoresVisitantes.get(i), goleiroMandante)) {
				penaltsCertosVisitante++;
			}
		}
		
		int i = NUMERO_PENALTS_DISPUTAR;
		while (penaltsCertosMandantes == penaltsCertosVisitante) {
			//Mandante
			if (disputarPenaltyHabilidadeGrupo(jogadoresMandantes.get(i), goleiroVisitante)) {
				penaltsCertosMandantes++;
			}
			//Visitante
			if (disputarPenaltyHabilidadeGrupo(jogadoresVisitantes.get(i), goleiroMandante)) {
				penaltsCertosVisitante++;
			}

			i = (i + 1) % 11;
		}

		((PartidaEliminatoriaResultado) partidaResultado).setGolsMandantePenalts(penaltsCertosMandantes);
		((PartidaEliminatoriaResultado) partidaResultado).setGolsVisitantePenalts(penaltsCertosVisitante);

	}
	
	private Boolean disputarPenaltyHabilidadeGrupo(Jogador batedor, Jogador goleiro) {//TODO: criar penalt para fora
		HabilidadeGrupoValor hvJogLinha = batedor.getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo.CONCLUSAO);
		HabilidadeGrupoValor hvGoleiro = goleiro.getHabilidadeGrupoValorByHabilidade(HabilidadeGrupo.GOLEIRO);
		
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
