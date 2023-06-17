package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
	
	private static final Integer NUM_THREAD_ANALISAR_PROPOSTA = FastfootApplication.NUM_THREAD;
	
	//###	REPOSITORY	###
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	//###	SERVICE	###

	@Autowired
	private AvaliarNecessidadeContratacaoClubeService avaliarNecessidadeContratacaoClubeService;
	
	@Autowired
	private ProporTransferenciaService proporTransferenciaService;
	
	@Autowired
	private AnalisarPropostaTransferenciaService analisarPropostaTransferenciaService;
	
	@Autowired
	private CalcularPrevisaoReceitaIngressosService calcularPrevisaoReceitaIngressosService;
	
	public void gerarTransferencias(Temporada temporada) {
		gerarTransferencias(temporada, null);
	}
	
	private void gerarTransferencias(Temporada temporada, List<Clube> clubes) {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		List<CompletableFuture<Boolean>> transferenciasFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		//Calcular necessidade contratacao
		
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

		for (Integer i : necessidadeContratacaoClubeX.keySet()) {
			transferenciasFuture.add(proporTransferenciaService.gerarPropostaTransferencia(temporada,
					necessidadeContratacaoClubeX.get(i)));
		}

		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#gerarPropostaTransferencia:" + (fim - inicio));

		transferenciasFuture.clear();

		inicio = stopWatch.getSplitTime();

		List<PropostaTransferenciaJogador> propostas = propostaTransferenciaJogadorRepository
				.findByTemporadaAndPropostaAceitaIsNull(temporada);

		Map<Integer, Map<Clube, List<PropostaTransferenciaJogador>>> propostasClubeX = propostas.stream()
				.collect(Collectors.groupingBy(p -> p.getClubeOrigem().getId() % NUM_THREAD_ANALISAR_PROPOSTA,
						Collectors.groupingBy(PropostaTransferenciaJogador::getClubeOrigem)));
		
		Set<Clube> clubesRefazerEscalacaoX = Collections.synchronizedSet(new HashSet<Clube>());
		
		/*double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * Constantes.NUM_SEMANAS;
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository.findSaldoProjetadoPorClube(porcSalarioAnual);
		
		Map<Clube, ClubeSaldo> clubesSaldo = new HashMap<Clube, ClubeSaldo>();
		
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
		}*/

		Map<Clube, ClubeSaldo> clubesSaldo = getClubeSaldo();
		
		//Analisar propostas transferencia

		for (Integer i : propostasClubeX.keySet()) {
			transferenciasFuture.add(analisarPropostaTransferenciaService.analisarPropostaTransferencia(temporada,
					propostasClubeX.get(i), clubesSaldo, clubesRefazerEscalacaoX));
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();

		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("\t#analisarPropostaTransferencia:" + (fim - inicio));
		
		stopWatch.stop();
		
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);

	}

	public Map<Clube, ClubeSaldo> getClubeSaldo() {

		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();

		double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * Constantes.NUM_SEMANAS;
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository.findSaldoProjetadoPorClube(porcSalarioAnual);

		stopWatch.split();
		mensagens.add("\t#findSaldoProjetadoPorClube:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		Map<Clube, ClubeSaldo> clubesSaldo = new HashMap<Clube, ClubeSaldo>();
		
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

		stopWatch.split();
		mensagens.add("\t#calcularPrevisaoReceitaIngressos:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total

		System.err.println(mensagens);

		return clubesSaldo;
	}
	
	public Map<Clube, ClubeSaldo> getClubeSaldo2() {

		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();

		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();

		double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * Constantes.NUM_SEMANAS;
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository.findSaldoProjetadoPorClube(porcSalarioAnual);

		stopWatch.split();
		mensagens.add("\t#findSaldoProjetadoPorClube:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		Map<Clube, ClubeSaldo> clubesSaldo = new HashMap<Clube, ClubeSaldo>();
		
		ClubeSaldo clubeSaldo = null;
		
		for (Map<String, Object> sc : saldoClubes) {
			clubeSaldo = new ClubeSaldo();
			clubeSaldo.setClube(new Clube((Integer) sc.get("id_clube")));
			clubeSaldo.setSaldo((Double) sc.get("saldo"));
			clubeSaldo.setPrevisaoSaidaSalarios((Double) sc.get("salarios_projetado"));
			/*clubeSaldo.setPrevisaoEntradaIngressos(
					calcularPrevisaoReceitaIngressosService.calcularPrevisaoReceitaIngressos(clubeSaldo.getClube()));*/
			clubeSaldo.setMovimentacoesTransferenciaCompra(0d);
			clubeSaldo.setMovimentacoesTransferenciaVenda(0d);
			clubesSaldo.put(clubeSaldo.getClube(), clubeSaldo);
		}
		
		//
		
		List<Clube> clubes = new ArrayList<Clube>(clubesSaldo.keySet());
		
		List<CompletableFuture<Map<Clube, Double>>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Map<Clube, Double>>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;

		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(calcularPrevisaoReceitaIngressosService
						.calcularPrevisaoReceitaIngressos(clubes.subList(i * offset, clubes.size())));
			} else {
				desenvolverJogadorFuture.add(calcularPrevisaoReceitaIngressosService
						.calcularPrevisaoReceitaIngressos(clubes.subList(i * offset, (i + 1) * offset)));
			}
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		
		Map<Clube, Double> clubePrevReceita = new HashMap<Clube, Double>();
		for (CompletableFuture<Map<Clube, Double>> completableFuture : desenvolverJogadorFuture) {
			try {
				clubePrevReceita.putAll(completableFuture.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Double previsaoReceita;
		
		for (Clube clube : clubePrevReceita.keySet()) {
			previsaoReceita = clubePrevReceita.get(clube);
			clubesSaldo.get(clube).setPrevisaoEntradaIngressos(previsaoReceita);
		}

		//

		stopWatch.split();
		mensagens.add("\t#calcularPrevisaoReceitaIngressos:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total

		System.err.println(mensagens);

		return clubesSaldo;
	}
}
