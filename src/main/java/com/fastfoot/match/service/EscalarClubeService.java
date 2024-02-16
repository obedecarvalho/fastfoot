package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.StatusEscalacaoJogador;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.service.util.ArraysUtil;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class EscalarClubeService {
	
	protected void escalarPosicao(Posicao posicao, EscalacaoPosicao ep1, EscalacaoPosicao ep2,
			List<Jogador> jogadoresDisponiveis, Boolean amistoso, EscalacaoClube escalacaoClubePartida, Clube clube,
			List<EscalacaoJogadorPosicao> escalacao, List<EscalacaoPosicao> posicoesVazias,
			Map<Posicao, List<Jogador>> jogadoresSuplentes) {

		List<Jogador> jogPos = jogadoresDisponiveis.stream().filter(j -> posicao.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeralEnergia()).collect(Collectors.toList());
		
		if (amistoso) {
			Collections.sort(jogPos, JogadorFactory.getComparatorForcaGeral());
			jogPos = ArraysUtil.deslocarListNPosicoes(jogPos, 2);
		}
		
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, ep1, jogPos.get(0), true,
					StatusEscalacaoJogador.TITULAR));
		} else {
			posicoesVazias.add(ep1);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, ep2, jogPos.get(1), true,
					StatusEscalacaoJogador.TITULAR));
		} else {
			posicoesVazias.add(ep2);
		}
		
		if (jogPos.size() > 2) {
			jogadoresSuplentes.put(posicao, jogPos.subList(2, jogPos.size()));
		}
	}
	
	protected void escalarPosicaoGoleiro(List<Jogador> jogadoresDisponiveis, Boolean amistoso,
			EscalacaoClube escalacaoClubePartida, Clube clube, List<EscalacaoJogadorPosicao> escalacao,
			List<EscalacaoPosicao> posicoesVazias, Map<Posicao, List<Jogador>> jogadoresSuplentes) {

		List<Jogador> jogPos = jogadoresDisponiveis.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeralEnergia()).collect(Collectors.toList());
		
		if (amistoso) {
			Collections.sort(jogPos, JogadorFactory.getComparatorForcaGeral());
			jogPos = ArraysUtil.deslocarListNPosicoes(jogPos, 1);
		}
		
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_GOL,
					jogPos.get(0), true, StatusEscalacaoJogador.TITULAR));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_GOL);
		}
		
		if (jogPos.size() > 1) {
			jogadoresSuplentes.put(Posicao.GOLEIRO, jogPos.subList(1, jogPos.size()));
		}
	}
	
	public EscalacaoClube gerarEscalacaoInicial(Clube clube, List<Jogador> jogadores, PartidaResultadoJogavel partida) {
		
		EscalacaoClube escalacaoClubePartida = new EscalacaoClube(clube, partida, true);

		List <EscalacaoJogadorPosicao> escalacao = new ArrayList<EscalacaoJogadorPosicao>();
		
		List<EscalacaoPosicao> posicoesVazias = new ArrayList<EscalacaoPosicao>();
		Map<Posicao, List<Jogador>> jogadoresSuplentes = new HashMap<Posicao, List<Jogador>>();
		
		List<Jogador> jogadoresDisponiveis = jogadores.stream().filter(
				j -> j.getJogadorEnergia().getEnergiaAtual() > Constantes.ENERGIA_MINIMA_JOGAR)
				.collect(Collectors.toList());
		
		//Gol
		escalarPosicaoGoleiro(jogadoresDisponiveis, partida.isAmistoso(), escalacaoClubePartida, clube, escalacao,
				posicoesVazias, jogadoresSuplentes);
		
		//Zag
		escalarPosicao(Posicao.ZAGUEIRO, EscalacaoPosicao.P_ZD, EscalacaoPosicao.P_ZE, jogadoresDisponiveis,
				partida.isAmistoso(), escalacaoClubePartida, clube, escalacao, posicoesVazias, jogadoresSuplentes);
		
		//Lateral
		escalarPosicao(Posicao.LATERAL, EscalacaoPosicao.P_LD, EscalacaoPosicao.P_LE, jogadoresDisponiveis,
				partida.isAmistoso(), escalacaoClubePartida, clube, escalacao, posicoesVazias, jogadoresSuplentes);
		
		//Vol
		escalarPosicao(Posicao.VOLANTE, EscalacaoPosicao.P_VD, EscalacaoPosicao.P_VE, jogadoresDisponiveis,
				partida.isAmistoso(), escalacaoClubePartida, clube, escalacao, posicoesVazias, jogadoresSuplentes);
		
		//Meia
		escalarPosicao(Posicao.MEIA, EscalacaoPosicao.P_MD, EscalacaoPosicao.P_ME, jogadoresDisponiveis,
				partida.isAmistoso(), escalacaoClubePartida, clube, escalacao, posicoesVazias, jogadoresSuplentes);
		
		//Ata
		escalarPosicao(Posicao.ATACANTE, EscalacaoPosicao.P_AD, EscalacaoPosicao.P_AE, jogadoresDisponiveis,
				partida.isAmistoso(), escalacaoClubePartida, clube, escalacao, posicoesVazias, jogadoresSuplentes);
		
		posicoesVazias.add(EscalacaoPosicao.P_RES_1);
		posicoesVazias.add(EscalacaoPosicao.P_RES_2);
		posicoesVazias.add(EscalacaoPosicao.P_RES_3);
		posicoesVazias.add(EscalacaoPosicao.P_RES_4);
		posicoesVazias.add(EscalacaoPosicao.P_RES_5);
		posicoesVazias.add(EscalacaoPosicao.P_RES_6);
		
		posicoesVazias.add(EscalacaoPosicao.P_RES_7);
		posicoesVazias.add(EscalacaoPosicao.P_RES_8);
		posicoesVazias.add(EscalacaoPosicao.P_RES_9);
		posicoesVazias.add(EscalacaoPosicao.P_RES_10);
		posicoesVazias.add(EscalacaoPosicao.P_RES_11);
		posicoesVazias.add(EscalacaoPosicao.P_RES_12);
		
		Collections.sort(posicoesVazias);
		
		List<Jogador> possiveisJogadores = null;
		for (EscalacaoPosicao ep : posicoesVazias) {
			boolean naoPreenchido = true;
			int i = 0;
			
			while (naoPreenchido && i < ep.getOrdemPosicaoParaEscalar().length) {
				possiveisJogadores = jogadoresSuplentes.get(ep.getOrdemPosicaoParaEscalar()[i]);
				if (!ValidatorUtil.isEmpty(possiveisJogadores)) {
					escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, ep,
							possiveisJogadores.get(0), true,
							ep.isTitular() ? StatusEscalacaoJogador.TITULAR : StatusEscalacaoJogador.RESERVA));
					possiveisJogadores.remove(0);
					naoPreenchido = false;
				}
				i++;
			}
			
			if (naoPreenchido && ep.isTitular()) throw new RuntimeException("Erro ao escalar clube:" + clube.getId());
		}
		
		escalacaoClubePartida.setListEscalacaoJogadorPosicao(escalacao);
		
		return escalacaoClubePartida;
	}

}
