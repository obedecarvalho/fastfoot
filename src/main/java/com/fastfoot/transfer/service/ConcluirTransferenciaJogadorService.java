package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AtualizarNumeroJogadoresService;
import com.fastfoot.player.service.CalcularSalarioContratoService;
import com.fastfoot.player.service.RenovarContratosAutomaticamenteService;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.transfer.model.dto.TransferenciaConcluidaDTO;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Deprecated
@Service
public class ConcluirTransferenciaJogadorService {
	
	//###	REPOSITORY	###
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	/*@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;*/
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	//###	SERVICE	###

	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;
	
	@Autowired
	private CalcularSalarioContratoService calcularSalarioContratoService;

	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	public void concluirTransferenciaJogadorEmLote(List<TransferenciaConcluidaDTO> transferenciaConcluidaDTOs) {

		Semana s = semanaCRUDService.getProximaSemana();

		List<PropostaTransferenciaJogador> propostasSalvar = new ArrayList<PropostaTransferenciaJogador>();
		List<Jogador> jogadoresSalvar = new ArrayList<Jogador>();
		List<NecessidadeContratacaoClube> necessidadeContratacaoSalvar = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelSalvar = new ArrayList<DisponivelNegociacao>();
		//List<JogadorDetalhe> detalheJogadoresSalvar = new ArrayList<JogadorDetalhe>();
		List<Contrato> contratosInserir = new ArrayList<Contrato>();
		List<Contrato> contratosDesativar = new ArrayList<Contrato>();
		//
		consultarContratos(transferenciaConcluidaDTOs.stream().map(tc -> tc.getPropostaAceita().getJogador())
				.collect(Collectors.toList()));
		//

		List<MovimentacaoFinanceira> movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();

		for (TransferenciaConcluidaDTO transferenciaConcluidaDTO : transferenciaConcluidaDTOs) {

			transferenciaConcluidaDTO.getPropostaAceita().setPropostaAceita(true);
			transferenciaConcluidaDTO.getPropostaAceita().setSemanaTransferencia(s);

			contratosDesativar.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador().getContratoAtual());
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(transferenciaConcluidaDTO.getPropostaAceita().getJogador().getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(transferenciaConcluidaDTO.getPropostaAceita().getJogador(), tempoContrato);
			contratosInserir.add(new Contrato(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino(),
					transferenciaConcluidaDTO.getPropostaAceita().getJogador(), s, tempoContrato, true, salario));
			transferenciaConcluidaDTO.getPropostaAceita().getJogador()
					.setClube(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino());

			transferenciaConcluidaDTO.getPropostaAceita().getNecessidadeContratacaoClube()
					.setNecessidadeSatisfeita(true);

			transferenciaConcluidaDTO.getPropostasRejeitar().stream().forEach(p -> p.setPropostaAceita(false));

			transferenciaConcluidaDTO.getDisponivelNegociacao().setAtivo(false);
			
			movimentacoesFinanceiras.add(
					new MovimentacaoFinanceira(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino(), s,
							TipoMovimentacaoFinanceira.SAIDA_COMPRA_JOGADOR,
							-1 * transferenciaConcluidaDTO.getPropostaAceita().getValorTransferencia(), "Compra de Jogador"));
			movimentacoesFinanceiras.add(
					new MovimentacaoFinanceira(transferenciaConcluidaDTO.getPropostaAceita().getClubeOrigem(), s,
							TipoMovimentacaoFinanceira.ENTRADA_VENDA_JOGADOR,
							transferenciaConcluidaDTO.getPropostaAceita().getValorTransferencia(), "Venda de Jogador"));

			propostasSalvar.add(transferenciaConcluidaDTO.getPropostaAceita());
			propostasSalvar.addAll(transferenciaConcluidaDTO.getPropostasRejeitar());
			jogadoresSalvar.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador());

			//detalheJogadoresSalvar.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorDetalhe());
			necessidadeContratacaoSalvar
					.add(transferenciaConcluidaDTO.getPropostaAceita().getNecessidadeContratacaoClube());
			disponivelSalvar.add(transferenciaConcluidaDTO.getDisponivelNegociacao());

		}
		
		atualizarNumeroJogadoresService.atualizarNumeroJogadores(jogadoresSalvar.stream().collect(Collectors.groupingBy(Jogador::getClube)), null);

		//jogadorDetalheRepository.saveAll(detalheJogadoresSalvar);
		jogadorRepository.saveAll(jogadoresSalvar);

		propostaTransferenciaJogadorRepository.saveAll(propostasSalvar);
		necessidadeContratacaoClubeRepository.saveAll(necessidadeContratacaoSalvar);
		disponivelNegociacaoRepository.saveAll(disponivelSalvar);

		movimentacaoFinanceiraRepository.saveAll(movimentacoesFinanceiras);

		contratoRepository.saveAll(contratosInserir);
		contratosDesativar.stream().forEach(c -> c.setAtivo(false));
		contratoRepository.saveAll(contratosDesativar);

	}

	private void consultarContratos(List<Jogador> jogadores) {
		List<Contrato> contratos = contratoRepository.findByJogadoresAndAtivo(jogadores, true);

		Map<Jogador, Contrato> jogadorContrato = contratos.stream()
				.collect(Collectors.toMap(Contrato::getJogador, Function.identity()));

		jogadores.stream().forEach(j -> j.setContratoAtual(jogadorContrato.get(j)));
	}
}
