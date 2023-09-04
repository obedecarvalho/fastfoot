package com.fastfoot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.club.model.entity.ClubeTituloRanking;
import com.fastfoot.club.model.repository.ClubeRankingRepository;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.club.model.repository.ClubeTituloRankingRepository;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.financial.model.repository.MovimentacaoFinanceiraRepository;
import com.fastfoot.model.Constantes;
import com.fastfoot.model.Liga;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.model.entity.Parametro;
import com.fastfoot.model.repository.JogoRepository;
import com.fastfoot.model.repository.LigaJogoRepository;
import com.fastfoot.model.repository.ParametroRepository;
import com.fastfoot.player.service.CriarJogadoresClubeService;

@Service
public class CriarJogoService {
	
	//###	REPOSITORY	###
	
	@Autowired
	private JogoRepository jogoRepository;
	
	@Autowired
	private LigaJogoRepository ligaJogoRepository;
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	@Autowired
	private ClubeRankingRepository clubeRankingRepository;
	
	@Autowired
	private ClubeTituloRankingRepository clubeTituloRankingRepository;
	
	@Autowired
	private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private PreCarregarClubeService preCarregarClubeService;
	
	@Autowired
	private PreCarregarParametrosService preCarregarParametrosService;
	
	@Autowired
	private CriarJogadoresClubeService criarJogadoresClubeService;

	public Jogo criarJogo() {
		
		Optional<Long> optLastId = clubeRepository.findLastId();//TODO: syncronized??
		
		Long lastId = 0l;
		
		if (optLastId.isPresent()) {
			lastId = optLastId.get();
		}
		
		Jogo jogo = new Jogo();
		jogo.setDataCriacao(new Date());
		
		List<LigaJogo> ligaJogos = new ArrayList<LigaJogo>();
		
		LigaJogo ligaJogo = null;
		
		List<ClubeRanking> clubeRankings = new ArrayList<ClubeRanking>();
		List<ClubeTituloRanking> clubeTituloRankings = new ArrayList<ClubeTituloRanking>();
		
		//GENEBE
		ligaJogo = new LigaJogo();
		ligaJogo.setLiga(Liga.GENEBE);
		ligaJogo.setJogo(jogo);
		ligaJogo.setIdClubeInicial(lastId + 1);
		ligaJogo.setIdClubeFinal(lastId + Constantes.NRO_CLUBES_POR_LIGA);
		ligaJogos.add(ligaJogo);
		ligaJogo.setClubes(preCarregarClubeService.inicializarClubesPadraoGENEBE(ligaJogo));
		clubeRankings.addAll(preCarregarClubeService.inicializarClubesRankingPorLiga(ligaJogo.getClubes()));
		clubeTituloRankings.addAll(preCarregarClubeService.inicializarClubeTituloRankingPorLiga(ligaJogo.getClubes()));
		lastId += Constantes.NRO_CLUBES_POR_LIGA;
		
		//SPAPOR
		ligaJogo = new LigaJogo();
		ligaJogo.setLiga(Liga.SPAPOR);
		ligaJogo.setJogo(jogo);
		ligaJogo.setIdClubeInicial(lastId + 1);
		ligaJogo.setIdClubeFinal(lastId + Constantes.NRO_CLUBES_POR_LIGA);
		ligaJogos.add(ligaJogo);
		ligaJogo.setClubes(preCarregarClubeService.inicializarClubesPadraoSPAPOR(ligaJogo));
		clubeRankings.addAll(preCarregarClubeService.inicializarClubesRankingPorLiga(ligaJogo.getClubes()));
		clubeTituloRankings.addAll(preCarregarClubeService.inicializarClubeTituloRankingPorLiga(ligaJogo.getClubes()));
		lastId += Constantes.NRO_CLUBES_POR_LIGA;
		
		//ITAFRA
		ligaJogo = new LigaJogo();
		ligaJogo.setLiga(Liga.ITAFRA);
		ligaJogo.setJogo(jogo);
		ligaJogo.setIdClubeInicial(lastId + 1);
		ligaJogo.setIdClubeFinal(lastId + Constantes.NRO_CLUBES_POR_LIGA);
		ligaJogos.add(ligaJogo);
		ligaJogo.setClubes(preCarregarClubeService.inicializarClubesPadraoITAFRA(ligaJogo));
		clubeRankings.addAll(preCarregarClubeService.inicializarClubesRankingPorLiga(ligaJogo.getClubes()));
		clubeTituloRankings.addAll(preCarregarClubeService.inicializarClubeTituloRankingPorLiga(ligaJogo.getClubes()));
		lastId += Constantes.NRO_CLUBES_POR_LIGA;
		
		//ENGLND
		ligaJogo = new LigaJogo();
		ligaJogo.setLiga(Liga.ENGLND);
		ligaJogo.setJogo(jogo);
		ligaJogo.setIdClubeInicial(lastId + 1);
		ligaJogo.setIdClubeFinal(lastId + Constantes.NRO_CLUBES_POR_LIGA);
		ligaJogos.add(ligaJogo);
		ligaJogo.setClubes(preCarregarClubeService.inicializarClubesPadraoENGLND(ligaJogo));
		clubeRankings.addAll(preCarregarClubeService.inicializarClubesRankingPorLiga(ligaJogo.getClubes()));
		clubeTituloRankings.addAll(preCarregarClubeService.inicializarClubeTituloRankingPorLiga(ligaJogo.getClubes()));
		lastId += Constantes.NRO_CLUBES_POR_LIGA;
		
		List<Parametro> parametros = preCarregarParametrosService.inicializarParametrosPadrao(jogo);
		
		List<MovimentacaoFinanceira> movimentacaoFinanceiras = preCarregarClubeService.inicializarCaixaInicial(
				ligaJogos.stream().flatMap(l -> l.getClubes().stream()).collect(Collectors.toList()));
		
		salvar(jogo, ligaJogos, parametros, clubeRankings, clubeTituloRankings, movimentacaoFinanceiras);
		
		criarJogadores(jogo, ligaJogos);
		
		return jogo;
	}
	
	private void salvar(Jogo jogo, List<LigaJogo> ligaJogos, List<Parametro> parametros,
			List<ClubeRanking> clubeRankings, List<ClubeTituloRanking> clubeTituloRankings,
			List<MovimentacaoFinanceira> movimentacaoFinanceiras) {
		jogoRepository.save(jogo);
		ligaJogoRepository.saveAll(ligaJogos);
		clubeRepository.saveAll(ligaJogos.stream().flatMap(l -> l.getClubes().stream()).collect(Collectors.toList()));
		parametroRepository.saveAll(parametros);
		clubeRankingRepository.saveAll(clubeRankings);
		clubeTituloRankingRepository.saveAll(clubeTituloRankings);
		movimentacaoFinanceiraRepository.saveAll(movimentacaoFinanceiras);
	}
	
	private void criarJogadores(Jogo jogo, List<LigaJogo> ligaJogos) {
		
		int qtdeClube = Constantes.NRO_CLUBES_POR_LIGA / 2;
		
		List<CompletableFuture<Boolean>> criarJogadorFuture = new ArrayList<CompletableFuture<Boolean>>();
		
		for (LigaJogo liga : ligaJogos) {
			criarJogadorFuture
					.add(criarJogadoresClubeService.criarJogadoresClube(jogo, liga.getClubes().subList(0, qtdeClube)));
			criarJogadorFuture.add(criarJogadoresClubeService.criarJogadoresClube(jogo,
					liga.getClubes().subList(qtdeClube, liga.getClubes().size())));
		}
		
		CompletableFuture.allOf(criarJogadorFuture.toArray(new CompletableFuture<?>[0])).join();
	}
}
