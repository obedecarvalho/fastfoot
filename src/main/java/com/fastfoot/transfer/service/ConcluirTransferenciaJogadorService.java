package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorDetalhe;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.JogadorDetalheRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.AtualizarNumeroJogadoresService;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.service.util.RandomUtil;
import com.fastfoot.transfer.model.dto.TransferenciaConcluidaDTO;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.NecessidadeContratacaoClube;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.NecessidadeContratacaoClubeRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class ConcluirTransferenciaJogadorService {
	
	//###	REPOSITORY	###
	
	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	/*@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;*/
	
	/*@Autowired
	private JogadorEstatisticasAmistososTemporadaRepository jogadorEstatisticasAmistososTemporadaRepository;*/
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	//###	SERVICE	###

	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;

	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	public void concluirTransferenciaJogadorEmLote(List<TransferenciaConcluidaDTO> transferenciaConcluidaDTOs) {

		Semana s = semanaCRUDService.getProximaSemana();

		//List<JogadorEstatisticasTemporada> estatisticasSalvar = new ArrayList<JogadorEstatisticasTemporada>();
		//List<JogadorEstatisticasAmistososTemporada> estatisticasAmistososSalvar = new ArrayList<JogadorEstatisticasAmistososTemporada>();
		//List<JogadorEstatisticasTemporada> estatisticasExcluir = new ArrayList<JogadorEstatisticasTemporada>();//maior parte do tempo é gasto aqui
		//List<JogadorEstatisticasAmistososTemporada> estatisticasAmistososExcluir = new ArrayList<JogadorEstatisticasAmistososTemporada>();//maior parte do tempo é gasto aqui
		List<PropostaTransferenciaJogador> propostasSalvar = new ArrayList<PropostaTransferenciaJogador>();
		List<Jogador> jogadoresSalvar = new ArrayList<Jogador>();
		List<NecessidadeContratacaoClube> necessidadeContratacaoSalvar = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelSalvar = new ArrayList<DisponivelNegociacao>();
		List<JogadorDetalhe> detalheJogadoresSalvar = new ArrayList<JogadorDetalhe>();
		List<Contrato> contratosInserir = new ArrayList<Contrato>();
		List<Contrato> contratosAtualizar = new ArrayList<Contrato>();

		List<MovimentacaoFinanceira> movimentacoesFinanceiras = new ArrayList<MovimentacaoFinanceira>();

		for (TransferenciaConcluidaDTO transferenciaConcluidaDTO : transferenciaConcluidaDTOs) {

			/*if (transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasTemporadaAtual()
					.isEmpty()) {
				estatisticasExcluir.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador()
						.getJogadorEstatisticasTemporadaAtual());
			}
			
			if (transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasAmistososTemporadaAtual()
					.isEmpty()) {
				estatisticasExcluir.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador()
						.getJogadorEstatisticasAmistososTemporadaAtual());
			}*/

			transferenciaConcluidaDTO.getPropostaAceita().setPropostaAceita(true);
			transferenciaConcluidaDTO.getPropostaAceita().setSemanaTransferencia(s);

			contratosInserir.add(new Contrato(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino(),
					transferenciaConcluidaDTO.getPropostaAceita().getJogador(), s,
					RandomUtil.sortearIntervalo(Constantes.NUMERO_ANO_MIN_CONTRATO_PADRAO,
							Constantes.NUMERO_ANO_MAX_CONTRATO_PADRAO + 1),
					false));
			transferenciaConcluidaDTO.getPropostaAceita().getJogador()
					.setClube(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino());
			/*transferenciaConcluidaDTO.getPropostaAceita().getJogador().setJogadorEstatisticasTemporadaAtual(
					new JogadorEstatisticasTemporada(transferenciaConcluidaDTO.getPropostaAceita().getJogador(),
							transferenciaConcluidaDTO.getPropostaAceita().getTemporada(),
							transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino(), false));
			transferenciaConcluidaDTO.getPropostaAceita().getJogador().setJogadorEstatisticasAmistososTemporadaAtual(
					new JogadorEstatisticasTemporada(transferenciaConcluidaDTO.getPropostaAceita().getJogador(),
							transferenciaConcluidaDTO.getPropostaAceita().getTemporada(),
							transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino(), true));*/

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
			/*estatisticasSalvar.add(
					transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasTemporadaAtual());
			estatisticasSalvar.add(
					transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasAmistososTemporadaAtual());*/
			detalheJogadoresSalvar.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorDetalhe());
			necessidadeContratacaoSalvar
					.add(transferenciaConcluidaDTO.getPropostaAceita().getNecessidadeContratacaoClube());
			disponivelSalvar.add(transferenciaConcluidaDTO.getDisponivelNegociacao());

		}
		
		atualizarNumeroJogadoresService.atualizarNumeroJogadores(jogadoresSalvar.stream().collect(Collectors.groupingBy(Jogador::getClube)), null);

		//jogadorEstatisticasAmistososTemporadaRepository.saveAll(estatisticasAmistososSalvar);
		//jogadorEstatisticasTemporadaRepository.saveAll(estatisticasSalvar);
		jogadorDetalheRepository.saveAll(detalheJogadoresSalvar);
		jogadorRepository.saveAll(jogadoresSalvar);
		//jogadorEstatisticasTemporadaRepository.deleteAll(estatisticasExcluir);
		//jogadorEstatisticasAmistososTemporadaRepository.deleteAll(estatisticasAmistososExcluir);
		propostaTransferenciaJogadorRepository.saveAll(propostasSalvar);
		necessidadeContratacaoClubeRepository.saveAll(necessidadeContratacaoSalvar);
		disponivelNegociacaoRepository.saveAll(disponivelSalvar);

		movimentacaoFinanceiraRepository.saveAll(movimentacoesFinanceiras);
		
		contratoRepository.saveAll(contratosInserir);

	}
}
