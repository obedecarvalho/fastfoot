package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.entity.PartidaLance;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class SimularConfrontoJogadorService {
	
	private static final Integer NUM_SIMULACOES = 10;
	
	private static final float MIN_FORA = 0.2f;

	public void simularConfrontoJogador(Jogador jogAcao, Jogador jogReacao, Jogador goleiro) {
		
		List<PartidaLance> lances = new ArrayList<PartidaLance>();
		
		//Estatisticas
		
		
		HabilidadeValor habilidadeValorAcao = null;
		HabilidadeValor habilidadeValorReacao = null;
		HabilidadeValor habilidadeFora = null;
		HabilidadeValor habilidadeVencedora = null;
		HabilidadeValor habilidadeVencedorAnterior = null;
		
		Boolean jogadorAcaoVenceu = null, goleiroVenceu = null;
		
		Integer ordemJogada = 1, golsMarcados = 0, defesasGoleiro = 0, finalizacoesFora = 0;
		
		Esquema esquema = new EsquemaImpl();//TODO
		
		
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
				
				lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
						jogadorAcaoVenceu, ordemJogada, true));
				lances.add(new PartidaLance(habilidadeValorReacao.getJogador(), habilidadeValorReacao.getHabilidade(),
						!jogadorAcaoVenceu, ordemJogada, false));
				ordemJogada++;
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}

			} while (jogadorAcaoVenceu && habilidadeValorAcao.getHabilidadeAcao().isAcaoInicial());// Dominio

			if (jogadorAcaoVenceu) {

				if (habilidadeValorAcao.getHabilidadeAcao().isAcaoMeio()
						|| habilidadeValorAcao.getHabilidadeAcao().isAcaoInicioMeio()) {

					habilidadeValorAcao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) jogAcao.getHabilidadesAcaoFimValor());
				}

				if (habilidadeValorAcao.getHabilidadeAcao().isExigeGoleiro()) {
					habilidadeValorReacao = (HabilidadeValor) RoletaUtil
							.executarN((List<? extends ElementoRoleta>) goleiro.getHabilidades(
									Arrays.asList(habilidadeValorAcao.getHabilidadeAcao().getReacaoGoleiro())));

					habilidadeFora = new HabilidadeValor(Habilidade.FORA, (int) Math.round(Math.max(
							((habilidadeValorAcao.getJogador().getForcaGeral() * 1.5) - habilidadeValorAcao.getValor()),
							(MIN_FORA * habilidadeValorAcao.getJogador().getForcaGeral()))));

					habilidadeVencedora = (HabilidadeValor) RoletaUtil
							.executarN(Arrays.asList(habilidadeValorAcao, habilidadeValorReacao, habilidadeFora));
					jogadorAcaoVenceu = habilidadeVencedora.equals(habilidadeValorAcao);
					goleiroVenceu = habilidadeVencedora.equals(habilidadeValorReacao);
					
					lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
							jogadorAcaoVenceu, ordemJogada, true));
					lances.add(new PartidaLance(habilidadeValorReacao.getJogador(), habilidadeValorReacao.getHabilidade(),
							goleiroVenceu, ordemJogada, false));
					ordemJogada++;

					if (jogadorAcaoVenceu) {
						// Gol
						golsMarcados++;
					} else if (goleiroVenceu) {
						// Defesa
						defesasGoleiro++;
					} else if (habilidadeVencedora.equals(habilidadeFora)) {
						// Fora
						finalizacoesFora++;
					}

				} else {
					// Transicao
					System.err.println("\t\tTransicao");
					lances.add(new PartidaLance(habilidadeValorAcao.getJogador(), habilidadeValorAcao.getHabilidade(),
							null, ordemJogada++, true));
				}
				
				if (jogadorAcaoVenceu) {
					habilidadeVencedorAnterior = habilidadeValorAcao;
				} else {
					habilidadeVencedorAnterior = null;
				}

			} else {
				// Recomecar
				habilidadeVencedorAnterior = null;
			}
		}
		
	}
}
