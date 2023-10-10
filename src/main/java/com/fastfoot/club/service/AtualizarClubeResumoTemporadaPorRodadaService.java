package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.club.model.repository.ClubeResumoTemporadaRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.CarregarParametroService;

@Service
public class AtualizarClubeResumoTemporadaPorRodadaService {
	
	@Autowired
	private ClubeResumoTemporadaRepository clubeResumoTemporadaRepository;

	/*
	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;
	*/
	
	@Autowired
	private CarregarParametroService carregarParametroService;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarClubeResumoTemporadaPorRodada(Jogo jogo, RodadaJogavel rodada) {
		
		List<ClubeResumoTemporada> trts = clubeResumoTemporadaRepository
				.findByTemporada(rodada.getSemana().getTemporada());
		
		Map<Clube, Map<NivelCampeonato, List<ClubeResumoTemporada>>> clubeNivelResumos = trts.stream().collect(
				Collectors.groupingBy(trt -> trt.getClube(), Collectors.groupingBy(trt -> trt.getNivelCampeonato())));
		
		Map<Clube, Classificacao> clubeClassificacao = null;
		Integer primeiraPosicaoEliminadoContinental = null;
		if (rodada.getNivelCampeonato().isNIOuNII()
				&& rodada.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {
			clubeClassificacao = ((Rodada) rodada).getCampeonato().getClassificacao()
					.stream().collect(Collectors.toMap(Classificacao::getClube, Function.identity()));
		} else if (rodada.getNivelCampeonato().isCIOuCIIOuCIII()
				&& rodada.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
			clubeClassificacao = ((Rodada) rodada).getGrupoCampeonato().getClassificacao()
					.stream().collect(Collectors.toMap(Classificacao::getClube, Function.identity()));
			primeiraPosicaoEliminadoContinental = carregarParametroService.getPrimeiraPosicaoEliminadoContinental(jogo,
					rodada.getNivelCampeonato());
		}
		
		ClubeResumoTemporada crtMandante, crtVisitante;
		
		List<ClubeResumoTemporada> resumos = new ArrayList<ClubeResumoTemporada>();

		for (PartidaResultadoJogavel p : rodada.getPartidas()) {
			
			crtMandante = getClubeResumoTemporada(p.getClubeMandante(), rodada.getNivelCampeonato(),
					rodada.getSemana().getTemporada(), clubeNivelResumos);

			crtVisitante = getClubeResumoTemporada(p.getClubeVisitante(), rodada.getNivelCampeonato(),
					rodada.getSemana().getTemporada(), clubeNivelResumos);

			crtMandante.incrementarJogos();
			if (p.isMandanteVencedor()) crtMandante.incrementarVitorias();
			if (p.isResultadoEmpatado()) crtMandante.incrementarEmpates();
			crtMandante.incrementarGolsPro(p.getGolsMandante());
			crtMandante.incrementarGolsContra(p.getGolsVisitante());

			crtVisitante.incrementarJogos();
			if (p.isVisitanteVencedor()) crtVisitante.incrementarVitorias();
			if (p.isResultadoEmpatado()) crtVisitante.incrementarEmpates();
			crtVisitante.incrementarGolsPro(p.getGolsVisitante());
			crtVisitante.incrementarGolsContra(p.getGolsMandante());
			
			resumos.add(crtMandante);
			resumos.add(crtVisitante);
			
			if (rodada.getNivelCampeonato().isCIOuCIIOuCIII()) {

				if (rodada.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {

					if (clubeClassificacao.get(crtMandante.getClube()).getPosicao() >= primeiraPosicaoEliminadoContinental) {
						crtMandante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoFaseGrupo(rodada.getNivelCampeonato()));
					}
					
					if (clubeClassificacao.get(crtVisitante.getClube()).getPosicao() >= primeiraPosicaoEliminadoContinental) {
						crtVisitante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoFaseGrupo(rodada.getNivelCampeonato()));
					}

				} else if (rodada.getNumero() > Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
				
					if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
						crtMandante.setClassificacaoContinental(ClassificacaoContinental
								.getClassificacao(rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada, false));
					}
	
					if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
						crtVisitante.setClassificacaoContinental(ClassificacaoContinental
								.getClassificacao(rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada, false));
					}
	
					if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final
						if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
							crtVisitante.setClassificacaoContinental(
									ClassificacaoContinental.getClassificacaoCampeao(rodada.getNivelCampeonato()));
						} else if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
							crtMandante.setClassificacaoContinental(
									ClassificacaoContinental.getClassificacaoCampeao(rodada.getNivelCampeonato()));
						}
					}
				}
				
			} else if (rodada.getNivelCampeonato().isCNIOuCNII()) {

				if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
					crtMandante.setClassificacaoCopaNacional(ClassificacaoCopaNacional.getClassificacao(
							rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada,
							rodada.getCampeonatoJogavel().getTotalRodadas(), false));

				}

				if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
					crtVisitante.setClassificacaoCopaNacional(ClassificacaoCopaNacional.getClassificacao(
							rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada,
							rodada.getCampeonatoJogavel().getTotalRodadas(), false));
				}

				if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final
					if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
						crtVisitante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(rodada.getNivelCampeonato()));
					} else if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
						crtMandante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(rodada.getNivelCampeonato()));
					}
				}
				
			} else if (rodada.getNivelCampeonato().isNIOuNII()
					&& rodada.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {

				crtMandante.setClassificacaoNacional(ClassificacaoNacional.getClassificacao(rodada.getNivelCampeonato(),
						clubeClassificacao.get(crtMandante.getClube()).getPosicao()));

				crtVisitante.setClassificacaoNacional(ClassificacaoNacional.getClassificacao(
						rodada.getNivelCampeonato(), clubeClassificacao.get(crtVisitante.getClube()).getPosicao()));

			}

		}
		
		clubeResumoTemporadaRepository.saveAll(resumos);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private ClubeResumoTemporada getClubeResumoTemporada(Clube clube, NivelCampeonato nivel, Temporada temporada,
			Map<Clube, Map<NivelCampeonato, List<ClubeResumoTemporada>>> clubeNivelResumos) {

		Map<NivelCampeonato, List<ClubeResumoTemporada>> nivelResumos;
		List<ClubeResumoTemporada> resumos;

		nivelResumos = clubeNivelResumos.get(clube);

		if (nivelResumos == null) {

			resumos = new ArrayList<ClubeResumoTemporada>();
			nivelResumos = new HashMap<NivelCampeonato, List<ClubeResumoTemporada>>();

			ClubeResumoTemporada treinadorResumoTemporada = new ClubeResumoTemporada();
			treinadorResumoTemporada.setTemporada(temporada);
			treinadorResumoTemporada.setClube(clube);
			treinadorResumoTemporada.setNivelCampeonato(nivel);

			resumos.add(treinadorResumoTemporada);
			nivelResumos.put(nivel, resumos);
			clubeNivelResumos.put(clube, nivelResumos);

		} else {

			resumos = nivelResumos.get(nivel);

			if (resumos == null) {

				resumos = new ArrayList<ClubeResumoTemporada>();

				ClubeResumoTemporada treinadorResumoTemporada = new ClubeResumoTemporada();
				treinadorResumoTemporada.setTemporada(temporada);
				treinadorResumoTemporada.setClube(clube);
				treinadorResumoTemporada.setNivelCampeonato(nivel);

				resumos.add(treinadorResumoTemporada);
				nivelResumos.put(nivel, resumos);

			}
		}

		if (resumos.size() != 1) {
			throw new RuntimeException("Inesperado....");
		}

		return resumos.get(0);
	}

	/*
	public void gerarClubeResumoTemporada(Temporada temporada) {
				
		carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		
		List<ClubeResumoTemporada> clubesResumo = new ArrayList<ClubeResumoTemporada>();
		
		clubesResumo.addAll(gerarClubeResumoTemporadaCampeonatoNacional(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaCopaNacional(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaCampeonatoContinental(temporada));
		clubesResumo.addAll(gerarClubeResumoTemporadaAmistosos(temporada));
		
		
		clubeResumoTemporadaRepository.saveAll(clubesResumo);
	}
	*/
	
	/*
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
		
		List<ClubeResumoTemporada> resumo = new ArrayList<ClubeResumoTemporada>();
		
		List<RodadaAmistosa> rodadasNacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_NACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());

		List<RodadaAmistosa> rodadasInternacional = temporada.getRodadasAmistosas().stream()
				.filter(r -> NivelCampeonato.AMISTOSO_INTERNACIONAL.equals(r.getNivelCampeonato()))
				.collect(Collectors.toList());
		
		resumo.addAll(gerarClubeResumoTemporadaAmistosos(temporada, rodadasNacional));

		resumo.addAll(gerarClubeResumoTemporadaAmistosos(temporada, rodadasInternacional));
		
		return resumo;
	}
	
	private Collection<ClubeResumoTemporada> gerarClubeResumoTemporadaAmistosos(Temporada temporada, List<RodadaAmistosa> rodadas) {
		
		Map<Clube, ClubeResumoTemporada> clubesResumo = new HashMap<Clube, ClubeResumoTemporada>();		
		ClubeResumoTemporada clubeResumoTemporadaMandante = null;
		ClubeResumoTemporada clubeResumoTemporadaVisitante = null;		
		
		for (RodadaAmistosa r : rodadas) {
			
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
		
		return clubesResumo.values();
	}
	*/
}
