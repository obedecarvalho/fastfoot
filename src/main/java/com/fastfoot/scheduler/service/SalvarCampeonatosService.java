package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.repository.CampeonatoEliminatorioRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoMistoRepository;
import com.fastfoot.scheduler.model.repository.CampeonatoRepository;
import com.fastfoot.scheduler.model.repository.ClassificacaoRepository;
import com.fastfoot.scheduler.model.repository.GrupoCampeonatoRepository;
import com.fastfoot.scheduler.model.repository.PartidaAmistosaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaEliminatoriaResultadoRepository;
import com.fastfoot.scheduler.model.repository.PartidaResultadoRepository;
import com.fastfoot.scheduler.model.repository.RodadaAmistoraRepository;
import com.fastfoot.scheduler.model.repository.RodadaEliminatoriaRepository;
import com.fastfoot.scheduler.model.repository.RodadaRepository;

@Service
public class SalvarCampeonatosService {
	
	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;
	
	@Autowired
	private CampeonatoRepository campeonatoRepository;

	@Autowired
	private CampeonatoMistoRepository campeonatoMistoRepository;

	@Autowired
	private CampeonatoEliminatorioRepository campeonatoEliminatorioRepository;

	@Autowired
	private RodadaRepository rodadaRepository;

	@Autowired
	private RodadaEliminatoriaRepository rodadaEliminatoriaRepository;

	@Autowired
	private PartidaResultadoRepository partidaRepository;

	@Autowired
	private PartidaEliminatoriaResultadoRepository partidaEliminatoriaRepository;

	@Autowired
	private GrupoCampeonatoRepository grupoCampeonatoRepository;

	@Autowired
	private ClassificacaoRepository classificacaoRepository;

	/*@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;*/

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> salvarRodadasAmistosas(List<RodadaAmistosa> rodadaAmistosas) {

		List<PartidaAmistosaResultado> partidas = new ArrayList<PartidaAmistosaResultado>();

		for (RodadaAmistosa r : rodadaAmistosas) {
			partidas.addAll(r.getPartidas());
		}

		rodadaAmistoraRepository.saveAll(rodadaAmistosas);
		partidaAmistosaResultadoRepository.saveAll(partidas);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> salvarCampeonato(List<Campeonato> campeonatos) {

		List<Classificacao> classificacao = new ArrayList<Classificacao>();
		List<Rodada> rodadas = new ArrayList<Rodada>();
		List<PartidaResultado> partidas = new ArrayList<PartidaResultado>();

		for (Campeonato c : campeonatos) {
			classificacao.addAll(c.getClassificacao());
			rodadas.addAll(c.getRodadas());
			
			for (Rodada r : c.getRodadas()) {
				partidas.addAll(r.getPartidas());
			}
		}

		campeonatoRepository.saveAll(campeonatos);
		classificacaoRepository.saveAll(classificacao);
		rodadaRepository.saveAll(rodadas);
		partidaRepository.saveAll(partidas);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> salvarCampeonatoEliminatorio(List<CampeonatoEliminatorio> campeonatos) {

		List<RodadaEliminatoria> rodadas = new ArrayList<RodadaEliminatoria>();
		List<PartidaEliminatoriaResultado> partidas = new ArrayList<PartidaEliminatoriaResultado>();

		for (CampeonatoEliminatorio c : campeonatos) {
			rodadas.addAll(c.getRodadas());
			for (RodadaEliminatoria r : c.getRodadas()) {
				partidas.addAll(r.getPartidas());
			}
		}
		
		//tem que ordenar por causa da referencia de 'proximaRodada'
		//
		Collections.sort(rodadas, new Comparator<RodadaEliminatoria>() {

			@Override
			public int compare(RodadaEliminatoria o1, RodadaEliminatoria o2) {
				return o2.getNumero().compareTo(o1.getNumero());
			}
		});
		//
		
		campeonatoEliminatorioRepository.saveAll(campeonatos);
		rodadaEliminatoriaRepository.saveAll(rodadas);
		partidaEliminatoriaRepository.saveAll(partidas);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> salvarCampeonatoMisto(List<CampeonatoMisto> campeonatos) {
		
		List<GrupoCampeonato> grupos = new ArrayList<GrupoCampeonato>();
		List<Classificacao> classificacao = new ArrayList<Classificacao>();
		List<Rodada> rodadas = new ArrayList<Rodada>();
		List<PartidaResultado> partidas = new ArrayList<PartidaResultado>();
		
		List<RodadaEliminatoria> rodadasFinal = new ArrayList<RodadaEliminatoria>();
		List<PartidaEliminatoriaResultado> partidasFinal = new ArrayList<PartidaEliminatoriaResultado>();
		
		for (CampeonatoMisto c : campeonatos) {
			
			rodadasFinal.addAll(c.getRodadasEliminatorias());

			for (RodadaEliminatoria r : c.getRodadasEliminatorias()) {
				partidasFinal.addAll(r.getPartidas());
			}
			
			grupos.addAll(c.getGrupos());
			
			for (GrupoCampeonato gc : c.getGrupos()) {
				
				classificacao.addAll(gc.getClassificacao());
				
				rodadas.addAll(gc.getRodadas());
				
				for (Rodada r : gc.getRodadas()) {
					partidas.addAll(r.getPartidas());
				}
			}
		}

		//tem que ordenar por causa da referencia de 'proximaRodada'
		//
		Collections.sort(rodadasFinal, new Comparator<RodadaEliminatoria>() {

			@Override
			public int compare(RodadaEliminatoria o1, RodadaEliminatoria o2) {
				return o2.getNumero().compareTo(o1.getNumero());
			}
		});
		//

		campeonatoMistoRepository.saveAll(campeonatos);

		rodadaEliminatoriaRepository.saveAll(rodadasFinal);
		partidaEliminatoriaRepository.saveAll(partidasFinal);

		grupoCampeonatoRepository.saveAll(grupos);
		classificacaoRepository.saveAll(classificacao);
		rodadaRepository.saveAll(rodadas);
		partidaRepository.saveAll(partidas);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> criarEstatisticasJogadorTemporada(List<Jogador> jogadores, Temporada temporada) {
		
		jogadores.stream().forEach(j -> j.setJogadorEstatisticasTemporadaAtual(
				new JogadorEstatisticasTemporada(j, temporada, j.getClube(), false)));
		jogadorEstatisticasTemporadaRepository.saveAll(
				jogadores.stream().map(Jogador::getJogadorEstatisticasTemporadaAtual).collect(Collectors.toList()));
		
		jogadores.stream().forEach(j -> j.setJogadorEstatisticasAmistososTemporadaAtual(
				new JogadorEstatisticasTemporada(j, temporada, j.getClube(), true)));
		jogadorEstatisticasTemporadaRepository.saveAll(jogadores.stream()
				.map(Jogador::getJogadorEstatisticasAmistososTemporadaAtual).collect(Collectors.toList()));

		jogadorRepository.saveAll(jogadores);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
}
