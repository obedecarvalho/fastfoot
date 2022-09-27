package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
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
	
	private static final int[] NUM_ZAG = new int[] {3, 4, 13, 14, 24, 33, 43, 44, 53, 54};
	
	private static final int[] NUM_LAT = new int[] {2, 6, 16, 22, 26, 32, 36, 42, 46, 52};
	
	private static final int[] NUM_VOL = new int[] {8, 5, 18, 15, 28, 25, 38, 35, 48, 55};
	
	private static final int[] NUM_MEI = new int[] {7, 10, 17, 20, 27, 30, 37, 40, 47, 50};
	
	private static final int[] NUM_ATA = new int[] {9, 11, 19, 21, 29, 31, 39, 41, 49, 51};
	
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
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);
		
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
}
