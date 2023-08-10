package com.fastfoot.match.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeGrupoValorEstatistica;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEnergia;
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;
import com.fastfoot.player.model.repository.JogadorEnergiaRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class CarregarEscalacaoJogadoresPartidaService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private JogadorEnergiaRepository jogadorEnergiaRepository;
	
	@Autowired
	private EscalarClubeService escalarClubeService;

	public EscalacaoClube carregarJogadoresPartida(Clube clube, PartidaResultadoJogavel partidaResultado) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogadorFetchHabilidades(clube,
				StatusJogador.ATIVO);//TODO: reduzir numero de consultas
		
		carregarJogadorEnergia(clube, jogadores);

		EscalacaoClube escalacao = escalarClubeService.gerarEscalacaoInicial(clube, jogadores, partidaResultado);

		inicializarEstatisticasJogador(escalacao.getListEscalacaoJogadorPosicao(),
				partidaResultado.getRodada().getSemana(), partidaResultado);

		inicializarEstatisticas(jogadores, partidaResultado.getRodada().getSemana(), partidaResultado);

		return escalacao;
	}

	public EscalacaoClube carregarJogadoresPartida(Clube clube, List<Jogador> jogadores, PartidaResultadoJogavel partidaResultado) {

		EscalacaoClube escalacao = escalarClubeService.gerarEscalacaoInicial(clube, jogadores, partidaResultado);

		inicializarEstatisticasJogador(escalacao.getListEscalacaoJogadorPosicao(),
				partidaResultado.getRodada().getSemana(), partidaResultado);

		inicializarEstatisticas(jogadores, partidaResultado.getRodada().getSemana(), partidaResultado);

		return escalacao;
	}
	
	private void carregarJogadorEnergia(Clube clube, List<Jogador> jogadores) {
		List<Map<String, Object>> jogEnergia = jogadorEnergiaRepository.findEnergiaJogadorByIdClube(clube.getId());

		Map<Jogador, Map<String, Object>> x = jogEnergia.stream()
				.collect(Collectors.toMap(ej -> new Jogador(((BigInteger) ej.get("id_jogador")).longValue()), Function.identity()));

		JogadorEnergia je = null;
		Map<String, Object> jes = null;
		Integer energia = null;

		for (Jogador j : jogadores) {
			je = new JogadorEnergia();
			je.setJogador(j);
			je.setEnergia(0);//Variacao de energia

			jes = x.get(j);
			if (!ValidatorUtil.isEmpty(jes)) {
				energia = ((BigInteger) jes.get("energia")).intValue();
				if (energia > Constantes.ENERGIA_INICIAL) {
					je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
				} else {
					je.setEnergiaInicial(energia);
				}
			} else {
				je.setEnergiaInicial(Constantes.ENERGIA_INICIAL);
			}

			j.setJogadorEnergia(je);
		}

	}
	
	/*private void carregarJogadorEnergia2(Clube clube, List<Jogador> jogadores) {
		List<JogadorEnergia> jogadorEnergias = jogadorEnergiaRepository.findByClube(clube);
		
		Map<Jogador, List<JogadorEnergia>> x = jogadorEnergias.stream().sorted(new Comparator<JogadorEnergia>() {

			@Override
			public int compare(JogadorEnergia o1, JogadorEnergia o2) {
				return o2.getId().compareTo(o1.getId());
			}
		}).collect(Collectors.groupingBy(JogadorEnergia::getJogador));
		
		List<JogadorEnergia> jes = null;
		JogadorEnergia je = null;
		
		for (Jogador j : jogadores) {

			je = new JogadorEnergia();
			//je.setTemporada(temporada);
			je.setJogador(j);

			jes = x.get(j);
			if (!ValidatorUtil.isEmpty(jes)) {
				je.setEnergia(jes.get(0).getEnergia());
			} else {
				je.setEnergia(Constantes.ENERGIA_INICIAL);
			}
			
			j.getJogadorDetalhe().setJogadorEnergia(je);
		}
	}*/
	
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
				.forEach(j -> j.getJogadorEstatisticasSemana().setNumeroJogos(1));

		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticasSemana().setNumeroJogosTitular(1));

		/*escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticaSemana().setNumeroMinutosJogados(90));*/
		
		escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).map(EscalacaoJogadorPosicao::getJogador)
				.forEach(j -> j.getJogadorEstatisticasSemana().setMinutoInicial(0));
	}

	private void inicializarEstatisticas(List<Jogador> jogadores, Semana semana,
			PartidaResultadoJogavel partidaResultado) {
		for (Jogador j : jogadores) {
			for (HabilidadeValor hv : j.getHabilidades()) {
				hv.setHabilidadeValorEstatistica(
						new HabilidadeValorEstatistica(hv, semana, partidaResultado.isAmistoso()));
			}
		}
	}

	public EscalacaoClube carregarJogadoresHabilidadeGrupoPartida(Clube clube, PartidaResultadoJogavel partidaResultado) {
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogadorFetchHabilidadesGrupo(clube,
				StatusJogador.ATIVO);//TODO: reduzir numero de consultas
		
		carregarJogadorEnergia(clube, jogadores);

		EscalacaoClube escalacao = escalarClubeService.gerarEscalacaoInicial(clube, jogadores, partidaResultado);

		inicializarEstatisticasJogador(escalacao.getListEscalacaoJogadorPosicao(),
				partidaResultado.getRodada().getSemana(), partidaResultado);

		inicializarEstatisticasHabilidadeGrupo(jogadores, partidaResultado.getRodada().getSemana(), partidaResultado);

		return escalacao;
	}

	private void inicializarEstatisticasHabilidadeGrupo(List<Jogador> jogadores, Semana semana,
			PartidaResultadoJogavel partidaResultado) {
		for (Jogador j : jogadores) {
			for (HabilidadeGrupoValor hv : j.getHabilidadesGrupo()) {
				hv.setHabilidadeGrupoValorEstatistica(
						new HabilidadeGrupoValorEstatistica(hv, semana, partidaResultado.isAmistoso()));
			}
		}
	}
}
