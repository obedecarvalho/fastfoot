package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AtualizarNumeroJogadoresService {
	
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
	
	/*
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarNumeroJogadores(LigaJogo liga, boolean primeirosIds) {

		List<Jogador> jogadores;
		
		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}
		
		Map<Clube, List<Jogador>> jogClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		for (Clube clube : jogClube.keySet()) {
			gerarNumeroJogadores(jogClube.get(clube));
		}
		
		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	*/

	/*
	public void atualizarNumeroJogadores(Map<Clube, List<Jogador>> jogadoresAtualizar, Map<Clube, List<Jogador>> jogadoresNroDesconsiderar) {
		
		List<Jogador> jogadoresNaoAtualizar;
		List<Integer> numeroJaUtilizadosClube;
		List<Jogador> jogadoresNroDesconsiderarClube;
		
		for (Clube c : jogadoresAtualizar.keySet()) {
			jogadoresNaoAtualizar = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);
			
			
			numeroJaUtilizadosClube = jogadoresNaoAtualizar.stream().map(Jogador::getNumero).collect(Collectors.toList());
			
			if (jogadoresNroDesconsiderar != null) {
				jogadoresNroDesconsiderarClube = jogadoresNroDesconsiderar.get(c);
				
				numeroJaUtilizadosClube.removeAll(jogadoresNroDesconsiderarClube.stream().map(Jogador::getNumero).collect(Collectors.toList()));
			}
			
			
			atualizarNumeroJogadoresClube(jogadoresAtualizar.get(c), jogadoresNaoAtualizar, numeroJaUtilizadosClube);
		}
	}
	*/
	
	public void atualizarNumeroJogadores(Map<Clube, List<Jogador>> jogadoresAtualizar,
			Map<Clube, List<Jogador>> jogadoresNroDesconsiderar) {

		List<Jogador> jogs = jogadorRepository.findByClubesAndStatusJogador(jogadoresAtualizar.keySet(),
				StatusJogador.ATIVO);

		Map<Clube, List<Jogador>> jogadoresNaoAtualizarPorClube = jogs.stream()
				.collect(Collectors.groupingBy(j -> j.getClube()));

		List<Jogador> jogadoresNaoAtualizar;
		List<Integer> numeroJaUtilizadosClube;
		List<Jogador> jogadoresNroDesconsiderarClube;

		for (Clube c : jogadoresAtualizar.keySet()) {
			jogadoresNaoAtualizar = jogadoresNaoAtualizarPorClube.get(c);

			numeroJaUtilizadosClube = jogadoresNaoAtualizar.stream().map(Jogador::getNumero)
					.collect(Collectors.toList());

			if (jogadoresNroDesconsiderar != null) {
				jogadoresNroDesconsiderarClube = jogadoresNroDesconsiderar.get(c);

				numeroJaUtilizadosClube.removeAll(
						jogadoresNroDesconsiderarClube.stream().map(Jogador::getNumero).collect(Collectors.toList()));
			}

			atualizarNumeroJogadoresClube(jogadoresAtualizar.get(c), jogadoresNaoAtualizar, numeroJaUtilizadosClube);
		}
	}
	
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
	
	protected Integer[] gerarNumeros(int qtde, Integer[] n) {
		
		Integer[] numerosGerados = new Integer[qtde];
		
		int passo = 10, numeroGerado, qtdeGerada = 0;
		
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
