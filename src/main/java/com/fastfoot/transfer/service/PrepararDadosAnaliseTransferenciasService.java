package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.PrepararDadosAnaliseTransferenciasReturn;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;

@Service
public class PrepararDadosAnaliseTransferenciasService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private AvaliarNecessidadeContratacaoClubeService avaliarNecessidadeContratacaoClubeService;
	
	@Async("defaultExecutor")
	public CompletableFuture<PrepararDadosAnaliseTransferenciasReturn> prepararDadosAnaliseTransferencias(Temporada temporada, Liga liga, boolean primeirosIds) {
		
		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
		}
		
		Map<Clube, List<Jogador>> jogadoresClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacaoEDisponivelNegociacao(temporada,
				jogadoresClube, necessidadeContratacaoClubes, disponivelNegociacao);
		
		return CompletableFuture.completedFuture(new PrepararDadosAnaliseTransferenciasReturn(necessidadeContratacaoClubes, disponivelNegociacao));
		
	}

}
