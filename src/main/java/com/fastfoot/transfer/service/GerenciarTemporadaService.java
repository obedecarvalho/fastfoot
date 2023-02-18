package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.service.AtualizarClubeNivelService;
import com.fastfoot.club.service.CalcularPrevisaoReceitaIngressosService;
import com.fastfoot.club.service.CalcularTrajetoriaForcaClubeService;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.financial.service.CalcularClubeSaldoSemanaService;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.player.service.AtualizarNumeroJogadoresService;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.SemanaRepository;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.transfer.model.ClubeSaldo;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class GerenciarTemporadaService {//TODO: avaliar modulo, melhorar nome classe
	
	private static final Integer NUM_THREAD_ANALISAR_PROPOSTA = 3;
	
	private static final Comparator<Semana> COMPARATOR;
	
	static {
		COMPARATOR = new Comparator<Semana>() {
			
			@Override
			public int compare(Semana o1, Semana o2) {
				
				int compare = o1.getTemporada().getAno().compareTo(o2.getTemporada().getAno());
				
				if (compare == 0) {
					compare = o1.getNumero().compareTo(o2.getNumero());
				}

				return compare;
			}
		};
	}
	
	//###	REPOSITORY	###
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	/*@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;*/

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	@Autowired
	private SemanaRepository semanaRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private AvaliarNecessidadeContratacaoClubeService avaliarNecessidadeContratacaoClubeService;
	
	@Autowired
	private ProporTransferenciaService proporTransferenciaService;
	
	@Autowired
	private AnalisarPropostaTransferenciaService analisarPropostaTransferenciaService;
	
	@Autowired
	private TemporadaCRUDService temporadaService;
	
	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;
	
	@Autowired
	private AtualizarClubeNivelService atualizarClubeNivelService;
	
	@Autowired
	private CalcularTrajetoriaForcaClubeService calcularTrajetoriaForcaClubeService;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private CalcularPrevisaoReceitaIngressosService calcularPrevisaoReceitaIngressosService;

	@Autowired
	private CalcularClubeSaldoSemanaService calcularClubeSaldoSemanaService;
	
	public void gerarTransferencias() {
		List<Clube> clubes = clubeRepository.findAll(); 
		Temporada temporada = temporadaService.getTemporadaAtual();
		gerarTransferencias(temporada, clubes);
		atualizarNumeroJogadores();
	}
	
	public void calcularClubeSaldoSemana() {
		
		List<Semana> semanas = semanaRepository.findAll();
		
		Collections.sort(semanas, COMPARATOR);
		
		List<MovimentacaoFinanceira> movimentaceosFinanceiras = movimentacaoFinanceiraRepository.findAll();

		Map<Integer, Map<Clube, List<MovimentacaoFinanceira>>> movimentacoesClube = movimentaceosFinanceiras.stream()
				.collect(Collectors.groupingBy(mf -> mf.getClube().getId() % FastfootApplication.NUM_THREAD,
						Collectors.groupingBy(MovimentacaoFinanceira::getClube)));
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			desenvolverJogadorFuture
					.add(calcularClubeSaldoSemanaService.calcularClubeSaldoSemana(semanas, movimentacoesClube.get(i)));
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}
	
	public void gerarMudancaClubeNivel() {
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		for (Liga l : Liga.getAll()) {
			desenvolverJogadorFuture.add(atualizarClubeNivelService.atualizarClubeNivel(temporada, l));
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		
		desenvolverJogadorFuture.clear();
		desenvolverJogadorFuture.add(atualizarClubeNivelService.atualizarClubeNivelInternacional(temporada));
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}
	
	public void calcularTrajetoriaForcaClube() {
		List<Clube> clubes = clubeRepository.findAll(); 
		Semana s = semanaCRUDService.getProximaSemana();

		List<CompletableFuture<Boolean>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				desenvolverJogadorFuture.add(calcularTrajetoriaForcaClubeService
						.calcularTrajetoriaForcaClube(clubes.subList(i * offset, clubes.size()), s));
			} else {
				desenvolverJogadorFuture.add(calcularTrajetoriaForcaClubeService
						.calcularTrajetoriaForcaClube(clubes.subList(i * offset, (i + 1) * offset), s));
			}
		}
		
		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();

	}
	
	private void atualizarNumeroJogadores() {
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
	}

	private void gerarTransferencias(Temporada temporada, List<Clube> clubes) {
		//if (semana.getNumero() == 0, 1, 2, 3) {		

		int offset = clubes.size() / FastfootApplication.NUM_THREAD;
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		long inicio = 0, fim = 0;
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.split();
		inicio = stopWatch.getSplitTime();
		
		List<CompletableFuture<Boolean>> transferenciasFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		//Calcular necessidade contratacao
		for (int i = 0; i < FastfootApplication.NUM_THREAD; i++) {
			if ((i + 1) == FastfootApplication.NUM_THREAD) {
				transferenciasFuture.add(avaliarNecessidadeContratacaoClubeService
						.calcularNecessidadeContratacao(clubes.subList(i * offset, clubes.size())));
			} else {
				transferenciasFuture.add(avaliarNecessidadeContratacaoClubeService
						.calcularNecessidadeContratacao(clubes.subList(i * offset, (i + 1) * offset)));
			}
		}
		
		CompletableFuture.allOf(transferenciasFuture.toArray(new CompletableFuture<?>[0])).join();
		
		stopWatch.split();
		fim = stopWatch.getSplitTime();
		mensagens.add("#calcularNecessidadeContratacao:" + (fim - inicio));

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
		mensagens.add("#gerarPropostaTransferencia:" + (fim - inicio));

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
		
		System.err.println(clubesSaldo.values());
		
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
		mensagens.add("#analisarPropostaTransferencia:" + (fim - inicio));
		
		stopWatch.stop();
		
		mensagens.add("#tempoTotal:" + stopWatch.getTime());
		
		System.err.println(mensagens);
		
		//}
	}

}
