package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.service.CalcularPrevisaoReceitaIngressosService;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AtualizarNumeroJogadoresService;
import com.fastfoot.player.service.CalcularSalarioContratoService;
import com.fastfoot.player.service.RenovarContratosAutomaticamenteService;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.util.RoletaUtil;
import com.fastfoot.service.util.ValidatorUtil;
import com.fastfoot.transfer.model.ClubeSaldo;
import com.fastfoot.transfer.model.NecessidadeContratacaoPossibilidades;
import com.fastfoot.transfer.model.ResultadoExecucaoTransferenciasAutomaticasDTO;
import com.fastfoot.transfer.model.TipoNegociacao;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;
import com.fastfoot.transfer.service.PrepararDadosAnaliseTransferenciasService.PrepararDadosAnaliseTransferenciasReturn;

@Service
public class ExecutarTransferenciasAutomaticamenteService {
	
	private static Long ID_TEMP = -1l;
	
	private static final Integer NUM_MAX_EXECUCOES = 5;
	
	private static final Double RANGE = 0.1;
	
	//###	REPOSITORY	###

	@Autowired
	private JogadorRepository jogadorRepository;

	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;

	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;

	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;

	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	//###	SERVICE	###

	@Autowired
	private TemporadaCRUDService temporadaService;

	/*@Autowired
	private GerarTransferenciasService gerarTransferenciasService;*/

	@Autowired
	private SemanaCRUDService semanaCRUDService;

	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;

	@Autowired
	private PrepararDadosAnaliseTransferenciasService prepararDadosAnaliseTransferenciasService;

	@Autowired
	private CalcularSalarioContratoService calcularSalarioContratoService;

	@Autowired
	private CalcularPrevisaoReceitaIngressosService calcularPrevisaoReceitaIngressosService;

	/*@Autowired
	private AvaliarNecessidadeContratacaoClubeService avaliarNecessidadeContratacaoClubeService;*/
	
	/*private static final Comparator<NecessidadeContratacaoPossibilidades> COMPARATOR = new Comparator<ExecutarTransferenciasAutomaticamenteService.NecessidadeContratacaoPossibilidades>() {
		@Override
		public int compare(NecessidadeContratacaoPossibilidades o1, NecessidadeContratacaoPossibilidades o2) {
			return o1.getNecessidadeContratacaoClube().getNivelAdequacaoMax().compareTo(o2.getNecessidadeContratacaoClube().getNivelAdequacaoMax());
		}
	};*/
	
	public void executarTransferenciasAutomaticamente() {
		
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO = new ResultadoExecucaoTransferenciasAutomaticasDTO();
		
		Temporada temporada = temporadaService.getTemporadaAtual();
		Semana s = semanaCRUDService.getProximaSemana();
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		//
		List<CompletableFuture<PrepararDadosAnaliseTransferenciasReturn>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<PrepararDadosAnaliseTransferenciasReturn>>();
		
		for (Liga liga : Liga.getAll()) {
			desenvolverJogadorFuture.add(prepararDadosAnaliseTransferenciasService.prepararDadosAnaliseTransferencias(
					temporada, liga, true));
			desenvolverJogadorFuture.add(prepararDadosAnaliseTransferenciasService.prepararDadosAnaliseTransferencias(
					temporada, liga, false));
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		//
		
		//
		for (CompletableFuture<PrepararDadosAnaliseTransferenciasReturn> completableFuture : desenvolverJogadorFuture) {
			try {
				disponivelNegociacao.addAll(completableFuture.get().getDisponivelNegociacao());
				necessidadeContratacaoClubes.addAll(completableFuture.get().getNecessidadeContratacaoClubes());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//
		
		//List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);
		
		//Map<Clube, List<Jogador>> jogadoresClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		stopWatch.split();
		mensagens.add("\t#prepararDadosAnaliseTransferencias:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();

		/*avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacaoEDisponivelNegociacao(temporada,
				jogadoresClube, necessidadeContratacaoClubes, disponivelNegociacao);*/
		
		elementosParaSalvarDTO.setDisponivelNegociacao(disponivelNegociacao);
		elementosParaSalvarDTO.setNecessidadeContratacaoClubes(necessidadeContratacaoClubes);
		
		Map<Clube, ClubeSaldo> clubeSaldo = getClubeSaldo(temporada);
		
		stopWatch.split();
		mensagens.add("\t#getClubeSaldo:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();
		
		processar(temporada, s,
				necessidadeContratacaoClubes.stream().filter(ncc -> ncc.getNecessidadePrioritaria()).collect(Collectors.toList()),
				disponivelNegociacao.stream().filter(dn -> TipoNegociacao.COMPRA_VENDA.equals(dn.getTipoNegociacao())).collect(Collectors.toList()),
				clubeSaldo,
				elementosParaSalvarDTO);
		
		stopWatch.split();
		mensagens.add("\t#processar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();
		
		atualizarNumeroJogadoresService.atualizarNumeroJogadores(
				elementosParaSalvarDTO.getJogadores().stream().collect(Collectors.groupingBy(Jogador::getClube)), null);
		stopWatch.split();
		mensagens.add("\t#atualizarNumero:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		salvar(elementosParaSalvarDTO);
		stopWatch.split();
		mensagens.add("\t#salvar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
		
		System.err.println(mensagens);
	}
	
	/*public void executarTransferenciasAutomaticamente() {
		
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO = new ResultadoExecucaoTransferenciasAutomaticasDTO();
		
		Temporada temporada = temporadaService.getTemporadaAtual();
		Semana s = semanaCRUDService.getProximaSemana();
		
		List<Jogador> jogadores = jogadorRepository.findByStatusJogador(StatusJogador.ATIVO);
		
		Map<Clube, List<Jogador>> jogadoresClube = jogadores.stream().collect(Collectors.groupingBy(Jogador::getClube));
		
		stopWatch.split();
		mensagens.add("\t#carregar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacaoEDisponivelNegociacao(temporada,
				jogadoresClube, necessidadeContratacaoClubes, disponivelNegociacao);
		
		elementosParaSalvarDTO.setDisponivelNegociacao(disponivelNegociacao);
		elementosParaSalvarDTO.setNecessidadeContratacaoClubes(necessidadeContratacaoClubes);
		
		Map<Clube, ClubeSaldo> clubeSaldo = gerarTransferenciasService.getClubeSaldo();
		
		stopWatch.split();
		mensagens.add("\t#prepararDados:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();
		
		processar(temporada, s,
				necessidadeContratacaoClubes
						.stream().filter(ncc -> ncc.getNecessidadePrioritaria()).collect(Collectors.toList())
						,
				disponivelNegociacao
						.stream().filter(dn -> TipoNegociacao.COMPRA_VENDA.equals(dn.getTipoNegociacao())).collect(Collectors.toList())
						,
				clubeSaldo,
				elementosParaSalvarDTO);
		
		stopWatch.split();
		mensagens.add("\t#processar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();
		
		//atualizarNumeroJogadoresService. //TODO
		stopWatch.split();
		mensagens.add("\t#atualizarNumero:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		//salvar(elementosParaSalvarDTO);
		stopWatch.split();
		mensagens.add("\t#salvar:" + (stopWatch.getSplitTime() - inicio));
		inicio = stopWatch.getSplitTime();//inicar outro bloco
		
		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
		
		System.err.println(mensagens);
	}*/
	
	private void processar(Temporada temporada, Semana semana,
			List<NecessidadeContratacaoClube> necessidadeContratacaoClubes,
			List<DisponivelNegociacao> disponivelNegociacao, Map<Clube, ClubeSaldo> clubeSaldo,
			ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO) {
		
		//Instanciar StopWatch
		StopWatch stopWatch = new StopWatch();
		List<String> mensagens = new ArrayList<String>();
		
		//Iniciar primeiro bloco
		stopWatch.start();
		stopWatch.split();
		long inicio = stopWatch.getSplitTime();
		
		//
		long qtdeTransferencia = 0, qtdeTransferenciaAnterior = -1;
		int i = 0;
		
		while (qtdeTransferencia > qtdeTransferenciaAnterior && i < NUM_MAX_EXECUCOES) {
			
			qtdeTransferenciaAnterior = elementosParaSalvarDTO.getPropostaTransferenciaJogadores().stream()
					.filter(ptj -> ptj.getPropostaAceita() != null && ptj.getPropostaAceita()).count();
			
			necessidadeContratacaoClubes = necessidadeContratacaoClubes.stream().filter(ncc -> !ncc.getNecessidadeSatisfeita()).collect(Collectors.toList());
			disponivelNegociacao = disponivelNegociacao.stream().filter(dn -> dn.getAtivo()).collect(Collectors.toList());
			
			//Associar a NecessidadeContratacaoClube com jogadores DisponivelNegociacao
			List<NecessidadeContratacaoPossibilidades> necessidadeContratacaoPossibilidades = new ArrayList<NecessidadeContratacaoPossibilidades>();
			
			NecessidadeContratacaoPossibilidades possibilidades;
			
			for (NecessidadeContratacaoClube necessidadeContratacaoClube : necessidadeContratacaoClubes) {
				possibilidades = new NecessidadeContratacaoPossibilidades();
				
				possibilidades.setNecessidadeContratacaoClube(necessidadeContratacaoClube);
				
				possibilidades.setPossiveisContratacoes(disponivelNegociacao.stream().filter(dn -> match(necessidadeContratacaoClube, dn)).collect(Collectors.toList()));
				
				necessidadeContratacaoPossibilidades.add(possibilidades);
			}
			
			stopWatch.split();
			mensagens.add(String.format("\t#associar[%d]:%d", i + 1, (stopWatch.getSplitTime() - inicio)));
			inicio = stopWatch.getSplitTime();//inicar outro bloco
	
			//Criar propostas de transferencia
			Map<Clube, List<NecessidadeContratacaoPossibilidades>> possibilidadesClube = necessidadeContratacaoPossibilidades
					.stream().collect(Collectors.groupingBy(ncp -> ncp.getNecessidadeContratacaoClube().getClube()));
			
			List<PropostaTransferenciaJogador> propostas = new ArrayList<PropostaTransferenciaJogador>();
	
			for (Clube c : possibilidadesClube.keySet()) {
				propostas.addAll(analisarPossibilidadesContratacoesClube(temporada, c, possibilidadesClube.get(c), clubeSaldo.get(c)));
			}
			
			elementosParaSalvarDTO.getPropostaTransferenciaJogadores().addAll(propostas);
			
			stopWatch.split();
			mensagens.add(String.format("\t#criarPropostas[%d]:%d", i + 1, (stopWatch.getSplitTime() - inicio)));
			inicio = stopWatch.getSplitTime();//inicar outro bloco
			
			//Analisar propostas
			Map<Clube, List<PropostaTransferenciaJogador>> propostasPorClube = propostas.stream().collect(Collectors.groupingBy(p -> p.getClubeOrigem()));
			
			for (Clube c : propostasPorClube.keySet()) {
				analisarPropostasContratacaoPorClube(c, propostasPorClube.get(c), clubeSaldo, semana, elementosParaSalvarDTO);
			}
	
			stopWatch.split();
			mensagens.add(String.format("\t#analisarPropostas[%d]:%d", i + 1, (stopWatch.getSplitTime() - inicio)));
			inicio = stopWatch.getSplitTime();//inicar outro bloco
			
			qtdeTransferencia = elementosParaSalvarDTO.getPropostaTransferenciaJogadores().stream()
					.filter(ptj -> ptj.getPropostaAceita() != null && ptj.getPropostaAceita()).count();

			i++;

			//System.err.println(qtdeTransferencia);
		}
		//
		
		elementosParaSalvarDTO.getPropostaTransferenciaJogadores().stream().forEach(p -> p.setId(0l));

		stopWatch.stop();
		mensagens.add("\t#tempoTotal:" + stopWatch.getTime());//Tempo total
		
		//System.err.println(mensagens);
	}
	
	private void analisarPropostasContratacaoPorClube(Clube clubeOrigem, List<PropostaTransferenciaJogador> propostas,
			Map<Clube, ClubeSaldo> clubesSaldo, Semana semana,
			ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO) {

		Map<Jogador, List<PropostaTransferenciaJogador>> propostasPorJogadorMap = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getJogador));

		List<PropostaTransferenciaJogador> propostasPorJogador;		
		List<PropostaTransferenciaJogador> propostasPorJogadorTmp;		
		PropostaTransferenciaJogador propostaAceitar = null;
		ClubeSaldo clubeSaldo = null;		
		Boolean haSaldo = null;

		double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * Constantes.NUM_SEMANAS;

		//
		consultarContratos(propostasPorJogadorMap.keySet());
		//

		for (Jogador j : propostasPorJogadorMap.keySet()) {

			propostasPorJogador = propostasPorJogadorMap.get(j);
			
			propostasPorJogadorTmp = new ArrayList<PropostaTransferenciaJogador>(propostasPorJogador);
			
			do {

				propostaAceitar = (PropostaTransferenciaJogador) RoletaUtil.sortearN(propostasPorJogadorTmp);
				propostasPorJogadorTmp.remove(propostaAceitar);
				
				clubeSaldo = clubesSaldo.get(propostaAceitar.getClubeDestino());
				haSaldo = clubeSaldo.inserirCompra(propostaAceitar.getValorTransferencia(), propostaAceitar.getValorTransferencia() * porcSalarioAnual);

			} while (propostasPorJogadorTmp.size() > 0 && !haSaldo);
			
			if (propostaAceitar != null && haSaldo) {

				propostasPorJogador.remove(propostaAceitar);
				clubesSaldo.get(propostaAceitar.getClubeOrigem()).inserirVenda(propostaAceitar.getValorTransferencia(), propostaAceitar.getValorTransferencia() * porcSalarioAnual);
			
				concluirTransferenciaJogador(propostaAceitar, propostasPorJogador, semana, elementosParaSalvarDTO);

			} else {
				//transferenciaConcluidaDTOs.add(new TransferenciaConcluidaDTO(null, propostasJog, null));//TODO: descartar outras propostas
			}

		}
		
	}
	
	private List<PropostaTransferenciaJogador> analisarPossibilidadesContratacoesClube(Temporada temporada,
			Clube clubeDestino, List<NecessidadeContratacaoPossibilidades> necessidadeContratacaoPossibilidades,
			ClubeSaldo clubeSaldo) {
		
		List<PropostaTransferenciaJogador> propostas = new ArrayList<PropostaTransferenciaJogador>();

		PropostaTransferenciaJogador proposta;
		List<DisponivelNegociacao> possiveisContratacoes;
		DisponivelNegociacao disponivelNegociacao;

		for (NecessidadeContratacaoPossibilidades possibilidades : necessidadeContratacaoPossibilidades) {

			possiveisContratacoes = possibilidades.getPossiveisContratacoes().stream()
					.filter(dn -> dn.getJogador().getValorTransferencia() < clubeSaldo.getSaldoPrevisto())
					.collect(Collectors.toList());
			
			if (!ValidatorUtil.isEmpty(possiveisContratacoes)) {
				
				disponivelNegociacao = RoletaUtil.sortearPesoUm(possiveisContratacoes);//TODO: criar peso para sorteio
				
				proposta = new PropostaTransferenciaJogador();
				
				proposta.setId(ID_TEMP--);
				proposta.setClubeDestino(clubeDestino);
				proposta.setClubeOrigem(disponivelNegociacao.getClube());
				proposta.setJogador(disponivelNegociacao.getJogador());
				proposta.setNecessidadeContratacaoClube(possibilidades.getNecessidadeContratacaoClube());
				proposta.setValorTransferencia(disponivelNegociacao.getJogador().getValorTransferencia());
				proposta.setPropostaAceita(null);
				proposta.setSemanaTransferencia(null);
				proposta.setTemporada(temporada);
				proposta.setDisponivelNegociacao(disponivelNegociacao);
				
				propostas.add(proposta);

			}
		}
		
		return propostas;
	}

	protected boolean match(NecessidadeContratacaoClube necessidadeContratacaoClube, DisponivelNegociacao dn) {//TODO: passar limites por parametro
		return necessidadeContratacaoClube.getPosicao().equals(dn.getJogador().getPosicao())
				&& !necessidadeContratacaoClube.getClube().equals(dn.getClube())
				&& dn.getJogador().getForcaGeral() >= (necessidadeContratacaoClube.getClube().getForcaGeral() * necessidadeContratacaoClube.getNivelAdequacaoMin().getPorcentagemMinima())
				&& dn.getJogador().getForcaGeral() < (necessidadeContratacaoClube.getClube().getForcaGeral() * (necessidadeContratacaoClube.getNivelAdequacaoMin().getPorcentagemMinima() + RANGE))
				&& dn.getJogador().getForcaGeralPotencial() >= (necessidadeContratacaoClube.getClube().getForcaGeral() * 0.925)
				&& dn.getJogador().getForcaGeralPotencial() < (necessidadeContratacaoClube.getClube().getForcaGeral() * 1.0725);
	}

	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	private void concluirTransferenciaJogador(PropostaTransferenciaJogador propostaAceitar,
			List<PropostaTransferenciaJogador> propostasRejeitar, Semana semana,
			ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO) {
		
		propostaAceitar.setPropostaAceita(true);
		propostaAceitar.setSemanaTransferencia(semana);
		propostaAceitar.getJogador().setClube(propostaAceitar.getClubeDestino());
		propostaAceitar.getNecessidadeContratacaoClube().setNecessidadeSatisfeita(true);		
		propostaAceitar.getDisponivelNegociacao().setAtivo(false);		
		propostasRejeitar.stream().forEach(p -> p.setPropostaAceita(false));

		//
		elementosParaSalvarDTO.getContratosInativar().add(propostaAceitar.getJogador().getContratoAtual());
		int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(propostaAceitar.getJogador().getIdade());
		double salario = calcularSalarioContratoService.calcularSalarioContrato(propostaAceitar.getJogador(), tempoContrato);
		elementosParaSalvarDTO.getContratos().add(new Contrato(propostaAceitar.getClubeDestino(),
				propostaAceitar.getJogador(), semana, tempoContrato, true, salario));
		//

		elementosParaSalvarDTO.getJogadores().add(propostaAceitar.getJogador());
		
		elementosParaSalvarDTO.getMovimentacoesFinanceiras()
				.add(new MovimentacaoFinanceira(propostaAceitar.getClubeDestino(), semana,
						TipoMovimentacaoFinanceira.SAIDA_COMPRA_JOGADOR, -1 * propostaAceitar.getValorTransferencia(),
						"Compra de Jogador"));
		
		elementosParaSalvarDTO.getMovimentacoesFinanceiras()
				.add(new MovimentacaoFinanceira(propostaAceitar.getClubeOrigem(), semana,
						TipoMovimentacaoFinanceira.ENTRADA_VENDA_JOGADOR, propostaAceitar.getValorTransferencia(),
						"Venda de Jogador"));

	}
	
	private void salvar(ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO) {

		//UPDATE
		jogadorRepository.saveAll(elementosParaSalvarDTO.getJogadores());

		//INSERT
		necessidadeContratacaoClubeRepository.saveAll(elementosParaSalvarDTO.getNecessidadeContratacaoClubes());
		disponivelNegociacaoRepository.saveAll(elementosParaSalvarDTO.getDisponivelNegociacao());
		propostaTransferenciaJogadorRepository.saveAll(elementosParaSalvarDTO.getPropostaTransferenciaJogadores());

		movimentacaoFinanceiraRepository.saveAll(elementosParaSalvarDTO.getMovimentacoesFinanceiras());
		contratoRepository.saveAll(elementosParaSalvarDTO.getContratos());

		//UPDATE
		elementosParaSalvarDTO.getContratosInativar().stream().forEach(c -> c.setAtivo(false));
		contratoRepository.desativar(elementosParaSalvarDTO.getContratosInativar());
	}

	private void consultarContratos(Collection<Jogador> jogadores) {
		List<Contrato> contratos = contratoRepository.findByJogadoresAndAtivo(jogadores, true);

		Map<Jogador, Contrato> jogadorContrato = contratos.stream()
				.collect(Collectors.toMap(Contrato::getJogador, Function.identity()));

		jogadores.stream().forEach(j -> j.setContratoAtual(jogadorContrato.get(j)));
	}

	private Map<Clube, ClubeSaldo> getClubeSaldo(Temporada temporada) {

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
		
		/*List<Clube> clubes = new ArrayList<Clube>(clubesSaldo.keySet());
		
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
		}*/
		
		Map<Clube, Double> clubePrevReceita = calcularPrevisaoReceitaIngressosService.calcularPrevisaoReceitaIngressos(temporada);
		
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

		//System.err.println(mensagens);

		return clubesSaldo;
	}
}
