package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.dto.ArtilhariaDTO;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.player.model.repository.JogadorEstatisticasTemporadaRepository;
import com.fastfoot.player.service.crud.JogadorEstatisticasTemporadaCRUDService;
import com.fastfoot.scheduler.model.entity.Temporada;

@Service
public class CalcularArtilhariaService {
	
	@Autowired
	private JogadorEstatisticasTemporadaRepository jogadorEstatisticasTemporadaRepository;
	
	@Autowired
	private JogadorEstatisticasTemporadaCRUDService jogadorEstatisticasTemporadaCRUDService;

	private static final Comparator<ArtilhariaDTO> COMPARATOR = new Comparator<ArtilhariaDTO>() {

		@Override
		public int compare(ArtilhariaDTO o1, ArtilhariaDTO o2) {
			return o2.getQtdeGols().compareTo(o1.getQtdeGols());
		}
	};

	public List<ArtilhariaDTO> getAll(Jogo jogo, Boolean amistoso) {
		
		List<JogadorEstatisticasTemporada> estatisticas = jogadorEstatisticasTemporadaCRUDService
				.getAgrupadoByTemporadaAtual(jogo, amistoso);

		estatisticas.addAll(jogadorEstatisticasTemporadaRepository.findByJogoAndAmistoso(jogo, amistoso));

		return agruparEstatisticas(estatisticas, 100);
		
	}
	
	private List<ArtilhariaDTO> agruparEstatisticas(List<JogadorEstatisticasTemporada> estatisticas, int qtdeMax) {
		Map<Jogador, List<JogadorEstatisticasTemporada>> estatisticasJogador = estatisticas.stream()
				.filter(e -> e.getGolsMarcados() > 0)
				.collect(Collectors.groupingBy(JogadorEstatisticasTemporada::getJogador));
		
		List<JogadorEstatisticasTemporada> estJogador = null;
		
		List<ArtilhariaDTO> artilharia = new ArrayList<ArtilhariaDTO>();
		
		ArtilhariaDTO artilhariaJogador = null;
		
		for (Jogador j : estatisticasJogador.keySet()) {
			estJogador = estatisticasJogador.get(j);
			
			int golsMarcados = estJogador.stream().mapToInt(e -> e.getGolsMarcados()).sum();
			
			artilhariaJogador = new ArtilhariaDTO();
			
			artilhariaJogador.setJogador(j);
			artilhariaJogador.setQtdeGols(golsMarcados);
			
			artilharia.add(artilhariaJogador);
		}
		
		Collections.sort(artilharia, COMPARATOR);
		
		if (qtdeMax < artilharia.size()) {
			artilharia = artilharia.subList(0, qtdeMax);
		}
		
		return artilharia;
	}

	public List<ArtilhariaDTO> getByTemporada(Temporada temporada, Boolean amistoso) {
		List<JogadorEstatisticasTemporada> estatisticas = jogadorEstatisticasTemporadaCRUDService
				.getAgrupadoByTemporada(temporada, amistoso);

		estatisticas.addAll(jogadorEstatisticasTemporadaRepository.findByTemporadaAndAmistoso(temporada, amistoso));

		return agruparEstatisticas(estatisticas, 100);
	}

	public List<ArtilhariaDTO> getByCampeonato(Long idCampeonato) {
		List<JogadorEstatisticasTemporada> estatisticas = jogadorEstatisticasTemporadaCRUDService
				.getAgrupadoByIdCampeonato(idCampeonato);

		return agruparEstatisticas(estatisticas, 100);
	}

	public List<ArtilhariaDTO> getByClube(Clube clube, Boolean amistoso) {
		List<JogadorEstatisticasTemporada> estatisticas = jogadorEstatisticasTemporadaCRUDService
				.getAgrupadoTemporadaAtualByClube(clube, amistoso);

		estatisticas.addAll(jogadorEstatisticasTemporadaRepository.findByClubeAndAmistoso(clube, amistoso));

		return agruparEstatisticas(estatisticas, 100);
	}
}
