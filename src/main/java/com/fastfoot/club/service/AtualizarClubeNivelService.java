package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.club.model.dto.ClubeRankingHistoricoDTO;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.MudancaClubeNivel;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.MudancaClubeNivelRepository;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.model.repository.TemporadaRepository;

@Service
public class AtualizarClubeNivelService {
	
	private static final Integer NUM_TEMPORADAS_ANALISAR = 5;
	
	private static final Comparator<ClubeRankingHistoricoDTO> COMPARATOR;
	
	private static final Comparator<ClubeRankingHistoricoDTO> COMPARATOR_INTERNACIONAL;
	
	//###	REPOSITORY	###
	
	@Autowired
	private MudancaClubeNivelRepository mudancaClubeNivelRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private TemporadaRepository temporadaRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	static {
		COMPARATOR = new Comparator<ClubeRankingHistoricoDTO>() {
			
			@Override
			public int compare(ClubeRankingHistoricoDTO o1, ClubeRankingHistoricoDTO o2) {
				return o1.getPosicaoGeralMedia().compareTo(o2.getPosicaoGeralMedia());
			}
		};
		
		COMPARATOR_INTERNACIONAL = new Comparator<ClubeRankingHistoricoDTO>() {
			
			@Override
			public int compare(ClubeRankingHistoricoDTO o1, ClubeRankingHistoricoDTO o2) {
				return o1.getPosicaoInternacionalMedia().compareTo(o2.getPosicaoInternacionalMedia());
			}
		};
	}
	
	/**
	 * 
	 * @param temporada Primeira temporada a ser analisada.
	 * @param liga
	 */
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarClubeNivel(Temporada temporada, Liga liga) {
		
		List<ClubeRanking> rankings = clubeRankingRepository.findByLigaAndTemporada(liga, temporada);
		
		Optional<Temporada> t = null;
		for (int i = 1; i < NUM_TEMPORADAS_ANALISAR; i++) {
			t = temporadaRepository.findFirstByAno(temporada.getAno() - i);
			if (t.isPresent()) {
				rankings.addAll(clubeRankingRepository.findByLigaAndTemporada(liga, t.get()));
			} else {
				//throw new RuntimeException("Não há temporadas suficientes.");
				return CompletableFuture.completedFuture(Boolean.FALSE);
			}
		}
		
		Map<Clube, List<ClubeRanking>> rankingClubeMap = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClube));
		
		List<ClubeRankingHistoricoDTO> rankingHistoricoDTOs = new ArrayList<ClubeRankingHistoricoDTO>();
		ClubeRankingHistoricoDTO rankingHistoricoDTO = null;
		List<ClubeRanking> clubeRankings = null;
		
		for (Clube c : rankingClubeMap.keySet()) {
			rankingHistoricoDTO = new ClubeRankingHistoricoDTO();
			
			clubeRankings = rankingClubeMap.get(c);
			
			rankingHistoricoDTO.setClube(c);
			rankingHistoricoDTO.setClubeRankings(clubeRankings);
			rankingHistoricoDTO.setPosicaoGeralMedia(
					clubeRankings.stream().mapToInt(ClubeRanking::getPosicaoGeral).average().getAsDouble());
			rankingHistoricoDTO.setPosicaoInternacionalMedia(
					clubeRankings.stream().mapToInt(ClubeRanking::getPosicaoInternacional).average().getAsDouble());
			rankingHistoricoDTO.setPosicaoNacionalMedia(
					clubeRankings.stream().mapToInt(cr -> cr.getClassificacaoNacional().ordinal()).average().getAsDouble());
			
			rankingHistoricoDTOs.add(rankingHistoricoDTO);
			
		}
		
		Collections.sort(rankingHistoricoDTOs, COMPARATOR);
		
		List<MudancaClubeNivel> mudancaClubeNivelList = new ArrayList<MudancaClubeNivel>();
		List<Clube> clubes = new ArrayList<Clube>();
		MudancaClubeNivel mudancaClubeNivel = null;
		
		for (ClubeNivel clubeNivel : ClubeNivel.values()) {
			for (int i = clubeNivel.getClubeRankingMin(); i <= clubeNivel.getClubeRankingMax(); i++) {
				if (!rankingHistoricoDTOs.get(i - 1).getClube().getNivelNacional().equals(clubeNivel)) {
					mudancaClubeNivel = new MudancaClubeNivel();
					mudancaClubeNivel.setClube(rankingHistoricoDTOs.get(i - 1).getClube());
					mudancaClubeNivel.setClubeNivelAntigo(rankingHistoricoDTOs.get(i - 1).getClube().getNivelNacional());
					mudancaClubeNivel.setClubeNivelNovo(clubeNivel);
					mudancaClubeNivel.setTemporada(temporada);
					mudancaClubeNivel.setInternacional(false);
					mudancaClubeNivelList.add(mudancaClubeNivel);
					
					rankingHistoricoDTOs.get(i - 1).getClube().setNivelNacional(clubeNivel);
					rankingHistoricoDTOs.get(i - 1).getClube().setForcaGeral(clubeNivel.getForcaGeral());
					clubes.add(rankingHistoricoDTOs.get(i - 1).getClube());
				}
			}		
		}

		mudancaClubeNivelRepository.saveAll(mudancaClubeNivelList);
		clubeRepository.saveAll(clubes);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarClubeNivelInternacional(Temporada temporada) {
		
		List<ClubeRanking> rankings = clubeRankingRepository.findByTemporada(temporada);
		
		Optional<Temporada> t = null;
		for (int i = 1; i < NUM_TEMPORADAS_ANALISAR; i++) {
			t = temporadaRepository.findFirstByAno(temporada.getAno() - i);
			if (t.isPresent()) {
				rankings.addAll(clubeRankingRepository.findByTemporada(t.get()));
			} else {
				//throw new RuntimeException("Não há temporadas suficientes.");
				return CompletableFuture.completedFuture(Boolean.FALSE);
			}
		}
		
		Map<Clube, List<ClubeRanking>> rankingClubeMap = rankings.stream()
				.collect(Collectors.groupingBy(ClubeRanking::getClube));
		
		List<ClubeRankingHistoricoDTO> rankingHistoricoDTOs = new ArrayList<ClubeRankingHistoricoDTO>();
		ClubeRankingHistoricoDTO rankingHistoricoDTO = null;
		List<ClubeRanking> clubeRankings = null;
		
		for (Clube c : rankingClubeMap.keySet()) {
			rankingHistoricoDTO = new ClubeRankingHistoricoDTO();
			
			clubeRankings = rankingClubeMap.get(c);
			
			rankingHistoricoDTO.setClube(c);
			rankingHistoricoDTO.setClubeRankings(clubeRankings);
			rankingHistoricoDTO.setPosicaoGeralMedia(
					clubeRankings.stream().mapToInt(ClubeRanking::getPosicaoGeral).average().getAsDouble());
			rankingHistoricoDTO.setPosicaoInternacionalMedia(
					clubeRankings.stream().mapToInt(ClubeRanking::getPosicaoInternacional).average().getAsDouble());
			rankingHistoricoDTO.setPosicaoNacionalMedia(
					clubeRankings.stream().mapToInt(cr -> cr.getClassificacaoNacional().ordinal()).average().getAsDouble());
			
			rankingHistoricoDTOs.add(rankingHistoricoDTO);
			
		}
		
		Collections.sort(rankingHistoricoDTOs, COMPARATOR_INTERNACIONAL);
		
		List<MudancaClubeNivel> mudancaClubeNivelList = new ArrayList<MudancaClubeNivel>();
		List<Clube> clubes = new ArrayList<Clube>();
		MudancaClubeNivel mudancaClubeNivel = null;

		for (ClubeNivel clubeNivel : ClubeNivel.values()) {
			for (int i = ((clubeNivel.getClubeRankingMin() -1) * 4 + 1); i <= clubeNivel.getClubeRankingMax() * 4; i++) {
				if (!rankingHistoricoDTOs.get(i - 1).getClube().getNivelInternacional().equals(clubeNivel)) {
					mudancaClubeNivel = new MudancaClubeNivel();
					mudancaClubeNivel.setClube(rankingHistoricoDTOs.get(i - 1).getClube());
					mudancaClubeNivel.setClubeNivelAntigo(rankingHistoricoDTOs.get(i - 1).getClube().getNivelInternacional());
					mudancaClubeNivel.setClubeNivelNovo(clubeNivel);
					mudancaClubeNivel.setTemporada(temporada);
					mudancaClubeNivel.setInternacional(true);
					mudancaClubeNivelList.add(mudancaClubeNivel);
					
					rankingHistoricoDTOs.get(i - 1).getClube().setNivelInternacional(clubeNivel);
					/*rankingHistoricoDTOs.get(i - 1).getClube().setForcaGeral(clubeNivel.getForcaGeral());*/
					clubes.add(rankingHistoricoDTOs.get(i - 1).getClube());
				}
			}		
		}
		
		mudancaClubeNivelRepository.saveAll(mudancaClubeNivelList);
		clubeRepository.saveAll(clubes);
		
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

}
