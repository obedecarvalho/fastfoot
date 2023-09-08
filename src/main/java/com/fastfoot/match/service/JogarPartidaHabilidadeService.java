package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.PartidaJogadorEstatisticaDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Service
public class JogarPartidaHabilidadeService extends JogarPartidaService {

	/*
	 * TODO:
	 * 
	 * Inserir FALTA (estilo HabilidadeValor(Habilidade.FORA) na roleta) e CARTOES
	 * Inserir ERRO (estilo HabilidadeValor(Habilidade.FORA) na roleta)
	 * Substituicoes
	 * 
	 */

	@Autowired
	private DisputarPenaltsService disputarPenaltsService;
	
	@Autowired
	private CarregarEscalacaoJogadoresPartidaService carregarEscalacaoJogadoresPartidaService;
	
	@Autowired
	private RealizarSubstituicoesJogadorPartidaService realizarSubstituicoesJogadorPartidaService;

	protected void salvarEstatisticas(List<Jogador> jogadores, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		List<HabilidadeValorEstatistica> estatisticas = new ArrayList<HabilidadeValorEstatistica>();
		
		for (Jogador j : jogadores) {
			estatisticas.addAll(j.getHabilidadesValor().stream().map(hv -> hv.getHabilidadeValorEstatistica())
					.filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		}

		partidaJogadorEstatisticaDTO.adicionarHabilidadeValorEstatistica(estatisticas);
	}

	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		
		EscalacaoClube escalacaoMandante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresPartida(partidaResultado.getClubeMandante(), partidaResultado);
		EscalacaoClube escalacaoVisitante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresPartida(partidaResultado.getClubeVisitante(), partidaResultado);

		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO);
	}

	@Override
	protected void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsService.disputarPenalts(partidaResultado, esquema);
	}
	
	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaJogadorEstatisticaDTO partidaJogadorEstatisticaDTO) {
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaJogadorEstatisticaDTO, false);
	}

	@Override
	protected HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor) {
		return new HabilidadeValor(Habilidade.FORA, valor);
	}

	protected void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {
		realizarSubstituicoesJogadorPartidaService.realizarSubstituicoesJogadorPartida(esquema,
				estrategiaSubstituicao, partidaResultado, mandante, minutoSubstituicao);
	}

}