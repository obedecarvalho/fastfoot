package com.fastfoot.bets.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.bets.model.TipoProbabilidadeResultadoPartida;
import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.bets.model.repository.PartidaProbabilidadeResultadoRepository;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.factory.EsquemaFactoryImpl;
import com.fastfoot.match.service.CarregarEscalacaoJogadoresPartidaService;
import com.fastfoot.match.service.EscalarClubeService;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.CarregarJogadorEnergiaService;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class CalcularPartidaProbabilidadeResultadoSimularPartidaService {
	
	private static final Integer NUM_SIMULACOES = 100;
	
	private static final Double NUM_LANCES_POR_MINUTO = 1d;
	
	private static final Integer MINUTOS = 90;

	private static final float MIN_FORA = 0.2f;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private PartidaResultadoRepository partidaResultadoRepository;
	
	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaResultadoRepository;
	
	@Autowired
	private PartidaProbabilidadeResultadoRepository partidaProbabilidadeResultadoRepository;
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	@Autowired
	private CarregarJogadorEnergiaService carregarJogadorEnergiaService;

	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;
	
	/*@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;*/
	
	/*@Autowired
	private ParametroService parametroService;*/

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> simularPartidas(RodadaJogavel rodada) {

		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();

		List<PartidaProbabilidadeResultado> probabilidades = new ArrayList<PartidaProbabilidadeResultado>();
		
		List<? extends PartidaResultadoJogavel> partidas = null;
		
		if (rodada instanceof Rodada) {
			partidas = partidaResultadoRepository.findByRodada((Rodada) rodada);
		} else if (rodada instanceof RodadaEliminatoria) {
			partidas = partidaEliminatoriaResultadoRepository.findByRodada((RodadaEliminatoria) rodada);
		}

		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		carregarEscalacaoJogadoresPartidaService.carregarEscalacao(partidas);

		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#carregarEscalacao:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		for (PartidaResultadoJogavel p : partidas) {
			probabilidades.add(simularPartida(p, p.getEscalacaoMandante(), p.getEscalacaoVisitante()));
			//probabilidades.add(simularPartida(p));
		}

		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#simularPartida:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		partidaProbabilidadeResultadoRepository.saveAll(probabilidades);

		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#partidaProbabilidadeResultadoRepository:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		//Finalizar
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total

		//System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);

	}

	public PartidaProbabilidadeResultado simularPartida(PartidaResultadoJogavel partidaResultado) {
		
		List<Jogador> jogadoresMandante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidades(partidaResultado.getClubeMandante(), StatusJogador.ATIVO);
		List<Jogador> jogadoresVisitante = jogadorRepository
				.findByClubeAndStatusJogadorFetchHabilidades(partidaResultado.getClubeVisitante(), StatusJogador.ATIVO);
		
		carregarJogadorEnergiaService.carregarJogadorEnergia(partidaResultado.getClubeMandante(), jogadoresMandante);
		carregarJogadorEnergiaService.carregarJogadorEnergia(partidaResultado.getClubeVisitante(), jogadoresVisitante);
		
		EscalacaoClube escalacaoMandante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeMandante(), jogadoresMandante, partidaResultado);
		EscalacaoClube escalacaoVisitante = escalarClubeService
				.gerarEscalacaoInicial(partidaResultado.getClubeVisitante(), jogadoresVisitante, partidaResultado);
		
		return simularPartida(partidaResultado, escalacaoMandante, escalacaoVisitante);
	}
	
	private PartidaProbabilidadeResultado simularPartida(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante) {

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(
				escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		double vitoriaMandante = 0, vitoriaVisitante = 0, empate = 0;
		
		PartidaProbabilidadeResultado partidaProbabilidadeResultado = new PartidaProbabilidadeResultado();
		partidaProbabilidadeResultado
				.setTipoProbabilidadeResultadoPartida(TipoProbabilidadeResultadoPartida.SIMULAR_PARTIDA);
		
		if (partidaResultado instanceof PartidaResultado) {
			partidaProbabilidadeResultado.setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			partidaProbabilidadeResultado.setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}
		
		for (int i = 0; i < NUM_SIMULACOES; i++) {
			//partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
			jogar(esquema, partidaResultado);
			
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
	
	private void jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		//List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValor habilidadeVencedorAnterior = null;
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		
		//Jogador jogadorAssistencia = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		for (int i = 0; i < (NUM_LANCES_POR_MINUTO * MINUTOS); i++) {
			
			do {
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidadeAcao().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidades(
									habilidadeVencedorAnterior.getHabilidadeAcao().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse());
				}
				
				habilidadeValorReacao = (HabilidadeValor) RoletaUtil
						.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesJogadorPosicaoAtualSemPosse(
								habilidadeValorAcao.getHabilidadeAcao().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);

				//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);
				//atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
				//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidade(), !jogadorAcaoVenceu, ordemJogada, false);
				//atualizarEstatisticasHabilidade(habilidadeValorReacao, !jogadorAcaoVenceu);
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
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoFimJogadorPosicaoAtualPosse());
					//if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidade().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()){
						//if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), true, ordemJogada, true);
						//atualizarEstatisticasHabilidade(habilidadeValorAcao, true);
					}
					ordemJogada++;
					//partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidades(
											Arrays.asList(habilidadeValorAcao.getHabilidadeAcao().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					habilidadeFora = new HabilidadeValor(Habilidade.FORA, (int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * esquema.getProbabilidadeArremateForaPosicaoPosse()) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));
					habilidadeFora.calcularValorN();
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					habilidadeVencedora = (HabilidadeValor) RoletaUtil.sortearN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					//
					/*if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);*/
					//atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
					
					/*if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidade(), goleiroVenceu, ordemJogada, false);*/
					//atualizarEstatisticasHabilidade(habilidadeValorReacao, goleiroVenceu);
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
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
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

		//if (IMPRIMIR) System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		
		partidaResultado.setPartidaJogada(true);
	}
	
	/*private EsquemaFactory getEsquemaFactory() {

		EsquemaFactory factory = null;
		String formacao = parametroService.getParametroString(ParametroConstantes.ESCALACAO_PADRAO);

		if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_41212.equals(formacao)) {
			factory = new EsquemaFactoryImplQuatroUmDoisUmDois();
		} else if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_4222.equals(formacao)) {
			factory = new EsquemaFactoryImplQuatroDoisDoisDois();
		} else if (ParametroConstantes.ESCALACAO_PADRAO_PARAM_4132.equals(formacao)) {
			factory = new EsquemaFactoryImplDoisTresTresDois();
		}

		return factory;
	}*/

	/*private void carregarJogadorEnergia(Clube clube, List<Jogador> jogadores) {
		List<Map<String, Object>> jogEnergia = jogadorEnergiaRepository.findEnergiaJogadorByIdClube(clube.getId());

		Map<Jogador, Map<String, Object>> x = jogEnergia.stream()
				.collect(Collectors.toMap(ej -> new Jogador(((BigInteger) ej.get("id_jogador")).longValue()), Function.identity()));

		JogadorEnergia je = null;
		Map<String, Object> jes = null;
		Integer energia = null;

		for (Jogador j : jogadores) {
			je = new JogadorEnergia();
			je.setJogador(j);
			je.setEnergia(0);//Variacao de energia

			jes = x.get(j);
			if (!ValidatorUtil.isEmpty(jes)) {
				energia = ((BigInteger) jes.get("energia")).intValue();
				if (energia > Constantes.ENERGIA_INICIAL) {
					je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
				} else {
					je.setEnergiaInicial(energia);
				}
			} else {
				je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
			}

			j.setJogadorEnergia(je);
		}

	}*/
}
