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
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.entity.Escalacao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.match.model.repository.EscalacaoRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
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

	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;
	
	@Autowired
	private EscalacaoRepository escalacaoRepository;

	/*public void criarJogadoresClube() {
		List<Clube> clubes = clubeRepository.findAll();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c);
		}
	}*/

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(List<Clube> clubes) {
		//List<Clube> clubes = clubeRepository.findAll();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c);
		}
		
		return CompletableFuture.completedFuture(true);
	}

	//@Async("jogadorServiceExecutor")
	protected CompletableFuture<Boolean> criarJogadoresClube(Clube clube) {
		
		Escalacao escalacao = new Escalacao();
		escalacao.setClube(clube);
		escalacao.setJogadorPosicoes(new ArrayList<EscalacaoJogadorPosicao>());
		
		List<Jogador> jogadores = new ArrayList<Jogador>();
		Jogador j = null;
		
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 1, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_1, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 12);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 23);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 3, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_3, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 4, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_4, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 13);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 14);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 2, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_2, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 6, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_6, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 22);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 16);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 5, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_5, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 8, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_8, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 15);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 18);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 7, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_7, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 10, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_10, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 17);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 20);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 9, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_9, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 11, true);
		escalacao.addJogadorPosicao(new EscalacaoJogadorPosicao(escalacao, EscalacaoPosicao.P_11, j));
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 19);
		jogadores.add(j);
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 21);
		jogadores.add(j);
		
		/*clube = clubeRepository.getById(clube.getId());
		clube.getForcaGeralAtual();*/

		//TODO: somente titulares ou usar potencial??
		clube.setForcaGeralAtual(
				(new Long(Math.round(jogadores.stream().mapToLong(Jogador::getForcaGeral).average().getAsDouble())))
						.intValue());
		
		clubeRepository.save(clube);

		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}
		
		escalacaoRepository.save(escalacao);
		
		escalacaoJogadorPosicaoRepository.saveAll(escalacao.getJogadorPosicoes());
		
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
