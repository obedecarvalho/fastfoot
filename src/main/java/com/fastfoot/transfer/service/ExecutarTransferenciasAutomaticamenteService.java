package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.service.CalcularPrevisaoReceitaIngressosService;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.LigaJogo;
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
import com.fastfoot.service.LigaJogoCRUDService;
import com.fastfoot.service.util.DatabaseUtil;
import com.fastfoot.service.util.RoletaUtil;
import com.fastfoot.service.util.ValidatorUtil;
import com.fastfoot.transfer.model.ClubeSaldo;
import com.fastfoot.transfer.model.NecessidadeContratacaoPossibilidades;
import com.fastfoot.transfer.model.PossivelTrocaJogador;
import com.fastfoot.transfer.model.PrepararDadosAnaliseTransferenciasReturn;
import com.fastfoot.transfer.model.ResultadoExecucaoTransferenciasAutomaticasDTO;
import com.fastfoot.transfer.model.TipoNegociacao;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class ExecutarTransferenciasAutomaticamenteService {
	
	private static Long ID_TEMP = -1l;
	
	private static final Integer NUM_MAX_EXECUCOES = 5;
	
	//private static final Double RANGE = 0.1;

	/**
	 * Valor percentual máximo que um clube pode ficar devendo ao fazer uma troca em relação ao jogador que está recebendo.
	 */
	private static final Double DIFF_PERC_MAX_SALDO_TROCA = 0.0;//0.25
	
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

	/*@Autowired
	private TemporadaCRUDService temporadaCRUDService;*/

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
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	public void executarTransferenciasAutomaticamente(Temporada temporada) {
		
		ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO = new ResultadoExecucaoTransferenciasAutomaticasDTO();
		
		//Temporada temporada = temporadaCRUDService.getTemporadaAtual();
		Semana s = semanaCRUDService.getProximaSemana(temporada.getJogo());
		
		List<NecessidadeContratacaoClube> necessidadeContratacaoClubes = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelNegociacao = new ArrayList<DisponivelNegociacao>();
		
		List<CompletableFuture<PrepararDadosAnaliseTransferenciasReturn>> desenvolverJogadorFuture = new ArrayList<CompletableFuture<PrepararDadosAnaliseTransferenciasReturn>>();
		
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		for (LigaJogo liga : ligaJogos) {
			desenvolverJogadorFuture.add(prepararDadosAnaliseTransferenciasService.prepararDadosAnaliseTransferencias(
					temporada, liga, true));
			desenvolverJogadorFuture.add(prepararDadosAnaliseTransferenciasService.prepararDadosAnaliseTransferencias(
					temporada, liga, false));
		}

		CompletableFuture.allOf(desenvolverJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
		
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

		/*avaliarNecessidadeContratacaoClubeService.calcularNecessidadeContratacaoEDisponivelNegociacao(temporada,
				jogadoresClube, necessidadeContratacaoClubes, disponivelNegociacao);*/
		
		elementosParaSalvarDTO.setDisponivelNegociacao(disponivelNegociacao);
		elementosParaSalvarDTO.setNecessidadeContratacaoClubes(necessidadeContratacaoClubes);
		
		Map<Clube, ClubeSaldo> clubeSaldo = getClubeSaldo(temporada);
		
		processar(temporada, s,
				necessidadeContratacaoClubes.stream().filter(ncc -> ncc.getNecessidadePrioritaria()).collect(Collectors.toList()),
				disponivelNegociacao.stream().filter(dn -> TipoNegociacao.COMPRA_VENDA.equals(dn.getTipoNegociacao())).collect(Collectors.toList()),
				clubeSaldo,
				elementosParaSalvarDTO);
		
		atualizarNumeroJogadoresService.atualizarNumeroJogadores(
				elementosParaSalvarDTO.getJogadores().stream().collect(Collectors.groupingBy(Jogador::getClube)), null);
		
		salvar(elementosParaSalvarDTO);
		
	}
	
	private void processar(Temporada temporada, Semana semana,
			List<NecessidadeContratacaoClube> necessidadeContratacaoClubes,
			List<DisponivelNegociacao> disponivelNegociacao, Map<Clube, ClubeSaldo> clubeSaldo,
			ResultadoExecucaoTransferenciasAutomaticasDTO elementosParaSalvarDTO) {
		
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
				
				if (possibilidades.getPossiveisContratacoes().size() > 0) {
				necessidadeContratacaoPossibilidades.add(possibilidades);
				}
			}
	
			//Criar propostas de transferencia
			Map<Clube, List<NecessidadeContratacaoPossibilidades>> possibilidadesClube = necessidadeContratacaoPossibilidades
					.stream().collect(Collectors.groupingBy(ncp -> ncp.getNecessidadeContratacaoClube().getClube()));
			
			List<PropostaTransferenciaJogador> propostas = new ArrayList<PropostaTransferenciaJogador>();
	
			for (Clube c : possibilidadesClube.keySet()) {
				propostas.addAll(analisarPossibilidadesContratacoesClube(temporada, c, possibilidadesClube.get(c),
						clubeSaldo.get(c), true, true));
			}
			
			elementosParaSalvarDTO.getPropostaTransferenciaJogadores().addAll(propostas);
			
			//Analisar propostas
			Map<Clube, List<PropostaTransferenciaJogador>> propostasPorClube = propostas.stream().collect(Collectors.groupingBy(p -> p.getClubeOrigem()));
			
			for (Clube c : propostasPorClube.keySet()) {
				analisarPropostasContratacaoPorClube(c, propostasPorClube.get(c), clubeSaldo, semana, elementosParaSalvarDTO);
			}
			
			qtdeTransferencia = elementosParaSalvarDTO.getPropostaTransferenciaJogadores().stream()
					.filter(ptj -> ptj.getPropostaAceita() != null && ptj.getPropostaAceita()).count();

			i++;

		}
		
		elementosParaSalvarDTO.getPropostaTransferenciaJogadores().stream().forEach(p -> p.setId(0l));
		
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

		consultarContratos(propostasPorJogadorMap.keySet());

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
			ClubeSaldo clubeSaldo, boolean filtrarSaldo, boolean propostaUnicaPorNecessidade) {
		
		List<PropostaTransferenciaJogador> propostas = new ArrayList<PropostaTransferenciaJogador>();

		PropostaTransferenciaJogador proposta;
		List<DisponivelNegociacao> possiveisContratacoes;
		DisponivelNegociacao disponivelNegociacao;

		for (NecessidadeContratacaoPossibilidades possibilidades : necessidadeContratacaoPossibilidades) {

			if (filtrarSaldo) {
			possiveisContratacoes = possibilidades.getPossiveisContratacoes().stream()
					.filter(dn -> dn.getJogador().getValorTransferencia() < clubeSaldo.getSaldoPrevisto())
					.collect(Collectors.toList());
			} else {
				possiveisContratacoes = possibilidades.getPossiveisContratacoes();
			}
			
			if (!ValidatorUtil.isEmpty(possiveisContratacoes)) {
				
				if (propostaUnicaPorNecessidade) {
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
				proposta.setTipoNegociacao(TipoNegociacao.COMPRA_VENDA);
				
				propostas.add(proposta);
				} else {
					for (DisponivelNegociacao dn : possiveisContratacoes) {
						
						proposta = new PropostaTransferenciaJogador();
						
						proposta.setId(ID_TEMP--);
						proposta.setClubeDestino(clubeDestino);
						proposta.setClubeOrigem(dn.getClube());
						proposta.setJogador(dn.getJogador());
						proposta.setNecessidadeContratacaoClube(possibilidades.getNecessidadeContratacaoClube());
						proposta.setValorTransferencia(dn.getJogador().getValorTransferencia());
						proposta.setPropostaAceita(null);
						proposta.setSemanaTransferencia(null);
						proposta.setTemporada(temporada);
						proposta.setDisponivelNegociacao(dn);
						proposta.setTipoNegociacao(TipoNegociacao.COMPRA_VENDA);
						
						propostas.add(proposta);
						
					}
				}

			}
		}
		
		return propostas;
	}

	protected boolean match(NecessidadeContratacaoClube necessidadeContratacaoClube, DisponivelNegociacao dn) {//TODO: passar limites por parametro
		return necessidadeContratacaoClube.getPosicao().equals(dn.getJogador().getPosicao())
				&& !necessidadeContratacaoClube.getClube().equals(dn.getClube())
				&& dn.getJogador().getForcaGeral() >= (necessidadeContratacaoClube.getClube().getForcaGeral() * necessidadeContratacaoClube.getNivelAdequacaoMin().getPorcentagemMinima())
				//&& dn.getJogador().getForcaGeral() < (necessidadeContratacaoClube.getClube().getForcaGeral() * (necessidadeContratacaoClube.getNivelAdequacaoMin().getPorcentagemMinima() + RANGE))
				&& dn.getJogador().getForcaGeralPotencial() >= (necessidadeContratacaoClube.getClube().getForcaGeral() * 0.90)
				//&& dn.getJogador().getForcaGeralPotencial() < (necessidadeContratacaoClube.getClube().getForcaGeral() * 1.10)
				&& dn.getJogador().getIdade() <= necessidadeContratacaoClube.getNivelAdequacaoMin().getIdadeMaxContratar();
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

		elementosParaSalvarDTO.getContratosInativar().add(propostaAceitar.getJogador().getContratoAtual());
		int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(propostaAceitar.getJogador().getIdade());
		double salario = calcularSalarioContratoService.calcularSalarioContrato(propostaAceitar.getJogador(), tempoContrato);
		elementosParaSalvarDTO.getContratos().add(new Contrato(propostaAceitar.getClubeDestino(),
				propostaAceitar.getJogador(), semana, tempoContrato, true, salario));

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

		//double porcSalarioAnual = Constantes.PORC_VALOR_JOG_SALARIO_SEMANAL * Constantes.NUM_SEMANAS;
		List<Map<String, Object>> saldoClubes = movimentacaoFinanceiraRepository
				.findSaldoProjetadoPorClube(Constantes.NUM_SEMANAS, temporada.getJogo().getId());
		
		Map<Clube, ClubeSaldo> clubesSaldo = new HashMap<Clube, ClubeSaldo>();
		
		ClubeSaldo clubeSaldo = null;
		
		for (Map<String, Object> sc : saldoClubes) {
			clubeSaldo = new ClubeSaldo();
			clubeSaldo.setClube(new Clube(DatabaseUtil.getValueLong(sc.get("id_clube"))));
			clubeSaldo.setSaldo(DatabaseUtil.getValueDecimal(sc.get("saldo")));
			clubeSaldo.setPrevisaoSaidaSalarios(DatabaseUtil.getValueDecimal(sc.get("salarios_projetado")));
			/*clubeSaldo.setPrevisaoEntradaIngressos(
					calcularPrevisaoReceitaIngressosService.calcularPrevisaoReceitaIngressos(clubeSaldo.getClube()));*/
			clubeSaldo.setMovimentacoesTransferenciaCompra(0d);
			clubeSaldo.setMovimentacoesTransferenciaVenda(0d);
			clubesSaldo.put(clubeSaldo.getClube(), clubeSaldo);
		}
		
		Map<Clube, Double> clubePrevReceita = calcularPrevisaoReceitaIngressosService.calcularPrevisaoReceitaIngressos(temporada);
		
		Double previsaoReceita;
		
		for (Clube clube : clubePrevReceita.keySet()) {
			previsaoReceita = clubePrevReceita.get(clube);
			clubesSaldo.get(clube).setPrevisaoEntradaIngressos(previsaoReceita);
		}

		return clubesSaldo;
	}

	@SuppressWarnings("unused")
	private void possiveisTrocas(List<PropostaTransferenciaJogador> propostas, Map<Clube, ClubeSaldo> clubeSaldo) {//TODO: implementar logica
		
		List<PropostaTransferenciaJogador> propostasPossiveisTrocas;
		
		Set<PossivelTrocaJogador> possiveisTrocas;
		
		List<PropostaTransferenciaJogador> propostasPossiveisTrocas2 = new ArrayList<PropostaTransferenciaJogador>();
		
		Set<PossivelTrocaJogador> possiveisTrocas2 = new HashSet<PossivelTrocaJogador>();

		Map<Clube, Map<Jogador, List<PropostaTransferenciaJogador>>> xyz = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getClubeDestino,
						Collectors.groupingBy(PropostaTransferenciaJogador::getJogador)));
		
		for (PropostaTransferenciaJogador ptj : propostas) {
			propostasPossiveisTrocas = propostas.stream().filter(p -> matchTroca(clubeSaldo, ptj, p)).collect(Collectors.toList());
			
			possiveisTrocas = propostas.stream().filter(p -> matchTroca(clubeSaldo, ptj, p))
					.map(p2 -> new PossivelTrocaJogador(ptj, p2)).collect(Collectors.toSet());
			
			if (propostasPossiveisTrocas.size() > 0) {
				
				/*System.err.println(propostasPossiveisTrocas.size());
				System.err.println(possiveisTrocas.size());*/
				
				propostasPossiveisTrocas2.addAll(propostasPossiveisTrocas);
				possiveisTrocas2.addAll(possiveisTrocas);

				System.err.println("\t\tINICIO");
				
				System.err.println("CS:" + clubeSaldo.get(ptj.getClubeDestino()));
				System.err.println("PTJ:" + ptj);
				
				for (PropostaTransferenciaJogador propostaTransferenciaJogador : propostasPossiveisTrocas) {
					System.err.println("\tCS: " + propostaTransferenciaJogador);
					System.err.println("\tPTJ: " + clubeSaldo.get(propostaTransferenciaJogador.getClubeDestino()));
				}
				
				
				System.err.println("\t\tFIM");
			}
		}
		
		System.err.println(propostasPossiveisTrocas2.size());
		System.err.println(possiveisTrocas2.size());
		
		System.err.println(possiveisTrocas2);
	}

	private boolean matchTroca(Map<Clube, ClubeSaldo> clubeSaldo, PropostaTransferenciaJogador ptj,
			PropostaTransferenciaJogador ptj2) {

		if (!(ptj.getClubeOrigem() == ptj2.getClubeDestino() && ptj.getClubeDestino() == ptj2.getClubeOrigem()
				/*&& (ptj.getNecessidadeContratacaoClube().getNecessidadePrioritaria()
						|| ptj2.getNecessidadeContratacaoClube().getNecessidadePrioritaria())*/)) {
			return false;
		}

		double diferencaValor = 0.0d;

		if (ptj.getValorTransferencia() >= ptj2.getValorTransferencia()) {
			diferencaValor = ptj.getValorTransferencia() - ptj2.getValorTransferencia();
			if (clubeSaldo.get(ptj.getClubeDestino()).getSaldoPrevisto() > diferencaValor) {
				return true;
			} else {
				return ((diferencaValor - clubeSaldo.get(ptj.getClubeDestino()).getSaldoPrevisto())
						/ ptj.getValorTransferencia()) < DIFF_PERC_MAX_SALDO_TROCA;
			}
		} else {
			diferencaValor = ptj2.getValorTransferencia() - ptj.getValorTransferencia();
			if (clubeSaldo.get(ptj2.getClubeDestino()).getSaldoPrevisto() > diferencaValor) {
				return true;
			} else {
				return ((diferencaValor - clubeSaldo.get(ptj2.getClubeDestino()).getSaldoPrevisto())
						/ ptj2.getValorTransferencia()) < DIFF_PERC_MAX_SALDO_TROCA;
			}
		}
	}
}
