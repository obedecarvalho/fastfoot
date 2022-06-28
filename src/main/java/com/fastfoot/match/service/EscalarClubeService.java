package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class EscalarClubeService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> escalarClubes(List<Clube> clubes) {
		for (Clube c : clubes) {
			escalarClube(c);
		}
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	public void escalarClube(Clube clube) {
		
		List<EscalacaoJogadorPosicao> escalacao = escalacaoJogadorPosicaoRepository.findByClube(clube);
		List<Jogador> jogadores = jogadorRepository.findByClubeAndAposentado(clube, Boolean.FALSE);
		
		if (ValidatorUtil.isEmpty(escalacao)) {
			escalacao = gerarEscalacaoInicial(clube, jogadores);
		} else {
			atualizarEscalacao(clube, escalacao, jogadores);
		}
		
		escalacaoJogadorPosicaoRepository.saveAll(escalacao);//TODO: fazer apenas um para todos os Clubes??
	}
	
	private Comparator<Jogador> getComparator() {
		 Comparator<Jogador> comparator = new Comparator<Jogador>() {

			@Override
			public int compare(Jogador o1, Jogador o2) {
				//return o1.getForcaGeral().compareTo(o2.getForcaGeral());
				return o2.getForcaGeral().compareTo(o1.getForcaGeral());//reverse
			}
		};
		return comparator;
	}
	
	private void atualizarEscalacao(Clube clube, List<EscalacaoJogadorPosicao> escalacao, List<Jogador> jogadores) {
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> escalacaoMap = escalacao.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));
		
		Comparator<Jogador> comparator = getComparator();
		
		List<Jogador> jogPos = null;
		
		EscalacaoJogadorPosicao ejp = null;
		
		//Gol
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());

		ejp = escalacaoMap.get(EscalacaoPosicao.P_1);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_12);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		//Zag
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_3);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_4);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_13);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Lat
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_2);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_6);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_16);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Vol
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_5);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_8);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_15);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Mei
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_7);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_10);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_17);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Ata
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_9);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_11);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_19);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
	}
	
	private List<EscalacaoJogadorPosicao> gerarEscalacaoInicial(Clube clube, List<Jogador> jogadores) {
		
		Comparator<Jogador> comparator = getComparator();
		
		List <EscalacaoJogadorPosicao> escalacao = new ArrayList<EscalacaoJogadorPosicao>();
		
		List<Jogador> jogPos = null;
		
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());

		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_1, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_12, jogPos.get(1)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_3, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_4, jogPos.get(1)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_13, jogPos.get(2)));
		
		
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_2, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_6, jogPos.get(1)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_16, jogPos.get(2)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_5, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_8, jogPos.get(1)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_15, jogPos.get(2)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_7, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_10, jogPos.get(1)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_17, jogPos.get(2)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_9, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_11, jogPos.get(1)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_19, jogPos.get(2)));
		
		return escalacao;
	}
}
