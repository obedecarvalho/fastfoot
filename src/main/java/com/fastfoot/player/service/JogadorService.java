package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
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
	private GrupoDesenvolvimentoJogadorRepository grupoDesenvolvimentoJogadorRepository;

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(List<Clube> clubes) {

		List<GrupoDesenvolvimentoJogador> gruposJogador = new ArrayList<GrupoDesenvolvimentoJogador>();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c, gruposJogador);
		}
		
		grupoDesenvolvimentoJogadorRepository.saveAll(gruposJogador);
		
		return CompletableFuture.completedFuture(true);
	}

	protected void associarGrupoDesenvolvimento(Jogador j, List<GrupoDesenvolvimentoJogador> gruposJogador, int pos) {
		int i = pos % CelulaDesenvolvimento.getAll().length;
		gruposJogador.add(new GrupoDesenvolvimentoJogador(CelulaDesenvolvimento.getAll()[i], j, true));
	}

	//@Async("jogadorServiceExecutor")
	protected CompletableFuture<Boolean> criarJogadoresClube(Clube clube, List<GrupoDesenvolvimentoJogador> grupoDesenvolvimentos) {

		List<Jogador> jogadores = new ArrayList<Jogador>();
		Jogador j = null;
		int i = 0;
		
		//Goleiro
		
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 1);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 12);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.GOLEIRO, 23);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		//Zagueiro

		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 3);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 4);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 13);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ZAGUEIRO, 14);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Lateral
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 2);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 6);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 22);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.LATERAL, 16);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Volante
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 5);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 8);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 15);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.VOLANTE, 18);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Meia
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 7);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 10);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 17);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.MEIA, 20);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 9);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 11);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 19);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.gerarJogador(clube, Posicao.ATACANTE, 21);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
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
		
		List <EscalacaoJogadorPosicao> escalacao = gerarEscalacaoInicial(clube, jogadores);
		
		escalacaoJogadorPosicaoRepository.saveAll(escalacao);
		
		return CompletableFuture.completedFuture(true);
	}
	
	private List<EscalacaoJogadorPosicao> gerarEscalacaoInicial(Clube clube, List<Jogador> jogadores) {
		
		Comparator<Jogador> comparator = new Comparator<Jogador>() {

			@Override
			public int compare(Jogador o1, Jogador o2) {
				//return o1.getForcaGeral().compareTo(o2.getForcaGeral());
				return o2.getForcaGeral().compareTo(o1.getForcaGeral());//reverse
			}
		};
		
		List <EscalacaoJogadorPosicao> escalacao = new ArrayList<EscalacaoJogadorPosicao>();
		
		List<Jogador> jogPos = null;
		
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());

		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_1, jogPos.get(0)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_3, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_4, jogPos.get(1)));
		
		
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_2, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_6, jogPos.get(1)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_5, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_8, jogPos.get(1)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_7, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_10, jogPos.get(1)));
		
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_9, jogPos.get(0)));
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_11, jogPos.get(1)));
		
		return escalacao;
	}

	public List<Jogador> getJogadoresPorClube(Integer idClube) {
		Optional<Clube> clubeOpt = clubeRepository.findById(idClube);

		if (clubeOpt.isPresent()) {
			return jogadorRepository.findByClubeAndAposentado(clubeOpt.get(), false);
		}

		return null;
	}
}
