package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.PartidaDadosSalvarDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.entity.PartidaDisputaPenalties;
import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.match.model.factory.EsquemaFactoryImpl;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RandomUtil;
import com.fastfoot.service.util.RoletaUtil;

public abstract class JogarPartidaService {
	//@see CalcularPartidaProbabilidadeResultadoSimularPartidaAbstractService, SimularConfrontoJogadorService
	
	protected static final Integer MINUTOS = 90;
	
	protected static final Double NUM_LANCES_POR_MINUTO = 1d;
	
	protected static final Boolean IMPRIMIR = false;
	
	protected static final Boolean LANCE_A_LANCE = true;
	
	//private static final float MIN_FORA = 0.2f;
	
	protected static final Integer[] PESO_FINALIZACAO = new Integer[] {2, 3};
	
	//protected abstract HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor);
	
	protected abstract void disputarPenalties(PartidaResultadoJogavel partidaResultado, Esquema esquema);
	
	protected abstract void salvarEstatisticas(List<Jogador> jogadores, PartidaDadosSalvarDTO partidaDadosSalvarDTO);
	
	protected abstract void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao);

	public abstract void jogar(PartidaResultadoJogavel partidaResultado,
			PartidaDadosSalvarDTO partidaDadosSalvarDTO);

	public abstract void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaDadosSalvarDTO partidaDadosSalvarDTO);
	
	protected void calcularMinutosJogador(Esquema esquema) {
		
		if (esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal() == null) {
			esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().setMinutoFinal(90);
		}

		if (esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal() == null) {
			esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().setMinutoFinal(90);
		}

		esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().setMinutosJogados(
				esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal()
						- esquema.getGoleiroMandante().getGoleiro().getJogadorEstatisticasSemana().getMinutoInicial());

		esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().setMinutosJogados(
				esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoFinal()
						- esquema.getGoleiroVisitante().getGoleiro().getJogadorEstatisticasSemana().getMinutoInicial());

		esquema.getPosicoes().stream().filter(
				p -> p.getMandante() != null && p.getMandante().getJogadorEstatisticasSemana().getMinutoFinal() == null)
				.forEach(p -> p.getMandante().getJogadorEstatisticasSemana().setMinutoFinal(90));

		esquema.getPosicoes().stream()
				.filter(p -> p.getVisitante() != null
						&& p.getVisitante().getJogadorEstatisticasSemana().getMinutoFinal() == null)
				.forEach(p -> p.getVisitante().getJogadorEstatisticasSemana().setMinutoFinal(90));

		esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
				.forEach(p -> p.getMandante().getJogadorEstatisticasSemana()
						.setMinutosJogados(p.getMandante().getJogadorEstatisticasSemana().getMinutoFinal()
								- p.getMandante().getJogadorEstatisticasSemana().getMinutoInicial()));

		esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
				.forEach(p -> p.getVisitante().getJogadorEstatisticasSemana()
						.setMinutosJogados(p.getVisitante().getJogadorEstatisticasSemana().getMinutoFinal()
								- p.getVisitante().getJogadorEstatisticasSemana().getMinutoInicial()));

	}

	protected void salvarEstatisticasJogador(List<Jogador> jogadores,
			PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		partidaDadosSalvarDTO.adicionarJogadorEstatisticaSemana(
				jogadores.stream().filter(j -> j.getJogadorEstatisticasSemana() != null)
						.map(Jogador::getJogadorEstatisticasSemana).collect(Collectors.toList()));
	}
	
	protected void salvarJogadorEnergia(List<Jogador> jogadores,
			PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		partidaDadosSalvarDTO
				.adicionarJogadorEnergias(jogadores.stream().map(j -> j.getJogadorEnergia())
						.filter(je -> je.getEnergia() != 0).collect(Collectors.toList()));
	}

	/**
	 * Todos os jogadores relacionados para o jogo tem o consumo fixo de energia (Viagens, treinamento....).
	 * 
	 * 
	 * @param escalacao
	 */
	protected void inserirConsumoEnergiaFixo(EscalacaoClube escalacao) {
		escalacao.getListEscalacaoJogadorPosicao().forEach(
				e -> e.getJogador().getJogadorEnergia().adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_FIXA));
	}
	
	protected void atualizarEstatisticasHabilidadeValorJogavel(HabilidadeValorJogavel habilidadeGrupoValor, Boolean vencedor) {
		habilidadeGrupoValor.getHabilidadeValorJogavelEstatistica().incrementarQuantidadeUso();
		if (vencedor) {
			habilidadeGrupoValor.getHabilidadeValorJogavelEstatistica().incrementarQuantidadeUsoVencedor();
		}
	}
	
	protected PartidaLance criarPartidaLance(List<PartidaLance> lances, PartidaResultadoJogavel partida, Jogador jogador,
			HabilidadeJogavel habilidade, Boolean vencedor, Integer ordem, Boolean acao, Integer minuto) {
		PartidaLance pl = new PartidaLance();
		if (partida instanceof PartidaResultado) {
			pl.setPartidaResultado((PartidaResultado) partida);
		} else if (partida instanceof PartidaAmistosaResultado) {
			pl.setPartidaAmistosaResultado((PartidaAmistosaResultado) partida);
		} else if (partida instanceof PartidaEliminatoriaResultado) {
			pl.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partida);
		}
		pl.setJogador(jogador);
		pl.setVencedor(vencedor);
		if (habilidade instanceof Habilidade) {
			pl.setHabilidadeUsada((Habilidade) habilidade);
		} else {
			pl.setHabilidadeGrupoUsada((HabilidadeGrupo) habilidade);
		}
		pl.setOrdem(ordem);
		pl.setAcao(acao);
		pl.setMinuto(minuto);
		lances.add(pl);
		return pl;
	}
	
	protected void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaDadosSalvarDTO partidaDadosSalvarDTO, Boolean agrupado) {

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		inserirConsumoEnergiaFixo(escalacaoMandante);
		inserirConsumoEnergiaFixo(escalacaoVisitante);
		
		partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
		List<PartidaLance> lances = jogar(esquema, partidaResultado, agrupado);
		
		if (partidaResultado.isDisputarPenalties() && partidaResultado.isResultadoEmpatado()) {
			((PartidaEliminatoriaResultado) partidaResultado).setPartidaDisputaPenalties(new PartidaDisputaPenalties());
			disputarPenalties(partidaResultado, esquema);
		}
		
		inserirConsumoEnergiaFixo(escalacaoMandante);
		inserirConsumoEnergiaFixo(escalacaoVisitante);
		
		calcularMinutosJogador(esquema);
		
		salvarEstatisticas(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		salvarEstatisticas(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		salvarEstatisticasJogador(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		salvarEstatisticasJogador(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		salvarJogadorEnergia(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		salvarJogadorEnergia(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaDadosSalvarDTO);
		partidaDadosSalvarDTO.addEscalacaoClube(escalacaoMandante);
		partidaDadosSalvarDTO.addEscalacaoClube(escalacaoVisitante);
		
		partidaDadosSalvarDTO.adicionarPartidaLance(lances);
	}
	
	protected List<PartidaLance> jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado, Boolean agrupado) {

		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValorJogavel habilidadeVencedorAnterior = null;
		HabilidadeValorJogavel habilidadeValorAcao = null;
		HabilidadeValorJogavel habilidadeValorReacao = null;
		//HabilidadeValorJogavel habilidadeFora = null;
		//HabilidadeValorJogavel habilidadeVencedora = null;
		
		Jogador jogadorAssistencia = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		int minutoSubstituicaoMandante = RandomUtil.sortearIntervalo(60, 75);
		int minutoSubstituicaoVisitante = RandomUtil.sortearIntervalo(60, 75);
		
		for (int minuto = 1; minuto <= MINUTOS; minuto++) {
			
			// Diminuir energia
			if (minuto % 15 == 0) {
				esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
						.forEach(p -> p.getMandante().getJogadorEnergia()
								.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA));
				esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
						.forEach(p -> p.getVisitante().getJogadorEnergia()
								.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA));
				
				esquema.getPosicoes().stream().filter(p -> p.getMandante() != null)
						.forEach(p -> p.getMandante().getHabilidadesValorJogavel(agrupado).stream().forEach(h -> h.calcularValorN()));

				esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
						.forEach(p -> p.getMandante().getHabilidadesValorJogavel(agrupado).stream().forEach(h -> h.calcularValorN()));
				
				if (minuto % 30 == 0) {
					esquema.getGoleiroMandante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					esquema.getGoleiroVisitante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					
					esquema.getGoleiroMandante().getGoleiro().getHabilidadesValorJogavel(agrupado).forEach(h -> h.calcularValorN());
					esquema.getGoleiroVisitante().getGoleiro().getHabilidadesValorJogavel(agrupado).forEach(h -> h.calcularValorN());
				}

			}
			
			if (minuto == minutoSubstituicaoMandante) {
				EstrategiaSubstituicao estrategia = RoletaUtil.sortearPesoUm(
						new EstrategiaSubstituicao[] { EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS_SO_SE_RESERVA_ESTIVER_MELHOR,
								EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES_SO_SE_RESERVA_ESTIVER_MELHOR });
				realizarSubstituicoesJogadorPartida(esquema,
						estrategia, partidaResultado, true, minuto);
			}
			
			if (minuto == minutoSubstituicaoVisitante) {
				EstrategiaSubstituicao estrategia = RoletaUtil.sortearPesoUm(
						new EstrategiaSubstituicao[] { EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS_SO_SE_RESERVA_ESTIVER_MELHOR,
								EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES_SO_SE_RESERVA_ESTIVER_MELHOR });
				realizarSubstituicoesJogadorPartida(esquema,
						estrategia, partidaResultado, false, minuto);
			}

			for (int j = 0; j < NUM_LANCES_POR_MINUTO; j++) {
			
			do {
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidadeAcaoJogavel().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValorJogavel) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesValorJogavel(agrupado,
									habilidadeVencedorAnterior.getHabilidadeAcaoJogavel().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeValorJogavel) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesValorJogavelAcaoMeioFimJogadorPosicaoAtualPosse(agrupado));
				}
				
				habilidadeValorReacao = (HabilidadeValorJogavel) RoletaUtil
						.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesValorJogavelJogadorPosicaoAtualSemPosse(agrupado,
								habilidadeValorAcao.getHabilidadeAcaoJogavel().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);

				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeJogavel(), jogadorAcaoVenceu, ordemJogada, true, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidadeJogavel(), !jogadorAcaoVenceu, ordemJogada, false, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValorJogavel) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesValorJogavelAcaoFimJogadorPosicaoAtualPosse(agrupado));
					if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidadeJogavel().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcaoJogavel().isExigeGoleiro()){
						if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeJogavel(), true, ordemJogada, true, minuto);
						atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, true);
					}
					ordemJogada++;
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeAcaoJogavel().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValorJogavel) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidadesValorJogavel(agrupado,
											Arrays.asList(habilidadeValorAcao.getHabilidadeAcaoJogavel().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					/*habilidadeFora = new HabilidadeGrupoValor(HabilidadeGrupo.FORA, (int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * esquema.getProbabilidadeArremateForaPosicaoPosse()) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));*/
					/*habilidadeFora = criarHabilidadeValorJogavelFora((int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * esquema.getProbabilidadeArremateForaPosicaoPosse()) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));
					habilidadeFora.calcularValorN();*/
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorNPonderado(habilidadeValorAcao, habilidadeValorReacao, PESO_FINALIZACAO);
					goleiroVenceu = !jogadorAcaoVenceu;
					//habilidadeVencedora = (HabilidadeValorJogavel) RoletaUtil.sortearN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					//jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					//goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidadeJogavel(), jogadorAcaoVenceu, ordemJogada, true, minuto);
					atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidadeJogavel(), goleiroVenceu, ordemJogada, false, minuto);
					atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, goleiroVenceu);
					ordemJogada++;
					
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());

					if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
					
					if (jogadorAcaoVenceu) {
						if (IMPRIMIR) System.err.println("\t\tGOLLL");
						if (esquema.getPosseBolaMandante()) {
							golMandante++;
						} else {
							golVisitante++;
						}
						partidaResultado.incrementarGol(esquema.getPosseBolaMandante());
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana().incrementarGolsMarcados();
						if (jogadorAssistencia != null) {
							jogadorAssistencia.getJogadorEstatisticasSemana().incrementarAssistencias();
						}
						esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticasSemana()
								.incrementarGolsSofridos();
					} else if (goleiroVenceu) {
						if (IMPRIMIR) System.err.println("\t\tGOLEIRO DEFENDEU");
						partidaResultado.incrementarFinalizacaoDefendida(esquema.getPosseBolaMandante());
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana()
								.incrementarFinalizacoesDefendidas();
						esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticasSemana()
								.incrementarGoleiroFinalizacoesDefendidas();
					/*} else if (habilidadeVencedora.equals(habilidadeFora)) {
						if (IMPRIMIR) System.err.println("\t\tFORA!!!!");
						partidaResultado.incrementarFinalizacaoFora(esquema.getPosseBolaMandante());
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana().incrementarFinalizacoesFora();*/
					}
					esquema.inverterPosse();//TODO: iniciar posse em qual jogador???
					jogadorAssistencia = null;
				} else {
					//PASSE, CRUZAMENTO, ARMACAO
					jogadorAssistencia = esquema.getJogadorPosse();
					EsquemaTransicao t = (EsquemaTransicao) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getTransicoesPosse());
					if (IMPRIMIR) System.err.println(String.format("%d ==> %d (%s)", esquema.getPosicaoAtual().getNumero(), t.getPosFinal().getNumero(), esquema.getPosseBolaMandante() ? "M" : "V"));
					esquema.setPosicaoAtual(t.getPosFinal());
				}
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			} else {
				esquema.inverterPosse();
				if (IMPRIMIR) System.err.println("\t\tPOSSE INVERTIDA");
				//habilidadeVencedorAnterior = habilidadeValorReacao;
				habilidadeVencedorAnterior = null;
				jogadorAssistencia = null;
			}
			
			}
		}

		if (IMPRIMIR) System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		
		partidaResultado.setPartidaJogada(true);
		
		return lances;
	}

	//###	TESTE	###

	protected void print(Esquema esquema, HabilidadeValorJogavel habilidadeValorAcao, HabilidadeValorJogavel habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s] [%s]", habilidadeValorAcao.getHabilidadeJogavel().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidadeJogavel().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu", habilidadeValorAcao.getJogador().getNumero()));
	}
}
