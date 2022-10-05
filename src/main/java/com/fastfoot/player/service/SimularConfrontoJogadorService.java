package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class SimularConfrontoJogadorService {
	
	private static final Integer NUM_SIMULACOES = 100;
	
	private static final float MIN_FORA = 0.2f;
	
	private static final float PROB_ARREMATE_FORA = 1.5f;

	public void simularConfrontoJogador(Jogador jogAcao, Jogador jogReacao, Jogador goleiro) {
		
		jogAcao.getHabilidades().stream()
				.forEach(hv -> hv.setHabilidadeValorEstatistica(new HabilidadeValorEstatistica(hv)));
		jogReacao.getHabilidades().stream()
				.forEach(hv -> hv.setHabilidadeValorEstatistica(new HabilidadeValorEstatistica(hv)));
		goleiro.getHabilidades().stream()
				.forEach(hv -> hv.setHabilidadeValorEstatistica(new HabilidadeValorEstatistica(hv)));
		
		JogadorEstatisticasTemporada jogAcaoEstatisticas = new JogadorEstatisticasTemporada(jogAcao);
		JogadorEstatisticasTemporada goleiroEstatisticas = new JogadorEstatisticasTemporada(goleiro);
		
		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		HabilidadeValor habilidadeVencedorAnterior = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null, houveAssistencia = false;
		
		Integer ordemJogada = 1;
		
		for (int i = 0; i < NUM_SIMULACOES; i++) {

			do {

				if (habilidadeVencedorAnterior != null
						&& habilidadeVencedorAnterior.getHabilidadeAcao().contemAcoesSubsequentes()) {
					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) jogAcao.getHabilidades(
									habilidadeVencedorAnterior.getHabilidadeAcao().getAcoesSubsequentes()));
				} else {

					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) jogAcao.getHabilidadesAcaoMeioFimValor());

				}

				habilidadeValorReacao = (HabilidadeValor) RoletaUtil
						.executarN((List<? extends ElementoRoleta>) jogReacao
								.getHabilidades(habilidadeValorAcao.getHabilidadeAcao().getPossiveisReacoes()));

				jogadorAcaoVenceu = RoletaUtil.isPrimeiroVencedorN(habilidadeValorAcao, habilidadeValorReacao);
				
				habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
				habilidadeValorReacao.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
				
				lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
						jogadorAcaoVenceu, ordemJogada, true));
				lances.add(new PartidaLance(habilidadeValorReacao.getJogador(), habilidadeValorReacao.getHabilidade(),
						!jogadorAcaoVenceu, ordemJogada, false));
				ordemJogada++;
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
					habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
				} else {
					habilidadeVencedorAnterior = null;
					habilidadeValorReacao.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
				}

			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());// Dominio

			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio()
						|| habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {

					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) jogAcao.getHabilidadesAcaoFimValor());
					
					if (!habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()){
						habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
						habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
						
						lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
								null, ordemJogada++, true));
					}
				}

				if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) goleiro.getHabilidades(
									Arrays.asList(habilidadeValorAcao.getHabilidadeAcao().getReacaoGoleiro())));

					habilidadeFora = new HabilidadeValor(Habilidade.FORA,
							(int) Math.max(
									((habilidadeValorAcao.getJogador().getForcaGeral() * PROB_ARREMATE_FORA)
											- habilidadeValorAcao.getValor()),
									(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral())));

					habilidadeVencedora = (HabilidadeValor) RoletaUtil
							.executarN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					
					habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
					habilidadeValorReacao.getHabilidadeValorEstatistica().incrementarQuantidadeUso();
					
					lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
							jogadorAcaoVenceu, ordemJogada, true));
					lances.add(new PartidaLance(habilidadeValorReacao.getJogador(), habilidadeValorReacao.getHabilidade(),
							goleiroVenceu, ordemJogada, false));
					ordemJogada++;

					if (jogadorAcaoVenceu) {
						// Gol
						habilidadeValorAcao.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
						goleiroEstatisticas.incrementarGolsSofridos();
						jogAcaoEstatisticas.incrementarGolsMarcados();
						if (houveAssistencia) jogAcaoEstatisticas.incrementarAssistencias();
					} else if (goleiroVenceu) {
						// Defesa
						habilidadeValorReacao.getHabilidadeValorEstatistica().incrementarQuantidadeUsoVencedor();
						goleiroEstatisticas.incrementarGoleiroFinalizacoesDefendidas();
						jogAcaoEstatisticas.incrementarFinalizacoesDefendidas();
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
						// Fora
						jogAcaoEstatisticas.incrementarFinalizacoesFora();
					}

					houveAssistencia = false;

				} else {
					// Transicao
					//PASSE, CRUZAMENTO, ARMACAO
					houveAssistencia = true;
				}
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}

			} else {
				// Recomecar
				habilidadeVencedorAnterior = null;
				houveAssistencia = false;
			}
		}
		
		System.err.println("\tJogador Acao");
		System.err.println(jogAcaoEstatisticas);		
		System.err.println(jogAcao.getHabilidades().stream().map(HabilidadeValor::getHabilidadeValorEstatistica).filter(e -> e.getQuantidadeUso() > 0).collect(Collectors.toList()));
		
		System.err.println("\tJogador Reacao");
		System.err.println(jogReacao.getHabilidades().stream().map(HabilidadeValor::getHabilidadeValorEstatistica).filter(e -> e.getQuantidadeUso() > 0).collect(Collectors.toList()));
		
		System.err.println("\tGoleiro");
		System.err.println(goleiroEstatisticas);		
		System.err.println(goleiro.getHabilidades().stream().map(HabilidadeValor::getHabilidadeValorEstatistica).filter(e -> e.getQuantidadeUso() > 0).collect(Collectors.toList()));
		
		//System.err.println("\tLances");
		//System.err.println(lances);
		
	}
}
