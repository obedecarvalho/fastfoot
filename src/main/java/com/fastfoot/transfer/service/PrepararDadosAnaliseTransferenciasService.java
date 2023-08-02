package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
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
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		List<Jogador> jogadores;

		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16);
		} else {
			jogadores = jogadorRepository.findByLigaClubeAndStatusJogadorFetchHabilidades(liga, StatusJogador.ATIVO,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32);
		}
		
		Map<Clube, List<Jogador>> jogadoresClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#jogadorRepository:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacaoEDisponivelNegociacao(temporada,
				jogadoresClube, necessidadeContratacaoClubes, disponivelNegociacao);
		
		//Finalizar bloco e já iniciar outro
		stopWatch.split();
		mensagens.add("\t#calcularNecessidadeContratacaoEDisponivelNegociacao:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		//Finalizar
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
		
		//System.err.println(mensagens);
		
		return CompletableFuture.completedFuture(new PrepararDadosAnaliseTransferenciasReturn(necessidadeContratacaoClubes, disponivelNegociacao));
		
	}

	public class PrepararDadosAnaliseTransferenciasReturn {
		
		private List<NecessidadeContratacaoClube> necessidadeContratacaoClubes;
		
		private List<DisponivelNegociacao> disponivelNegociacao;

		public List<NecessidadeContratacaoClube> getNecessidadeContratacaoClubes() {
			return necessidadeContratacaoClubes;
		}

		public List<DisponivelNegociacao> getDisponivelNegociacao() {
			return disponivelNegociacao;
		}

		public PrepararDadosAnaliseTransferenciasReturn(List<NecessidadeContratacaoClube> necessidadeContratacaoClubes,
				List<DisponivelNegociacao> disponivelNegociacao) {
			super();
			this.necessidadeContratacaoClubes = necessidadeContratacaoClubes;
			this.disponivelNegociacao = disponivelNegociacao;
		}

	}
}
