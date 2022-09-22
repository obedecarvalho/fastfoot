package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.SemanaService;
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
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Autowired
	private NecessidadeContratacaoClubeRepository necessidadeContratacaoClubeRepository;
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	//###	SERVICE	###

	@Autowired
	private SemanaService semanaService;

	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	public void concluirTransferenciaJogador(PropostaTransferenciaJogador propostaTransferenciaJogador,
			List<PropostaTransferenciaJogador> propostasRejeitar, DisponivelNegociacao disponivelNegociacao) {
		//TODO: fazer método concluirTransferenciaJogadorEmLote
		
		Semana s = semanaService.getSemanaAtual();
		
		propostaTransferenciaJogador.setPropostaAceita(true);
		propostaTransferenciaJogador.setSemanaTransferencia(s);
		
		propostaTransferenciaJogador.getJogador().setClube(propostaTransferenciaJogador.getClubeDestino());
		
		//TODO: se Temporada.semana.numero = 0 e jogadorEstatisticasTemporadaAtual.isEmpty -> excluir jogadorEstatisticasTemporadaAtual
		propostaTransferenciaJogador.getJogador()
				.setJogadorEstatisticasTemporadaAtual(new JogadorEstatisticasTemporada(
						propostaTransferenciaJogador.getJogador(), propostaTransferenciaJogador.getTemporada(),
						propostaTransferenciaJogador.getClubeDestino()));
		
		propostaTransferenciaJogador.getNecessidadeContratacaoClube().setNecessidadeSatisfeita(true);
		
		propostasRejeitar.stream().forEach(p -> p.setPropostaAceita(false));
		
		propostasRejeitar.add(propostaTransferenciaJogador);
		
		disponivelNegociacao.setAtivo(false);

		jogadorEstatisticasTemporadaRepository.save(propostaTransferenciaJogador.getJogador().getJogadorEstatisticasTemporadaAtual());
		jogadorRepository.save(propostaTransferenciaJogador.getJogador());
		propostaTransferenciaJogadorRepository.saveAll(propostasRejeitar);
		necessidadeContratacaoClubeRepository.save(propostaTransferenciaJogador.getNecessidadeContratacaoClube());
		disponivelNegociacaoRepository.save(disponivelNegociacao);

		//TODO: gerar entradas e saidas financeiras
	}
	
	//É esperado que validações já tenham sido feitas: Elenco dos clubes, disponibilidade financeira, janela de transferencias
	public void concluirTransferenciaJogadorEmLote(List<TransferenciaConcluidaDTO> transferenciaConcluidaDTOs) {

		Semana s = semanaService.getSemanaAtual();

		List<JogadorEstatisticasTemporada> estatisticasSalvar = new ArrayList<JogadorEstatisticasTemporada>();
		List<JogadorEstatisticasTemporada> estatisticasExcluir = new ArrayList<JogadorEstatisticasTemporada>();
		List<PropostaTransferenciaJogador> propostasSalvar = new ArrayList<PropostaTransferenciaJogador>();
		List<Jogador> jogadoresSalvar = new ArrayList<Jogador>();
		List<NecessidadeContratacaoClube> necessidadeContratacaoSalvar = new ArrayList<NecessidadeContratacaoClube>();
		List<DisponivelNegociacao> disponivelSalvar = new ArrayList<DisponivelNegociacao>();

		for (TransferenciaConcluidaDTO transferenciaConcluidaDTO : transferenciaConcluidaDTOs) {

			if (transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasTemporadaAtual()
					.isEmpty()) {
				estatisticasExcluir.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador()
						.getJogadorEstatisticasTemporadaAtual());
			}

			transferenciaConcluidaDTO.getPropostaAceita().setPropostaAceita(true);
			transferenciaConcluidaDTO.getPropostaAceita().setSemanaTransferencia(s);

			transferenciaConcluidaDTO.getPropostaAceita().getJogador()
					.setClube(transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino());
			transferenciaConcluidaDTO.getPropostaAceita().getJogador().setJogadorEstatisticasTemporadaAtual(
					new JogadorEstatisticasTemporada(transferenciaConcluidaDTO.getPropostaAceita().getJogador(),
							transferenciaConcluidaDTO.getPropostaAceita().getTemporada(),
							transferenciaConcluidaDTO.getPropostaAceita().getClubeDestino()));

			transferenciaConcluidaDTO.getPropostaAceita().getNecessidadeContratacaoClube()
					.setNecessidadeSatisfeita(true);

			transferenciaConcluidaDTO.getPropostasRejeitar().stream().forEach(p -> p.setPropostaAceita(false));

			transferenciaConcluidaDTO.getDisponivelNegociacao().setAtivo(false);

			propostasSalvar.add(transferenciaConcluidaDTO.getPropostaAceita());
			propostasSalvar.addAll(transferenciaConcluidaDTO.getPropostasRejeitar());
			jogadoresSalvar.add(transferenciaConcluidaDTO.getPropostaAceita().getJogador());
			estatisticasSalvar.add(
					transferenciaConcluidaDTO.getPropostaAceita().getJogador().getJogadorEstatisticasTemporadaAtual());
			necessidadeContratacaoSalvar
					.add(transferenciaConcluidaDTO.getPropostaAceita().getNecessidadeContratacaoClube());
			disponivelSalvar.add(transferenciaConcluidaDTO.getDisponivelNegociacao());

		}

		jogadorEstatisticasTemporadaRepository.saveAll(estatisticasSalvar);
		jogadorRepository.saveAll(jogadoresSalvar);
		jogadorEstatisticasTemporadaRepository.deleteAll(estatisticasExcluir);
		propostaTransferenciaJogadorRepository.saveAll(propostasSalvar);
		necessidadeContratacaoClubeRepository.saveAll(necessidadeContratacaoSalvar);
		disponivelNegociacaoRepository.saveAll(disponivelSalvar);

		// TODO: gerar entradas e saidas financeiras
	}
}
