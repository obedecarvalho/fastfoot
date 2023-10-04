package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.TreinadorResumoTemporada;
import com.fastfoot.club.model.repository.TreinadorResumoTemporadaRepository;
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

@Service
public class GerarTreinadorResumoTemporadaService {

	@Autowired
	private TreinadorResumoTemporadaRepository treinadorResumoTemporadaRepository;

	/*@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;*/

	public void gerarTreinadorResumoTemporada(Temporada temporada) {
				
		//carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		
		List<TreinadorResumoTemporada> treinadoresResumo = new ArrayList<TreinadorResumoTemporada>();
		
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCampeonatoNacional(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCopaNacional(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCampeonatoContinental(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaAmistosos(temporada));
		
		
		treinadorResumoTemporadaRepository.saveAll(treinadoresResumo);
	}
	
	private List<TreinadorResumoTemporada> gerarTreinadorResumoTemporadaCampeonatoNacional(Temporada temporada) {
		
		List<TreinadorResumoTemporada> treinadoresResumo = new ArrayList<TreinadorResumoTemporada>();
		
		TreinadorResumoTemporada treinadorResumoTemporada = null;
		
		for (Campeonato campeonato : temporada.getCampeonatosNacionais()) {
			for (Classificacao classificacao : campeonato.getClassificacao()) {
				treinadorResumoTemporada = new TreinadorResumoTemporada();
				
				treinadorResumoTemporada.setTreinador(classificacao.getClube().getTreinador());
				treinadorResumoTemporada.setTemporada(temporada);
				treinadorResumoTemporada.setNivelCampeonato(campeonato.getNivelCampeonato());
				treinadorResumoTemporada.setJogos(classificacao.getNumJogos());
				treinadorResumoTemporada.setVitorias(classificacao.getVitorias());
				treinadorResumoTemporada.setEmpates(classificacao.getEmpates());
				treinadorResumoTemporada.setGolsPro(classificacao.getGolsPro());
				treinadorResumoTemporada.setGolsContra(classificacao.getGolsContra());
				//treinadorResumoTemporada.setPosicaoFinal(classificacao.getPosicao());
				
				treinadorResumoTemporada.setClassificacaoNacional(ClassificacaoNacional
						.getClassificacao(campeonato.getNivelCampeonato(), classificacao.getPosicao()));
				
				treinadoresResumo.add(treinadorResumoTemporada);
			}
		}
		
		return treinadoresResumo;
	}
	
	private List<TreinadorResumoTemporada> gerarTreinadorResumoTemporadaCopaNacional(Temporada temporada) {

		List<TreinadorResumoTemporada> treinadoresResumo = new ArrayList<TreinadorResumoTemporada>();

		TreinadorResumoTemporada treinadorResumoTemporadaMandante = null;
		TreinadorResumoTemporada treinadorResumoTemporadaVisitante = null;

		for (CampeonatoEliminatorio campeonato : temporada.getCampeonatosCopasNacionais()) {

			List<PartidaEliminatoriaResultado> partidas = campeonato.getRodadas().stream()
					.flatMap(r -> r.getPartidas().stream()).collect(Collectors.toList());
			
			Map<Clube, TreinadorResumoTemporada> treinadorResumo = new HashMap<Clube, TreinadorResumoTemporada>();
			
			for (PartidaEliminatoriaResultado partida : partidas) {
				
				if (treinadorResumo.containsKey(partida.getClubeMandante())) {
					treinadorResumoTemporadaMandante = treinadorResumo.get(partida.getClubeMandante());
				} else {
					treinadorResumoTemporadaMandante = new TreinadorResumoTemporada();					
					treinadorResumoTemporadaMandante.setTreinador(partida.getClubeMandante().getTreinador());
					treinadorResumoTemporadaMandante.setTemporada(temporada);
					treinadorResumoTemporadaMandante.setNivelCampeonato(campeonato.getNivelCampeonato());					
					treinadorResumo.put(partida.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				if (treinadorResumo.containsKey(partida.getClubeVisitante())) {
					treinadorResumoTemporadaVisitante = treinadorResumo.get(partida.getClubeVisitante());
				} else {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					treinadorResumoTemporadaVisitante.setTreinador(partida.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					treinadorResumo.put(partida.getClubeVisitante(), treinadorResumoTemporadaVisitante);
				}
				
				treinadorResumoTemporadaMandante.setJogos(treinadorResumoTemporadaMandante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					treinadorResumoTemporadaMandante.setEmpates(treinadorResumoTemporadaMandante.getEmpates() + 1);
				} else if (partida.isMandanteVencedor()) {
					treinadorResumoTemporadaMandante.setVitorias(treinadorResumoTemporadaMandante.getVitorias() + 1);
				}
				treinadorResumoTemporadaMandante.setGolsPro(treinadorResumoTemporadaMandante.getGolsPro() + partida.getGolsMandante());
				treinadorResumoTemporadaMandante.setGolsContra(treinadorResumoTemporadaMandante.getGolsContra() + partida.getGolsVisitante());
				if (partida.isMandanteEliminado()) {
					treinadorResumoTemporadaMandante.setClassificacaoCopaNacional(
							ClassificacaoCopaNacional.getClassificacao(campeonato.getNivelCampeonato(),
									partida.getRodada(), campeonato.getRodadas().size(), false));
				}
				
				
				treinadorResumoTemporadaVisitante.setJogos(treinadorResumoTemporadaVisitante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					treinadorResumoTemporadaVisitante.setEmpates(treinadorResumoTemporadaVisitante.getEmpates() + 1);
				} else if (partida.isVisitanteVencedor()) {
					treinadorResumoTemporadaVisitante.setVitorias(treinadorResumoTemporadaVisitante.getVitorias() + 1);
				}
				treinadorResumoTemporadaVisitante.setGolsPro(treinadorResumoTemporadaVisitante.getGolsPro() + partida.getGolsVisitante());
				treinadorResumoTemporadaVisitante.setGolsContra(treinadorResumoTemporadaVisitante.getGolsContra() + partida.getGolsMandante());
				if (partida.isVisitanteEliminado()) {
					treinadorResumoTemporadaVisitante.setClassificacaoCopaNacional(
							ClassificacaoCopaNacional.getClassificacao(campeonato.getNivelCampeonato(),
									partida.getRodada(), campeonato.getRodadas().size(), false));
				}

				if (partida.getRodada().getNumero() == campeonato.getRodadas().size()) {//Final
					if (partida.isMandanteEliminado()) {
						treinadorResumoTemporadaVisitante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					} else if (partida.isVisitanteEliminado()) {
						treinadorResumoTemporadaMandante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					}
				}

			}
			
			treinadoresResumo.addAll(treinadorResumo.values());
		}
		
		return treinadoresResumo;
	}
	
	private List<TreinadorResumoTemporada> gerarTreinadorResumoTemporadaCampeonatoContinental(Temporada temporada) {
		
		List<TreinadorResumoTemporada> treinadoresResumo = new ArrayList<TreinadorResumoTemporada>();
		
		TreinadorResumoTemporada treinadorResumoTemporada = null;
		TreinadorResumoTemporada treinadorResumoTemporadaMandante = null;
		TreinadorResumoTemporada treinadorResumoTemporadaVisitante = null;
		
		for (CampeonatoMisto campeonato : temporada.getCampeonatosContinentais()) {
			
			Map<Clube, TreinadorResumoTemporada> treinadorResumo = new HashMap<Clube, TreinadorResumoTemporada>();
			
			for (GrupoCampeonato grupoCampeonato : campeonato.getGrupos()) {
			
				for (Classificacao classificacao : grupoCampeonato.getClassificacao()) {
					treinadorResumoTemporada = new TreinadorResumoTemporada();
					
					treinadorResumoTemporada.setTreinador(classificacao.getClube().getTreinador());
					treinadorResumoTemporada.setTemporada(temporada);
					treinadorResumoTemporada.setNivelCampeonato(campeonato.getNivelCampeonato());
					treinadorResumoTemporada.setJogos(classificacao.getNumJogos());
					treinadorResumoTemporada.setVitorias(classificacao.getVitorias());
					treinadorResumoTemporada.setEmpates(classificacao.getEmpates());
					treinadorResumoTemporada.setGolsPro(classificacao.getGolsPro());
					treinadorResumoTemporada.setGolsContra(classificacao.getGolsContra());
					//treinadorResumoTemporada.setPosicaoFinal(classificacao.getPosicao());
					
					treinadorResumoTemporada.setClassificacaoContinental(
							ClassificacaoContinental.getClassificacaoFaseGrupo(campeonato.getNivelCampeonato()));
					
					//treinadorResumo.add(treinadorResumoTemporada);
					
					treinadorResumo.put(classificacao.getClube(), treinadorResumoTemporada);
				}
			
			}
			
			List<PartidaEliminatoriaResultado> partidas = campeonato.getRodadasEliminatorias().stream()
					.flatMap(r -> r.getPartidas().stream()).collect(Collectors.toList());
			
			for (PartidaEliminatoriaResultado partida : partidas) {
				
				if (treinadorResumo.containsKey(partida.getClubeMandante())) {
					treinadorResumoTemporadaMandante = treinadorResumo.get(partida.getClubeMandante());
				} else {
					treinadorResumoTemporadaMandante = new TreinadorResumoTemporada();					
					treinadorResumoTemporadaMandante.setTreinador(partida.getClubeMandante().getTreinador());
					treinadorResumoTemporadaMandante.setTemporada(temporada);
					treinadorResumoTemporadaMandante.setNivelCampeonato(campeonato.getNivelCampeonato());					
					treinadorResumo.put(partida.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				if (treinadorResumo.containsKey(partida.getClubeVisitante())) {
					treinadorResumoTemporadaVisitante = treinadorResumo.get(partida.getClubeVisitante());
				} else {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					treinadorResumoTemporadaVisitante.setTreinador(partida.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					treinadorResumo.put(partida.getClubeVisitante(), treinadorResumoTemporadaVisitante);
				}
				
				treinadorResumoTemporadaMandante.setJogos(treinadorResumoTemporadaMandante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					treinadorResumoTemporadaMandante.setEmpates(treinadorResumoTemporadaMandante.getEmpates() + 1);
				} else if (partida.isMandanteVencedor()) {
					treinadorResumoTemporadaMandante.setVitorias(treinadorResumoTemporadaMandante.getVitorias() + 1);
				}
				treinadorResumoTemporadaMandante.setGolsPro(treinadorResumoTemporadaMandante.getGolsPro() + partida.getGolsMandante());
				treinadorResumoTemporadaMandante.setGolsContra(treinadorResumoTemporadaMandante.getGolsContra() + partida.getGolsVisitante());
				if (partida.isMandanteEliminado()) {
					treinadorResumoTemporadaMandante.setClassificacaoContinental(ClassificacaoContinental
							.getClassificacao(campeonato.getNivelCampeonato(), partida.getRodada(), false));
				}
				
				
				treinadorResumoTemporadaVisitante.setJogos(treinadorResumoTemporadaVisitante.getJogos() + 1);
				if (partida.isResultadoEmpatado()) {
					treinadorResumoTemporadaVisitante.setEmpates(treinadorResumoTemporadaVisitante.getEmpates() + 1);
				} else if (partida.isVisitanteVencedor()) {
					treinadorResumoTemporadaVisitante.setVitorias(treinadorResumoTemporadaVisitante.getVitorias() + 1);
				}
				treinadorResumoTemporadaVisitante.setGolsPro(treinadorResumoTemporadaVisitante.getGolsPro() + partida.getGolsVisitante());
				treinadorResumoTemporadaVisitante.setGolsContra(treinadorResumoTemporadaVisitante.getGolsContra() + partida.getGolsMandante());
				if (partida.isVisitanteEliminado()) {
					treinadorResumoTemporadaVisitante.setClassificacaoContinental(ClassificacaoContinental
							.getClassificacao(campeonato.getNivelCampeonato(), partida.getRodada(), false));
				}
				
				if (partida.getRodada().getNumero() == 6) {//Final
					if (partida.isMandanteEliminado()) {
						treinadorResumoTemporadaVisitante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					} else if (partida.isVisitanteEliminado()) {
						treinadorResumoTemporadaMandante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoCampeao(campeonato.getNivelCampeonato()));
					}
				}
			}

			treinadoresResumo.addAll(treinadorResumo.values());

		}
		
		return treinadoresResumo;
	}
	
	private List<TreinadorResumoTemporada> gerarTreinadorResumoTemporadaAmistosos(Temporada temporada) {
		
		List<TreinadorResumoTemporada> resumo = new ArrayList<TreinadorResumoTemporada>();
		
		List<RodadaAmistosa> rodadasNacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_NACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());

		List<RodadaAmistosa> rodadasInternacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_INTERNACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());
		
		resumo.addAll(gerarTreinadorResumoTemporadaAmistosos(temporada, rodadasNacional));

		resumo.addAll(gerarTreinadorResumoTemporadaAmistosos(temporada, rodadasInternacional));
		
		return resumo;
	}
	
	private Collection<TreinadorResumoTemporada> gerarTreinadorResumoTemporadaAmistosos(Temporada temporada, List<RodadaAmistosa> rodadas) {
		
		Map<Clube, TreinadorResumoTemporada> treinadoresResumo = new HashMap<Clube, TreinadorResumoTemporada>();		
		TreinadorResumoTemporada treinadorResumoTemporadaMandante = null;
		TreinadorResumoTemporada treinadorResumoTemporadaVisitante = null;		
		
		for (RodadaAmistosa r : rodadas) {
			
			for (PartidaAmistosaResultado p : r.getPartidas()) {
				
				treinadorResumoTemporadaMandante = treinadoresResumo.get(p.getClubeMandante());
				if (treinadorResumoTemporadaMandante == null) {
					treinadorResumoTemporadaMandante = new TreinadorResumoTemporada();
					
					treinadorResumoTemporadaMandante.setTreinador(p.getClubeMandante().getTreinador());
					treinadorResumoTemporadaMandante.setTemporada(temporada);
					treinadorResumoTemporadaMandante.setNivelCampeonato(r.getNivelCampeonato());
					
					treinadoresResumo.put(p.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				treinadorResumoTemporadaVisitante = treinadoresResumo.get(p.getClubeVisitante());
				if (treinadorResumoTemporadaVisitante == null) {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					
					treinadorResumoTemporadaVisitante.setTreinador(p.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(r.getNivelCampeonato());
					
					treinadoresResumo.put(p.getClubeVisitante(), treinadorResumoTemporadaVisitante);
				}
				
				
				treinadorResumoTemporadaMandante.setJogos(treinadorResumoTemporadaMandante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					treinadorResumoTemporadaMandante.setEmpates(treinadorResumoTemporadaMandante.getEmpates() + 1);
				} else if (p.isMandanteVencedor()) {
					treinadorResumoTemporadaMandante.setVitorias(treinadorResumoTemporadaMandante.getVitorias() + 1);
				}
				treinadorResumoTemporadaMandante.setGolsPro(treinadorResumoTemporadaMandante.getGolsPro() + p.getGolsMandante());
				treinadorResumoTemporadaMandante.setGolsContra(treinadorResumoTemporadaMandante.getGolsContra() + p.getGolsVisitante());
				
				
				treinadorResumoTemporadaVisitante.setJogos(treinadorResumoTemporadaVisitante.getJogos() + 1);
				if (p.isResultadoEmpatado()) {
					treinadorResumoTemporadaVisitante.setEmpates(treinadorResumoTemporadaVisitante.getEmpates() + 1);
				} else if (p.isVisitanteVencedor()) {
					treinadorResumoTemporadaVisitante.setVitorias(treinadorResumoTemporadaVisitante.getVitorias() + 1);
				}
				treinadorResumoTemporadaVisitante.setGolsPro(treinadorResumoTemporadaVisitante.getGolsPro() + p.getGolsVisitante());
				treinadorResumoTemporadaVisitante.setGolsContra(treinadorResumoTemporadaVisitante.getGolsContra() + p.getGolsMandante());
			}
		}
		
		return treinadoresResumo.values();
	}
}
