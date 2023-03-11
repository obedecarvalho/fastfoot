package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticaSemana;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class RealizarSubstituicoesJogadorPartidaService {

	public void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {//TODO: sortear numero de substituicoes
		
		if (EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS.equals(estrategiaSubstituicao)) {
			substituirMaisCansados(esquema, partidaResultado, mandante, minutoSubstituicao);
		} else if (EstrategiaSubstituicao.SUBSTITUIR_SO_SE_ESTIVER_MELHOR.equals(estrategiaSubstituicao)) {
			
		} else if (EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES.equals(estrategiaSubstituicao)) {
			
		}
		
	}
	
	private void substituirMaisCansados(Esquema esquema, PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {

		Map<Jogador, EsquemaPosicao> jogPosicao;

		if (mandante) {
			jogPosicao = esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
					.collect(Collectors.toMap(EsquemaPosicao::getMandante, Function.identity()));
		} else {
			jogPosicao = esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
					.collect(Collectors.toMap(EsquemaPosicao::getVisitante, Function.identity()));
		}

		List<Jogador> jogs = new ArrayList<Jogador>(jogPosicao.keySet());
		Collections.sort(jogs, JogadorFactory.getComparatorEnergia());

		Map<Jogador, EscalacaoJogadorPosicao> jogReserva;
		if (mandante) {
			jogReserva = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		} else {
			jogReserva = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		}

		List<Jogador> jogadoresReservas;
		Jogador jogadorSubstituto;

		int i = 0, pos = 0;
		while (i < Constantes.NUMERO_MAX_SUBSTITUICOES) {

			Jogador jogadorSubstituir = jogs.get(pos);

			jogadoresReservas = (new ArrayList<Jogador>(jogReserva.keySet())).stream()
					.filter(j -> j.getPosicao().equals(jogadorSubstituir.getPosicao()))
					.sorted(JogadorFactory.getComparatorForcaGeralEnergia()).collect(Collectors.toList());

			if (!ValidatorUtil.isEmpty(jogadoresReservas)) {
				
				jogadorSubstituto = jogadoresReservas.get(0);
				
				if (!partidaResultado.isAmistoso()) {
					jogadorSubstituto.setJogadorEstatisticaSemana(
							new JogadorEstatisticaSemana(jogadorSubstituto, partidaResultado.getRodada().getSemana(),
									jogadorSubstituto.getClube(), partidaResultado, false));
				} else {
					jogadorSubstituto.setJogadorEstatisticaSemana(new JogadorEstatisticaSemana(jogadorSubstituto,
							partidaResultado.getRodada().getSemana(), jogadorSubstituto.getClube(), null, true));
				}
				jogadorSubstituto.getJogadorEstatisticaSemana().setNumeroJogos(1);
				
				
				jogadorSubstituir.getJogadorEstatisticaSemana().setMinutoFinal(minutoSubstituicao);
				jogadorSubstituto.getJogadorEstatisticaSemana().setMinutoInicial(minutoSubstituicao);
				
				jogadorSubstituir.getJogadorEstatisticaSemana()
						.setNumeroMinutosJogados(jogadorSubstituir.getJogadorEstatisticaSemana().getMinutoFinal()
								- jogadorSubstituir.getJogadorEstatisticaSemana().getMinutoInicial());
				
				if (mandante) {
					jogPosicao.get(jogadorSubstituir).setMandante(jogadorSubstituto);
				} else {
					jogPosicao.get(jogadorSubstituir).setVisitante(jogadorSubstituto);
				}
				jogReserva.remove(jogadoresReservas.get(0));
				pos++;
				i++;
			} else {
				pos++;
			}

			if (pos >= jogPosicao.size())
				i = Constantes.NUMERO_MAX_SUBSTITUICOES;
		}
	}
}
