package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.GrupoDesenvolvimentoJogadorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;

@Service
public class AposentarJogadorService {
	
	@Autowired
	private GrupoDesenvolvimentoJogadorRepository grupoDesenvolvimentoJogadorRepository;

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> aposentarJogador(List<GrupoDesenvolvimentoJogador> gruposDesenvolvimento) {
		
		//GrupoDesenvolvimentoJogador newGrupoDesenvolvimento = null;
		List<GrupoDesenvolvimentoJogador> newGruposDesenvolvimento = new ArrayList<GrupoDesenvolvimentoJogador>();

		for (GrupoDesenvolvimentoJogador gdj : gruposDesenvolvimento) {

			//aposentar
			//gdj.getJogador().setAposentado(true);
			gdj.getJogador().setStatusJogador(StatusJogador.APOSENTADO);
			gdj.setAtivo(false);

			//substituto
			/*newGrupoDesenvolvimento = criarNovoJogadorSubsAposentado(gdj.getJogador().getClube(),
					gdj.getJogador().getPosicao(), gdj.getJogador().getNumero(), gdj.getCelulaDesenvolvimento());
			
			jogadorRepository.save(gdj.getJogador());
			grupoDesenvolvimentoJogadorRepository.save(gdj);
			
			jogadorRepository.save(newGrupoDesenvolvimento.getJogador());
			grupoDesenvolvimentoJogadorRepository.save(newGrupoDesenvolvimento);
			habilidadeValorRepository.saveAll(newGrupoDesenvolvimento.getJogador().getHabilidades());*/

			newGruposDesenvolvimento.add(criarNovoJogadorSubsAposentado(gdj.getJogador().getClube(),
					gdj.getJogador().getPosicao(), gdj.getJogador().getNumero(), gdj.getCelulaDesenvolvimento(),
					gdj.getJogador().getForcaGeralPotencialEfetiva().intValue()));
			
		}
		
		//
		List<Jogador> jogadores = null;

		//Novos
		jogadores = newGruposDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(newGruposDesenvolvimento);
		List<HabilidadeValor> jogHab = new ArrayList<HabilidadeValor>();
		for (Jogador j : jogadores) {
			//habilidadeValorRepository.saveAll(j.getHabilidades());
			jogHab.addAll(j.getHabilidades());
		}
		habilidadeValorRepository.saveAll(jogHab);
		

		jogadores = gruposDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(gruposDesenvolvimento);

		//

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private GrupoDesenvolvimentoJogador criarNovoJogadorSubsAposentado(Clube clube, Posicao posicao, Integer numero, CelulaDesenvolvimento celulaDesenvolvimento, Integer forcaGeral) {

		//Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(clube, posicao, numero, JogadorFactory.IDADE_MIN);
		
		Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(clube, posicao, numero, JogadorFactory.IDADE_MIN, forcaGeral);
		
		GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador = new GrupoDesenvolvimentoJogador(celulaDesenvolvimento, novoJogador, true);

		return grupoDesenvolvimentoJogador;
	}
}
