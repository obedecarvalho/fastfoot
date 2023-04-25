package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.ParametroConstantes;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorDetalheRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.service.CarregarParametroService;
import com.fastfoot.service.util.RandomUtil;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class CriarJogadoresClubeService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	/*@Autowired
	private ClubeRepository clubeRepository;*/

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;
	
	@Autowired
	private CarregarParametroService carregarParametroService;
	
	@Autowired
	private CalcularValorTransferenciaService calcularValorTransferenciaService;
	
	@Autowired
	private CalcularValorTransferenciaJogadorPorHabilidadeService calcularValorTransferenciaJogadorPorHabilidadeService;
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;
	
	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;

	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;
	
	private static final Integer NUM_JOGADORES_LINHA_POR_POSICAO = 4;
	
	private static final Integer NUM_JOGADORES_GOLEIRO = 3;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(List<Clube> clubes) {

		List<Jogador> jogadores = new ArrayList<Jogador>();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c, jogadores);
		}
		
		calcularValorTransferencia(jogadores);
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : jogadores) {
			calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		List<HabilidadeValor> habilidades = jogadores.stream().flatMap(j -> j.getHabilidades().stream())
				.collect(Collectors.toList());

		jogadorDetalheRepository
				.saveAll(jogadores.stream().map(Jogador::getJogadorDetalhe).collect(Collectors.toList()));
		jogadorRepository.saveAll(jogadores);
		habilidadeValorRepository.saveAll(habilidades);
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);

		return CompletableFuture.completedFuture(true);
	}
	
	protected void calcularValorTransferencia(List<Jogador> jogadores) {
		if (carregarParametroService.getParametroBoolean(ParametroConstantes.USAR_VERSAO_SIMPLIFICADA)) {
			for (Jogador jogador : jogadores) {
				calcularValorTransferenciaService.calcularValorTransferencia(jogador);
			}
		} else {
			for (Jogador jogador : jogadores) {
				calcularValorTransferenciaJogadorPorHabilidadeService.calcularValorTransferencia(jogador);
			}
		}
	}

	/*protected void associarGrupoDesenvolvimento(Jogador j, List<GrupoDesenvolvimentoJogador> gruposJogador, int pos) {
		int i = pos % CelulaDesenvolvimento.getAll().length;
		gruposJogador.add(new GrupoDesenvolvimentoJogador(CelulaDesenvolvimento.getAll()[i], j, true));
	}*/

	protected CompletableFuture<Boolean> criarJogadoresClube(Clube clube, List<Jogador> jogadores) {

		Jogador j = null;
		List<Jogador> jogadoresClube = new ArrayList<Jogador>();

		//Goleiro
		
		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 1);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 12);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO, 23);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_GOLEIRO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.GOLEIRO));
		}
		
		//Zagueiro

		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 3);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 4);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 13);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO, 14);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.ZAGUEIRO));
		}

		//Lateral
		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 2);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 6);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 22);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL, 16);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.LATERAL));
		}

		//Volante
		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 5);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 8);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 15);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE, 18);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.VOLANTE));
		}

		//Meia
		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 7);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 10);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 17);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA, 20);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.MEIA));
		}

		//Atacante
		/*j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 9);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 11);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 19);
		jogadores.add(j);
		
		j = JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE, 21);
		jogadores.add(j);*/
		
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			jogadoresClube.add(JogadorFactory.getInstance().gerarJogador(clube, Posicao.ATACANTE));
		}
		
		//Jogadores Adicionais
		
		int qtdeJogadoresAdicionais = RandomUtil.sortearIntervalo(0, 4);
		
		Posicao p;
		
		for (int i = 0; i < qtdeJogadoresAdicionais; i++) {
			p = RoletaUtil.sortearPesoUm(
					Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE));
			j = JogadorFactory.getInstance().gerarJogador(clube, p/*, 24 + i*/);
			jogadoresClube.add(j);
		}
		
		atualizarNumeroJogadoresService.gerarNumeroJogadores(jogadoresClube);

		//
		
		/*clube = clubeRepository.getById(clube.getId());
		clube.getForcaGeralAtual();*/

		//TODO: somente titulares ou usar potencial??
		/*clube.setForcaGeralAtual(
				(new Long(Math.round(jogadores.stream().mapToLong(Jogador::getForcaGeral).average().getAsDouble())))
						.intValue());*/
		
		//clubeRepository.save(clube);

		/*jogadorRepository.saveAll(jogadores);
		for (Jogador jog : jogadores) {
			habilidadeValorRepository.saveAll(jog.getHabilidades());
		}*/
		
		jogadores.addAll(jogadoresClube);

		return CompletableFuture.completedFuture(true);
	}

}
