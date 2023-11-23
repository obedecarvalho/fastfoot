package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.StatusEscalacaoJogador;
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
		
		/*
		//TODO: validar para que jogador que acabou de ser entrar na substituição não seja substituido
		 * TODO: remover jogador que entrou da reserva
		 */
		
		if (EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS.equals(estrategiaSubstituicao)) {
			substituirMaisCansados(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes(), false);
		} else if (EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS_SO_SE_RESERVA_ESTIVER_MELHOR.equals(estrategiaSubstituicao)) {
			substituirMaisCansados(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes(), true);
		} else if (EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES.equals(estrategiaSubstituicao)) {
			entrarSubstituitoMaisForte(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes(), false);
		} else if (EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES_SO_SE_RESERVA_ESTIVER_MELHOR.equals(estrategiaSubstituicao)) {
			entrarSubstituitoMaisForte(esquema, partidaResultado, mandante, minutoSubstituicao,
					sortearNumeroSubstituicoes(), false);
		} else if (EstrategiaSubstituicao.ENTRAR_JOGADORES_A_DESENVOLVER.equals(estrategiaSubstituicao)) {
			//TODO: implementar logica
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
			int minutoSubstituicao, int numeroSubstituicoes, boolean soSeEstiverMelhor) {
		
		List<Jogador> jogadoresReservas;

		if (mandante) {
			jogadoresReservas = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao())
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto()
							&& !e.getJogador().getPosicao().isGoleiro())
					.map(EscalacaoJogadorPosicao::getJogador).sorted(JogadorFactory.getComparatorForcaGeralEnergia())
					.collect(Collectors.toList());
		} else {
			jogadoresReservas = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao())
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto()
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
		Map<Jogador, EscalacaoJogadorPosicao> jogReserva;
		if (mandante) {
			jogReserva = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		} else {
			jogReserva = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		}
		
		Map<Jogador, EscalacaoJogadorPosicao> jogTitular;
		if (mandante) {
			jogTitular = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituido() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		} else {
			jogTitular = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituido() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		}
		
		//System.err.println("CHECK3: " + jogPosicao.keySet().equals(jogTitular.keySet()));
		//System.err.println("CHECK2: " + (new HashSet<Jogador>(jogadoresReservas)).equals(jogReserva.keySet()));
		//
		
		//
		List<Jogador> jogs = new ArrayList<Jogador>(jogPosicao.keySet());
		List<Jogador> jogadoresTitularesPosicao;
		Jogador jogadorSubstituir;
		//
		
		numeroSubstituicoes = Math.min(numeroSubstituicoes, jogadoresReservas.size());
		double forcaPonderadaSubstituido, forcaPonderadaSubstituto;
		
		//System.err.println("\t***INICIO:" + (mandante ? partidaResultado.getClubeMandante() : partidaResultado.getClubeVisitante()));
		//print(esquema, mandante);

		int i = 0, pos = 0;
		while (i < numeroSubstituicoes && pos < jogadoresReservas.size()) {
			
			Jogador jogadorSubstituto = jogadoresReservas.get(pos);
			
			jogadoresTitularesPosicao = jogs.stream().filter(j -> j.getPosicao().equals(jogadorSubstituto.getPosicao()))
					//.sorted(JogadorFactory.getComparatorForcaGeralEnergia())
					.sorted(Collections.reverseOrder(JogadorFactory.getComparatorForcaGeralEnergia()))
					.collect(Collectors.toList());

			if (!ValidatorUtil.isEmpty(jogadoresTitularesPosicao)) {
				
				jogadorSubstituir = jogadoresTitularesPosicao.get(0);
				
				//
				if (soSeEstiverMelhor) {
					forcaPonderadaSubstituido = Math.pow(jogadorSubstituir.getForcaGeral(), Constantes.ROLETA_N_POWER)
							* (jogadorSubstituir.getJogadorEnergia().getEnergiaAtual() / 100.0);
					
					forcaPonderadaSubstituto = Math.pow(jogadorSubstituto.getForcaGeral(), Constantes.ROLETA_N_POWER)
							* (jogadorSubstituto.getJogadorEnergia().getEnergiaAtual() / 100.0);
					
					if (forcaPonderadaSubstituto < forcaPonderadaSubstituido) {
						pos++;
						continue;
					}
				}
				//
				
				if (!partidaResultado.isAmistoso()) {
					jogadorSubstituto.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(jogadorSubstituto, partidaResultado.getRodada().getSemana(),
									jogadorSubstituto.getClube(), partidaResultado, false));
				} else {
					jogadorSubstituto.setJogadorEstatisticasSemana(new JogadorEstatisticasSemana(jogadorSubstituto,
							partidaResultado.getRodada().getSemana(), jogadorSubstituto.getClube(), null, true));
				}
				jogadorSubstituto.getJogadorEstatisticasSemana().setJogos(1);
				
				
				jogadorSubstituir.getJogadorEstatisticasSemana().setMinutoFinal(minutoSubstituicao);
				jogadorSubstituto.getJogadorEstatisticasSemana().setMinutoInicial(minutoSubstituicao);
				
				jogadorSubstituir.getJogadorEstatisticasSemana()
						.setMinutosJogados(jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoFinal()
								- jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoInicial());
				
				//
				jogTitular.get(jogadorSubstituir).setStatusEscalacaoJogador(
						jogTitular.get(jogadorSubstituir).getStatusEscalacaoJogador().getStatusSubstituido());
				jogReserva.get(jogadorSubstituto).setStatusEscalacaoJogador(StatusEscalacaoJogador.RESERVA_SUBSTITUTO);
				//
				
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

			/*if (pos >= jogPosicao.size())
				i = Constantes.NUMERO_MAX_SUBSTITUICOES;*/
		}
		
		//System.err.println("\t***FIM:" + (mandante ? partidaResultado.getClubeMandante() : partidaResultado.getClubeVisitante()));
		//print(esquema, mandante);
	}
	
	private void substituirMaisCansados(Esquema esquema, PartidaResultadoJogavel partidaResultado, boolean mandante,
			int minutoSubstituicao, int numeroSubstituicoes, boolean soSeEstiverMelhor) {

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
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		} else {
			jogReserva = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoReservas().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituto())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		}
		
		//
		Map<Jogador, EscalacaoJogadorPosicao> jogTitular;
		if (mandante) {
			jogTitular = esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituido() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		} else {
			jogTitular = esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao().stream()
					//.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
					.filter(e -> e.getStatusEscalacaoJogador().isPossivelSubstituido() && !e.getJogador().getPosicao().isGoleiro())
					.collect(Collectors.toMap(EscalacaoJogadorPosicao::getJogador, Function.identity()));
		}
		
		//System.err.println("CHECK: " + new HashSet<Jogador>(jogs).equals(jogTitular.keySet()));
		//

		List<Jogador> jogadoresReservas;
		Jogador jogadorSubstituto;
		
		List<Jogador> jogadoresReservasDisponiveis = new ArrayList<Jogador>(jogReserva.keySet());
		numeroSubstituicoes = Math.min(numeroSubstituicoes, jogadoresReservasDisponiveis.size());
		double forcaPonderadaSubstituido, forcaPonderadaSubstituto;
		
		//System.err.println("\t***INICIO:" + (mandante ? partidaResultado.getClubeMandante() : partidaResultado.getClubeVisitante()));
		//print(esquema, mandante);

		int i = 0, pos = 0;
		while (i < numeroSubstituicoes && pos < jogPosicao.size()) {

			Jogador jogadorSubstituir = jogs.get(pos);

			jogadoresReservas = jogadoresReservasDisponiveis.stream()
					.filter(j -> j.getPosicao().equals(jogadorSubstituir.getPosicao()))
					.sorted(JogadorFactory.getComparatorForcaGeralEnergia()).collect(Collectors.toList());

			if (!ValidatorUtil.isEmpty(jogadoresReservas)) {
				
				jogadorSubstituto = jogadoresReservas.get(0);
				
				//
				if (soSeEstiverMelhor) {
					forcaPonderadaSubstituido = Math.pow(jogadorSubstituir.getForcaGeral(), Constantes.ROLETA_N_POWER)
							* (jogadorSubstituir.getJogadorEnergia().getEnergiaAtual() / 100.0);
					
					forcaPonderadaSubstituto = Math.pow(jogadorSubstituto.getForcaGeral(), Constantes.ROLETA_N_POWER)
							* (jogadorSubstituto.getJogadorEnergia().getEnergiaAtual() / 100.0);
					
					if (forcaPonderadaSubstituto < forcaPonderadaSubstituido) {
						pos++;
						continue;
					}
				}
				//
				
				if (!partidaResultado.isAmistoso()) {
					jogadorSubstituto.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(jogadorSubstituto, partidaResultado.getRodada().getSemana(),
									jogadorSubstituto.getClube(), partidaResultado, false));
				} else {
					jogadorSubstituto.setJogadorEstatisticasSemana(new JogadorEstatisticasSemana(jogadorSubstituto,
							partidaResultado.getRodada().getSemana(), jogadorSubstituto.getClube(), null, true));
				}
				jogadorSubstituto.getJogadorEstatisticasSemana().setJogos(1);
				
				
				jogadorSubstituir.getJogadorEstatisticasSemana().setMinutoFinal(minutoSubstituicao);
				jogadorSubstituto.getJogadorEstatisticasSemana().setMinutoInicial(minutoSubstituicao);
				
				jogadorSubstituir.getJogadorEstatisticasSemana()
						.setMinutosJogados(jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoFinal()
								- jogadorSubstituir.getJogadorEstatisticasSemana().getMinutoInicial());
				
				//
				jogTitular.get(jogadorSubstituir).setStatusEscalacaoJogador(
						jogTitular.get(jogadorSubstituir).getStatusEscalacaoJogador().getStatusSubstituido());
				jogReserva.get(jogadorSubstituto).setStatusEscalacaoJogador(StatusEscalacaoJogador.RESERVA_SUBSTITUTO);
				//
				
				if (mandante) {
					jogPosicao.get(jogadorSubstituir).setMandante(jogadorSubstituto);
				} else {
					jogPosicao.get(jogadorSubstituir).setVisitante(jogadorSubstituto);
				}
				jogadoresReservasDisponiveis.remove(jogadorSubstituto);
				jogReserva.remove(jogadorSubstituto);
				pos++;
				i++;
			} else {
				pos++;
			}

			/*if (pos >= jogPosicao.size())
				i = Constantes.NUMERO_MAX_SUBSTITUICOES;*/
		}
		
		//System.err.println("\t***FIM:" + (mandante ? partidaResultado.getClubeMandante() : partidaResultado.getClubeVisitante()));
		//print(esquema, mandante);
	}
	
	public void print(Esquema esquema, boolean mandante) {
		
		if (mandante) {
			for (EsquemaPosicao ep : esquema.getPosicoes()) {
				System.err.println(String.format("%d:\t%s", ep.getNumero(),
						ep.getMandante() != null ? ep.getMandante().toString() : ""));
			}
			
			for (EscalacaoJogadorPosicao ejp : esquema.getEscalacaoClubeMandante().getListEscalacaoJogadorPosicao()) {
				System.err.println(ejp);
			}
			
		} else {
			for (EsquemaPosicao ep : esquema.getPosicoes()) {
				System.err.println(String.format("%d:\t%s", 12 - ep.getNumero(),
						ep.getVisitante() != null ? ep.getVisitante().toString() : ""));
			}
			
			for (EscalacaoJogadorPosicao ejp : esquema.getEscalacaoClubeVisitante().getListEscalacaoJogadorPosicao()) {
				System.err.println(ejp);
			}
		}
	}
}
