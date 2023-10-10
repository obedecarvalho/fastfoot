package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.Collection;
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
import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.entity.TreinadorResumoTemporada;
import com.fastfoot.club.model.repository.TreinadorResumoTemporadaRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.CarregarCampeonatoService;
import com.fastfoot.service.CarregarParametroService;

@Service
public class GerarTreinadorResumoTemporadaService {//TODO: renomear para AtualizarTreinadorResumoTemporadaPorRodadaService

	@Autowired
	private TreinadorResumoTemporadaRepository treinadorResumoTemporadaRepository;

	@Autowired
	private CarregarParametroService carregarParametroService;
	
	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;

	@Deprecated
	public void gerarTreinadorResumoTemporada(Temporada temporada) {
		
		carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
				
		List<TreinadorResumoTemporada> treinadoresResumo = new ArrayList<TreinadorResumoTemporada>();
		
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCampeonatoNacional(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCopaNacional(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaCampeonatoContinental(temporada));
		treinadoresResumo.addAll(gerarTreinadorResumoTemporadaAmistosos(temporada));
		
		
		treinadorResumoTemporadaRepository.saveAll(treinadoresResumo);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarTreinadorResumoTemporadaPorRodada(Jogo jogo, RodadaJogavel rodada) {
		
		List<TreinadorResumoTemporada> trts = treinadorResumoTemporadaRepository
				.findByTemporada(rodada.getSemana().getTemporada());
		
		Map<Treinador, Map<Clube, Map<NivelCampeonato, List<TreinadorResumoTemporada>>>> treinadorClubeNivelResumos = trts.stream()
				.collect(Collectors.groupingBy(trt -> trt.getTreinador(), Collectors.groupingBy(trt -> trt.getClube(),
						Collectors.groupingBy(trt -> trt.getNivelCampeonato()))));
		
		
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
		
		
		TreinadorResumoTemporada trtMandante, trtVisitante;
		
		List<TreinadorResumoTemporada> resumos = new ArrayList<TreinadorResumoTemporada>();

		for (PartidaResultadoJogavel p : rodada.getPartidas()) {
			
			trtMandante = getTreinadorResumoTemporada(p.getClubeMandante().getTreinador(), p.getClubeMandante(),
					rodada.getNivelCampeonato(), rodada.getSemana().getTemporada(), treinadorClubeNivelResumos);

			trtVisitante = getTreinadorResumoTemporada(p.getClubeVisitante().getTreinador(), p.getClubeVisitante(),
					rodada.getNivelCampeonato(), rodada.getSemana().getTemporada(), treinadorClubeNivelResumos);

			trtMandante.incrementarJogos();
			if (p.isMandanteVencedor()) trtMandante.incrementarVitorias();
			if (p.isResultadoEmpatado()) trtMandante.incrementarEmpates();
			trtMandante.incrementarGolsPro(p.getGolsMandante());
			trtMandante.incrementarGolsContra(p.getGolsVisitante());

			trtVisitante.incrementarJogos();
			if (p.isVisitanteVencedor()) trtVisitante.incrementarVitorias();
			if (p.isResultadoEmpatado()) trtVisitante.incrementarEmpates();
			trtVisitante.incrementarGolsPro(p.getGolsVisitante());
			trtVisitante.incrementarGolsContra(p.getGolsMandante());
			
			resumos.add(trtMandante);
			resumos.add(trtVisitante);
			
			if (rodada.getNivelCampeonato().isCIOuCIIOuCIII()) {

				if (rodada.getNumero() == Constantes.NRO_PARTIDAS_FASE_GRUPOS) {

					if (clubeClassificacao.get(trtMandante.getClube()).getPosicao() >= primeiraPosicaoEliminadoContinental) {
						trtMandante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoFaseGrupo(rodada.getNivelCampeonato()));
					}
					
					if (clubeClassificacao.get(trtVisitante.getClube()).getPosicao() >= primeiraPosicaoEliminadoContinental) {
						trtVisitante.setClassificacaoContinental(
								ClassificacaoContinental.getClassificacaoFaseGrupo(rodada.getNivelCampeonato()));
					}

				} else if (rodada.getNumero() > Constantes.NRO_PARTIDAS_FASE_GRUPOS) {
				
					if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
						trtMandante.setClassificacaoContinental(ClassificacaoContinental
								.getClassificacao(rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada, false));
					}
	
					if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
						trtVisitante.setClassificacaoContinental(ClassificacaoContinental
								.getClassificacao(rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada, false));
					}
	
					if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final
						if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
							trtVisitante.setClassificacaoContinental(
									ClassificacaoContinental.getClassificacaoCampeao(rodada.getNivelCampeonato()));
						} else if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
							trtMandante.setClassificacaoContinental(
									ClassificacaoContinental.getClassificacaoCampeao(rodada.getNivelCampeonato()));
						}
					}
				}
				
			} else if (rodada.getNivelCampeonato().isCNIOuCNII()) {

				if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
					trtMandante.setClassificacaoCopaNacional(ClassificacaoCopaNacional.getClassificacao(
							rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada,
							rodada.getCampeonatoJogavel().getTotalRodadas(), false));

				}

				if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
					trtVisitante.setClassificacaoCopaNacional(ClassificacaoCopaNacional.getClassificacao(
							rodada.getNivelCampeonato(), (RodadaEliminatoria) rodada,
							rodada.getCampeonatoJogavel().getTotalRodadas(), false));
				}

				if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final
					if (((PartidaEliminatoriaResultado) p).isMandanteEliminado()) {
						trtVisitante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(rodada.getNivelCampeonato()));
					} else if (((PartidaEliminatoriaResultado) p).isVisitanteEliminado()) {
						trtMandante.setClassificacaoCopaNacional(
								ClassificacaoCopaNacional.getClassificacaoCampeao(rodada.getNivelCampeonato()));
					}
				}
				
			} else if (rodada.getNivelCampeonato().isNIOuNII()
					&& rodada.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {

				trtMandante.setClassificacaoNacional(ClassificacaoNacional.getClassificacao(rodada.getNivelCampeonato(),
						clubeClassificacao.get(trtMandante.getClube()).getPosicao()));

				trtVisitante.setClassificacaoNacional(ClassificacaoNacional.getClassificacao(
						rodada.getNivelCampeonato(), clubeClassificacao.get(trtVisitante.getClube()).getPosicao()));

			}

		}
		
		treinadorResumoTemporadaRepository.saveAll(resumos);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private TreinadorResumoTemporada getTreinadorResumoTemporada(Treinador treinador, Clube clube,
			NivelCampeonato nivel, Temporada temporada,
			Map<Treinador, Map<Clube, Map<NivelCampeonato, List<TreinadorResumoTemporada>>>> treinadorClubeNivelResumos) {

		Map<Clube, Map<NivelCampeonato, List<TreinadorResumoTemporada>>> clubeNivelResumos;
		Map<NivelCampeonato, List<TreinadorResumoTemporada>> nivelResumos;
		List<TreinadorResumoTemporada> resumos;
		
		clubeNivelResumos = treinadorClubeNivelResumos.get(treinador);

		if (clubeNivelResumos == null) {

			resumos = new ArrayList<TreinadorResumoTemporada>();
			nivelResumos = new HashMap<NivelCampeonato, List<TreinadorResumoTemporada>>();
			clubeNivelResumos = new HashMap<Clube, Map<NivelCampeonato, List<TreinadorResumoTemporada>>>();

			TreinadorResumoTemporada treinadorResumoTemporada = new TreinadorResumoTemporada();
			treinadorResumoTemporada.setTreinador(treinador);
			treinadorResumoTemporada.setTemporada(temporada);
			treinadorResumoTemporada.setClube(clube);
			treinadorResumoTemporada.setNivelCampeonato(nivel);

			resumos.add(treinadorResumoTemporada);
			nivelResumos.put(nivel, resumos);
			clubeNivelResumos.put(clube, nivelResumos);
			treinadorClubeNivelResumos.put(treinador, clubeNivelResumos);

		} else {

			nivelResumos = clubeNivelResumos.get(clube);

			if (nivelResumos == null) {

				resumos = new ArrayList<TreinadorResumoTemporada>();
				nivelResumos = new HashMap<NivelCampeonato, List<TreinadorResumoTemporada>>();

				TreinadorResumoTemporada treinadorResumoTemporada = new TreinadorResumoTemporada();
				treinadorResumoTemporada.setTreinador(treinador);
				treinadorResumoTemporada.setTemporada(temporada);
				treinadorResumoTemporada.setClube(clube);
				treinadorResumoTemporada.setNivelCampeonato(nivel);

				resumos.add(treinadorResumoTemporada);
				nivelResumos.put(nivel, resumos);
				clubeNivelResumos.put(clube, nivelResumos);

			} else {

				resumos = nivelResumos.get(nivel);

				if (resumos == null) {

					resumos = new ArrayList<TreinadorResumoTemporada>();

					TreinadorResumoTemporada treinadorResumoTemporada = new TreinadorResumoTemporada();
					treinadorResumoTemporada.setTreinador(treinador);
					treinadorResumoTemporada.setTemporada(temporada);
					treinadorResumoTemporada.setClube(clube);
					treinadorResumoTemporada.setNivelCampeonato(nivel);

					resumos.add(treinadorResumoTemporada);
					nivelResumos.put(nivel, resumos);

				}

			}

		}

		if (resumos.size() != 1) {
			throw new RuntimeException("Inesperado....");// TODO
		}

		return resumos.get(0);
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
				treinadorResumoTemporada.setClube(classificacao.getClube());
				
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
					treinadorResumoTemporadaMandante.setClube(partida.getClubeMandante());
					treinadorResumo.put(partida.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				if (treinadorResumo.containsKey(partida.getClubeVisitante())) {
					treinadorResumoTemporadaVisitante = treinadorResumo.get(partida.getClubeVisitante());
				} else {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					treinadorResumoTemporadaVisitante.setTreinador(partida.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					treinadorResumoTemporadaVisitante.setClube(partida.getClubeVisitante());
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
					treinadorResumoTemporada.setClube(classificacao.getClube());
					
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
					treinadorResumoTemporadaMandante.setClube(partida.getClubeMandante());
					treinadorResumo.put(partida.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				if (treinadorResumo.containsKey(partida.getClubeVisitante())) {
					treinadorResumoTemporadaVisitante = treinadorResumo.get(partida.getClubeVisitante());
				} else {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					treinadorResumoTemporadaVisitante.setTreinador(partida.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(campeonato.getNivelCampeonato());
					treinadorResumoTemporadaVisitante.setClube(partida.getClubeVisitante());
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
					treinadorResumoTemporadaMandante.setClube(p.getClubeMandante());
					
					treinadoresResumo.put(p.getClubeMandante(), treinadorResumoTemporadaMandante);
				}
				
				treinadorResumoTemporadaVisitante = treinadoresResumo.get(p.getClubeVisitante());
				if (treinadorResumoTemporadaVisitante == null) {
					treinadorResumoTemporadaVisitante = new TreinadorResumoTemporada();
					
					treinadorResumoTemporadaVisitante.setTreinador(p.getClubeVisitante().getTreinador());
					treinadorResumoTemporadaVisitante.setTemporada(temporada);
					treinadorResumoTemporadaVisitante.setNivelCampeonato(r.getNivelCampeonato());
					treinadorResumoTemporadaVisitante.setClube(p.getClubeVisitante());
					
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
