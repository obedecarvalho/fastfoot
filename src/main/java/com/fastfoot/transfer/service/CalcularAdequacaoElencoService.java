package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.transfer.model.entity.AdequacaoElencoPosicao;
import com.fastfoot.transfer.model.repository.AdequacaoElencoPosicaoRepository;

@Deprecated
@Service
public class CalcularAdequacaoElencoService {
	
	/*@Autowired
	private JogadorRepository jogadorRepository;*/
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;

	@Autowired
	private AdequacaoElencoPosicaoRepository adequacaoElencoPosicaoRepository;

	public void calcularAdequacaoElenco(Clube clube) {
		//List<Jogador> jogadores = jogadorRepository.findByClubeAndAposentado(clube, false);
		List<EscalacaoJogadorPosicao> escalacao = escalacaoJogadorPosicaoRepository.findByClube(clube);
		calcularAdequacaoElenco(clube, escalacao);
	}
	
	private void calcularAdequacaoElenco(Clube clube, List<EscalacaoJogadorPosicao> escalacao) {
		
		Map<EscalacaoPosicao, List<EscalacaoJogadorPosicao>> map = escalacao.stream()
				.collect(Collectors.groupingBy(EscalacaoJogadorPosicao::getEscalacaoPosicao));
		
		List<AdequacaoElencoPosicao> adequacoes = new ArrayList<AdequacaoElencoPosicao>();
		
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.GOLEIRO, map.get(EscalacaoPosicao.P_1).get(0)));
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.ZAGUEIRO, map.get(EscalacaoPosicao.P_3).get(0),
				map.get(EscalacaoPosicao.P_4).get(0)));
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.LATERAL, map.get(EscalacaoPosicao.P_2).get(0),
				map.get(EscalacaoPosicao.P_6).get(0)));
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.VOLANTE, map.get(EscalacaoPosicao.P_5).get(0),
				map.get(EscalacaoPosicao.P_8).get(0)));
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.MEIA, map.get(EscalacaoPosicao.P_7).get(0),
				map.get(EscalacaoPosicao.P_10).get(0)));
		adequacoes.add(calcularAdequacaoPosicao(clube, Posicao.ATACANTE, map.get(EscalacaoPosicao.P_9).get(0),
				map.get(EscalacaoPosicao.P_11).get(0)));
		
		adequacaoElencoPosicaoRepository.saveAll(adequacoes);
		
	}
	
	private AdequacaoElencoPosicao calcularAdequacaoPosicao(Clube clube, Posicao posicao, EscalacaoJogadorPosicao e1) {
		AdequacaoElencoPosicao adequacao = new AdequacaoElencoPosicao(clube, posicao);
		adequacao.setAdequacao((double) e1.getJogador().getForcaGeral() / clube.getForcaGeral());
		return adequacao;
	}
	
	private AdequacaoElencoPosicao calcularAdequacaoPosicao(Clube clube, Posicao posicao, EscalacaoJogadorPosicao e1,
			EscalacaoJogadorPosicao e2) {
		AdequacaoElencoPosicao adequacao = new AdequacaoElencoPosicao(clube, posicao);

		adequacao.setAdequacao((double) (e1.getJogador().getForcaGeral() + e2.getJogador().getForcaGeral())
				/ (clube.getForcaGeral() * 2));

		return adequacao;
	}
	
	/**
	 * 
	 * Recebe jogadores de determinada posicao
	 * 
	 * @param jogadores
	 */
	/*private void adequacaoJogadoresPosicao(List<Jogador> jogadores) {
		
	}*/
}
