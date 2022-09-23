package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class CriarJogadoresClubeService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;

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

	protected CompletableFuture<Boolean> criarJogadoresClube(Clube clube, List<GrupoDesenvolvimentoJogador> grupoDesenvolvimentos) {

		List<Jogador> jogadores = new ArrayList<Jogador>();
		Jogador j = null;
		int i = 0;
		
		//Goleiro
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 1);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 12);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 23);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		//Zagueiro

		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 3);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 4);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 13);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 14);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Lateral
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 2);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 6);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 22);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 16);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Volante
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 5);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 8);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 15);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 18);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Meia
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 7);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 10);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 17);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 20);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);

		//Atacante
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 9);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 11);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 19);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 21);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		//Jogadores Adicionais
		
		
		Posicao p = RoletaUtil.sortearPesoUm(Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE));
		j = JogadorFactory.getInstance().gerarJogador(clube, p, 24);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		p = RoletaUtil.sortearPesoUm(Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE));
		j = JogadorFactory.getInstance().gerarJogador(clube, p, 25);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		
		p = RoletaUtil.sortearPesoUm(Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE));
		j = JogadorFactory.getInstance().gerarJogador(clube, p, 26);
		associarGrupoDesenvolvimento(j, grupoDesenvolvimentos, i++);
		jogadores.add(j);
		//
		
		/*clube = clubeRepository.getById(clube.getId());
		clube.getForcaGeralAtual();*/

		//TODO: somente titulares ou usar potencial??
		/*clube.setForcaGeralAtual(
				(new Long(Math.round(jogadores.stream().mapToLong(Jogador::getForcaGeral).average().getAsDouble())))
						.intValue());*/
		
		clubeRepository.save(clube);

		jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());//TODO: agrupar habilidades e fazer apenas um saveAll
		}

		return CompletableFuture.completedFuture(true);
	}

}
