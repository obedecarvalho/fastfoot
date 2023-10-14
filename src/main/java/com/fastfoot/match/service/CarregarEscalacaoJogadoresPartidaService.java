package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeGrupoValorEstatistica;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.player.service.CarregarJogadorEnergiaService;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.Semana;

@Service
public class CarregarEscalacaoJogadoresPartidaService {
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Autowired
	private EscalarClubeService escalarClubeService;

	@Autowired
	private CarregarJogadorEnergiaService carregarJogadorEnergiaService;
	
	//###	HABILIDADE_VALOR	###
	
	public void carregarEscalacao(Jogo jogo, List<? extends PartidaResultadoJogavel> partidas) {

		List<Clube> clubes = new ArrayList<Clube>();

		clubes.addAll(partidas.stream().map(p -> p.getClubeMandante()).collect(Collectors.toList()));
		clubes.addAll(partidas.stream().map(p -> p.getClubeVisitante()).collect(Collectors.toList()));

		List<Jogador> jogadores = jogadorRepository.findByClubesAndStatusJogadorFetchHabilidades(clubes,
				StatusJogador.ATIVO);

		carregarJogadorEnergiaService.carregarJogadorEnergia(jogo, jogadores);

		Map<Clube, List<Jogador>> jogadoresPorClube = jogadores.stream()
				.collect(Collectors.groupingBy(Jogador::getClube));

		EscalacaoClube escalacaoClubeMandante, escalacaoClubeVisitante;

		for (PartidaResultadoJogavel p : partidas) {
			escalacaoClubeMandante = carregarJogadoresPartida(p.getClubeMandante(), jogadoresPorClube.get(p.getClubeMandante()), p);
			escalacaoClubeVisitante = carregarJogadoresPartida(p.getClubeVisitante(), jogadoresPorClube.get(p.getClubeVisitante()), p);
			p.setEscalacaoMandante(escalacaoClubeMandante);
			p.setEscalacaoVisitante(escalacaoClubeVisitante);
		}
	}

	public EscalacaoClube carregarJogadoresPartida(Clube clube, PartidaResultadoJogavel partidaResultado) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogadorFetchHabilidades(clube,
				StatusJogador.ATIVO);
		
		carregarJogadorEnergiaService.carregarJogadorEnergia(clube, jogadores);

		return carregarJogadoresPartida(clube, jogadores, partidaResultado);
	}

	private EscalacaoClube carregarJogadoresPartida(Clube clube, List<Jogador> jogadores, PartidaResultadoJogavel partidaResultado) {

		EscalacaoClube escalacao = escalarClubeService.gerarEscalacaoInicial(clube, jogadores, partidaResultado);

		inicializarEstatisticasJogador(escalacao.getListEscalacaoJogadorPosicao(),
				partidaResultado.getRodada().getSemana(), partidaResultado);

		inicializarEstatisticas(jogadores, partidaResultado.getRodada().getSemana(), partidaResultado);

		return escalacao;
	}

	private void inicializarEstatisticas(List<Jogador> jogadores, Semana semana,
			PartidaResultadoJogavel partidaResultado) {
		for (Jogador j : jogadores) {
			for (HabilidadeValor hv : j.getHabilidadesValor()) {
				hv.setHabilidadeValorEstatistica(
						new HabilidadeValorEstatistica(hv, semana, partidaResultado.isAmistoso()));
			}
		}
	}

	//###	/HABILIDADE_VALOR	###
	
	//###	HABILIDADE_GRUPO_VALOR	###
	
	public void carregarEscalacaoHabilidadeGrupo(Jogo jogo, List<? extends PartidaResultadoJogavel> partidas) {

		List<Clube> clubes = new ArrayList<Clube>();

		clubes.addAll(partidas.stream().map(p -> p.getClubeMandante()).collect(Collectors.toList()));
		clubes.addAll(partidas.stream().map(p -> p.getClubeVisitante()).collect(Collectors.toList()));

		List<Jogador> jogadores = jogadorRepository.findByClubesAndStatusJogadorFetchHabilidadesGrupo(clubes,
				StatusJogador.ATIVO);

		carregarJogadorEnergiaService.carregarJogadorEnergia(jogo, jogadores);

		Map<Clube, List<Jogador>> jogadoresPorClube = jogadores.stream()
				.collect(Collectors.groupingBy(Jogador::getClube));

		EscalacaoClube escalacaoClubeMandante, escalacaoClubeVisitante;

		for (PartidaResultadoJogavel p : partidas) {
			escalacaoClubeMandante = carregarJogadoresHabilidadeGrupoPartida(p.getClubeMandante(), jogadoresPorClube.get(p.getClubeMandante()), p);
			escalacaoClubeVisitante = carregarJogadoresHabilidadeGrupoPartida(p.getClubeVisitante(), jogadoresPorClube.get(p.getClubeVisitante()), p);
			p.setEscalacaoMandante(escalacaoClubeMandante);
			p.setEscalacaoVisitante(escalacaoClubeVisitante);
		}
	}

	public EscalacaoClube carregarJogadoresHabilidadeGrupoPartida(Clube clube, PartidaResultadoJogavel partidaResultado) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogadorFetchHabilidadesGrupo(clube,
				StatusJogador.ATIVO);
		
		carregarJogadorEnergiaService.carregarJogadorEnergia(clube, jogadores);

		return carregarJogadoresHabilidadeGrupoPartida(clube, jogadores, partidaResultado);
	}

	private EscalacaoClube carregarJogadoresHabilidadeGrupoPartida(Clube clube, List<Jogador> jogadores, PartidaResultadoJogavel partidaResultado) {

		EscalacaoClube escalacao = escalarClubeService.gerarEscalacaoInicial(clube, jogadores, partidaResultado);

		inicializarEstatisticasJogador(escalacao.getListEscalacaoJogadorPosicao(),
				partidaResultado.getRodada().getSemana(), partidaResultado);

		inicializarEstatisticasHabilidadeGrupo(jogadores, partidaResultado.getRodada().getSemana(), partidaResultado);

		return escalacao;
	}

	private void inicializarEstatisticasHabilidadeGrupo(List<Jogador> jogadores, Semana semana,
			PartidaResultadoJogavel partidaResultado) {
		for (Jogador j : jogadores) {
			for (HabilidadeGrupoValor hv : j.getHabilidadesGrupoValor()) {
				hv.setHabilidadeGrupoValorEstatistica(
						new HabilidadeGrupoValorEstatistica(hv, semana, partidaResultado.isAmistoso()));
			}
		}
	}

	//###	/HABILIDADE_GRUPO_VALOR	###
	
	private void inicializarEstatisticasJogador(List<EscalacaoJogadorPosicao> escalacao, Semana semana,
			PartidaResultadoJogavel partidaResultado) {


		if (!partidaResultado.isAmistoso()) {// Partidas Oficiais

			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(j, semana, j.getClube(), partidaResultado, false)));

		} else {

			escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
					.forEach(j -> j.setJogadorEstatisticasSemana(
							new JogadorEstatisticasSemana(j, semana, j.getClube(), null, true)));

		}

		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticasSemana().setJogos(1));

		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticasSemana().setJogosTitular(1));
		
		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticasSemana().setMinutoInicial(0));
	}
}
