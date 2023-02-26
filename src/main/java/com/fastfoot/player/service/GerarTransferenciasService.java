package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.fastfoot.club.service.CalcularPrevisaoReceitaIngressosService;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.transfer.model.ClubeSaldo;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;
import com.fastfoot.transfer.service.AnalisarPropostaTransferenciaService;
import com.fastfoot.transfer.service.AvaliarNecessidadeContratacaoClubeService;
import com.fastfoot.transfer.service.ProporTransferenciaService;

@Service
public class GerarTransferenciasService {
	
	private static final Integer NUM_THREAD_ANALISAR_PROPOSTA = 3;
	
	//###	REPOSITORY	###
	
	/*@Autowired
	private ClubeRepository clubeRepository;*/
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	//###	SERVICE	###
	
	/*@Autowired
	private TemporadaCRUDService temporadaService;*/
	
	@Autowired
	private AvaliarNecessidadeContratacaoClubeService avaliarNecessidadeContratacaoClubeService;
	
	@Autowired
	private ProporTransferenciaService proporTransferenciaService;
	
	@Autowired
	private AnalisarPropostaTransferenciaService analisarPropostaTransferenciaService;
	
	@Autowired
	private CalcularPrevisaoReceitaIngressosService calcularPrevisaoReceitaIngressosService;
	
	/*@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;*/
	
	public void gerarTransferencias(Temporada temporada) {
		//Temporada temporada = temporadaService.getTemporadaAtual();
		//List<Clube> clubes = clubeRepository.findAll();
		gerarTransferencias(temporada, null);
		//atualizarNumeroJogadores();//TODO: tem necessidade?
	}
	
	private void gerarTransferencias(Temporada temporada, List<Clube> clubes) {
		//if (semana.getNumero() == 0, 1, 2, 3) {		

		//int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		List<CompletableFuture<Boolean>> transferenciasFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		//Calcular necessidade contratacao
		/*for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(avaliarNecessidadeContratacaoClubeService
						.calcularNecessidadeContratacao(clubes.subList(i * offset, clubes.size())));
			} else {
				transferenciasFuture.add(avaliarNecessidadeContratacaoClubeService
						.calcularNecessidadeContratacao(clubes.subList(i * offset, (i + 1) * offset)));
			}
		}*/
		
		for (Liga liga : Liga.getAll()) {
			transferenciasFuture
					.add(avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacao(liga, true));
			transferenciasFuture
					.add(avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacao(liga, false));
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#calcularNecessidadeContratacao:" + (fim - inicio));

		transferenciasFuture.clear();
		
		inicio = stopWatch.getSplitTime();
		
		List<NecessidadeContratacaoClube> necessidadeContratacao = necessidadeContratacaoClubeRepository
				.findByTemporadaAndNecessidadeSatisfeitaAndNecessidadePrioritaria(temporada, false, true);
		
		Map<Integer, Map<Clube, List<NecessidadeContratacaoClube>>> necessidadeContratacaoClubeX = necessidadeContratacao
				.stream().collect(Collectors.groupingBy(nc -> nc.getClube().getId() % FastfootApplication.NUM_THREAD,
						Collectors.groupingBy(NecessidadeContratacaoClube::getClube)));
		
		//Fazer propostas transferencia
		//for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
		for (Integer i : necessidadeContratacaoClubeX.keySet()) {
			transferenciasFuture.add(proporTransferenciaService.gerarPropostaTransferencia(temporada,
					necessidadeContratacaoClubeX.get(i)));
		}
		
		/*Map<Clube, List<NecessidadeContratacaoClube>> necessidadeContratacaoClube = necessidadeContratacao
				.stream().collect(Collectors.groupingBy(NecessidadeContratacaoClube::getClube));
		
		clubes = new ArrayList<Clube>(necessidadeContratacaoClube.keySet());
		offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		//Fazer propostas transferencia
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(proporTransferenciaService.gerarPropostaTransferencia(temporada,
						clubes.subList(i * offset, clubes.size()), necessidadeContratacaoClube));
			} else {
				transferenciasFuture.add(proporTransferenciaService.gerarPropostaTransferencia(temporada,
						clubes.subList(i * offset, (i + 1) * offset), necessidadeContratacaoClube));
			}
		}*/
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#gerarPropostaTransferencia:" + (fim - inicio));

		transferenciasFuture.clear();
		
		inicio = stopWatch.getSplitTime();
		
		//
		List<PropostaTransferenciaJogador> propostas = propostaTransferenciaJogadorRepository
				.findByTemporadaAndPropostaAceitaIsNull(temporada);

		Map<Integer, Map<Clube, List<PropostaTransferenciaJogador>>> propostasClubeX = propostas.stream()
				.collect(Collectors.groupingBy(p -> p.getClubeOrigem().getId() % NUM_THREAD_ANALISAR_PROPOSTA,
						Collectors.groupingBy(PropostaTransferenciaJogador::getClubeOrigem)));
		
		Set<Clube> clubesRefazerEscalacaoX = Collections.synchronizedSet(new HashSet<Clube>());
		
		double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * 25;
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository.findSaldoProjetadoPorClube(porcSalarioAnual);//movimentacaoFinanceiraRepository.findSaldoPorClube();
		
		Map<Clube, ClubeSaldo> clubesSaldo = new HashMap<Clube, ClubeSaldo>();//Collections.synchronizedList(new ArrayList<ClubeSaldo>());
		
		ClubeSaldo clubeSaldo = null;
		
		for (Map<String, Object> sc : saldoClubes) {
			clubeSaldo = new ClubeSaldo();
			clubeSaldo.setClube(new Clube((Integer) sc.get("id_clube")));
			clubeSaldo.setSaldo((Double) sc.get("saldo"));
			clubeSaldo.setPrevisaoSaidaSalarios((Double) sc.get("salarios_projetado"));
			clubeSaldo.setPrevisaoEntradaIngressos(
					calcularPrevisaoReceitaIngressosService.calcularPrevisaoReceitaIngressos(clubeSaldo.getClube()));
			clubeSaldo.setMovimentacoesTransferenciaCompra(0d);
			clubeSaldo.setMovimentacoesTransferenciaVenda(0d);
			clubesSaldo.put(clubeSaldo.getClube(), clubeSaldo);
		}
		
		//System.err.println(clubesSaldo.values());
		
		//Analisar propostas transferencia
		//for (int i = 0; i < (FastfootApplication.NUM_THREAD / 2); i++) {
		for (Integer i : propostasClubeX.keySet()) {
			transferenciasFuture.add(analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada,
					propostasClubeX.get(i), clubesSaldo, clubesRefazerEscalacaoX));
		}

		/*Map<Clube, List<PropostaTransferenciaJogador>> propostasClube = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getClubeOrigem));
		
		clubes = new ArrayList<Clube>(propostasClube.keySet());
		Integer nroThread = Math.min(FastfootApplication.NUM_THREAD, (clubes.size()/5) + 1);//pelo menos 5 clubes por thread
		offset = clubes.size() / nroThread;
		System.err.println("#propostasClube" + clubes.size() + ", #th:" + nroThread);
		Set<Clube> clubesRefazerEscalacao = Collections.synchronizedSet(new HashSet<Clube>());*/
		
		//Analisar propostas transferencia
		/*analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada, clubes, propostasClube,
				clubesRefazerEscalacao);*/

		//Analisar propostas transferencia
		/*for (int i = 0; i < nroThread; i++) {
			if ((i + 1) == nroThread) {
				transferenciasFuture.add(analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada,
						clubes.subList(i * offset, clubes.size()), propostasClube, clubesRefazerEscalacao));
			} else {
				transferenciasFuture.add(analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada,
						clubes.subList(i * offset, (i + 1) * offset), propostasClube, clubesRefazerEscalacao));
			}
		}*/
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		//
		
		//System.err.println(clubesRefazerEscalacao);//TODO:ajustar escalação: apenas caso de semanas nao contempladas em SemanaService
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#analisarPropostaTransferencia:" + (fim - inicio));
		
		stopWatch.stop();
		
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		//}
	}
	
	/*private void atualizarNumeroJogadores() {
		List<Clube> clubes = clubeRepository.findAll(); 
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(atualizarNumeroJogadoresService
						.atualizarNumeroJogadores(clubes.subList(i * offset, clubes.size())));
			} else {
				desenvolverJogadorFuture.add(atualizarNumeroJogadoresService
						.atualizarNumeroJogadores(clubes.subList(i * offset, (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}*/

}
