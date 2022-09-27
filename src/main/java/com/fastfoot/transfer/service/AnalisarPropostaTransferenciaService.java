package com.fastfoot.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.RoletaUtil;
import com.fastfoot.transfer.model.dto.TransferenciaConcluidaDTO;
import com.fastfoot.transfer.model.entity.DisponivelNegociacao;
import com.fastfoot.transfer.model.entity.PropostaTransferenciaJogador;
import com.fastfoot.transfer.model.repository.DisponivelNegociacaoRepository;

@Service
public class AnalisarPropostaTransferenciaService {

	//###	REPOSITORY	###

	/*@Autowired
	private PropostaTransferenciaJogadorRepository propostaTransferenciaJogadorRepository;*/
	
	@Autowired
	private DisponivelNegociacaoRepository disponivelNegociacaoRepository;
	
	//###	SERVICE	###
	
	/*@Autowired
	private TemporadaService temporadaService;*/
	
	@Autowired
	private ConcluirTransferenciaJogadorService concluirTransferenciaJogadorService;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> analisarPropostaTransferencia(Temporada temporada, List<Clube> clubes,
			Map<Clube, List<PropostaTransferenciaJogador>> propostasClube, Set<Clube> clubesRefazerEscalacao) {

		for (Clube c : clubes) {
			analisarPropostaTransferenciaClube(c, temporada, propostasClube.get(c), clubesRefazerEscalacao);
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> analisarPropostaTransferencia(Temporada temporada,
			Map<Clube, List<PropostaTransferenciaJogador>> propostasClube, Set<Clube> clubesRefazerEscalacao) {
		
		System.err.println("\t#c:" + propostasClube.keySet().size());

		for (Clube c : propostasClube.keySet()) {
			analisarPropostaTransferenciaClube(c, temporada, propostasClube.get(c), clubesRefazerEscalacao);
		}

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> analisarPropostaTransferencia(List<Clube> clubes){
		
		Temporada temporada = temporadaService.getTemporadaAtual();
		
		Set<Clube> clubesRefazerEscalacao = new HashSet<Clube>();
		
		for (Clube c : clubes) {
			List<PropostaTransferenciaJogador> propostas = propostaTransferenciaJogadorRepository //TODO: where propostaAceita is null?
					.findByTemporadaAndClubeOrigem(temporada, c);
			
			analisarPropostaTransferenciaClube(c, temporada, propostas, clubesRefazerEscalacao);
		}
		
		//System.err.println(clubesRefazerEscalacao);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	/*public void analisarPropostaTransferenciaClube(Clube clube, Temporada temporada,
			List<PropostaTransferenciaJogador> propostas, Set<Clube> clubesRefazerEscalacao) {

		Map<Jogador, List<PropostaTransferenciaJogador>> jogadorPropostas = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getJogador));

		List<PropostaTransferenciaJogador> propostasJog = null;
		PropostaTransferenciaJogador propostaAceitar = null;
		
		for (Jogador j : jogadorPropostas.keySet()) {
			
			propostasJog = jogadorPropostas.get(j);
			
			Optional<DisponivelNegociacao> disNegJogOpt = disponivelNegociacaoRepository
					.findFirstByTemporadaAndJogadorAndAtivo(temporada, j, true); 

			if (disNegJogOpt.isPresent()) {
				//propostaAceitar = RoletaUtil.sortearPesoUm(propostasJog);
				propostaAceitar = (PropostaTransferenciaJogador) RoletaUtil.executarN(propostasJog);
				
				propostasJog.remove(propostaAceitar);
				
				//System.err.println(Thread.currentThread().getName() + propostaAceitar.getClubeOrigem() + propostaAceitar.getClubeDestino());
				clubesRefazerEscalacao.add(propostaAceitar.getClubeOrigem());
				clubesRefazerEscalacao.add(propostaAceitar.getClubeDestino());
				concluirTransferenciaJogadorService.concluirTransferenciaJogador(propostaAceitar, propostasJog, disNegJogOpt.get());

			}

		}
	}*/
	
	private void analisarPropostaTransferenciaClube(Clube clube, Temporada temporada,
			List<PropostaTransferenciaJogador> propostas, Set<Clube> clubesRefazerEscalacao) {

		Map<Jogador, List<PropostaTransferenciaJogador>> jogadorPropostas = propostas.stream()
				.collect(Collectors.groupingBy(PropostaTransferenciaJogador::getJogador));

		List<PropostaTransferenciaJogador> propostasJog = null;
		PropostaTransferenciaJogador propostaAceitar = null;
		List<TransferenciaConcluidaDTO> transferenciaConcluidaDTOs = new ArrayList<TransferenciaConcluidaDTO>();
		
		for (Jogador j : jogadorPropostas.keySet()) {
			
			propostasJog = jogadorPropostas.get(j);
			
			Optional<DisponivelNegociacao> disNegJogOpt = disponivelNegociacaoRepository
					.findFirstByTemporadaAndJogadorAndAtivo(temporada, j, true); 

			if (disNegJogOpt.isPresent()) {
				//propostaAceitar = RoletaUtil.sortearPesoUm(propostasJog);
				propostaAceitar = (PropostaTransferenciaJogador) RoletaUtil.executarN(propostasJog);
				
				propostasJog.remove(propostaAceitar);
				
				//System.err.println(Thread.currentThread().getName() + propostaAceitar.getClubeOrigem() + propostaAceitar.getClubeDestino());
				clubesRefazerEscalacao.add(propostaAceitar.getClubeOrigem());
				clubesRefazerEscalacao.add(propostaAceitar.getClubeDestino());
				//concluirTransferenciaJogadorService.concluirTransferenciaJogador(propostaAceitar, propostasJog, disNegJogOpt.get());

				transferenciaConcluidaDTOs.add(new TransferenciaConcluidaDTO(propostaAceitar, propostasJog, disNegJogOpt.get()));
			}

		}
		
		concluirTransferenciaJogadorService.concluirTransferenciaJogadorEmLote(transferenciaConcluidaDTOs);
	}
}
