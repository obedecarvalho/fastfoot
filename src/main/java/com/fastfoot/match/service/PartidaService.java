package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaFactory;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class PartidaService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;

	private double NUM_LANCES_POR_MINUTO = 1;
	
	private Integer MINUTOS = 90;

	private float MIN_FORA = 0.2f;

	private Boolean imprimir = false;
	
	private Boolean lanceALance = true;
	
	private PartidaLance criarPartidaLance(List<PartidaLance> lances, PartidaResultadoJogavel partida, Jogador jogador,
			Habilidade habilidade, Boolean vencedor, Integer ordem, Boolean acao) {
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
		lances.add(pl);
		return pl;
	}
	
	private void atualizarEstatisticasHabilidade(HabilidadeValor habilidadeValor, Boolean vencedor) {
		habilidadeValor.incrementarQuantidadeUso();
		if (vencedor) {
			habilidadeValor.incrementarQuantidadeUsoVencedor();
		}
	}

	public void jogar(PartidaResultadoJogavel partidaResultado) {
		//TODO: mudar de classe
		List<Jogador> jogadoresMandante = jogadorRepository.findByClubeCompleto(partidaResultado.getClubeMandante());//TODO: transformar em entidade Escalacao
		List<Jogador> jogadoresVisitante = jogadorRepository.findByClubeCompleto(partidaResultado.getClubeVisitante());//TODO: transformar em entidade Escalacao
		
		/*for (Jogador j : jogadoresMandante) {
			j.setHabilidades(habilidadeValorRepository.findByJogador(j));
		}

		for (Jogador j : jogadoresVisitante) {
			j.setHabilidades(habilidadeValorRepository.findByJogador(j));
		}*/

		Esquema esquema = EsquemaFactory.gerarEsquema(jogadoresMandante, jogadoresVisitante);
		
		jogar(esquema, partidaResultado);
		
		for (Jogador j : jogadoresMandante) {
			habilidadeValorRepository.saveAll(j.getHabilidades());
		}
	
		for (Jogador j : jogadoresVisitante) {
			habilidadeValorRepository.saveAll(j.getHabilidades());
		}
	}

	private void jogar(Esquema esquema, PartidaResultadoJogavel partidaResultado) {

		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		Integer ordemJogada = 1;
		Integer golMandante = 0, golVisitante = 0;

		HabilidadeValor habilidadeVencedorAnterior = null;
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		for (int i = 0; i < (NUM_LANCES_POR_MINUTO * MINUTOS); i++) {
			
			do {
			
				if (habilidadeVencedorAnterior != null && habilidadeVencedorAnterior.getHabilidadeAcao().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) esquema.getHabilidades(
									habilidadeVencedorAnterior.getHabilidadeAcao().getAcoesSubsequentes()));
				} else {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.executarN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoMeioFimJogadorPosicaoAtualPosse());
				}
				
				habilidadeValorReacao = (HabilidadeValor) RoletaUtil
						.executarN((List<? extends ElementoRoleta>) esquema.getHabilidadesJogadorPosicaoAtualSemPosse(
								habilidadeValorAcao.getHabilidadeAcao().getPossiveisReacoes()));
				
				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);

				if (lanceALance) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);
				atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
				if (lanceALance) criarPartidaLance(lances, partidaResultado, esquema.getJogadorSemPosse(), habilidadeValorReacao.getHabilidade(), !jogadorAcaoVenceu, ordemJogada, false);
				atualizarEstatisticasHabilidade(habilidadeValorReacao, !jogadorAcaoVenceu);
				ordemJogada++;
				
				partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				
				//
				if (imprimir) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
				//
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			
			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());//Dominio
			
			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio() || habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil.executarN((List<? extends ElementoRoleta>) esquema.getHabilidadesAcaoFimJogadorPosicaoAtualPosse());
					if (imprimir) System.err.println(String.format("\t\t-> %s", habilidadeValorAcao.getHabilidade().getDescricao()));
					if (!habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()){
						if (lanceALance) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(), habilidadeValorAcao.getHabilidade(), true, ordemJogada, true);
						atualizarEstatisticasHabilidade(habilidadeValorAcao, true);
					}
					ordemJogada++;
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());
				}
				
				if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {//FINALIZACAO, CABECEIO
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) esquema.getGoleiroSemPosse().getGoleiro()
									.getHabilidades(
											Arrays.asList(habilidadeValorAcao.getHabilidadeAcao().getReacaoGoleiro())));
					
					//fora
					//habilidadeFora = new HabilidadeValor(Habilidade.NULL, (habilidadeValorAcao.getValor() + habilidadeValorReacao.getValor())/2);
					habilidadeFora = new HabilidadeValor(Habilidade.FORA,
							Math.max(habilidadeValorAcao.getJogador().getForcaGeral() - habilidadeValorAcao.getValor(),
									Math.round(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral())));
					//System.err.println(String.format("\t\t\tJ:%d A:%d F:%d", habilidadeValorAcao.getJogador().getForcaGeral(), habilidadeValorAcao.getValor(), habilidadeFora.getValor()));

					//jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedor(habilidadeValorAcao, habilidadeValorReacao);
					habilidadeVencedora = (HabilidadeValor) RoletaUtil.executarN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					//System.err.println("\t\tFORA!!!!");
					
					//
					if (lanceALance) criarPartidaLance(lances, partidaResultado, esquema.getJogadorPosse(),
							habilidadeValorAcao.getHabilidade(), jogadorAcaoVenceu, ordemJogada, true);
					atualizarEstatisticasHabilidade(habilidadeValorAcao, jogadorAcaoVenceu);
					
					if (lanceALance) criarPartidaLance(lances, partidaResultado, esquema.getGoleiroSemPosse().getGoleiro(),
							habilidadeValorReacao.getHabilidade(), goleiroVenceu, ordemJogada, false);
					atualizarEstatisticasHabilidade(habilidadeValorReacao, goleiroVenceu);
					ordemJogada++;
					
					partidaResultado.incrementarLance(esquema.getPosseBolaMandante());

					//
					if (imprimir) print(esquema, habilidadeValorAcao, habilidadeValorReacao, jogadorAcaoVenceu);
					//
					
					if (jogadorAcaoVenceu) {
						if (imprimir) System.err.println("\t\tGOLLL");
						if (esquema.getPosseBolaMandante()) {
							golMandante++;
						} else {
							golVisitante++;
						}
						partidaResultado.incrementarGol(esquema.getPosseBolaMandante());
					} else if (goleiroVenceu) {
						if (imprimir) System.err.println("\t\tGOLEIRO DEFENDEU");
						partidaResultado.incrementarFinalizacaoDefendida(esquema.getPosseBolaMandante());
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
						if (imprimir) System.err.println("\t\tFORA!!!!");
						partidaResultado.incrementarFinalizacaoFora(esquema.getPosseBolaMandante());
					}
					esquema.inverterPosse();//TODO
				} else {
					//PASSE, CRUZAMENTO, ARMACAO
					EsquemaTransicao t = (EsquemaTransicao) RoletaUtil.executarN((List<? extends ElementoRoleta>) esquema.getTransicoesPosse());
					if (imprimir) System.err.println(String.format("%d ==> %d (%s)", esquema.getPosicaoAtual().getNumero(), t.getPosFinal().getNumero(), esquema.getPosseBolaMandante() ? "M" : "V"));
					esquema.setPosicaoAtual(t.getPosFinal());
				}
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}
			} else {
				esquema.inverterPosse();
				if (imprimir) System.err.println("\t\tPOSSE INVERTIDA");
				//habilidadeVencedorAnterior = habilidadeValorReacao;
				habilidadeVencedorAnterior = null;
			}
		}

		//JogadorAgruparGrupoEstatisticasUtil.agruparEstatisticas(lances);

		if (imprimir) System.err.println(String.format("\n\t\t%d x %d", golMandante, golVisitante));
		//TODO: penalts
		
		partidaResultado.setPartidaJogada(true);
	}
	
	//###	TESTE	###
	
	public void print(Esquema esquema, HabilidadeValor habilidadeValorAcao, HabilidadeValor habilidadeValorReacao, boolean jogadorAcaoVenceu) {
		System.err.println(String.format("\t\t%s (%d) x %s (%d) [%s] [%s]", habilidadeValorAcao.getHabilidade().getDescricao(),
				habilidadeValorAcao.getValor(), habilidadeValorReacao.getHabilidade().getDescricao(),
				habilidadeValorReacao.getValor(), esquema.getPosseBolaMandante() ? "M" : "V",
				jogadorAcaoVenceu ? "Venceu" : "Perdeu"));
	}
}
