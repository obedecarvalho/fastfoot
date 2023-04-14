package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.util.RoletaUtil;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class RealizarSubstituicoesJogadorPartidaService {

	public void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {
		
		//TODO: validar para que jogador que acabou de ser entrar na substituição não seja substituido
		
		if (EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS.equals(estrategiaSubstituicao)) {
			substituirMaisCansados(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes());
		} else if (EstrategiaSubstituicao.SUBSTITUIR_SO_SE_ESTIVER_MELHOR.equals(estrategiaSubstituicao)) {
			//TODO
		} else if (EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES.equals(estrategiaSubstituicao)) {
			entrarSubstituitoMaisForte(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes());
		}
		
	}
	
	private Integer sortearNumeroSubstituicoes() {
		
		Map<Integer, Integer> numeroSubstituicoes = new HashMap<Integer, Integer>();
		
		for (int i = 1; i <= Constantes.NUMERO_MAX_SUBSTITUICOES; i++) {
			numeroSubstituicoes.put(i, i);
		}

		return RoletaUtil.sortear(numeroSubstituicoes);
	}
	
	private void entrarSubstituitoMaisForte(Esquema esquema, PartidaResultadoJogavel partidaResultado, boolean mandante,
			int minutoSubstituicao, int numeroSubstituicoes) {
		
		List<Jogador> jogadoresReservas;

		if (mandante) {
			jogadoresReservas = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao())
							&& !e.getJogador().getPosicao().isGoleiro())
					.map(EscalacaoJogadorPosicao::getJogador).sorted(JogadorFactory.getComparatorForcaGeralEnergia())
					.collect(Collectors.toList());
		} else {
			jogadoresReservas = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao())
							&& !e.getJogador().getPosicao().isGoleiro())
					.map(EscalacaoJogadorPosicao::getJogador).sorted(JogadorFactory.getComparatorForcaGeralEnergia())
					.collect(Collectors.toList());
		}

		Map<Jogador, EsquemaPosicao> jogPosicao;

		if (mandante) {
			jogPosicao = esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
					.collect(Collectors.toMap(EsquemaPosicao::getMandante, Function.identity()));
		} else {
			jogPosicao = esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
					.collect(Collectors.toMap(EsquemaPosicao::getVisitante, Function.identity()));
		}
		
		//
		List<Jogador> jogs = new ArrayList<Jogador>(jogPosicao.keySet());
		List<Jogador> jogadoresTitularesPosicao;
		Jogador jogadorSubstituir;
		//

		int i = 0, pos = 0;
		while (i < numeroSubstituicoes) {
			
			Jogador jogadorSubstituto = jogadoresReservas.get(pos);
			
			jogadoresTitularesPosicao = jogs.stream().filter(j -> j.getPosicao().equals(jogadorSubstituto.getPosicao()))
					//.sorted(JogadorFactory.getComparatorForcaGeralEnergia())
					.sorted(Collections.reverseOrder(JogadorFactory.getComparatorForcaGeralEnergia()))
					.collect(Collectors.toList());

			if (!ValidatorUtil.isEmpty(jogadoresTitularesPosicao)) {
				
				jogadorSubstituir = jogadoresTitularesPosicao.get(0);
				
				if (!partidaResultado.isAmistoso()) {
					jogadorSubstituto.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(jogadorSubstituto, partidaResultado.getRodada().getSemana(),
									jogadorSubstituto.getClube(), partidaResultado, false));
				} else {
					jogadorSubstituto.setJogadorEstatisticasSemana(new JogadorEstatisticasSemana(jogadorSubstituto,
							partidaResultado.getRodada().getSemana(), jogadorSubstituto.getClube(), null, true));
				}
				jogadorSubstituto.getJogadorEstatisticasSemana().setNumeroJogos(1);
				
				
				jogadorSubstituir.getJogadorEstatisticasSemana().setMinutoFinal(minutoSubstituicao);
				jogadorSubstituto.getJogadorEstatisticasSemana().setMinutoInicial(minutoSubstituicao);
				
				jogadorSubstituir.getJogadorEstatisticasSemana()
						.setNumeroMinutosJogados(jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoFinal()
								- jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoInicial());
				
				if (mandante) {
					jogPosicao.get(jogadorSubstituir).setMandante(jogadorSubstituto);
				} else {
					jogPosicao.get(jogadorSubstituir).setVisitante(jogadorSubstituto);
				}
				jogs.remove(jogadorSubstituir);
				pos++;
				i++;
			} else {
				pos++;
			}

			if (pos >= jogPosicao.size())
				i = Constantes.NUMERO_MAX_SUBSTITUICOES;
		}
	}
	
	private void substituirMaisCansados(Esquema esquema, PartidaResultadoJogavel partidaResultado, boolean mandante,
			int minutoSubstituicao, int numeroSubstituicoes) {

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
		while (i < numeroSubstituicoes) {

			Jogador jogadorSubstituir = jogs.get(pos);

			jogadoresReservas = (new ArrayList<Jogador>(jogReserva.keySet())).stream()
					.filter(j -> j.getPosicao().equals(jogadorSubstituir.getPosicao()))
					.sorted(JogadorFactory.getComparatorForcaGeralEnergia()).collect(Collectors.toList());

			if (!ValidatorUtil.isEmpty(jogadoresReservas)) {
				
				jogadorSubstituto = jogadoresReservas.get(0);
				
				if (!partidaResultado.isAmistoso()) {
					jogadorSubstituto.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(jogadorSubstituto, partidaResultado.getRodada().getSemana(),
									jogadorSubstituto.getClube(), partidaResultado, false));
				} else {
					jogadorSubstituto.setJogadorEstatisticasSemana(new JogadorEstatisticasSemana(jogadorSubstituto,
							partidaResultado.getRodada().getSemana(), jogadorSubstituto.getClube(), null, true));
				}
				jogadorSubstituto.getJogadorEstatisticasSemana().setNumeroJogos(1);
				
				
				jogadorSubstituir.getJogadorEstatisticasSemana().setMinutoFinal(minutoSubstituicao);
				jogadorSubstituto.getJogadorEstatisticasSemana().setMinutoInicial(minutoSubstituicao);
				
				jogadorSubstituir.getJogadorEstatisticasSemana()
						.setNumeroMinutosJogados(jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoFinal()
								- jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoInicial());
				
				if (mandante) {
					jogPosicao.get(jogadorSubstituir).setMandante(jogadorSubstituto);
				} else {
					jogPosicao.get(jogadorSubstituir).setVisitante(jogadorSubstituto);
				}
				jogReserva.remove(jogadorSubstituto);
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
