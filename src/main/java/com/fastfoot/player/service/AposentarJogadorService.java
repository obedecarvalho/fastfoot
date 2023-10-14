package com.fastfoot.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.QuantitativoPosicaoClubeDTO;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.ContratoRepository;
import com.fastfoot.player.model.repository.HabilidadeGrupoValorRepository;
import com.fastfoot.player.model.repository.HabilidadeValorRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;

@Service
public class AposentarJogadorService {
	
	//###	REPOSITORY	###

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;

	@Autowired
	private HabilidadeGrupoValorRepository habilidadeGrupoValorRepository;

	@Autowired
	private ContratoRepository contratoRepository;
	
	//###	SERVICE	###
	
	@Autowired
	private AtualizarNumeroJogadoresService atualizarNumeroJogadoresService;
	
	@Autowired
	private CalcularHabilidadeGrupoValorService calcularHabilidadeGrupoValorService;

	@Autowired
	private SemanaCRUDService semanaCRUDService;

	@Autowired
	private CalcularSalarioContratoService calcularSalarioContratoService;

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> aposentarJogador(Jogo jogo, Map<Clube, List<Jogador>> jogadoresAposentarPorClube,
			Map<Clube, List<QuantitativoPosicaoClubeDTO>> quantitativoPosicaoPorClube) {
		//TODO: deletar HabilidadeValor do jogador aposentado?
		
		List<Jogador> novosJogadores = new ArrayList<Jogador>();
		List<Jogador> jogadoresAposentar = new ArrayList<Jogador>();
		
		List<Jogador> jogadoresClubeAposentar = null;
		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeDTOs = null;
		
		Semana s = semanaCRUDService.getProximaSemana(jogo);

		Posicao p = null;
		
		for (Clube c : jogadoresAposentarPorClube.keySet()) {
			jogadoresClubeAposentar =  jogadoresAposentarPorClube.get(c);
			quantitativoPosicaoClubeDTOs = quantitativoPosicaoPorClube.get(c);
			
			if (quantitativoPosicaoClubeDTOs.size() != (Posicao.values().length - 1)) {

				List<Posicao> posicoes = new ArrayList<Posicao>(Arrays.asList(Posicao.values()));

				posicoes.removeAll(quantitativoPosicaoClubeDTOs.stream().map(QuantitativoPosicaoClubeDTO::getPosicao)
						.collect(Collectors.toList()));

				QuantitativoPosicaoClubeDTO quantitativoPosicaoClubeDTO;

				for (Posicao posicao : posicoes) {
					quantitativoPosicaoClubeDTO = new QuantitativoPosicaoClubeDTO();

					quantitativoPosicaoClubeDTO.setClube(c);
					quantitativoPosicaoClubeDTO.setPosicao(posicao);
					quantitativoPosicaoClubeDTO.setQtde(0);

					quantitativoPosicaoClubeDTOs.add(quantitativoPosicaoClubeDTO);
				}
				
				//throw new RuntimeException(String.format("Quantitativo de jogadores por posição diferente do esperado [%d]", c.getId()));
				//System.err.println(String.format("Quantitativo de jogadores por posição diferente do esperado [%d]", c.getId()));
			}

			for (Jogador jogador : jogadoresClubeAposentar) {
				
				Collections.sort(quantitativoPosicaoClubeDTOs);
				
				p = jogador.getPosicao().isGoleiro() ? jogador.getPosicao() : quantitativoPosicaoClubeDTOs.get(0).getPosicao();
				if (!jogador.getPosicao().isGoleiro()) {
					quantitativoPosicaoClubeDTOs.get(0).setQtde(quantitativoPosicaoClubeDTOs.get(0).getQtde() + 1);
				}
				
				//aposentar
				jogador.setStatusJogador(StatusJogador.APOSENTADO);
				
				//substituto
				novosJogadores.add(criarNovoJogadorSubsAposentado(jogador.getClube(), p, null,
						/*jogador.getForcaGeralPotencial().intValue()*/ jogador.getClube().getForcaGeral(), s));
				
			}
			
			jogadoresAposentar.addAll(jogadoresClubeAposentar);
		}
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : novosJogadores) {
			calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		atualizarNumeroJogadoresService.atualizarNumeroJogadores(
				novosJogadores.stream().collect(Collectors.groupingBy(Jogador::getClube)),
				jogadoresAposentar.stream().collect(Collectors.groupingBy(Jogador::getClube)));
		
		jogadorRepository.saveAll(novosJogadores);
		contratoRepository.saveAll(novosJogadores.stream().map(j -> j.getContratoAtual()).collect(Collectors.toList()));
		List<HabilidadeValor> jogHab = novosJogadores.stream().flatMap(j -> j.getHabilidadesValor().stream())
				.collect(Collectors.toList());
		habilidadeValorRepository.saveAll(jogHab);
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);
		
		contratoRepository.desativarPorJogadores(jogadoresAposentar);
		
		//jogadorRepository.saveAll(jogadoresAposentar);
		jogadorRepository.updateStatusJogadores(jogadoresAposentar, StatusJogador.APOSENTADO);

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private Jogador criarNovoJogadorSubsAposentado(Clube clube, Posicao posicao, Integer numero,
			Integer potencial, Semana semanaInicioContrato) {

		//TODO: passar modo de desenvolvimento do jogador aposentado??
		//TODO: potencial: sortear entre força jogador e força clube?

		Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(posicao, JogadorFactory.IDADE_MIN, potencial);
		novoJogador.setClube(clube);
		novoJogador.setNumero(numero);
		int tempoContrato = RenovarContratosAutomaticamenteService.sortearTempoContrato(novoJogador.getIdade());
		double salario = calcularSalarioContratoService.calcularSalarioContrato(novoJogador, tempoContrato);
		novoJogador.setContratoAtual(new Contrato(clube, novoJogador, semanaInicioContrato, tempoContrato, true, salario));

		return novoJogador;
	}
}
