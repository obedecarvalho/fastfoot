package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Service
public class JogarPartidaHabilidadeService extends JogarPartidaService {

	/*
	 * TODO:
	 * 
	 * Inserir FALTA (estilo HabilidadeValor(Habilidade.FORA) na roleta) e CARTOES
	 * Inserir ERRO (estilo HabilidadeValor(Habilidade.FORA) na roleta)
	 * Substituicoes
	 * 
	 */

	@Autowired
	private DisputarPenaltsService disputarPenaltsService;
	
	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;
	
	@Autowired
	private RealizarSubstituicoesJogadorPartidaService realizarSubstituicoesJogadorPartidaService;

	/*private static final Double NUM_LANCES_POR_MINUTO = 1d;

	private static final Integer MINUTOS = 90;

	private static final float MIN_FORA = 0.2f;

	private static final Boolean IMPRIMIR = false;
	
	private static final Boolean LANCE_A_LANCE = true;*/

	/*private PartidaLance criarPartidaLance(List<PartidaLance> lances, PartidaResultadoJogavel partida, Jogador jogador,
			Habilidade habilidade, Boolean vencedor, Integer ordem, Boolean acao, Integer minuto) {
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
		pl.setHabilidadeUsada(habilidade);
		pl.setOrdem(ordem);
		pl.setAcao(acao);
		pl.setMinuto(minuto);
		lances.add(pl);
		return pl;
	}*/
	
	/*private void atualizarEstatisticasHabilidade(HabilidadeValor habilidadeValor, Boolean vencedor) {
		habilidadeValor.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
		if (vencedor) {
			habilidadeValor.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
		}
	}*/
	
	protected void salvarEstatisticas(List<Jogador> jogadores, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		List<HabilidadeValorEstatistica> estatisticas = new ArrayList<HabilidadeValorEstatistica>();
		
		for (Jogador j : jogadores) {
			estatisticas.addAll(j.getHabilidades().stream().map(hv -> hv.getHabilidadeValorEstatistica())
					.filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		}

		partidaJogadorEstatisticaDTO.adicionarHabilidadeValorEstatistica(estatisticas);
	}

	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		EscalacaoClube escalacaoMandante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresPartida(partidaResultado.getClubeMandante(), partidaResultado);
		EscalacaoClube escalacaoVisitante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresPartida(partidaResultado.getClubeVisitante(), partidaResultado);

		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO);
	}

	@Override
	protected void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsService.disputarPenalts(partidaResultado, esquema);
	}
	
	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO, false);
	}

	/*@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		inserirConsumoEnergiaFixo(escalacaoMandante);
		inserirConsumoEnergiaFixo(escalacaoVisitante);
		
		partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
		List<PartidaLance> lances = jogar(esquema, partidaResultado, false);
		
		if (partidaResultado.isDisputarPenalts() && partidaResultado.isResultadoEmpatado()) {
			disputarPenaltsService.disputarPenalts(partidaResultado, esquema);
		}
		
		inserirConsumoEnergiaFixo(escalacaoMandante);
		inserirConsumoEnergiaFixo(escalacaoVisitante);
		
		calcularMinutosJogador(esquema);
		
		salvarEstatisticas(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		salvarEstatisticas(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		salvarEstatisticasJogador(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		salvarEstatisticasJogador(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		salvarJogadorEnergia(escalacaoMandante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		salvarJogadorEnergia(escalacaoVisitante.getListEscalacaoJogadorPosicao().stream()
				.map(EscalacaoJogadorPosicao::getJogador).collect(Collectors.toList()), partidaJogadorEstatisticaDTO);
		
		partidaJogadorEstatisticaDTO.adicionarPartidaLance(lances);
	}*/
	
	@Override
	protected HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor) {
		return new HabilidadeValor(Habilidade.FORA, valor);
	}

	/*private List<PartidaLance> jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValor habilidadeVencedorAnterior = null;
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		
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
						.forEach(p -> p.getMandante().getHabilidades().stream().forEach(h -> h.calcularValorN()));

				esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
						.forEach(p -> p.getMandante().getHabilidades().stream().forEach(h -> h.calcularValorN()));
				
				if (minuto % 30 == 0) {
					esquema.getGoleiroMandante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					esquema.getGoleiroVisitante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					
					esquema.getGoleiroMandante().getGoleiro().getHabilidades().forEach(h -> h.calcularValorN());
					esquema.getGoleiroVisitante().getGoleiro().getHabilidades().forEach(h -> h.calcularValorN());
				}

			}
			
			if (minuto == minutoSubstituicaoMandante) {
				EstrategiaSubstituicao estrategia = RoletaUtil.sortearPesoUm(
						new EstrategiaSubstituicao[] { EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS });
				realizarSubstituicoesJogadorPartidaService.realizarSubstituicoesJogadorPartida(esquema,
						estrategia, partidaResultado, true, minuto);
			}
			
			if (minuto == minutoSubstituicaoVisitante) {
				EstrategiaSubstituicao estrategia = RoletaUtil.sortearPesoUm(
						new EstrategiaSubstituicao[] { EstrategiaSubstituicao.ENTRAR_SUBSTITUTOS_MAIS_FORTES,
								EstrategiaSubstituicao.SUBSTITUIR_MAIS_CANSADOS });
				realizarSubstituicoesJogadorPartidaService.realizarSubstituicoesJogadorPartida(esquema,
						estrategia, partidaResultado, false, minuto);
			}

			for (int j = 0; j < NUM_LANCES_POR_MINUTO; j++) {
			
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

				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidade(), !jogadorAcaoVenceu, ordemJogada, false, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoFimJogadorPosicaoAtualPosse());
					if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidade().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()){
						if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), true, ordemJogada, true, minuto);
						atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, true);
					}
					ordemJogada++;
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
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
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));//TODO: ajustar para compreender diminuição da energia??
					habilidadeFora.calcularValorN();
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					habilidadeVencedora = (HabilidadeValor) RoletaUtil.sortearN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true, minuto);
					atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidade(), goleiroVenceu, ordemJogada, false, minuto);
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
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
						if (IMPRIMIR) System.err.println("\t\tFORA!!!!");
						partidaResultado.incrementarFinalizacaoFora(esquema.getPosseBolaMandante());
						habilidadeValorAcao.getJogador().getJogadorEstatisticasSemana().incrementarFinalizacoesFora();
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
	}*/
	
	protected void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {
		realizarSubstituicoesJogadorPartidaService.realizarSubstituicoesJogadorPartida(esquema,
				estrategiaSubstituicao, partidaResultado, mandante, minutoSubstituicao);
	}
	
	//###	TESTE	###
	
	/*public void print(Esquema esquema, HabilidadeValor habilidadeValorAcao, HabilidadeValor habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s] [%s]", habilidadeValorAcao.getHabilidade().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidade().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu", habilidadeValorAcao.getJogador().getNumero()));
	}*/

}
