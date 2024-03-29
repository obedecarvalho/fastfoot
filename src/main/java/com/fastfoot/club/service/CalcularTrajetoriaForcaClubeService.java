package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.TrajetoriaForcaClube;
import com.fastfoot.club.model.repository.TrajetoriaForcaClubeRepository;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;

@Service
public class CalcularTrajetoriaForcaClubeService {
	
	/*private static final Comparator<Jogador> COMPARATOR = new Comparator<Jogador>() {
		
		@Override
		public int compare(Jogador o1, Jogador o2) {
			return o1.getForcaGeralPotencial().compareTo(o2.getForcaGeralPotencial());
		}
	};*/
	
	//###	REPOSITORY	###
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private TrajetoriaForcaClubeRepository trajetoriaForcaClubeRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularTrajetoriaForcaClube(LigaJogo liga, boolean primeirosIds, Semana semana){
		
		List<TrajetoriaForcaClube> trajetoriaForcaClubes = new ArrayList<TrajetoriaForcaClube>();
		
		List<Jogador> jogadores;
		
		if (primeirosIds) {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial(), liga.getIdClubeInicial() + 15);
		} else {
			jogadores = jogadorRepository.findByLigaJogoClubeAndStatusJogador(liga, StatusJogador.ATIVO,
					liga.getIdClubeInicial() + 16, liga.getIdClubeFinal());
		}

		Map<Clube, List<Jogador>> jogadoresPorClube = jogadores.stream().collect(Collectors.groupingBy(j -> j.getClube()));

		for (Clube clube : jogadoresPorClube.keySet()) {
			calcularTrajetoriaForcaClube(clube, jogadores, trajetoriaForcaClubes, semana);
		}

		trajetoriaForcaClubeRepository.saveAll(trajetoriaForcaClubes);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	/*
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> calcularTrajetoriaForcaClube(List<Clube> clubes, Semana semana){
		
		List<TrajetoriaForcaClube> trajetoriaForcaClubes = new ArrayList<TrajetoriaForcaClube>();
		
		List<Jogador> jogadores;
		
		for (Clube clube : clubes) {
			jogadores = jogadorRepository.findByClubeAndStatusJogador(clube, StatusJogador.ATIVO);
			calcularTrajetoriaForcaClube(clube, jogadores, trajetoriaForcaClubes, semana);
		}
		
		trajetoriaForcaClubeRepository.saveAll(trajetoriaForcaClubes);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	*/

	private void calcularTrajetoriaForcaClube(Clube clube, List<Jogador> jogadores, List<TrajetoriaForcaClube> trajetoriaForcaClubes, Semana semana) {
		
		TrajetoriaForcaClube trajetoriaForcaClube = new TrajetoriaForcaClube();
		
		trajetoriaForcaClube.setClube(clube);
		
		trajetoriaForcaClube.setSemana(semana);

		int numTemporadas = JogadorFactory.IDADE_MAX - JogadorFactory.IDADE_MIN;
		
		int qtdeGol = (int) jogadores.stream().filter(j -> j.getPosicao().isGoleiro()).count();
		int qtdeZag = (int) jogadores.stream().filter(j -> j.getPosicao().isZagueiro()).count();
		int qtdeLat = (int) jogadores.stream().filter(j -> j.getPosicao().isLateral()).count();
		int qtdeVol = (int) jogadores.stream().filter(j -> j.getPosicao().isVolante()).count();
		int qtdeMei = (int) jogadores.stream().filter(j -> j.getPosicao().isMeia()).count();
		int qtdeAta = (int) jogadores.stream().filter(j -> j.getPosicao().isAtacante()).count();
		
		List<Double> forcaJogTemporada = new ArrayList<Double>();
		List<Jogador> jogadorTitularTemporada = new ArrayList<Jogador>();
		
		List<String> idadeMediaTitulares = new ArrayList<String>();
		List<String> forcaMediaTitulares = new ArrayList<String>();

		for (int i = 0; i < numTemporadas; i++) {
			
			forcaJogTemporada.clear();
			jogadorTitularTemporada.clear();
			
			for (Jogador j : jogadores) {
				j.setIdade(JogadorFactory.IDADE_MIN + ((j.getIdade() + 1 - JogadorFactory.IDADE_MIN) % numTemporadas));
				/*j.setForcaGeralPotencialEfetiva(j.getForcaGeralPotencial()
						* JogadorFactory.VALOR_AJUSTE.get(j.getIdade() - JogadorFactory.IDADE_MIN));*/
				j.setForcaGeralPotencial(j.getForcaGeralPotencial()
						* j.getModoDesenvolvimentoJogador().getValorAjuste()[j.getIdade() - JogadorFactory.IDADE_MIN]);
			}
			
			//TODO: verificar se há jogadores para serem escalados como titular (jogador da posicao faltante)

			//G
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isGoleiro()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeGol - 1));
			
			//Z
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isZagueiro()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeZag - 1));
			
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isZagueiro()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeZag - 2));
			
			//L
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isLateral()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeLat - 1));
			
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isLateral()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeLat - 2));
			
			//V
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isVolante()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeVol - 1));
			
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isVolante()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeVol - 2));
			
			//M
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isMeia()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeMei - 1));
			
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isMeia()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeMei - 2));
			
			//A
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isAtacante()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeAta - 1));
			
			jogadorTitularTemporada.add(jogadores.stream().filter(j -> j.getPosicao().isAtacante()).sorted(JogadorFactory.getComparatorForcaGeralPotencial())
					.collect(Collectors.toList()).get(qtdeAta - 2));
			
			idadeMediaTitulares.add(String.format(Locale.US, "%.2f",
					jogadorTitularTemporada.stream().mapToInt(Jogador::getIdade).average().getAsDouble()));
			forcaMediaTitulares.add(String.format(Locale.US, "%.2f", jogadorTitularTemporada.stream()
					.mapToDouble(Jogador::getForcaGeralPotencial).average().getAsDouble()));
			
		}
		
		trajetoriaForcaClube.setTrajetoriaForcaTitulares(forcaMediaTitulares.toString());
		trajetoriaForcaClube.setTrajetoriaIdadeTitulares(idadeMediaTitulares.toString());
		
		trajetoriaForcaClubes.add(trajetoriaForcaClube);
		
		//System.err.println(idadeMediaTitulares);
		//System.err.println(forcaMediaTitulares);
	}
}
