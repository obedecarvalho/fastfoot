package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AtualizarNumeroJogadoresService {
	
	private static final Integer[] NUM_ZAG = new Integer[] {3, 4, 13, 14, 24, 33, 43, 44, 53, 54};
	
	private static final Integer[] NUM_LAT = new Integer[] {2, 6, 16, 22, 26, 32, 36, 42, 46, 52};
	
	private static final Integer[] NUM_VOL = new Integer[] {8, 5, 18, 15, 28, 25, 38, 35, 48, 55};
	
	private static final Integer[] NUM_MEI = new Integer[] {7, 10, 17, 20, 27, 30, 37, 40, 47, 50};
	
	private static final Integer[] NUM_ATA = new Integer[] {9, 11, 19, 21, 29, 31, 39, 41, 49, 51};
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarNumeroJogadores(List<Clube> clubes) {
		List<Jogador> jogadoresAtualizar = new ArrayList<Jogador>();
		
		for (Clube c : clubes) {
			atualizarNumeroJogadores(c, jogadoresAtualizar);
		}
		
		jogadorRepository.saveAll(jogadoresAtualizar);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	public void atualizarNumeroJogadores(Clube c, List<Jogador> jogadoresAtualizar) {//TODO: tratar qdo numero de jog por posicao for maior que 10
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);//TODO: otimizar
		
		List<Jogador> jogPosicao = null;
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());		
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(i * 11 + 1);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());
		//X3, X4
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_ZAG[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());
		//X2, X6
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_LAT[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());
		//X8, X5
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_VOL[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());
		//X7, X0
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_MEI[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparator()).collect(Collectors.toList());
		//X9, X1
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_ATA[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogadoresAtualizar.addAll(jogadores);
	}
	
	public void atualizarNumeroJogadores(Map<Clube, List<Jogador>> jogadoresAtualizar) {
		
		List<Jogador> jogadoresNaoAtualizar;
		
		for (Clube c : jogadoresAtualizar.keySet()) {
			jogadoresNaoAtualizar = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);
			atualizarNumeroJogadoresClube(jogadoresAtualizar.get(c), jogadoresNaoAtualizar);
		}
	}
	
	protected Integer[] getNumerosPorPosicao(Posicao posicao) {
		if (Posicao.ZAGUEIRO.equals(posicao)) return NUM_ZAG;
		if (Posicao.LATERAL.equals(posicao)) return NUM_LAT;
		if (Posicao.VOLANTE.equals(posicao)) return NUM_VOL;
		if (Posicao.MEIA.equals(posicao)) return NUM_MEI;
		if (Posicao.ATACANTE.equals(posicao)) return NUM_ATA;
		return null;
	}
	
	protected void atualizarNumeroJogadoresClube(List<Jogador> jogadoresAtualizar, List<Jogador> jogadoresNaoAtualizar) {
		
		List<Integer> numerosDisponiveis;
		
		for (Jogador jogador : jogadoresAtualizar) {
			
			if (jogador.getPosicao().isGoleiro()) {
				numerosDisponiveis = new ArrayList<Integer>();
				for (int i = 0; i < 10; i++) {
					numerosDisponiveis.add(i * 11 + 1);
				}
			} else {
				numerosDisponiveis = new ArrayList<Integer>(Arrays.asList(getNumerosPorPosicao(jogador.getPosicao())));
			}
			
			numerosDisponiveis
					.removeAll(jogadoresNaoAtualizar.stream()
							/*.filter(j -> j.getPosicao().equals(jogador.getPosicao()))*/
							.map(j -> j.getNumero()).collect(Collectors.toList()));
			
			if (numerosDisponiveis.isEmpty()) {
				throw new RuntimeException("Erro ao atualizarNumeroJogadoresClube");
			} else {
				jogador.setNumero(numerosDisponiveis.get(0));
				jogadoresNaoAtualizar.add(jogador);
			}
		}
	}
}
