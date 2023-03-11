package com.fastfoot.match.model.factory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EsquemaImpl;
import com.fastfoot.match.model.EsquemaPosicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;

@Deprecated
public class EsquemaFactoryImplDoisTresTresDois extends EsquemaFactory {//2-3-3-2 ou 4-1-3-2*

	@Override
	public Esquema gerarEsquemaEscalacao(EscalacaoClube mandantes,
			EscalacaoClube visitantes, JogadorApoioCriacao jogadorApoioCriacaoMandante, JogadorApoioCriacao jogadorApoioCriacaoVisitante) {
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> mandantesMap = mandantes.getListEscalacaoJogadorPosicao().stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> visitantesMap = visitantes.getListEscalacaoJogadorPosicao().stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		EsquemaImpl esquema = new EsquemaImpl();

		int i = 0;
		
		//GM
		EsquemaPosicao gm = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_GOL).getJogador());i++;
		
		//ZD ZE
		EsquemaPosicao zd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_ZD).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_AE).getJogador(), PROBABILIDADE_ARREMATE_FORA_ZAG, PROBABILIDADE_ARREMATE_FORA_ATA);
		i++;
		EsquemaPosicao ze = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_ZE).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_AD).getJogador(), PROBABILIDADE_ARREMATE_FORA_ZAG, PROBABILIDADE_ARREMATE_FORA_ATA);
		i++;
		
		//VD VE
		EsquemaPosicao vd = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_VD).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_ME).getJogador(), PROBABILIDADE_ARREMATE_FORA_VOL, PROBABILIDADE_ARREMATE_FORA_MEI);
		i++;
		EsquemaPosicao ve = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_VE).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_MD).getJogador(), PROBABILIDADE_ARREMATE_FORA_VOL, PROBABILIDADE_ARREMATE_FORA_MEI);
		i++;

		//LD LE
		EsquemaPosicao ld = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_LD).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_LE).getJogador(), PROBABILIDADE_ARREMATE_FORA_LAT, PROBABILIDADE_ARREMATE_FORA_LAT);
		i++;
		EsquemaPosicao le = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_LE).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_LD).getJogador(), PROBABILIDADE_ARREMATE_FORA_LAT, PROBABILIDADE_ARREMATE_FORA_LAT);
		i++;

		//MD ME
		EsquemaPosicao md = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_MD).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_VE).getJogador(), PROBABILIDADE_ARREMATE_FORA_MEI, PROBABILIDADE_ARREMATE_FORA_VOL);
		i++;
		EsquemaPosicao me = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_ME).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_VD).getJogador(), PROBABILIDADE_ARREMATE_FORA_MEI, PROBABILIDADE_ARREMATE_FORA_VOL);
		i++;

		//AD AE
		EsquemaPosicao ad = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_AD).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_ZE).getJogador(), PROBABILIDADE_ARREMATE_FORA_ATA, PROBABILIDADE_ARREMATE_FORA_ZAG);
		i++;
		EsquemaPosicao ae = new EsquemaPosicao(i, mandantesMap.get(EscalacaoPosicao.P_AE).getJogador(),
				visitantesMap.get(EscalacaoPosicao.P_ZD).getJogador(), PROBABILIDADE_ARREMATE_FORA_ATA, PROBABILIDADE_ARREMATE_FORA_ZAG);
		i++;

		//GV
		EsquemaPosicao gv = new EsquemaPosicao(i, visitantesMap.get(EscalacaoPosicao.P_GOL).getJogador());

		addTransicaoMandante(zd, ze, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(vd, ve, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(md, me, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(ad, ae, PESO_LATERAL, PESO_LATERAL);

		addTransicaoMandante(ld, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoMandante(le, me, PESO_LATERAL, PESO_LATERAL);
		
		//VE -> LD/LE -> MD??

		addTransicaoMandante(zd, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(zd, vd, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoMandante(zd, ve, PESO_AVANCAR, PESO_RECUAR);
		
		//addTransicaoMandante(ze, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ze, ve, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(vd, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(vd, le, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoMandante(ve, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ve, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(ld, me, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoMandante(ld, le, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoMandante(md, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(md, ae, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(me, ad, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(me, ae, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoMandante(le, ad, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoMandante(le, ae, PESO_AVANCAR, PESO_RECUAR);

		//------------------

		addTransicaoVisitante(ze, zd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ve, vd, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, md, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(ae, ad, PESO_LATERAL, PESO_LATERAL);

		addTransicaoVisitante(vd, ld, PESO_LATERAL, PESO_LATERAL);
		addTransicaoVisitante(me, le, PESO_LATERAL, PESO_LATERAL);

		addTransicaoVisitante(ld, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(vd, zd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, zd, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);

		addTransicaoVisitante(ld, ze, PESO_AVANCAR_DUPLO, PESO_RECUAR_DUPLO);
		addTransicaoVisitante(vd, ze, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ve, ze, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoVisitante(md, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, vd, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, vd, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoVisitante(md, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(le, ve, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(md, ld, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(me, ld, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoVisitante(le, ld, PESO_AVANCAR, PESO_RECUAR);

		addTransicaoVisitante(ad, md, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoVisitante(ae, md, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ad, me, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, me, PESO_AVANCAR, PESO_RECUAR);
		//addTransicaoVisitante(ad, le, PESO_AVANCAR, PESO_RECUAR);
		addTransicaoVisitante(ae, le, PESO_AVANCAR, PESO_RECUAR);

		esquema.setPosicoes(Arrays.asList(gm, zd, ze, ld, le, vd, ve, md, me, ad, ae, gv));
		
		esquema.setPosicaoAtual(zd);
		
		esquema.setPosseBolaMandante(true);
		
		esquema.setGoleiroMandante(gm);
		
		esquema.setGoleiroVisitante(gv);
		
		//print(Arrays.asList(zd, ze, ld, le, ve, vd, md, me, ad, ae));

		return esquema;
	}

}
