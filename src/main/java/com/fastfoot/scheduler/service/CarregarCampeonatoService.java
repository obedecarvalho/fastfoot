package com.fastfoot.scheduler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.scheduler.model.CampeonatoJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
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
public class CarregarCampeonatoService {

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
	
	@Autowired
	private RodadaAmistoraRepository rodadaAmistoraRepository;
	
	@Autowired
	private PartidaAmistosaResultadoRepository partidaAmistosaResultadoRepository;
	
	public List<? extends CampeonatoJogavel> carregarCampeonatosTemporada(Temporada temporada, String nivel) {//'NACIONAL', 'COPA NACIONAL', 'CONTINENTAL'
		List<? extends CampeonatoJogavel> campeonatos = null;
		
		switch (nivel) {
		case "NACIONAL":
			campeonatos = campeonatoRepository.findByTemporada(temporada);
			break;
		case "COPA-NACIONAL":
			campeonatos = campeonatoEliminatorioRepository.findByTemporada(temporada);
			break;
		case "CONTINENTAL":
			campeonatos = campeonatoMistoRepository.findByTemporada(temporada);
			break;

		default:
			break;
		}
		return campeonatos;
	}

	public void carregarCampeonatosTemporada(Temporada temporada) {
		carregarCampeonatosNacionais(temporada);
		carregarCopasNacionais(temporada);
		carregarCampeonatosContinentais(temporada);
		carregarAmistosos(temporada);
	}

	private void carregarCampeonatosNacionais(Temporada temporada) {
		List<Rodada> rodadas = null;
		List<Campeonato> campeonatos = campeonatoRepository.findByTemporada(temporada);
		temporada.setCampeonatosNacionais(campeonatos);

		for (Campeonato c : campeonatos) {
			c.setClassificacao(classificacaoRepository.findByCampeonato(c));
			rodadas = rodadaRepository.findByCampeonato(c);
			c.setRodadas(rodadas);
			for (Rodada r : rodadas) {
				r.setPartidas(partidaRepository.findByRodada(r));
			}
		}
	}

	private void carregarCopasNacionais(Temporada temporada) {
		List<RodadaEliminatoria> rodadas = null;
		List<CampeonatoEliminatorio> campeonatos = campeonatoEliminatorioRepository.findByTemporada(temporada);
		temporada.setCampeonatosCopasNacionais(campeonatos);
		
		for (CampeonatoEliminatorio c : campeonatos) {
			rodadas = rodadaEliminatoriaRepository.findByCampeonatoEliminatorio(c);
			c.setRodadas(rodadas);
			for (RodadaEliminatoria r : rodadas) {
				r.setPartidas(partidaEliminatoriaRepository.findByRodada(r));
			}
		}
	}

	private void carregarCampeonatosContinentais(Temporada temporada) {
		List<Rodada> rodadas = null;
		List<RodadaEliminatoria> rodadasEliminatorias = null;
		List<GrupoCampeonato> grupos = null;
		List<CampeonatoMisto> campeonatos = campeonatoMistoRepository.findByTemporada(temporada);
		temporada.setCampeonatosContinentais(campeonatos);

		for (CampeonatoMisto c : campeonatos) {
			//Fase final
			rodadasEliminatorias = rodadaEliminatoriaRepository.findByCampeonatoMisto(c);
			c.setRodadasEliminatorias(rodadasEliminatorias);
			for (RodadaEliminatoria r : rodadasEliminatorias) {
				r.setPartidas(partidaEliminatoriaRepository.findByRodada(r));
			}

			//Grupos
			grupos = grupoCampeonatoRepository.findByCampeonato(c);
			c.setGrupos(grupos);
			for (GrupoCampeonato gc : grupos) {
				gc.setClassificacao(classificacaoRepository.findByGrupoCampeonato(gc));
				rodadas = rodadaRepository.findByGrupoCampeonato(gc);
				gc.setRodadas(rodadas);
				for (Rodada r : rodadas) {
					r.setPartidas(partidaRepository.findByRodada(r));
				}
			}
		}
	}
	
	private void carregarAmistosos(Temporada temporada) {

		temporada.setRodadasAmistosas(rodadaAmistoraRepository.findByTemporada(temporada));

		for (RodadaAmistosa r : temporada.getRodadasAmistosas()) {
			r.setPartidas(partidaAmistosaResultadoRepository.findByRodada(r));
		}

	}

	public void salvarCampeonatoEliminatorio(List<CampeonatoEliminatorio> campeonatos) {
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
	}

	public void salvarCampeonatoMisto(List<CampeonatoMisto> campeonatos) {
		
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

	}

	public void salvarCampeonato(List<Campeonato> campeonatos) {
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
	}
}
