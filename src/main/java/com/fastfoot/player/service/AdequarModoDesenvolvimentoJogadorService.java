package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.ModoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class AdequarModoDesenvolvimentoJogadorService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> adequarModoDesenvolvimentoJogador(Temporada temporada, LigaJogo liga, boolean primeirosIds) {

		List<JogadorEstatisticasTemporada> jets;

		if (primeirosIds) {
			jets = jogadorEstatisticasTemporadaRepository.findByLigaJogoClubeAndStatusJogadorAndIdadeBetween(liga,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15, JogadorFactory.IDADE_MIN, 23);
		} else {
			jets = jogadorEstatisticasTemporadaRepository.findByLigaJogoClubeAndStatusJogadorAndIdadeBetween(liga,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal(), JogadorFactory.IDADE_MIN, 23);
		}

		Map<Jogador, List<JogadorEstatisticasTemporada>> jogadorEstatisticas = jets.stream()
				.collect(Collectors.groupingBy(JogadorEstatisticasTemporada::getJogador));

		List<Jogador> jogadores = new ArrayList<Jogador>(jogadorEstatisticas.keySet());
		
		List<JogadorEstatisticasTemporada> estatisticasJogador = null;
		Optional<JogadorEstatisticasTemporada> jogadorEstatisticasTemporadaOpt = null;
		Optional<JogadorEstatisticasTemporada> jogadorEstatisticasTemporadaAmistososOpt = null;

		for (Jogador jd : jogadores) {
			estatisticasJogador = jogadorEstatisticas.get(jd);
			
			if (!ValidatorUtil.isEmpty(estatisticasJogador)) {

				jogadorEstatisticasTemporadaOpt = estatisticasJogador.stream().filter(e -> !e.getAmistoso()).findFirst();
				jogadorEstatisticasTemporadaAmistososOpt = estatisticasJogador.stream().filter(e -> e.getAmistoso())
						.findFirst();
	
				if (jogadorEstatisticasTemporadaOpt.isPresent()) {
					jd.setJogadorEstatisticasTemporadaAtual(jogadorEstatisticasTemporadaOpt.get());
				}
	
				if (jogadorEstatisticasTemporadaAmistososOpt.isPresent()) {
					jd.setJogadorEstatisticasAmistososTemporadaAtual(jogadorEstatisticasTemporadaAmistososOpt.get());
				}
			}
		}

		adequarModoDesenvolvimentoJogador(jogadores);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	//@Async("defaultExecutor")
	private CompletableFuture<Boolean> adequarModoDesenvolvimentoJogador(List<Jogador> jogadores) {
		
		List<Jogador> detalheJogadoresSalvar = new ArrayList<Jogador>();
		
		for (Jogador j : jogadores) {
			
			/*int totalMinutosJogados = j.getEstatisticasTemporadaAtual()
					.getNumeroMinutosJogados()
					+ j.getEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados();*/
			
			int totalMinutosJogados = j.getJogadorEstatisticasTemporadaAtual() != null
					? j.getJogadorEstatisticasTemporadaAtual().getMinutosJogados()
					: 0;
			totalMinutosJogados += j.getJogadorEstatisticasAmistososTemporadaAtual() != null
					? j.getJogadorEstatisticasAmistososTemporadaAtual().getMinutosJogados()
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
		
		//System.err.println(detalheJogadoresSalvar.size());
		jogadorRepository.saveAll(detalheJogadoresSalvar);
		
		//System.err.println(detalheJogadoresSalvar.size() + ":" + jogadores.size());
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
}
