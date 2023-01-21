package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.club.model.repository.ClubeResumoTemporadaRepository;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.CampeonatoService;

@Service
public class GerarClubeResumoTemporadaService {
	
	@Autowired
	private ClubeResumoTemporadaRepository clubeResumoTemporadaRepository;

	@Autowired
	private CampeonatoService campeonatoService;

	public void gerarClubeResumoTemporada(Temporada temporada) {
				
		campeonatoService.carregarCampeonatosTemporada(temporada);
		
		List<ClubeResumoTemporada> clubesResumo = new ArrayList<ClubeResumoTemporada>();
		
		clubesResumo.addAll(gerarClubeResumoTemporadaCampeonatoNacional(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaCopaNacional(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaCampeonatoContinental(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaAmistosos(temporada));
		
		
		clubeResumoTemporadaRepository.saveAll(clubesResumo);
	}
	
	private List<ClubeResumoTemporada> gerarClubeResumoTemporadaCampeonatoNacional(Temporada temporada) {
		
		List<ClubeResumoTemporada> clubesResumo = new ArrayList<ClubeResumoTemporada>();
		
		ClubeResumoTemporada clubeResumoTemporada = null;
		
		for (Campeonato campeonato : temporada.getCampeonatosNacionais()) {
			for (Classificacao classificacao : campeonato.getClassificacao()) {
				clubeResumoTemporada = new ClubeResumoTemporada();
				
				clubeResumoTemporada.setClube(classificacao.getClube());
				clubeResumoTemporada.setTemporada(temporada);
				clubeResumoTemporada.setNivelCampeonato(campeonato.getNivelCampeonato());
				clubeResumoTemporada.setJogos(classificacao.getNumJogos());
				clubeResumoTemporada.setVitorias(classificacao.getVitorias());
				clubeResumoTemporada.setEmpates(classificacao.getEmpates());
				clubeResumoTemporada.setGolsPro(classificacao.getGolsPro());
				clubeResumoTemporada.setGolsContra(classificacao.getGolsContra());
				//clubeResumoTemporada.setPosicaoFinal(classificacao.getPosicao());
				
				clubeResumoTemporada.setClassificacaoNacional(ClassificacaoNacional
						.getClassificacao(campeonato.getNivelCampeonato(), classificacao.getPosicao()));
				
				clubesResumo.add(clubeResumoTemporada);
			}
		}
		
		return clubesResumo;
	}
	
	private List<ClubeResumoTemporada> gerarClubeResumoTemporadaCopaNacional(Temporada temporada) {

		List<ClubeResumoTemporada> clubesResumo = new ArrayList<ClubeResumoTemporada>();

		ClubeResumoTemporada clubeResumoTemporadaMandante = null;
		ClubeResumoTemporada clubeResumoTemporadaVisitante = null;

		for (CampeonatoEliminatorio campeonato : temporada.getCampeonatosCopasNacionais()) {

			List<PartidaEliminatoriaResultado> partidas = campeonato.getRodadas().stream()
					.flatMap(r -> r.getPartidas().stream()).collect(Collectors.toList());
			
			Map<Clube, ClubeResumoTemporada> clubeResumo = new HashMap<Clube, ClubeResumoTemporada>();
			
			for (PartidaEliminatoriaResultado partida : partidas) {
				
				if (clubeResumo.containsKey(partida.getClubeMandante())) {
					clubeResumoTemporadaMandante = clubeResumo.get(partida.getClubeMandante());
				} else {
					clubeResumoTemporadaMandante = new ClubeResumoTemporada();					
					clubeResumoTemporadaMandante.setClube(partida.getClubeMandante());
					clubeResumoTemporadaMandante.setTemporada(temporada);
					clubeResumoTemporadaMandante.setNivelCampeonato(campeonato.getNivelCampeonato());					
					clubeResumo.put(partida.getClubeMandante(), clubeResumoTemporadaMandante);
				}
				
				if (clubeResumo.containsKey(partida.getClubeVisitante())) {
					clubeResumoTemporadaVisitante = clubeResumo.get(partida.getClubeVisitante());
				} else {
					clubeResumoTemporadaVisitante = new ClubeResumoTemporada();
					clubeResumoTemporadaVisitante.setClube(partida.getClubeVisitante());
					clubeResumoTemporadaVisitante.setTemporada(temporada);
					clubeResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					clubeResumo.put(partida.getClubeVisitante(), clubeResumoTemporadaVisitante);
				}
				
				clubeResumoTemporadaMandante.setJogos(clubeResumoTemporadaMandante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					clubeResumoTemporadaMandante.setEmpates(clubeResumoTemporadaMandante.getEmpates() + 1);
				} else if (partida.isMandanteVencedor()) {
					clubeResumoTemporadaMandante.setVitorias(clubeResumoTemporadaMandante.getVitorias() + 1);
				}
				clubeResumoTemporadaMandante.setGolsPro(clubeResumoTemporadaMandante.getGolsPro() + partida.getGolsMandante());
				clubeResumoTemporadaMandante.setGolsContra(clubeResumoTemporadaMandante.getGolsContra() + partida.getGolsVisitante());
				if (partida.isMandanteEliminado()) {
					clubeResumoTemporadaMandante.setClassificacaoCopaNacional(
							ClassificacaoCopaNacional.getClassificacao(campeonato.getNivelCampeonato(),
									partida.getRodada(), campeonato.getRodadas().size(), false));
				}
				
				
				clubeResumoTemporadaVisitante.setJogos(clubeResumoTemporadaVisitante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					clubeResumoTemporadaVisitante.setEmpates(clubeResumoTemporadaVisitante.getEmpates() + 1);
				} else if (partida.isVisitanteVencedor()) {
					clubeResumoTemporadaVisitante.setVitorias(clubeResumoTemporadaVisitante.getVitorias() + 1);
				}
				clubeResumoTemporadaVisitante.setGolsPro(clubeResumoTemporadaVisitante.getGolsPro() + partida.getGolsVisitante());
				clubeResumoTemporadaVisitante.setGolsContra(clubeResumoTemporadaVisitante.getGolsContra() + partida.getGolsMandante());
				if (partida.isVisitanteEliminado()) {
					clubeResumoTemporadaVisitante.setClassificacaoCopaNacional(
							ClassificacaoCopaNacional.getClassificacao(campeonato.getNivelCampeonato(),
									partida.getRodada(), campeonato.getRodadas().size(), false));
				}

				if (partida.getRodada().getNumero() == campeonato.getRodadas().size()) {//Final
					if (partida.isMandanteEliminado()) {
						clubeResumoTemporadaVisitante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					} else if (partida.isVisitanteEliminado()) {
						clubeResumoTemporadaMandante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					}
				}

			}
			
			clubesResumo.addAll(clubeResumo.values());
		}
		
		return clubesResumo;
	}
	
	private List<ClubeResumoTemporada> gerarClubeResumoTemporadaCampeonatoContinental(Temporada temporada) {
		
		List<ClubeResumoTemporada> clubesResumo = new ArrayList<ClubeResumoTemporada>();
		
		ClubeResumoTemporada clubeResumoTemporada = null;
		ClubeResumoTemporada clubeResumoTemporadaMandante = null;
		ClubeResumoTemporada clubeResumoTemporadaVisitante = null;
		
		for (CampeonatoMisto campeonato : temporada.getCampeonatosContinentais()) {
			
			Map<Clube, ClubeResumoTemporada> clubeResumo = new HashMap<Clube, ClubeResumoTemporada>();
			
			for (GrupoCampeonato grupoCampeonato : campeonato.getGrupos()) {
			
				for (Classificacao classificacao : grupoCampeonato.getClassificacao()) {
					clubeResumoTemporada = new ClubeResumoTemporada();
					
					clubeResumoTemporada.setClube(classificacao.getClube());
					clubeResumoTemporada.setTemporada(temporada);
					clubeResumoTemporada.setNivelCampeonato(campeonato.getNivelCampeonato());
					clubeResumoTemporada.setJogos(classificacao.getNumJogos());
					clubeResumoTemporada.setVitorias(classificacao.getVitorias());
					clubeResumoTemporada.setEmpates(classificacao.getEmpates());
					clubeResumoTemporada.setGolsPro(classificacao.getGolsPro());
					clubeResumoTemporada.setGolsContra(classificacao.getGolsContra());
					//clubeResumoTemporada.setPosicaoFinal(classificacao.getPosicao());
					
					clubeResumoTemporada.setClassificacaoContinental(
							ClassificacaoContinental.getClassificacaoFaseGrupo(campeonato.getNivelCampeonato()));
					
					//clubesResumo.add(clubeResumoTemporada);
					
					clubeResumo.put(classificacao.getClube(), clubeResumoTemporada);
				}
			
			}
			
			List<PartidaEliminatoriaResultado> partidas = campeonato.getRodadasEliminatorias().stream()
					.flatMap(r -> r.getPartidas().stream()).collect(Collectors.toList());
			
			for (PartidaEliminatoriaResultado partida : partidas) {
				
				if (clubeResumo.containsKey(partida.getClubeMandante())) {
					clubeResumoTemporadaMandante = clubeResumo.get(partida.getClubeMandante());
				} else {
					clubeResumoTemporadaMandante = new ClubeResumoTemporada();					
					clubeResumoTemporadaMandante.setClube(partida.getClubeMandante());
					clubeResumoTemporadaMandante.setTemporada(temporada);
					clubeResumoTemporadaMandante.setNivelCampeonato(campeonato.getNivelCampeonato());					
					clubeResumo.put(partida.getClubeMandante(), clubeResumoTemporadaMandante);
				}
				
				if (clubeResumo.containsKey(partida.getClubeVisitante())) {
					clubeResumoTemporadaVisitante = clubeResumo.get(partida.getClubeVisitante());
				} else {
					clubeResumoTemporadaVisitante = new ClubeResumoTemporada();
					clubeResumoTemporadaVisitante.setClube(partida.getClubeVisitante());
					clubeResumoTemporadaVisitante.setTemporada(temporada);
					clubeResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					clubeResumo.put(partida.getClubeVisitante(), clubeResumoTemporadaVisitante);
				}
				
				clubeResumoTemporadaMandante.setJogos(clubeResumoTemporadaMandante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					clubeResumoTemporadaMandante.setEmpates(clubeResumoTemporadaMandante.getEmpates() + 1);
				} else if (partida.isMandanteVencedor()) {
					clubeResumoTemporadaMandante.setVitorias(clubeResumoTemporadaMandante.getVitorias() + 1);
				}
				clubeResumoTemporadaMandante.setGolsPro(clubeResumoTemporadaMandante.getGolsPro() + partida.getGolsMandante());
				clubeResumoTemporadaMandante.setGolsContra(clubeResumoTemporadaMandante.getGolsContra() + partida.getGolsVisitante());
				if (partida.isMandanteEliminado()) {
					clubeResumoTemporadaMandante.setClassificacaoContinental(ClassificacaoContinental
							.getClassificacao(campeonato.getNivelCampeonato(), partida.getRodada(), false));
				}
				
				
				clubeResumoTemporadaVisitante.setJogos(clubeResumoTemporadaVisitante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					clubeResumoTemporadaVisitante.setEmpates(clubeResumoTemporadaVisitante.getEmpates() + 1);
				} else if (partida.isVisitanteVencedor()) {
					clubeResumoTemporadaVisitante.setVitorias(clubeResumoTemporadaVisitante.getVitorias() + 1);
				}
				clubeResumoTemporadaVisitante.setGolsPro(clubeResumoTemporadaVisitante.getGolsPro() + partida.getGolsVisitante());
				clubeResumoTemporadaVisitante.setGolsContra(clubeResumoTemporadaVisitante.getGolsContra() + partida.getGolsMandante());
				if (partida.isVisitanteEliminado()) {
					clubeResumoTemporadaVisitante.setClassificacaoContinental(ClassificacaoContinental
							.getClassificacao(campeonato.getNivelCampeonato(), partida.getRodada(), false));
				}
				
				if (partida.getRodada().getNumero() == 6) {//Final
					if (partida.isMandanteEliminado()) {
						clubeResumoTemporadaVisitante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					} else if (partida.isVisitanteEliminado()) {
						clubeResumoTemporadaMandante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					}
				}
			}

			clubesResumo.addAll(clubeResumo.values());

		}
		
		return clubesResumo;
	}
	
	private List<ClubeResumoTemporada> gerarClubeResumoTemporadaAmistosos(Temporada temporada) {
		
		Map<Clube, ClubeResumoTemporada> clubesResumo = new HashMap<Clube, ClubeResumoTemporada>();		
		ClubeResumoTemporada clubeResumoTemporadaMandante = null;
		ClubeResumoTemporada clubeResumoTemporadaVisitante = null;		
		List<ClubeResumoTemporada> resumo = new ArrayList<ClubeResumoTemporada>();
		
		
		List<RodadaAmistosa> rodadasNacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_NACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());

		List<RodadaAmistosa> rodadasInternacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_INTERNACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());
		

		for (RodadaAmistosa r : rodadasNacional) {
			
			for (PartidaAmistosaResultado p : r.getPartidas()) {
				
				clubeResumoTemporadaMandante = clubesResumo.get(p.getClubeMandante());
				if (clubeResumoTemporadaMandante == null) {
					clubeResumoTemporadaMandante = new ClubeResumoTemporada();
					
					clubeResumoTemporadaMandante.setClube(p.getClubeMandante());
					clubeResumoTemporadaMandante.setTemporada(temporada);
					clubeResumoTemporadaMandante.setNivelCampeonato(r.getNivelCampeonato());
					
					clubesResumo.put(p.getClubeMandante(), clubeResumoTemporadaMandante);
				}
				
				clubeResumoTemporadaVisitante = clubesResumo.get(p.getClubeVisitante());
				if (clubeResumoTemporadaVisitante == null) {
					clubeResumoTemporadaVisitante = new ClubeResumoTemporada();
					
					clubeResumoTemporadaVisitante.setClube(p.getClubeVisitante());
					clubeResumoTemporadaVisitante.setTemporada(temporada);
					clubeResumoTemporadaVisitante.setNivelCampeonato(r.getNivelCampeonato());
					
					clubesResumo.put(p.getClubeVisitante(), clubeResumoTemporadaVisitante);
				}
				
				
				clubeResumoTemporadaMandante.setJogos(clubeResumoTemporadaMandante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					clubeResumoTemporadaMandante.setEmpates(clubeResumoTemporadaMandante.getEmpates() + 1);
				} else if (p.isMandanteVencedor()) {
					clubeResumoTemporadaMandante.setVitorias(clubeResumoTemporadaMandante.getVitorias() + 1);
				}
				clubeResumoTemporadaMandante.setGolsPro(clubeResumoTemporadaMandante.getGolsPro() + p.getGolsMandante());
				clubeResumoTemporadaMandante.setGolsContra(clubeResumoTemporadaMandante.getGolsContra() + p.getGolsVisitante());
				
				
				clubeResumoTemporadaVisitante.setJogos(clubeResumoTemporadaVisitante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					clubeResumoTemporadaVisitante.setEmpates(clubeResumoTemporadaVisitante.getEmpates() + 1);
				} else if (p.isVisitanteVencedor()) {
					clubeResumoTemporadaVisitante.setVitorias(clubeResumoTemporadaVisitante.getVitorias() + 1);
				}
				clubeResumoTemporadaVisitante.setGolsPro(clubeResumoTemporadaVisitante.getGolsPro() + p.getGolsVisitante());
				clubeResumoTemporadaVisitante.setGolsContra(clubeResumoTemporadaVisitante.getGolsContra() + p.getGolsMandante());
			}
			
		}
		
		
		resumo.addAll(clubesResumo.values());		
		clubesResumo = new HashMap<Clube, ClubeResumoTemporada>();
		
		for (RodadaAmistosa r : rodadasInternacional) {
			
			for (PartidaAmistosaResultado p : r.getPartidas()) {
				
				clubeResumoTemporadaMandante = clubesResumo.get(p.getClubeMandante());
				if (clubeResumoTemporadaMandante == null) {
					clubeResumoTemporadaMandante = new ClubeResumoTemporada();
					
					clubeResumoTemporadaMandante.setClube(p.getClubeMandante());
					clubeResumoTemporadaMandante.setTemporada(temporada);
					clubeResumoTemporadaMandante.setNivelCampeonato(r.getNivelCampeonato());
					
					clubesResumo.put(p.getClubeMandante(), clubeResumoTemporadaMandante);
				}
				
				clubeResumoTemporadaVisitante = clubesResumo.get(p.getClubeVisitante());
				if (clubeResumoTemporadaVisitante == null) {
					clubeResumoTemporadaVisitante = new ClubeResumoTemporada();
					
					clubeResumoTemporadaVisitante.setClube(p.getClubeVisitante());
					clubeResumoTemporadaVisitante.setTemporada(temporada);
					clubeResumoTemporadaVisitante.setNivelCampeonato(r.getNivelCampeonato());
					
					clubesResumo.put(p.getClubeVisitante(), clubeResumoTemporadaVisitante);
				}
				
				
				clubeResumoTemporadaMandante.setJogos(clubeResumoTemporadaMandante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					clubeResumoTemporadaMandante.setEmpates(clubeResumoTemporadaMandante.getEmpates() + 1);
				} else if (p.isMandanteVencedor()) {
					clubeResumoTemporadaMandante.setVitorias(clubeResumoTemporadaMandante.getVitorias() + 1);
				}
				clubeResumoTemporadaMandante.setGolsPro(clubeResumoTemporadaMandante.getGolsPro() + p.getGolsMandante());
				clubeResumoTemporadaMandante.setGolsContra(clubeResumoTemporadaMandante.getGolsContra() + p.getGolsVisitante());
				
				
				clubeResumoTemporadaVisitante.setJogos(clubeResumoTemporadaVisitante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					clubeResumoTemporadaVisitante.setEmpates(clubeResumoTemporadaVisitante.getEmpates() + 1);
				} else if (p.isVisitanteVencedor()) {
					clubeResumoTemporadaVisitante.setVitorias(clubeResumoTemporadaVisitante.getVitorias() + 1);
				}
				clubeResumoTemporadaVisitante.setGolsPro(clubeResumoTemporadaVisitante.getGolsPro() + p.getGolsVisitante());
				clubeResumoTemporadaVisitante.setGolsContra(clubeResumoTemporadaVisitante.getGolsContra() + p.getGolsMandante());
			}
		}
		
		resumo.addAll(clubesResumo.values());
		
		return resumo;
	}
}
