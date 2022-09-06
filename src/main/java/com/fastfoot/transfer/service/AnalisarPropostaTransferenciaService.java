package com.fastfoot.transfer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.TemporadaService;
import com.fastfoot.service.util.RoletaUtil;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;
import com.fastfoot.transfer.model.repository.PropostaTransferenciaJogadorRepository;

@Service
public class AnalisarPropostaTransferenciaService {

	//###	REPOSITORY	###

	@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private TemporadaService temporadaService;
	
	@Autowired
	private ConcluirTransferenciaJogadorService concluirTransferenciaJogadorService;
	
	@Async("transferenciaExecutor")
	public CompletableFuture<Boolean> analisarPropostaTransferencia(List<Clube> clubes){
		
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		for (Clube c : clubes) {
			analisarPropostaTransferenciaClube(c, temporada);
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	public void analisarPropostaTransferenciaClube(Clube clube, Temporada temporada) {//TODO: melhorar para caso em que há apenas 1 proposta transferencia

		List<PropostaTransferenciaJogador> propostas = propostaTransferenciaJogadorRepository
				.findByTemporadaAndClubeOrigem(temporada, clube);
		
		Map<Jogador, List<PropostaTransferenciaJogador>> jogadorPropostas = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getJogador));

		List<PropostaTransferenciaJogador> propostasJog = null;
		PropostaTransferenciaJogador propostaAceitar = null;
		
		for (Jogador j : jogadorPropostas.keySet()) {
			
			propostasJog = jogadorPropostas.get(j);
			
			Optional<DisponivelNegociacao> disNegJogOpt = disponivelNegociacaoRepository
					.findFirstByTemporadaAndJogadorAndAtivo(temporada, j, true); 
			
			//System.err.println(propostasJog.stream().map(p -> p.getValor()).collect(Collectors.toList()));
			
			if (disNegJogOpt.isPresent()) {
				//propostaAceitar = RoletaUtil.sortearPesoUm(propostasJog);
				propostaAceitar = (PropostaTransferenciaJogador) RoletaUtil.executarN(propostasJog);
				
				propostasJog.remove(propostaAceitar);
				
				concluirTransferenciaJogadorService.concluirTransferenciaJogador(propostaAceitar, propostasJog, disNegJogOpt.get());

			}
			
			
		}
	}
}
