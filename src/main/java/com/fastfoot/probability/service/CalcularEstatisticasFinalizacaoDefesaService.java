package com.fastfoot.probability.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.scheduler.model.entity.Temporada;

@Service
public class CalcularEstatisticasFinalizacaoDefesaService {//TODO: avaliar modulo
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	public Map<Clube, ClubeProbabilidadeFinalizacao> getEstatisticasFinalizacaoClube(Temporada temporada) {
		
		List<Map<String, Object>> estatisticasFinalizacoes = jogadorEstatisticasTemporadaRepository
				.findEstatisticasFinalizacoesPorClube(temporada.getId());
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = new HashMap<Clube, ClubeProbabilidadeFinalizacao>();
		ClubeProbabilidadeFinalizacao clubeProbabilidadeFinalizacao = null;

		for (Map<String, Object> e : estatisticasFinalizacoes) {
			clubeProbabilidadeFinalizacao = new ClubeProbabilidadeFinalizacao();

			clubeProbabilidadeFinalizacao.setClube(new Clube((Integer) e.get("id_clube")));
			clubeProbabilidadeFinalizacao
					.setFinalizacoesPartidas(((BigDecimal) e.get("finalizacoes_partidas")).doubleValue());
			clubeProbabilidadeFinalizacao.setGolsPartida(((BigDecimal) e.get("gols_partida")).doubleValue());
			clubeProbabilidadeFinalizacao
					.setProbabilidadeGolFinalizacao(((BigDecimal) e.get("probilidade_gols")).doubleValue());
			clubeProbabilidadeFinalizacao
					.setProbabilidadeFinalizacaoNoGol(((BigDecimal) e.get("probilidade_finalizacoes_no_gol")).doubleValue());

			clubeProbabilidadeFinalizacoes.put(clubeProbabilidadeFinalizacao.getClube(), clubeProbabilidadeFinalizacao);
		}
		
		return clubeProbabilidadeFinalizacoes;
	}
	
	public Map<Clube, ClubeProbabilidadeDefesa> getEstatisticasDefesaClube(Temporada temporada) {
		
		List<Map<String, Object>> estatisticasDefesa = jogadorEstatisticasTemporadaRepository
				.findEstatisticasDefesaPorClube(temporada.getId());
		Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa = new HashMap<Clube, ClubeProbabilidadeDefesa>();
		ClubeProbabilidadeDefesa clubeProbabilidadeDefesa = null;

		for (Map<String, Object> e : estatisticasDefesa) {
			clubeProbabilidadeDefesa = new ClubeProbabilidadeDefesa();

			clubeProbabilidadeDefesa.setClube(new Clube((Integer) e.get("id_clube")));
			clubeProbabilidadeDefesa.setProbabilidadeDefesa(((BigDecimal) e.get("probabilidade_defesa")).doubleValue());

			clubesProbabilidadeDefesa.put(clubeProbabilidadeDefesa.getClube(), clubeProbabilidadeDefesa);
		}
		
		return clubesProbabilidadeDefesa;
	}

}
