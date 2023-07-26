package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AdequarModoDesenvolvimentoJogadorService {
	
	/*@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;*/
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> adequarModoDesenvolvimentoJogador(List<Jogador> jogadores) {
		
		//List<Jogador> jogadores = jogadorRepository.findByIdadeBetween(JogadorFactory.IDADE_MIN, 23);
		
		List<Jogador> detalheJogadoresSalvar = new ArrayList<Jogador>();
		
		for (Jogador j : jogadores) {
			
			/*int totalMinutosJogados = j.getEstatisticasTemporadaAtual()
					.getNumeroMinutosJogados()
					+ j.getEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados();*/
			
			int totalMinutosJogados = j.getEstatisticasTemporadaAtual() != null
					? j.getEstatisticasTemporadaAtual().getNumeroMinutosJogados()
					: 0;
			totalMinutosJogados += j.getEstatisticasAmistososTemporadaAtual() != null
					? j.getEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados()
					: 0;
			
			if (totalMinutosJogados < j.getModoDesenvolvimentoJogador().getNumeroMinimoMinutos()
					&& j.getIdade() >= 21) {

				if (ModoDesenvolvimentoJogador.TARDIO.equals(j.getModoDesenvolvimentoJogador())) {
					//Não fazer nada
				} else if (ModoDesenvolvimentoJogador.REGULAR.equals(j.getModoDesenvolvimentoJogador())) {
					j.setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador.TARDIO);
					detalheJogadoresSalvar.add(j);
				} else if (ModoDesenvolvimentoJogador.PRECOCE.equals(j.getModoDesenvolvimentoJogador())) {
					j.setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador.REGULAR);
					detalheJogadoresSalvar.add(j);
				}
			} else {
				
				if (ModoDesenvolvimentoJogador.TARDIO.equals(j.getModoDesenvolvimentoJogador())) {
					
					if (totalMinutosJogados >= ModoDesenvolvimentoJogador.REGULAR.getNumeroMinimoMinutos()) {
						j.setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador.REGULAR);
						detalheJogadoresSalvar.add(j);
					}
					
				} else if (ModoDesenvolvimentoJogador.REGULAR.equals(j.getModoDesenvolvimentoJogador())) {
					
					if (totalMinutosJogados >= ModoDesenvolvimentoJogador.PRECOCE.getNumeroMinimoMinutos()) {
						j.setModoDesenvolvimentoJogador(ModoDesenvolvimentoJogador.PRECOCE);
						detalheJogadoresSalvar.add(j);
					}
					
				} else if (ModoDesenvolvimentoJogador.PRECOCE.equals(j.getModoDesenvolvimentoJogador())) {
					//Não fazer nada
				}
				
			}
			
			
		}
		
		jogadorRepository.saveAll(detalheJogadoresSalvar);
		
		//System.err.println(detalheJogadoresSalvar.size() + ":" + jogadores.size());
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
}
