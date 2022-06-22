package com.fastfoot.match.model.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.EsquemaTransicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaFactoryDoisDoisDoisDoisDois extends EsquemaFactory {

	@Override
	public Esquema gerarEsquema(List<Jogador> mandantes, List<Jogador> visitantes) {

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 0;
		
		ordenarJogadores(mandantes);
		ordenarJogadores(visitantes);
		
		//GM
		EsquemaPosicao gm = new EsquemaPosicao(i, mandantes.get(1-1));i++;
		
		//ZD ZE
		EsquemaPosicao zd = new EsquemaPosicao(i, mandantes.get(3-1), visitantes.get(11-1)); i++;
		EsquemaPosicao ze = new EsquemaPosicao(i, mandantes.get(4-1), visitantes.get(9-1)); i++;
		
		//VD VE
		EsquemaPosicao vd = new EsquemaPosicao(i, mandantes.get(5-1), visitantes.get(10-1)); i++;
		EsquemaPosicao ve = new EsquemaPosicao(i, mandantes.get(8-1), visitantes.get(7-1)); i++;
	
		//LD LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantes.get(2-1), visitantes.get(6-1)); i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantes.get(6-1), visitantes.get(2-1)); i++;
		
		//MD ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantes.get(7-1), visitantes.get(8-1)); i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantes.get(10-1), visitantes.get(5-1)); i++;

		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantes.get(9-1), visitantes.get(4-1)); i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantes.get(11-1), visitantes.get(3-1)); i++;
		
		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantes.get(1-1));

		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(vd, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(ld, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, md, PESO_LATERAL, PESO_LATERAL);		
		addTransicaoMandante(le, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(le, me, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, ve, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(ld, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, ae, PESO_AVANCAR, PESO_RECUAR);
		
		addTransicaoMandante(vd, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, me, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(md, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(md, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);

		//------------------

		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ve, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, ad, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(vd, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(md, ld, PESO_LATERAL, PESO_LATERAL);		
		addTransicaoVisitante(ve, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, le, PESO_AVANCAR, PESO_RECUAR);

		esquema.setPosicoes(Arrays.asList(gm, zd, ze, ld, le, vd, ve, md, me, ad, ae, gv));
		
		esquema.setPosicaoAtual(zd);
		
		esquema.setPosseBolaMandante(true);
		
		esquema.setGoleiroMandante(gm);
		
		esquema.setGoleiroVisitante(gv);
		
		//print(Arrays.asList(zd, ze, ld, le, ve, vd, md, me, ad, ae));

		return esquema;
	}

	public Esquema gerarEsquemaEscalacao(List<EscalacaoJogadorPosicao> mandantes, List<EscalacaoJogadorPosicao> visitantes) {
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> mandantesMap = mandantes.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> visitantesMap = visitantes.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 0;
		
		//ordenarJogadores(mandantes);
		//ordenarJogadores(visitantes);
		
		//GM
		EsquemaPosicao gm = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_1).getJogador());i++;
		
		//ZD ZE
		EsquemaPosicao zd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_3).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_11).getJogador());
		i++;
		EsquemaPosicao ze = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_4).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_9).getJogador());
		i++;
		
		//VD VE
		EsquemaPosicao vd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_5).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_10).getJogador());
		i++;
		EsquemaPosicao ve = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_8).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_7).getJogador());
		i++;

		//LD LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_2).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_6).getJogador());
		i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_6).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_2).getJogador());
		i++;

		//MD ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_7).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_8).getJogador());
		i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_10).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_5).getJogador());
		i++;

		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_9).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_4).getJogador());
		i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_11).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_3).getJogador());
		i++;

		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantesMap.get(EscalacaoPosicao.P_1).getJogador());

		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(vd, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(ld, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, md, PESO_LATERAL, PESO_LATERAL);		
		addTransicaoMandante(le, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(le, me, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, ve, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(ld, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, ae, PESO_AVANCAR, PESO_RECUAR);
		
		addTransicaoMandante(vd, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, me, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(md, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(md, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);

		//------------------

		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ve, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, ad, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(vd, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(md, ld, PESO_LATERAL, PESO_LATERAL);		
		addTransicaoVisitante(ve, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, le, PESO_AVANCAR, PESO_RECUAR);

		esquema.setPosicoes(Arrays.asList(gm, zd, ze, ld, le, vd, ve, md, me, ad, ae, gv));
		
		esquema.setPosicaoAtual(zd);
		
		esquema.setPosseBolaMandante(true);
		
		esquema.setGoleiroMandante(gm);
		
		esquema.setGoleiroVisitante(gv);
		
		//print(Arrays.asList(zd, ze, ld, le, ve, vd, md, me, ad, ae));

		return esquema;
	}

	@SuppressWarnings("unused")
	synchronized private static void print(List<EsquemaPosicao> x) {
		//
		System.err.println("----------------------------------------------");
		for (EsquemaPosicao ep : x) {

			System.err.println(String.format("Posicao: %d", ep.getNumero()));

			for (EsquemaTransicao et : ep.getTransicoesMandante()) {
				System.err.println(String.format("\t%d ---> %d [p:%d]", et.getPosInicial().getMandante().getNumero(), et.getPosFinal().getMandante().getNumero(), et.getPeso()));
			}
		}

		for (EsquemaPosicao ep : x) {

			System.err.println(String.format("Posicao: %d", ep.getNumero()));

			for (EsquemaTransicao et : ep.getTransicoesVisitante()) {
				System.err.println(String.format("\t%d ---> %d [p:%d]", et.getPosInicial().getVisitante().getNumero(), et.getPosFinal().getVisitante().getNumero(), et.getPeso()));
			}
		}
		//
	}

}
