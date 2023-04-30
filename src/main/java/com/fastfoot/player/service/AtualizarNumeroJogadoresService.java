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
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AtualizarNumeroJogadoresService {
	
	/*private static final Integer[] NUM_ZAG = new Integer[] {3, 4, 13, 14, 24, 33, 43, 44, 53, 54};
	
	private static final Integer[] NUM_LAT = new Integer[] {2, 6, 16, 22, 26, 32, 36, 42, 46, 52};
	
	private static final Integer[] NUM_VOL = new Integer[] {8, 5, 18, 15, 28, 25, 38, 35, 48, 55};
	
	private static final Integer[] NUM_MEI = new Integer[] {7, 10, 17, 20, 27, 30, 37, 40, 47, 50};
	
	private static final Integer[] NUM_ATA = new Integer[] {9, 11, 19, 21, 29, 31, 39, 41, 49, 51};*/
	
	protected static final Integer[] BASE_NUM_ZAG = new Integer[] {3, 4};
	
	protected static final Integer[] BASE_NUM_LAT = new Integer[] {2, 6};
	
	protected static final Integer[] BASE_NUM_VOL = new Integer[] {8, 5};
	
	protected static final Integer[] BASE_NUM_MEI = new Integer[] {7, 10};
	
	protected static final Integer[] BASE_NUM_ATA = new Integer[] {9, 11};
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	public void gerarNumeroJogadores(List<Jogador> jogadores) {
		
		List<Jogador> jogPosicao = null;
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(i * 11 + 1);
		}
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_ZAG);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_LAT);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_VOL);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_MEI);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_ATA);
		
	}
	
	private void gerarNumeroJogadores(List<Jogador> jogadores, Integer[] n) {
		Integer[] numeros = gerarNumeros(jogadores.size(), n);
		for (int i = 0; i < jogadores.size(); i++) {
			jogadores.get(i).setNumero(numeros[i]);
		}
	}
	
	/*private void atualizarNumeroJogadoresClube(List<Jogador> jogadores) {
		
		List<Jogador> jogPosicao = null;
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(i * 11 + 1);
		}
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_ZAG);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_LAT);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_VOL);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_MEI);
		
		gerarNumeroJogadores(jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao()))
				.sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList()), BASE_NUM_ATA);
	}*/
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarNumeroJogadores(Liga liga, boolean primeirosIds) {

		List<Jogador> jogadores;
		
		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
		}
		
		Map<Clube, List<Jogador>> jogClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		for (Clube clube : jogClube.keySet()) {
			gerarNumeroJogadores(jogClube.get(clube));
		}
		
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarNumeroJogadores(List<Clube> clubes) {
		List<Jogador> jogadoresAtualizar = new ArrayList<Jogador>();
		
		for (Clube c : clubes) {
			atualizarNumeroJogadores(c, jogadoresAtualizar);
		}
		
		jogadorRepository.saveAll(jogadoresAtualizar);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/

	/*private void atualizarNumeroJogadores(Clube c, List<Jogador> jogadoresAtualizar) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);
		
		List<Jogador> jogPosicao = null;
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());		
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(i * 11 + 1);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		//X3, X4
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_ZAG[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		//X2, X6
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_LAT[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		//X8, X5
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_VOL[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		//X7, X0
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_MEI[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogPosicao = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparatorForcaGeral()).collect(Collectors.toList());
		//X9, X1
		for (int i = 0; i < jogPosicao.size(); i++) {
			jogPosicao.get(i).setNumero(NUM_ATA[i]);
			//System.err.println("\t->" + jogPosicao.get(i).getPosicao().name() + ":" + jogPosicao.get(i).getNumero() + ", J:" + jogPosicao.get(i).getNome());
		}
		
		jogadoresAtualizar.addAll(jogadores);
	}*/
	
	public void atualizarNumeroJogadores(Map<Clube, List<Jogador>> jogadoresAtualizar, Map<Clube, List<Jogador>> jogadoresNroDesconsiderar) {
		
		List<Jogador> jogadoresNaoAtualizar;
		List<Integer> numeroJaUtilizadosClube;
		List<Jogador> jogadoresNroDesconsiderarClube;
		
		for (Clube c : jogadoresAtualizar.keySet()) {
			jogadoresNaoAtualizar = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);//TODO: otimizar
			
			
			numeroJaUtilizadosClube = jogadoresNaoAtualizar.stream().map(Jogador::getNumero).collect(Collectors.toList());
			
			if (jogadoresNroDesconsiderar != null) {
				jogadoresNroDesconsiderarClube = jogadoresNroDesconsiderar.get(c);
				
				numeroJaUtilizadosClube.removeAll(jogadoresNroDesconsiderarClube.stream().map(Jogador::getNumero).collect(Collectors.toList()));
			}
			
			
			atualizarNumeroJogadoresClube(jogadoresAtualizar.get(c), jogadoresNaoAtualizar, numeroJaUtilizadosClube);
		}
	}
	
	/*protected Integer[] getNumerosPorPosicao(Posicao posicao) {
		if (Posicao.ZAGUEIRO.equals(posicao)) return NUM_ZAG;
		if (Posicao.LATERAL.equals(posicao)) return NUM_LAT;
		if (Posicao.VOLANTE.equals(posicao)) return NUM_VOL;
		if (Posicao.MEIA.equals(posicao)) return NUM_MEI;
		if (Posicao.ATACANTE.equals(posicao)) return NUM_ATA;
		return null;
	}*/
	
	protected void atualizarNumeroJogadoresClube(List<Jogador> jogadoresAtualizar,
			List<Jogador> jogadoresNaoAtualizar, List<Integer> numeroJaUtilizadosClube) {
		
		atualizarNumeroJogadoresClubeGoleiro(
				jogadoresAtualizar.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).count());

		atualizarNumeroJogadoresClubePosicao(
				jogadoresAtualizar.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube, BASE_NUM_ZAG,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).count());

		atualizarNumeroJogadoresClubePosicao(
				jogadoresAtualizar.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube, BASE_NUM_LAT,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).count());

		atualizarNumeroJogadoresClubePosicao(
				jogadoresAtualizar.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube, BASE_NUM_VOL,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).count());

		atualizarNumeroJogadoresClubePosicao(
				jogadoresAtualizar.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube, BASE_NUM_MEI,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).count());

		atualizarNumeroJogadoresClubePosicao(
				jogadoresAtualizar.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao()))
						.collect(Collectors.toList()),
				numeroJaUtilizadosClube, BASE_NUM_ATA,
				(int) jogadoresNaoAtualizar.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).count());
		
	}
	
	protected void atualizarNumeroJogadoresClubeGoleiro(List<Jogador> jogadoresAtualizar,
			List<Integer> numeroJaUtilizadosClube, Integer qtdeJogadoresExistente) {
		
		if (jogadoresAtualizar.isEmpty()) return;
		
		List<Integer> numeros = new ArrayList<Integer>();
		
		for (int i = 0; i < (jogadoresAtualizar.size() + qtdeJogadoresExistente); i++) {
			numeros.add(i * 11 + 1);
		}
		
		numeros.removeAll(numeroJaUtilizadosClube);
		
		for (int i = 0; i < jogadoresAtualizar.size(); i++) {
			jogadoresAtualizar.get(i).setNumero(numeros.get(i));
		}	
		
	}
	
	protected void atualizarNumeroJogadoresClubePosicao(List<Jogador> jogadoresAtualizar,
			List<Integer> numeroJaUtilizadosClube, Integer[] n, Integer qtdeJogadoresExistente) {

		if (jogadoresAtualizar.isEmpty()) return;
		
		List<Integer> numeros = new ArrayList<Integer>(Arrays.asList(gerarNumeros(jogadoresAtualizar.size() + qtdeJogadoresExistente, n)));
		
		numeros.removeAll(numeroJaUtilizadosClube);
		
		for (int i = 0; i < jogadoresAtualizar.size(); i++) {
			jogadoresAtualizar.get(i).setNumero(numeros.get(i));
		}		
	}
	
	/*protected void atualizarNumeroJogadoresClube(List<Jogador> jogadoresAtualizar, List<Jogador> jogadoresNaoAtualizar) {
		
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
							/*.filter(j -> j.getPosicao().equals(jogador.getPosicao()))* /
							.map(j -> j.getNumero()).collect(Collectors.toList()));
			
			if (numerosDisponiveis.isEmpty()) {
				throw new RuntimeException("Erro ao atualizarNumeroJogadoresClube");
			} else {
				jogador.setNumero(numerosDisponiveis.get(0));
				jogadoresNaoAtualizar.add(jogador);
			}
		}
	}*/
	
	protected Integer[] gerarNumeros(int qtde, Integer[] n) {
		
		Integer[] numerosGerados = new Integer[qtde];
		
		int passo = 10, numeroGerado, qtdeGerada = 0;
		
		//for (int i = 0; i < qtde; i++) {
		int i = 0;
		while (qtdeGerada < qtde) {
			
			if (i % 2 == 0) {
				numeroGerado = n[0] + passo * (i / 2);
			} else {
				numeroGerado = n[1] + passo * (i / 2);
			}
			
			if (numeroGerado % 11 != 1) {
				numerosGerados[qtdeGerada] = numeroGerado;
				qtdeGerada++;
			}
			
			i++;
		}
		
		return numerosGerados;
	}
}
