package com.fastfoot.match.model.factory;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaFactoryDoisUmQuatroUmDois extends EsquemaFactory {

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
		
		//V
		EsquemaPosicao v = new EsquemaPosicao(i, mandantes.get(5-1), visitantes.get(10-1)); i++;
	
		//LD MD ME LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantes.get(2-1), visitantes.get(6-1)); i++;
		EsquemaPosicao md = new EsquemaPosicao(i, mandantes.get(7-1), visitantes.get(8-1)); i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantes.get(8-1), visitantes.get(7-1)); i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantes.get(6-1), visitantes.get(2-1)); i++;
		
		//M
		EsquemaPosicao m = new EsquemaPosicao(i, mandantes.get(10-1), visitantes.get(5-1)); i++;
		
		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantes.get(9-1), visitantes.get(4-1)); i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantes.get(11-1), visitantes.get(3-1)); i++;
		
		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantes.get(1-1));


		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(v, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(v, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(me, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(le, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(le, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(md, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(v, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(v, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(m, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(m, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(m, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);

		//------------------

		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ld, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(md, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(m, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ad, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(le, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(m, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(m, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(m, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ad, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, ad, PESO_LATERAL, PESO_LATERAL);

		esquema.setPosicoes(Arrays.asList(gm, zd, ze, ld, le, v, md, me, m, ad, ae, gv));
		
		esquema.setPosicaoAtual(zd);
		
		esquema.setPosseBolaMandante(true);
		
		esquema.setGoleiroMandante(gm);
		
		esquema.setGoleiroVisitante(gv);
		
		//print(Arrays.asList(zd, ze, ld, le, v, md, me, m, ad, ae));

		return esquema;
	}

	/*synchronized private static void print(List<EsquemaPosicao> x) {
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
	}*/
}
