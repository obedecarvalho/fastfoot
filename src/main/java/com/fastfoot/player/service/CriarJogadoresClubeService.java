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
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorDetalheRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.scheduler.service.crud.TemporadaCRUDService;
import com.fastfoot.service.CarregarParametroService;
import com.fastfoot.service.util.RandomUtil;
import com.fastfoot.service.util.RoletaUtil;

@Service
public class CriarJogadoresClubeService {
	
	private static final Integer NUM_JOGADORES_LINHA_POR_POSICAO = 4;
	
	private static final Integer NUM_JOGADORES_GOLEIRO = 3;
	
	//###	REPOSITORY	###
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	/*@Autowired
	private ClubeRepository clubeRepository;*/

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;
	
	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private CarregarParametroService carregarParametroService;
	
	@Autowired
	private CalcularValorTransferenciaService calcularValorTransferenciaService;
	
	@Autowired
	private CalcularValorTransferenciaJogadorPorHabilidadeService calcularValorTransferenciaJogadorPorHabilidadeService;
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;

	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;

	@Autowired
	private TemporadaCRUDService temporadaCRUDService;
	
	@Autowired
	private SemanaCRUDService semanaCRUDService;
	
	@Autowired
	private CalcularSalarioContratoService calcularSalarioContratoService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> criarJogadoresClube(List<Clube> clubes) {

		List<Jogador> jogadores = new ArrayList<Jogador>();
		
		Semana s = semanaCRUDService.getProximaSemana();
		
		for (Clube c : clubes) {
			criarJogadoresClube(c, jogadores, s);
		}
		
		calcularValorTransferencia(jogadores);
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : jogadores) {
			calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		List<HabilidadeValor> habilidades = jogadores.stream().flatMap(j -> j.getHabilidades().stream())
				.collect(Collectors.toList());
		
		List<Contrato> contratos = jogadores.stream().map(j -> j.getContratoAtual()).collect(Collectors.toList());

		jogadorDetalheRepository
				.saveAll(jogadores.stream().map(Jogador::getJogadorDetalhe).collect(Collectors.toList()));
		jogadorRepository.saveAll(jogadores);
		contratoRepository.saveAll(contratos);
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

	protected CompletableFuture<Boolean> criarJogadoresClube(Clube clube, List<Jogador> jogadores, Semana semanaInicialContrato) {

		Jogador j = null;
		List<Jogador> jogadoresClube = new ArrayList<Jogador>();

		//Goleiro
		for (int i = 0; i < NUM_JOGADORES_GOLEIRO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.GOLEIRO, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}

		//Jogadores Linha
		for (Posicao posicao : Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE)) {
			for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
				j = JogadorFactory.getInstance().gerarJogador(posicao, clube.getForcaGeral());
				j.setClube(clube);
				int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
				double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
				j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
				jogadoresClube.add(j);
			}
		}
		
		/*//Zagueiro
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.ZAGUEIRO, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}

		//Lateral
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.LATERAL, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}

		//Volante
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.VOLANTE, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}

		//Meia
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.MEIA, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}

		//Atacante
		for (int i = 0; i < NUM_JOGADORES_LINHA_POR_POSICAO; i++) {
			j = JogadorFactory.getInstance().gerarJogador(Posicao.ATACANTE, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
			jogadoresClube.add(j);
		}*/
		
		//Jogadores Adicionais
		int qtdeJogadoresAdicionais = RandomUtil.sortearIntervalo(0, 4);
		Posicao p;
		for (int i = 0; i < qtdeJogadoresAdicionais; i++) {
			p = RoletaUtil.sortearPesoUm(
					Arrays.asList(Posicao.ZAGUEIRO, Posicao.LATERAL, Posicao.VOLANTE, Posicao.MEIA, Posicao.ATACANTE));
			j = JogadorFactory.getInstance().gerarJogador(p, clube.getForcaGeral());
			j.setClube(clube);
			int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(j.getIdade());
			double salario = calcularSalarioContratoService.calcularSalarioContrato(j, tempoContrato);
			j.setContratoAtual(new Contrato(clube, j, semanaInicialContrato, tempoContrato, true, salario));
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
