package com.fastfoot.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.SemanaService;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
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
		
		Semana s = semanaService.getSemanaAtual();
		
		propostaTransferenciaJogador.setPropostaAceita(true);
		propostaTransferenciaJogador.setSemanaTransferencia(s);
		
		propostaTransferenciaJogador.getJogador().setClube(propostaTransferenciaJogador.getClubeDestino());
		
		propostaTransferenciaJogador.getJogador()
				.setJogadorEstatisticasTemporadaAtual(new JogadorEstatisticasTemporada(
						propostaTransferenciaJogador.getJogador(), propostaTransferenciaJogador.getTemporada(),
						propostaTransferenciaJogador.getClubeDestino()));
		
		propostaTransferenciaJogador.getNecessidadeContratacaoClube().setNecessidadeSatisfeita(true);
		
		propostasRejeitar.stream().forEach(p -> p.setPropostaAceita(false));
		
		propostasRejeitar.add(propostaTransferenciaJogador);
		
		disponivelNegociacao.setAtivo(false);

		propostaTransferenciaJogadorRepository.saveAll(propostasRejeitar);
		jogadorEstatisticasTemporadaRepository.save(propostaTransferenciaJogador.getJogador().getJogadorEstatisticasTemporadaAtual());
		jogadorRepository.save(propostaTransferenciaJogador.getJogador());
		necessidadeContratacaoClubeRepository.save(propostaTransferenciaJogador.getNecessidadeContratacaoClube());
		disponivelNegociacaoRepository.save(disponivelNegociacao);

		//TODO: gerar entradas e saidas financeiras
		//TODO: ajustar numero do jogador
	}
}
