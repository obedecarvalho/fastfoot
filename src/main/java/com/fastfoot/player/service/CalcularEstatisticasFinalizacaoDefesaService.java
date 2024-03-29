package com.fastfoot.player.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.repository.PartidaEstatisticasRepository;
import com.fastfoot.probability.model.ClubeProbabilidadeDefesa;
import com.fastfoot.probability.model.ClubeProbabilidadeFinalizacao;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.DatabaseUtil;

@Service
public class CalcularEstatisticasFinalizacaoDefesaService {
	
	@Autowired
	private PartidaEstatisticasRepository partidaEstatisticasRepository;
	
	public Map<Clube, ClubeProbabilidadeFinalizacao> getEstatisticasFinalizacaoClube(Temporada temporada) {
		
		List<Map<String, Object>> estatisticasFinalizacoes = partidaEstatisticasRepository
				.findEstatisticasFinalizacoesClubePorTemporada(temporada.getId());
		Map<Clube, ClubeProbabilidadeFinalizacao> clubeProbabilidadeFinalizacoes = new HashMap<Clube, ClubeProbabilidadeFinalizacao>();
		ClubeProbabilidadeFinalizacao clubeProbabilidadeFinalizacao = null;

		for (Map<String, Object> e : estatisticasFinalizacoes) {
			clubeProbabilidadeFinalizacao = new ClubeProbabilidadeFinalizacao();

			clubeProbabilidadeFinalizacao.setClube(new Clube(DatabaseUtil.getValueLong(e.get("id_clube"))));
			clubeProbabilidadeFinalizacao
					.setFinalizacoesPartidas(DatabaseUtil.getValueDecimal(e.get("finalizacoes_partidas")));
			clubeProbabilidadeFinalizacao.setGolsPartida(DatabaseUtil.getValueDecimal(e.get("gols_partida")));
			clubeProbabilidadeFinalizacao
					.setProbabilidadeGolFinalizacao(DatabaseUtil.getValueDecimal(e.get("probilidade_gols")));
			clubeProbabilidadeFinalizacao
					.setProbabilidadeFinalizacaoNoGol(DatabaseUtil.getValueDecimal(e.get("probilidade_finalizacoes_no_gol")));

			clubeProbabilidadeFinalizacoes.put(clubeProbabilidadeFinalizacao.getClube(), clubeProbabilidadeFinalizacao);
		}
		
		return clubeProbabilidadeFinalizacoes;
	}
	
	public Map<Clube, ClubeProbabilidadeDefesa> getEstatisticasDefesaClube(Temporada temporada) {
		
		List<Map<String, Object>> estatisticasDefesa = partidaEstatisticasRepository
				.findEstatisticasDefesaClubePorTemporada(temporada.getId());
		Map<Clube, ClubeProbabilidadeDefesa> clubesProbabilidadeDefesa = new HashMap<Clube, ClubeProbabilidadeDefesa>();
		ClubeProbabilidadeDefesa clubeProbabilidadeDefesa = null;

		for (Map<String, Object> e : estatisticasDefesa) {
			clubeProbabilidadeDefesa = new ClubeProbabilidadeDefesa();

			clubeProbabilidadeDefesa.setClube(new Clube(DatabaseUtil.getValueLong(e.get("id_clube"))));
			clubeProbabilidadeDefesa.setProbabilidadeDefesa(DatabaseUtil.getValueDecimal(e.get("probabilidade_defesa")));

			clubesProbabilidadeDefesa.put(clubeProbabilidadeDefesa.getClube(), clubeProbabilidadeDefesa);
		}
		
		return clubesProbabilidadeDefesa;
	}

}
