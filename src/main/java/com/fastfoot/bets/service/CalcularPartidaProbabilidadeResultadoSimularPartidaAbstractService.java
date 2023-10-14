package com.fastfoot.bets.service;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.factory.EsquemaFactoryImpl;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

public abstract class CalcularPartidaProbabilidadeResultadoSimularPartidaAbstractService implements CalcularPartidaProbabilidadeResultadoService {
	//@see JogarPartidaService, SimularConfrontoJogadorService
	
	private static final Integer NUM_SIMULACOES = 100;
	
	protected static final Double NUM_LANCES_POR_MINUTO = 1d;
	
	private static final Integer MINUTOS = 90;
	
	//private static final float MIN_FORA = 0.2f;
	
	protected static final Integer[] PESO_FINALIZACAO = new Integer[] {2, 3};
	
	//protected abstract HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor);
	
	public abstract PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante);
	
	protected PartidaProbabilidadeResultado calcularPartidaProbabilidadeResultado(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante,
			TipoProbabilidadeResultadoPartida tipoProbabilidadeResultadoPartida, Boolean agrupado) {

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(
				escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		double vitoriaMandante = 0, vitoriaVisitante = 0, empate = 0;
		
		PartidaProbabilidadeResultado partidaProbabilidadeResultado = new PartidaProbabilidadeResultado();
		partidaProbabilidadeResultado
				.setTipoProbabilidadeResultadoPartida(tipoProbabilidadeResultadoPartida);
		
		partidaResultado.setPartidaProbabilidadeResultado(partidaProbabilidadeResultado);
		/*if (partidaResultado instanceof PartidaResultado) {
			partidaProbabilidadeResultado.setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			partidaProbabilidadeResultado.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}*/
		
		for (int i = 0; i < NUM_SIMULACOES; i++) {
			//partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
			jogar(esquema, partidaResultado, agrupado);
			
			if (partidaResultado.getGolsMandante() > partidaResultado.getGolsVisitante()) {
				vitoriaMandante++;
			} else if (partidaResultado.getGolsMandante() < partidaResultado.getGolsVisitante()) {
				vitoriaVisitante++;
			} else if (partidaResultado.getGolsMandante() == partidaResultado.getGolsVisitante()) {
				empate++;
			}

			partidaResultado.setGolsMandante(0);
			partidaResultado.setGolsVisitante(0);
		}
		
		partidaProbabilidadeResultado.setProbabilidadeVitoriaMandante(vitoriaMandante/NUM_SIMULACOES);
		
		partidaProbabilidadeResultado.setProbabilidadeVitoriaVisitante(vitoriaVisitante/NUM_SIMULACOES);
		
		partidaProbabilidadeResultado.setProbabilidadeEmpate(empate/NUM_SIMULACOES);
		
		return partidaProbabilidadeResultado;

	}

	protected void jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado, Boolean agrupado) {

		//List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValorJogavel habilidadeVencedorAnterior = null;
		HabilidadeValorJogavel habilidadeValorAcao = null;
		HabilidadeValorJogavel habilidadeValorReacao = null;
		//HabilidadeValorJogavel habilidadeFora = null;
		//HabilidadeValorJogavel habilidadeVencedora = null;
		
		//Jogador jogadorAssistencia = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		for (int minuto = 1; minuto <= MINUTOS; minuto++) {

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

				//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeJogavel(), jogadorAcaoVenceu, ordemJogada, true, minuto);
				//atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
				//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidadeJogavel(), !jogadorAcaoVenceu, ordemJogada, false, minuto);
				//atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				//partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				//
				//if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				//
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcaoJogavel().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValorJogavel) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesValorJogavelAcaoFimJogadorPosicaoAtualPosse(agrupado));
					//if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidadeJogavel().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcaoJogavel().isExigeGoleiro()){
						//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeJogavel(), true, ordemJogada, true, minuto);
						//atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, true);
					}
					ordemJogada++;
					//partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeAcaoJogavel().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValorJogavel) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidadesValorJogavel(agrupado,
											Arrays.asList(habilidadeValorAcao.getHabilidadeAcaoJogavel().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					/*habilidadeFora = new HabilidadeValor(Habilidade.FORA, (int) Math.round(Math.max(
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
					
					//
					/*if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidadeJogavel(), jogadorAcaoVenceu, ordemJogada, true, minuto);*/
					//atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
					
					/*if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidadeJogavel(), goleiroVenceu, ordemJogada, false, minuto);*/
					//atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, goleiroVenceu);
					ordemJogada++;
					
					//partidaResultado.incrementarLance(esquema.getPosseBolaMandante());

					//
					//if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
					//
					
					if (jogadorAcaoVenceu) {
						//if (IMPRIMIR) System.err.println("\t\tGOLLL");
						if (esquema.getPosseBolaMandante()) {
							golMandante++;
						} else {
							golVisitante++;
						}
						partidaResultado.incrementarGol(esquema.getPosseBolaMandante());
						/*
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana().incrementarGolsMarcados();
						if (jogadorAssistencia != null) {
							jogadorAssistencia.getJogadorEstatisticasSemana().incrementarAssistencias();
						}
						esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticasSemana()
								.incrementarGolsSofridos();
						*/
					} else if (goleiroVenceu) {
						//if (IMPRIMIR) System.err.println("\t\tGOLEIRO DEFENDEU");
						//partidaResultado.incrementarFinalizacaoDefendida(esquema.getPosseBolaMandante());
						/*
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana()
								.incrementarFinalizacoesDefendidas();
						esquema.getGoleiroSemPosse().getGoleiro().getJogadorEstatisticasSemana()
								.incrementarGoleiroFinalizacoesDefendidas();
						*/
					//} else if (habilidadeVencedora.equals(habilidadeFora)) {
						//if (IMPRIMIR) System.err.println("\t\tFORA!!!!");
						//partidaResultado.incrementarFinalizacaoFora(esquema.getPosseBolaMandante());
						/*
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana().incrementarFinalizacoesFora();
						*/
					}
					esquema.inverterPosse();//TODO: iniciar posse em qual jogador???
					//jogadorAssistencia = null;
				} else {
					//PASSE, CRUZAMENTO, ARMACAO
					//jogadorAssistencia = esquema.getJogadorPosse();
					EsquemaTransicao t = (EsquemaTransicao) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getTransicoesPosse());
					//if (IMPRIMIR) System.err.println(String.format("%d ==> %d (%s)", esquema.getPosicaoAtual().getNumero(), t.getPosFinal().getNumero(), esquema.getPosseBolaMandante() ? "M" : "V"));
					esquema.setPosicaoAtual(t.getPosFinal());
				}
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			} else {
				esquema.inverterPosse();
				//if (IMPRIMIR) System.err.println("\t\tPOSSE INVERTIDA");
				//habilidadeVencedorAnterior = habilidadeValorReacao;
				habilidadeVencedorAnterior = null;
				//jogadorAssistencia = null;
			}
			
			}
		}

		//if (IMPRIMIR) System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		
		partidaResultado.setPartidaJogada(true);
	}

}
