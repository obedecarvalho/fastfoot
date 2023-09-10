package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.match.model.Esquema;
import com.fastfoot.match.model.EstrategiaSubstituicao;
import com.fastfoot.match.model.PartidaDadosSalvarDTO;
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeGrupoValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;

@Service
public class JogarPartidaHabilidadeGrupoService extends JogarPartidaService {

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

	protected void salvarEstatisticas(List<Jogador> jogadores, PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		List<HabilidadeGrupoValorEstatistica> estatisticas = new ArrayList<HabilidadeGrupoValorEstatistica>();
		
		for (Jogador j : jogadores) {
			estatisticas.addAll(j.getHabilidadesGrupoValor().stream().map(hv -> hv.getHabilidadeGrupoValorEstatistica())
					.filter(hve -> hve.getQuantidadeUso() > 0).collect(Collectors.toList()));
		}

		partidaDadosSalvarDTO.adicionarHabilidadeGrupoValorEstatistica(estatisticas);
	}

	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		
		EscalacaoClube escalacaoMandante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresHabilidadeGrupoPartida(partidaResultado.getClubeMandante(), partidaResultado);
		EscalacaoClube escalacaoVisitante = carregarEscalacaoJogadoresPartidaService
				.carregarJogadoresHabilidadeGrupoPartida(partidaResultado.getClubeVisitante(), partidaResultado);
		
		partidaResultado.setEscalacaoMandante(escalacaoMandante);
		partidaResultado.setEscalacaoVisitante(escalacaoVisitante);
		
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaDadosSalvarDTO);
	}

	@Override
	protected void disputarPenalts(PartidaResultadoJogavel partidaResultado, Esquema esquema) {
		disputarPenaltsService.disputarPenaltsHabilidadeGrupo(partidaResultado, esquema);
	}
	
	@Override
	public void jogar(PartidaResultadoJogavel partidaResultado, EscalacaoClube escalacaoMandante,
			EscalacaoClube escalacaoVisitante, PartidaDadosSalvarDTO partidaDadosSalvarDTO) {
		jogar(partidaResultado, escalacaoMandante, escalacaoVisitante, partidaDadosSalvarDTO, true);
	}

	@Override
	protected HabilidadeValorJogavel criarHabilidadeValorJogavelFora(Integer valor) {
		return new HabilidadeGrupoValor(HabilidadeGrupo.FORA, valor);
	}

	protected void realizarSubstituicoesJogadorPartida(Esquema esquema, EstrategiaSubstituicao estrategiaSubstituicao,
			PartidaResultadoJogavel partidaResultado, boolean mandante, int minutoSubstituicao) {
		realizarSubstituicoesJogadorPartidaService.realizarSubstituicoesJogadorPartida(esquema,
				estrategiaSubstituicao, partidaResultado, mandante, minutoSubstituicao);
	}
	
}
