package com.fastfoot.match.model;

import java.util.Arrays;
import java.util.List;

import com.fastfoot.player.model.entity.Jogador;

public class EsquemaFactory {
	
	private static final Integer PESO_LATERAL = 75;
	
	private static final Integer PESO_AVANCAR = 100;
	
	private static final Integer PESO_RECUAR = 50;

	public static Esquema gerarEsquema(List<Jogador> mandantes, List<Jogador> visitantes) {

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 0;
		
		//GM
		EsquemaPosicao gm = new EsquemaPosicao(i, mandantes.get(i));i++;
		
		//ZD ZE
		EsquemaPosicao zd = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao ze = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
	
		//LD V LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao v = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		
		//MD M ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao m = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		
		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantes.get(i), visitantes.get(11-i)); i++;
		
		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantes.get(11-i));

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

		return esquema;
	}
	
	private static void addTransicaoMandante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoMandante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoMandante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
	
	private static void addTransicaoVisitante(EsquemaPosicao p1, EsquemaPosicao p2, Integer pesoP1P2, Integer pesoP2P1) {
		p1.addTransicaoVisitante(new EsquemaTransicao(p1, p2, pesoP1P2));
		p2.addTransicaoVisitante(new EsquemaTransicao(p2, p1, pesoP2P1));
	}
}
