package com.fastfoot.match.service.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.JogadorApoioCriacao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;

public class DefinirJogadorApoioCriacaoUtil {

	public static JogadorApoioCriacao definirJogadorApoioCriacao(List<EscalacaoJogadorPosicao> escalacao) {

		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> escalacaoMap = escalacao.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));

		EscalacaoJogadorPosicao ld = escalacaoMap.get(EscalacaoPosicao.P_LD);
		EscalacaoJogadorPosicao vd = escalacaoMap.get(EscalacaoPosicao.P_VD);
		EscalacaoJogadorPosicao ve = escalacaoMap.get(EscalacaoPosicao.P_VE);
		EscalacaoJogadorPosicao le = escalacaoMap.get(EscalacaoPosicao.P_LE);

		//TODO
		
		ld.getAtivo();
		vd.getAtivo();
		ve.getAtivo();
		le.getAtivo();
		
		return null;
	}

}
