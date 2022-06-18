package com.fastfoot.match.model.factory;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.player.model.entity.Jogador;

public class EsquemaFactoryDoisTresTresDois extends EsquemaFactory {
	
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
	
		//LD V LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantes.get(2-1), visitantes.get(8-1)); i++;
		EsquemaPosicao v = new EsquemaPosicao(i, mandantes.get(5-1), visitantes.get(10-1)); i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantes.get(6-1), visitantes.get(7-1)); i++;
		
		//MD M ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantes.get(7-1), visitantes.get(6-1)); i++;
		EsquemaPosicao m = new EsquemaPosicao(i, mandantes.get(10-1), visitantes.get(5-1)); i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantes.get(8-1), visitantes.get(2-1)); i++;
		
		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantes.get(9-1), visitantes.get(4-1)); i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantes.get(11-1), visitantes.get(3-1)); i++;
		
		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantes.get(1-1));

		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(v, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(v, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(v, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(v, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(md, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(m, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(m, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(m, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);
		
		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(v, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(le, v, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(md, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(m, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(m, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, v, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(m, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(m, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, m, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ad, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, m, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
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
