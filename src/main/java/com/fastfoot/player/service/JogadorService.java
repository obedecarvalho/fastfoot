package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.factory.JogadorZagueiroFactory;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class JogadorService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;

	public void criarJogadoresClube() {
		List<Clube> clubes = clubeRepository.findAll();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c);
		}
	}

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(List<Clube> clubes) {
		//List<Clube> clubes = clubeRepository.findAll();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c);
		}
		
		return CompletableFuture.completedFuture(true);
	}

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(Clube clube) {
		JogadorFactory jogadorFactory = JogadorZagueiroFactory.getInstance();
		
		List<Jogador> jogadores = new ArrayList<Jogador>();
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 1));
		
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 3));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 4));
		
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.LATERAL, 2));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 5));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.LATERAL, 6));
		
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.MEIA, 7));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.MEIA, 10));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 8));
		
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 9));
		jogadores.add(jogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 11));
		
		clube.setForcaGeralAtual((new Long(Math.round(jogadores.stream().mapToLong(Jogador::getForcaGeral).average().getAsDouble()))).intValue());
		
		clubeRepository.save(clube);

		jogadorRepository.saveAll(jogadores);
		for (Jogador j : jogadores) {
			habilidadeValorRepository.saveAll(j.getHabilidades());
		}
		
		return CompletableFuture.completedFuture(true);
	}

	public List<Jogador> getJogadoresPorClube(Integer idClube) {
		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);

		if (clubeOpt.isPresent()) {
			return jogadorRepository.findByClube(clubeOpt.get());
		}

		return null;
	}
}
