package com.fastfoot.club.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.ClubeNivel;
import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.MudancaTreinador;
import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.MudancaTreinadorRepository;
import com.fastfoot.club.service.crud.TreinadorCRUDService;
import com.fastfoot.model.Constantes;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class AnalisarDesempenhoTreinadorService {

	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private MudancaTreinadorRepository mudancaTreinadorRepository;

	@Autowired
	private TreinadorCRUDService treinadorCRUDService;

	public void analisarDesempenhoTreinador(Temporada temporada) {
		
		List<ClubeRanking> clubeRankings = clubeRankingRepository.findByTemporada(temporada);
		
		List<Clube> clubesMudarTreinador = new ArrayList<Clube>();
		
		List<Treinador> treinadoresDisponiveis = treinadorCRUDService.getTreinadoresSemClube(temporada.getJogo());
		
		for (ClubeRanking clubeRanking : clubeRankings) {
			int posicaoMax = Constantes.NRO_CLUBES_POR_LIGA;
			
			if (clubeRanking.getClube().getNivelNacional().ordinal() < (ClubeNivel.values().length - 1)) {
				posicaoMax = Math.min(ClubeNivel.values()[clubeRanking.getClube().getNivelNacional().ordinal() + 1]
						.getClubeRankingMax(), Constantes.POSICAO_MAX_RANKING_NIVEL_6);
			} else if (clubeRanking.getClube().getNivelNacional().ordinal() == (ClubeNivel.values().length - 1)) {
				posicaoMax = Constantes.POSICAO_MAX_RANKING_NIVEL_7;
			}

			if (clubeRanking.getPosicaoGeral() > posicaoMax) {

				treinadoresDisponiveis.add(clubeRanking.getClube().getTreinador());				

				//System.err.println(String.format("MUDAR\t%d\t%d", clubeRanking.getPosicaoGeral(), posicaoMax));
				clubesMudarTreinador.add(clubeRanking.getClube());

			}
		}
		
		if (treinadoresDisponiveis.size() < 2) {
			throw new RuntimeException("Não há treinadores suficientes para fazer a troca.");
		}
		
		Treinador treinador = null;
		boolean novoTreinadorEscolhido = false;
		List<MudancaTreinador> mudancaTreinadors = new ArrayList<MudancaTreinador>();
		MudancaTreinador mudancaTreinador = null;

		for (Clube c : clubesMudarTreinador) {
			
			novoTreinadorEscolhido = false;
			treinador = null;
			
			int i = 0;
			while (!novoTreinadorEscolhido && i < treinadoresDisponiveis.size()) {
				treinador = RoletaUtil.sortearPesoUm(treinadoresDisponiveis);
				if (!c.getTreinador().equals(treinador)) {
					novoTreinadorEscolhido = true;
				}
				
				i++;
			}
			
			if (novoTreinadorEscolhido) {
				mudancaTreinador = new MudancaTreinador();
				mudancaTreinador.setClube(c);
				mudancaTreinador.setTreinadorAntigo(c.getTreinador());
				mudancaTreinador.setTreinadorNovo(treinador);
				mudancaTreinador.setTemporada(temporada);
				mudancaTreinadors.add(mudancaTreinador);
	
				//System.err.println(String.format("%s\t->\t%s", c.getTreinador().toString(), treinador.toString()));
	
				c.setTreinador(treinador);
	
				treinadoresDisponiveis.remove(treinador);
			} else {
				System.err.println("Não há treinador disponível");
			}

		}

		clubeRepository.saveAll(clubesMudarTreinador);
		mudancaTreinadorRepository.saveAll(mudancaTreinadors);

	}

}
