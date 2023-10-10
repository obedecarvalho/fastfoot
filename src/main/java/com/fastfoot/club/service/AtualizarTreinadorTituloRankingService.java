package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.TreinadorTituloRankingRepository;
import com.fastfoot.club.service.util.ClubeRankingUtil;
import com.fastfoot.club.service.util.TreinadorTituloRankingUtil;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.scheduler.service.CarregarCampeonatoService;
import com.fastfoot.service.LigaJogoCRUDService;

@Service
public class AtualizarTreinadorTituloRankingService {
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private CarregarCampeonatoService carregarCampeonatoService;
	
	@Autowired
	private LigaJogoCRUDService ligaJogoCRUDService;

	@Autowired
	private TreinadorTituloRankingRepository treinadorTituloRankingRepository;

	@Deprecated
	public void atualizarTreinadorTituloRanking(Temporada temporada) {
		carregarCampeonatoService.carregarCampeonatosTemporada(temporada);
		List<LigaJogo> ligaJogos = ligaJogoCRUDService.getByJogo(temporada.getJogo());
		List<Clube> clubes = clubeRepository.findByJogo(temporada.getJogo());
		List<ClubeRanking> rankings = ClubeRankingUtil.rankearClubesTemporada(temporada, clubes, ligaJogos);
		
		List<TreinadorTituloRanking> rankingsTitulos = treinadorTituloRankingRepository.findByJogo(temporada.getJogo());
		TreinadorTituloRankingUtil.atualizarRankingTitulos(rankings, rankingsTitulos);
		treinadorTituloRankingRepository.saveAll(rankingsTitulos);
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarTreinadorTituloRanking(Jogo jogo, RodadaJogavel rodada) {

		List<TreinadorTituloRanking> rankingsTitulos = treinadorTituloRankingRepository.findByJogo(jogo);

		Map<Treinador, TreinadorTituloRanking> treinadorRanking = rankingsTitulos.stream()
				.collect(Collectors.toMap(TreinadorTituloRanking::getTreinador, Function.identity()));

		List<TreinadorTituloRanking> rankingsSalvar = new ArrayList<TreinadorTituloRanking>();

		TreinadorTituloRanking ttr = null;

		if (rodada.getNivelCampeonato().isCIOuCIIOuCIII() || rodada.getNivelCampeonato().isCNIOuCNII()) {

			for (PartidaResultadoJogavel p : rodada.getPartidas()) {

				if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final

					ttr = treinadorRanking.get(p.getClubeVencedor().getTreinador());
					if (NivelCampeonato.CONTINENTAL.equals(rodada.getNivelCampeonato())) {
						ttr.incrementartitulosContinental();
					} else if (NivelCampeonato.CONTINENTAL_II.equals(rodada.getNivelCampeonato())) {
						ttr.incrementartitulosContinentalII();
					} else if (NivelCampeonato.CONTINENTAL_III.equals(rodada.getNivelCampeonato())) {
						ttr.incrementartitulosContinentalIII();
					} else if (NivelCampeonato.COPA_NACIONAL.equals(rodada.getNivelCampeonato())) {
						ttr.incrementartitulosCopaNacional();
					} else if (NivelCampeonato.COPA_NACIONAL_II.equals(rodada.getNivelCampeonato())) {
						ttr.incrementartitulosCopaNacionalII();
					}
					rankingsSalvar.add(ttr);
				}
			}
		} else if (rodada.getNivelCampeonato().isNIOuNII() && rodada.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {

			Classificacao classificacaoCampeao = ((Rodada) rodada).getCampeonato().getClassificacao().stream()
					.filter(c -> c.getPosicao() == 1).findFirst().get();

			ttr = treinadorRanking.get(classificacaoCampeao.getClube().getTreinador());
			if (NivelCampeonato.NACIONAL.equals(rodada.getNivelCampeonato())) {
				ttr.incrementartitulosNacional();;
			} else if (NivelCampeonato.NACIONAL_II.equals(rodada.getNivelCampeonato())) {
				ttr.incrementartitulosNacionalII();;
			}
			rankingsSalvar.add(ttr);

		}
		
		rankingsSalvar.stream().forEach(t -> TreinadorTituloRankingUtil.calcularPontuacao(t));

		treinadorTituloRankingRepository.saveAll(rankingsSalvar);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

}
