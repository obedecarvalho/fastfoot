package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.FastfootApplication;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class GerenciarTransferenciasTemporadaService {
	
	//private static final Integer NUM_THREAD_ANALISAR_PROPOSTA = 2;
	
	//###	REPOSITORY	###
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private CalcularNecessidadeContratacaoClubeService calcularNecessidadeContratacaoClubeService;
	
	@Autowired
	private GerarPropostaTransferenciaService gerarPropostaTransferenciaService;
	
	@Autowired
	private AnalisarPropostaTransferenciaService analisarPropostaTransferenciaService;
	
	@Autowired
	private TemporadaService temporadaService;
	
	public void gerarTransferencias() {
		//if (semana.getNumero() == 0, 1, 2, 3) {
		List<Clube> clubes = clubeRepository.findAll(); 
		Temporada temporada = temporadaService.getTemporadaAtual();

		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitNanoTime();
		
		List<CompletableFuture<Boolean>> transferenciasFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		//Calcular necessidade contratacao
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(calcularNecessidadeContratacaoClubeService.calcularNecessidadeContratacao(clubes.subList(i * offset, clubes.size())));
			} else {
				transferenciasFuture.add(calcularNecessidadeContratacaoClubeService.calcularNecessidadeContratacao(clubes.subList(i * offset, (i+1) * offset)));
			}
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#calcularNecessidadeContratacao:" + (fim - inicio));

		transferenciasFuture.clear();
		
		inicio = stopWatch.getSplitNanoTime();
		
		List<NecessidadeContratacaoClube> necessidadeContratacao = necessidadeContratacaoClubeRepository
				.findByTemporadaAndNecessidadeSatisfeitaAndNecessidadePrioritaria(temporada, false, true);
		
		Map<Clube, List<NecessidadeContratacaoClube>> necessidadeContratacaoClube = necessidadeContratacao
				.stream().collect(Collectors.groupingBy(NecessidadeContratacaoClube::getClube));
		
		clubes = new ArrayList<Clube>(necessidadeContratacaoClube.keySet());
		offset = clubes.size() / FastfootApplication.NUM_THREAD;
		//System.err.println("#necessidadeContratacaoClube" + clubes.size());
		
		//Fazer propostas transferencia
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(gerarPropostaTransferenciaService.gerarPropostaTransferencia(temporada,
						clubes.subList(i * offset, clubes.size()), necessidadeContratacaoClube));
			} else {
				transferenciasFuture.add(gerarPropostaTransferenciaService.gerarPropostaTransferencia(temporada,
						clubes.subList(i * offset, (i + 1) * offset), necessidadeContratacaoClube));
			}
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#gerarPropostaTransferencia:" + (fim - inicio));

		transferenciasFuture.clear();
		
		inicio = stopWatch.getSplitNanoTime();
		
		//
		List<PropostaTransferenciaJogador> propostas = propostaTransferenciaJogadorRepository
				.findByTemporadaAndPropostaAceitaIsNull(temporada);
		
		Map<Clube, List<PropostaTransferenciaJogador>> propostasClube = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getClubeOrigem));
		
		clubes = new ArrayList<Clube>(propostasClube.keySet());
		//offset = clubes.size() / NUM_THREAD_ANALISAR_PROPOSTA;//offset = clubes.size() / FastfootApplication.NUM_THREAD;
		//System.err.println("#propostasClube" + clubes.size());
		Set<Clube> clubesRefazerEscalacao = Collections.synchronizedSet(new HashSet<Clube>());
		
		//Analisar propostas transferencia
		analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada, clubes, propostasClube,
				clubesRefazerEscalacao);
		
		//Analisar propostas transferencia
		/*for (int i = 0; i < NUM_THREAD_ANALISAR_PROPOSTA; i++) {//for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == NUM_THREAD_ANALISAR_PROPOSTA) {//if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(analisarPropostaTransferenciaService
						.analisarPropostaTransferencia(temporada, clubes.subList(i * offset, clubes.size()), propostasClube, clubesRefazerEscalacao));
			} else {
				transferenciasFuture.add(analisarPropostaTransferenciaService
						.analisarPropostaTransferencia(temporada, clubes.subList(i * offset, (i + 1) * offset), propostasClube, clubesRefazerEscalacao));
			}
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();*/
		//
		
		//System.err.println(clubesRefazerEscalacao);//TODO:ajustar escalação: apenas caso de semanas nao contempladas em SemanaService
		
		stopWatch.split();
		fim = stopWatch.getSplitNanoTime();
		mensagens.add("#analisarPropostaTransferencia:" + (fim - inicio));
		
		stopWatch.stop();
		
		mensagens.add("#tempoTotal:" + stopWatch.getNanoTime());
		
		System.err.println(mensagens);
		
		//}
	}

}
