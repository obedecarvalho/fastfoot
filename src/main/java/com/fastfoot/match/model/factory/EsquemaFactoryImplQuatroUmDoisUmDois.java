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

public class EsquemaFactoryImplQuatroUmDoisUmDois extends EsquemaFactory {//4-1-2-1-2 ou 2-1-4-1-2 ou 4-4-2 Losango

	@Override
	public Esquema gerarEsquemaEscalacao(List<EscalacaoJogadorPosicao> mandantes,
			List<EscalacaoJogadorPosicao> visitantes) {
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> mandantesMap = mandantes.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> visitantesMap = visitantes.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 0;
		
		//GM
		EsquemaPosicao gm = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_1).getJogador());i++;
		
		//ZD ZE
		EsquemaPosicao zd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_3).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_11).getJogador(), PROBABILIDADE_ARREMATE_FORA_ZAG, PROBABILIDADE_ARREMATE_FORA_ATA);
		i++;
		EsquemaPosicao ze = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_4).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_9).getJogador(), PROBABILIDADE_ARREMATE_FORA_ZAG, PROBABILIDADE_ARREMATE_FORA_ATA);
		i++;
		
		//VD VE
		EsquemaPosicao vd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_5).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_10).getJogador(), PROBABILIDADE_ARREMATE_FORA_VOL, PROBABILIDADE_ARREMATE_FORA_MEI);
		i++;
		EsquemaPosicao ve = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_8).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_7).getJogador(), PROBABILIDADE_ARREMATE_FORA_VOL, PROBABILIDADE_ARREMATE_FORA_MEI);
		i++;

		//LD LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_2).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_6).getJogador(), PROBABILIDADE_ARREMATE_FORA_LAT, PROBABILIDADE_ARREMATE_FORA_LAT);
		i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_6).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_2).getJogador(), PROBABILIDADE_ARREMATE_FORA_LAT, PROBABILIDADE_ARREMATE_FORA_LAT);
		i++;

		//MD ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_7).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_8).getJogador(), PROBABILIDADE_ARREMATE_FORA_MEI, PROBABILIDADE_ARREMATE_FORA_VOL);
		i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_10).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_5).getJogador(), PROBABILIDADE_ARREMATE_FORA_MEI, PROBABILIDADE_ARREMATE_FORA_VOL);
		i++;

		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_9).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_4).getJogador(), PROBABILIDADE_ARREMATE_FORA_ATA, PROBABILIDADE_ARREMATE_FORA_ZAG);
		i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_11).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_3).getJogador(), PROBABILIDADE_ARREMATE_FORA_ATA, PROBABILIDADE_ARREMATE_FORA_ZAG);
		i++;

		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantesMap.get(EscalacaoPosicao.P_1).getJogador());

		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ld, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ve, le, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);
		
		//ZE -> VE?
		//MD -> AD?

		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, le, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(vd, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, ve, PESO_AVANCAR, PESO_RECUAR);		
		addTransicaoMandante(vd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, le, PESO_AVANCAR, PESO_RECUAR);		
		
		addTransicaoMandante(md, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, me, PESO_AVANCAR, PESO_RECUAR);		
		
		addTransicaoMandante(me, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, ad, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, ae, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);

		//------------------

		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(md, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ve, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(le, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, ad, PESO_LATERAL, PESO_LATERAL);

		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, zd, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(vd, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ld, ze, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);

		addTransicaoVisitante(md, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ld, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, vd, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoVisitante(me, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, le, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoVisitante(ad, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
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
				System.err.println(String.format("\t%s ---> %s [p:%d]", et.getPosInicial().getMandante().getPosicao(), et.getPosFinal().getMandante().getPosicao(), et.getPeso()));
			}
		}

		for (EsquemaPosicao ep : x) {

			System.err.println(String.format("Posicao: %d", ep.getNumero()));

			for (EsquemaTransicao et : ep.getTransicoesVisitante()) {
				System.err.println(String.format("\t%s ---> %s [p:%d]", et.getPosInicial().getVisitante().getPosicao(), et.getPosFinal().getVisitante().getPosicao(), et.getPeso()));
			}
		}
		//
	}
}
