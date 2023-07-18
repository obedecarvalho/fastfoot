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
import com.fastfoot.model.Constantes;
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
import com.fastfoot.player.model.repository.JogadorDetalheRepository;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.service.crud.SemanaCRUDService;
import com.fastfoot.service.util.RandomUtil;

@Service
public class AposentarJogadorService {
	
	//###	REPOSITORY	###

	@Autowired
	private HabilidadeValorRepository habilidadeValorRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private JogadorDetalheRepository jogadorDetalheRepository;

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

	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> aposentarJogador(List<GrupoDesenvolvimentoJogador> gruposDesenvolvimento) {

		List<GrupoDesenvolvimentoJogador> newGruposDesenvolvimento = new ArrayList<GrupoDesenvolvimentoJogador>();

		for (GrupoDesenvolvimentoJogador gdj : gruposDesenvolvimento) {

			//aposentar
			gdj.getJogador().setStatusJogador(StatusJogador.APOSENTADO);
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
	
	/*@Async("defaultExecutor")
	public CompletableFuture<Boolean> aposentarJogador(
			Map<Clube, List<GrupoDesenvolvimentoJogador>> grupoDesenvolvimentoClube,
			Map<Clube, List<QuantitativoPosicaoClubeDTO>> quantitativoPosicaoPorClube) {
		
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
			}* /

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
		
		List<HabilidadeGrupoValor> habilidadeGrupoValores = new ArrayList<HabilidadeGrupoValor>();
		
		for (Jogador jogador : jogadores) {
			calcularHabilidadeGrupoValorService.calcularHabilidadeGrupoValor(jogador, habilidadeGrupoValores);
		}
		
		jogadorDetalheRepository.saveAll(jogadores.stream().map(Jogador::getJogadorDetalhe).collect(Collectors.toList()));
		jogadorRepository.saveAll(jogadores);
		grupoDesenvolvimentoJogadorRepository.saveAll(newGruposDesenvolvimento);
		List<HabilidadeValor> jogHab = new ArrayList<HabilidadeValor>();
		for (Jogador j : jogadores) {
			jogHab.addAll(j.getHabilidades());
		}
		habilidadeValorRepository.saveAll(jogHab);
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);
		
		
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
	}*/
	
	@Async("defaultExecutor")
	public CompletableFuture<Boolean> aposentarJogador(Map<Clube, List<Jogador>> jogadoresAposentarPorClube,
			Map<Clube, List<QuantitativoPosicaoClubeDTO>> quantitativoPosicaoPorClube) {
		//TODO: deletar HabilidadeValorEstatisticaGrupo do jogador aposentado?
		//TODO: deletar HabilidadeValor do jogador aposentado?
		
		List<Jogador> novosJogadores = new ArrayList<Jogador>();
		List<Jogador> jogadoresAposentar = new ArrayList<Jogador>();
		
		List<Jogador> jogadoresClubeAposentar = null;
		List<QuantitativoPosicaoClubeDTO> quantitativoPosicaoClubeDTOs = null;
		
		Semana s = semanaCRUDService.getProximaSemana();

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
				System.err.println(String.format("Quantitativo de jogadores por posição diferente do esperado [%d]", c.getId()));
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
		
		jogadorDetalheRepository.saveAll(novosJogadores.stream().map(Jogador::getJogadorDetalhe).collect(Collectors.toList()));
		jogadorRepository.saveAll(novosJogadores);
		contratoRepository.saveAll(novosJogadores.stream().map(j -> j.getContratoAtual()).collect(Collectors.toList()));
		/*List<HabilidadeValor> jogHab = new ArrayList<HabilidadeValor>();
		for (Jogador j : novosJogadores) {
			jogHab.addAll(j.getHabilidades());
		}*/
		List<HabilidadeValor> jogHab = novosJogadores.stream().flatMap(j -> j.getHabilidades().stream())
				.collect(Collectors.toList());
		habilidadeValorRepository.saveAll(jogHab);
		habilidadeGrupoValorRepository.saveAll(habilidadeGrupoValores);
		
		//TODO: transformar em UPDATE?
		jogadorRepository.saveAll(jogadoresAposentar);
		//

		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private Jogador criarNovoJogadorSubsAposentado(Clube clube, Posicao posicao, Integer numero,
			Integer potencial, Semana semanaInicioContrato) {

		//TODO: passar modo de desenvolvimento do jogador aposentado??
		//TODO: potencial: sortear entre força jogador e força clube?

		Jogador novoJogador = JogadorFactory.getInstance().gerarJogador(posicao, JogadorFactory.IDADE_MIN, potencial);
		novoJogador.setClube(clube);
		novoJogador.setNumero(numero);
		novoJogador.setContratoAtual(new Contrato(clube, novoJogador, semanaInicioContrato, RandomUtil.sortearIntervalo(
				Constantes.NUMERO_ANO_MIN_CONTRATO_PADRAO, Constantes.NUMERO_ANO_MAX_CONTRATO_PADRAO + 1), false));

		return novoJogador;
	}
}
