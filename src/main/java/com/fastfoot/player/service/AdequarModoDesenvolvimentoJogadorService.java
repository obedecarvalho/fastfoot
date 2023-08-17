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

import com.fastfoot.model.Liga;
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
	
	/*@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;*/
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> adequarModoDesenvolvimentoJogador(Temporada temporada, Liga liga, boolean primeirosIds) {

		List<JogadorEstatisticasTemporada> jets;

		if (primeirosIds) {
			jets = jogadorEstatisticasTemporadaRepository.findByLigaClubeAndStatusJogadorAndIdadeBetween(liga,
					liga.getIdBaseLiga() + 1, liga.getIdBaseLiga() + 16, JogadorFactory.IDADE_MIN, 23);
		} else {
			jets = jogadorEstatisticasTemporadaRepository.findByLigaClubeAndStatusJogadorAndIdadeBetween(liga,
					liga.getIdBaseLiga() + 17, liga.getIdBaseLiga() + 32, JogadorFactory.IDADE_MIN, 23);
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

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> adequarModoDesenvolvimentoJogador(List<Jogador> jogadores) {
		
		//List<Jogador> jogadores = jogadorRepository.findByIdadeBetween(JogadorFactory.IDADE_MIN, 23);
		
		List<Jogador> detalheJogadoresSalvar = new ArrayList<Jogador>();
		
		for (Jogador j : jogadores) {
			
			/*int totalMinutosJogados = j.getEstatisticasTemporadaAtual()
					.getNumeroMinutosJogados()
					+ j.getEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados();*/
			
			int totalMinutosJogados = j.getJogadorEstatisticasTemporadaAtual() != null
					? j.getJogadorEstatisticasTemporadaAtual().getNumeroMinutosJogados()
					: 0;
			totalMinutosJogados += j.getJogadorEstatisticasAmistososTemporadaAtual() != null
					? j.getJogadorEstatisticasAmistososTemporadaAtual().getNumeroMinutosJogados()
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
