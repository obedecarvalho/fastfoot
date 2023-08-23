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
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeGrupoValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Service
public class JogarPartidaHabilidadeGrupoService extends JogarPartidaService {

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
			HabilidadeGrupo habilidadeGrupo, Boolean vencedor, Integer ordem, Boolean acao, Integer minuto) {
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
		pl.setHabilidadeGrupoUsada(habilidadeGrupo);
		pl.setOrdem(ordem);
		pl.setAcao(acao);
		pl.setMinuto(minuto);
		lances.add(pl);
		return pl;
	}*/
	
	/*private void atualizarEstatisticasHabilidadeGrupo(HabilidadeGrupoValor habilidadeGrupoValor, Boolean vencedor) {
		habilidadeGrupoValor.getHabilidadeGrupoValorEstatistica().incrementarQuantidadeUso();
		if (vencedor) {
			habilidadeGrupoValor.getHabilidadeGrupoValorEstatistica().incrementarQuantidadeUsoVencedor();
		}
	}*/
	
	protected void salvarEstatisticas(List<Jogador> jogadores, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		List<HabilidadeGrupoValorEstatistica> estatisticas = new ArrayList<HabilidadeGrupoValorEstatistica>();
		
		for (Jogador j : jogadores) {
			estatisticas.addAll(j.getHabilidadesGrupo().stream().map(hv -> hv.getHabilidadeGrupoValorEstatistica())
					.filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		}

		partidaJogadorEstatisticaDTO.adicionarHabilidadeGrupoValorEstatistica(estatisticas);
	}

	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		EscalacaoClube escalacaoMandante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresHabilidadeGrupoPartida(partidaResultado.getClubeMandante(), partidaResultado);
		EscalacaoClube escalacaoVisitante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresHabilidadeGrupoPartida(partidaResultado.getClubeVisitante(), partidaResultado);
		
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO);
	}

	@Override
	protected void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsService.disputarPenaltsHabilidadeGrupo(partidaResultado, esquema);
	}
	
	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO, true);
	}

	/*@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante, EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {

		Esquema esquema = EsquemaFactoryImpl.getInstance().gerarEsquemaEscalacao(escalacaoMandante, escalacaoVisitante,
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()),
				RoletaUtil.sortearPesoUm(JogadorApoioCriacao.values()));
		
		inserirConsumoEnergiaFixo(escalacaoMandante);
		inserirConsumoEnergiaFixo(escalacaoVisitante);
		
		partidaResultado.setPartidaEstatisticas(new PartidaEstatisticas());
		List<PartidaLance> lances = jogar(esquema, partidaResultado, true);
		
		if (partidaResultado.isDisputarPenalts() && partidaResultado.isResultadoEmpatado()) {
			disputarPenaltsService.disputarPenaltsHabilidadeGrupo(partidaResultado, esquema);
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
		return new HabilidadeGrupoValor(HabilidadeGrupo.FORA, valor);
	}

	/*private List<PartidaLance> jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeGrupoValor habilidadeVencedorAnterior = null;
		HabilidadeGrupoValor habilidadeValorAcao = null;
		HabilidadeGrupoValor habilidadeValorReacao = null;
		HabilidadeGrupoValor habilidadeFora = null;
		HabilidadeGrupoValor habilidadeVencedora = null;
		
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
						.forEach(p -> p.getMandante().getHabilidadesGrupo().stream().forEach(h -> h.calcularValorN()));

				esquema.getPosicoes().stream().filter(p -> p.getVisitante() != null)
						.forEach(p -> p.getMandante().getHabilidadesGrupo().stream().forEach(h -> h.calcularValorN()));
				
				if (minuto % 30 == 0) {
					esquema.getGoleiroMandante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					esquema.getGoleiroVisitante().getGoleiro().getJogadorEnergia()
							.adicionarEnergia(-Constantes.CONSUMO_PARCIAL_ENERGIA_PARTIDA_GOLEIRO);
					
					esquema.getGoleiroMandante().getGoleiro().getHabilidadesGrupo().forEach(h -> h.calcularValorN());
					esquema.getGoleiroVisitante().getGoleiro().getHabilidadesGrupo().forEach(h -> h.calcularValorN());
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
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidadeGrupoAcao().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeGrupoValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesGrupo(
									habilidadeVencedorAnterior.getHabilidadeGrupoAcao().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeGrupoValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesGrupoAcaoMeioFimJogadorPosicaoAtualPosse());
				}
				
				habilidadeValorReacao = (HabilidadeGrupoValor) RoletaUtil
						.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesGrupoJogadorPosicaoAtualSemPosse(
								habilidadeValorAcao.getHabilidadeGrupoAcao().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);

				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeGrupo(), jogadorAcaoVenceu, ordemJogada, true, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
				if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidadeGrupo(), !jogadorAcaoVenceu, ordemJogada, false, minuto);
				atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				if (IMPRIMIR) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeGrupoAcao().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeGrupoAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeGrupoAcao().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeGrupoValor) RoletaUtil.sortearN((List<? extends ElementoRoleta>) esquema.getHabilidadesGrupoAcaoFimJogadorPosicaoAtualPosse());
					if (IMPRIMIR) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidadeGrupo().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeGrupoAcao().isExigeGoleiro()){
						if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidadeGrupo(), true, ordemJogada, true, minuto);
						atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, true);
					}
					ordemJogada++;
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeGrupoAcao().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeGrupoValor) RoletaUtil
							.sortearN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidadesGrupo(
											Arrays.asList(habilidadeValorAcao.getHabilidadeGrupoAcao().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					habilidadeFora = new HabilidadeGrupoValor(HabilidadeGrupo.FORA, (int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * esquema.getProbabilidadeArremateForaPosicaoPosse()) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));//TODO: ajustar para compreender diminuição da energia??
					habilidadeFora.calcularValorN();
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					habilidadeVencedora = (HabilidadeGrupoValor) RoletaUtil.sortearN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidadeGrupo(), jogadorAcaoVenceu, ordemJogada, true, minuto);
					atualizarEstatisticasHabilidadeValorJogavel(habilidadeValorAcao, jogadorAcaoVenceu);
					
					if (LANCE_A_LANCE) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidadeGrupo(), goleiroVenceu, ordemJogada, false, minuto);
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
	
	/*public void print(Esquema esquema, HabilidadeGrupoValor habilidadeValorAcao, HabilidadeGrupoValor habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s] [%s]", habilidadeValorAcao.getHabilidadeGrupo().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidadeGrupo().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu", habilidadeValorAcao.getJogador().getNumero()));
	}*/

}
