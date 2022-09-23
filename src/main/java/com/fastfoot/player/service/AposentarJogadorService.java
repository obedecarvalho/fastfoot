package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.QuantitativoPosicaoClubeDTO;
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

	/*@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> aposentarJogador(List<GrupoDesenvolvimentoJogador> gruposDesenvolvimento) {

		List<GrupoDesenvolvimentoJogador> newGruposDesenvolvimento = new ArrayList<GrupoDesenvolvimentoJogador>();

		for (GrupoDesenvolvimentoJogador gdj : gruposDesenvolvimento) {

			//aposentar
			gdj.getJogador().setStatusJogador(StatusJogador.APOSENTADO);//TODO: transformar em UPDATE?
			gdj.setAtivo(false);

			//substituto
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
			jogHab.addAll(j.getHabilidades());
		}
		habilidadeValorRepository.saveAll(jogHab);
		

		jogadores = gruposDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(gruposDesenvolvimento);

		//

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}*/
	
	@Async("jogadorServiceExecutor")
	public CompletableFuture<Boolean> aposentarJogador(
			Map<Clube, List<GrupoDesenvolvimentoJogador>> grupoDesenvolvimentoClube,
			Map<Clube, List<QuantitativoPosicaoClubeDTO>> quantitativoPosicaoPorClube) {//TODO: deletar HabilidadeValorEstatisticaGrupo do jogador aposentado?
		
		List<GrupoDesenvolvimentoJogador> newGruposDesenvolvimento = new ArrayList<GrupoDesenvolvimentoJogador>();
		List<GrupoDesenvolvimentoJogador> gruposDesenvolvimentoAposentar = new ArrayList<GrupoDesenvolvimentoJogador>();
		
		List<GrupoDesenvolvimentoJogador> gruposDesenvolvimento = null;
		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeDTOs = null;
		//List<Posicao> posicaoNecessidade = new ArrayList<Posicao>();
		Posicao p = null;
		
		for (Clube c : grupoDesenvolvimentoClube.keySet()) {
			gruposDesenvolvimento =  grupoDesenvolvimentoClube.get(c);
			quantitativoPosicaoClubeDTOs = quantitativoPosicaoPorClube.get(c);
			//posicaoNecessidade.clear();
			
			if (quantitativoPosicaoClubeDTOs.size() != (Posicao.values().length - 1)) {
				throw new RuntimeException("Quantitativo de jogadores por posição diferente do esperado");
			}
			
			/*for (int i = 0; i < gruposDesenvolvimento.size(); i++) {
				Collections.sort(quantitativoPosicaoClubeDTOs);
				posicaoNecessidade.add(quantitativoPosicaoClubeDTOs.get(0).getPosicao());
				quantitativoPosicaoClubeDTOs.get(0).setQtde(quantitativoPosicaoClubeDTOs.get(0).getQtde() + 1);
			}*/

			for (GrupoDesenvolvimentoJogador gdj : gruposDesenvolvimento) {
				
				Collections.sort(quantitativoPosicaoClubeDTOs);
				quantitativoPosicaoClubeDTOs.get(0).setQtde(quantitativoPosicaoClubeDTOs.get(0).getQtde() + 1);
				p = gdj.getJogador().getPosicao().isGoleiro() ? gdj.getJogador().getPosicao()
						: quantitativoPosicaoClubeDTOs.get(0).getPosicao();
				
				//aposentar
				gdj.getJogador().setStatusJogador(StatusJogador.APOSENTADO);
				gdj.setAtivo(false);
				
				//substituto
				newGruposDesenvolvimento.add(criarNovoJogadorSubsAposentado(gdj.getJogador().getClube(),
						p, //gdj.getJogador().getPosicao(), 
						gdj.getJogador().getNumero(), gdj.getCelulaDesenvolvimento(),
						gdj.getJogador().getForcaGeralPotencialEfetiva().intValue()));
				
			}
			
			gruposDesenvolvimentoAposentar.addAll(gruposDesenvolvimento);
		}

		//
		List<Jogador> jogadores = null;

		//Novos
		jogadores = newGruposDesenvolvimento.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(newGruposDesenvolvimento);
		List<HabilidadeValor> jogHab = new ArrayList<HabilidadeValor>();
		for (Jogador j : jogadores) {
			jogHab.addAll(j.getHabilidades());
		}
		habilidadeValorRepository.saveAll(jogHab);
		
		//TODO: transformar em UPDATE?
		jogadores = gruposDesenvolvimentoAposentar.stream().map(gd -> gd.getJogador()).collect(Collectors.toList());
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(gruposDesenvolvimentoAposentar);
		//

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private GrupoDesenvolvimentoJogador criarNovoJogadorSubsAposentado(Clube clube, Posicao posicao, Integer numero,
			CelulaDesenvolvimento celulaDesenvolvimento, Integer forcaGeral) {

		Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(clube, posicao, numero,
				JogadorFactory.IDADE_MIN);
		
		//Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(clube, posicao, numero, JogadorFactory.IDADE_MIN, forcaGeral);
		
		GrupoDesenvolvimentoJogador grupoDesenvolvimentoJogador = new GrupoDesenvolvimentoJogador(celulaDesenvolvimento,
				novoJogador, true);

		return grupoDesenvolvimentoJogador;
	}
}
