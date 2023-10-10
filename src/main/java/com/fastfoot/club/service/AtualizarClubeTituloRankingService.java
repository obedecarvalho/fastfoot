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
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.club.service.util.ClubeTituloRankingUtil;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
import com.fastfoot.scheduler.model.RodadaJogavel;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.Rodada;

@Service
public class AtualizarClubeTituloRankingService {
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> atualizarClubeTituloRanking(Jogo jogo, RodadaJogavel rodada) {

		List<ClubeTituloRanking> rankingsTitulos = clubeTituloRankingRepository.findByJogo(jogo);

		Map<Clube, ClubeTituloRanking> clubeRanking = rankingsTitulos.stream()
				.collect(Collectors.toMap(ClubeTituloRanking::getClube, Function.identity()));

		List<ClubeTituloRanking> rankingsSalvar = new ArrayList<ClubeTituloRanking>();

		ClubeTituloRanking ctr = null;

		if (rodada.getNivelCampeonato().isCIOuCIIOuCIII() || rodada.getNivelCampeonato().isCNIOuCNII()) {

			for (PartidaResultadoJogavel p : rodada.getPartidas()) {

				if (rodada.getNumero() == rodada.getCampeonatoJogavel().getTotalRodadas()) {// Final

					ctr = clubeRanking.get(p.getClubeVencedor());
					if (NivelCampeonato.CONTINENTAL.equals(rodada.getNivelCampeonato())) {
						ctr.incrementartitulosContinental();
					} else if (NivelCampeonato.CONTINENTAL_II.equals(rodada.getNivelCampeonato())) {
						ctr.incrementartitulosContinentalII();
					} else if (NivelCampeonato.CONTINENTAL_III.equals(rodada.getNivelCampeonato())) {
						ctr.incrementartitulosContinentalIII();
					} else if (NivelCampeonato.COPA_NACIONAL.equals(rodada.getNivelCampeonato())) {
						ctr.incrementartitulosCopaNacional();
					} else if (NivelCampeonato.COPA_NACIONAL_II.equals(rodada.getNivelCampeonato())) {
						ctr.incrementartitulosCopaNacionalII();
					}
					rankingsSalvar.add(ctr);
				}
			}
		} else if (rodada.getNivelCampeonato().isNIOuNII() && rodada.getNumero() == Constantes.NRO_RODADAS_CAMP_NACIONAL) {

			Classificacao classificacaoCampeao = ((Rodada) rodada).getCampeonato().getClassificacao().stream()
					.filter(c -> c.getPosicao() == 1).findFirst().get();

			ctr = clubeRanking.get(classificacaoCampeao.getClube());
			if (NivelCampeonato.NACIONAL.equals(rodada.getNivelCampeonato())) {
				ctr.incrementartitulosNacional();;
			} else if (NivelCampeonato.NACIONAL_II.equals(rodada.getNivelCampeonato())) {
				ctr.incrementartitulosNacionalII();;
			}
			rankingsSalvar.add(ctr);

		}
		
		rankingsSalvar.stream().forEach(t -> ClubeTituloRankingUtil.calcularPontuacao(t));

		clubeTituloRankingRepository.saveAll(rankingsSalvar);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}

}
