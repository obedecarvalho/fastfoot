package com.fastfoot.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.repository.ClubeRepository;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.dto.EscalacaoClubeDTO;
import com.fastfoot.match.model.dto.EscalacaoJogadorDTO;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.dto.JogadorDTO;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.dto.ClubeDTO;
import com.fastfoot.service.util.ValidatorUtil;

@Service
public class EscalarClubeService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;
	
	@Autowired
	private ClubeRepository clubeRepository;
	
	private static final Comparator<EscalacaoJogadorDTO> COMPARATOR;
	
	static {

		COMPARATOR = new Comparator<EscalacaoJogadorDTO>() {
			
			@Override
			public int compare(EscalacaoJogadorDTO o1, EscalacaoJogadorDTO o2) {
				return o1.getEscalacao().compareTo(o2.getEscalacao());
			}
		};

	}
	
	public EscalacaoClubeDTO getEscalacaoClube(Clube c) {
		List<EscalacaoJogadorPosicao> escalacao = escalacaoJogadorPosicaoRepository
				.findByClubeAndAtivoFetchJogadorHabilidades(c, true);
		
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(c, StatusJogador.ATIVO);
		
		EscalacaoClubeDTO escalacaoClubeDTO = new EscalacaoClubeDTO();
		
		escalacaoClubeDTO.setEscalacaoTitular(EscalacaoJogadorDTO.convertToDTO(
				escalacao.stream().filter(e -> e.getEscalacaoPosicao().isTitular()).collect(Collectors.toList())));
		
		escalacaoClubeDTO.setEscalacaoReserva(EscalacaoJogadorDTO.convertToDTO(
				escalacao.stream().filter(e -> !e.getEscalacaoPosicao().isTitular()).collect(Collectors.toList())));
		
		jogadores.removeAll(escalacao.stream().map(e -> e.getJogador()).collect(Collectors.toList()));
		
		ClubeDTO clubeDTO = ClubeDTO.convertToDTO(c);
		
		escalacaoClubeDTO.setEscalacaoSuplente(jogadores.stream()
				.map(j -> new EscalacaoJogadorDTO(JogadorDTO.convertToDTO(j), clubeDTO, EscalacaoPosicao.P_SUPLENTE))
				.collect(Collectors.toList()));
		
		Collections.sort(escalacaoClubeDTO.getEscalacaoTitular(), COMPARATOR);
		Collections.sort(escalacaoClubeDTO.getEscalacaoReserva(), COMPARATOR);
		
		return escalacaoClubeDTO;
	}

	@Async("defaultExecutor")
	public CompletableFuture<Boolean> escalarClubes(List<Clube> clubes) {
		List<EscalacaoJogadorPosicao> escalacoes = new ArrayList<EscalacaoJogadorPosicao>();
		for (Clube c : clubes) {
			escalarClube(c, escalacoes);
		}
		escalacaoJogadorPosicaoRepository.saveAll(escalacoes);
		clubeRepository.saveAll(clubes);
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	public void escalarClube(Clube clube, List<EscalacaoJogadorPosicao> escalacoes) {
		
		//List<EscalacaoJogadorPosicao> escalacao = escalacaoJogadorPosicaoRepository.findByClubeAndAtivo(clube, true);
		//escalacaoJogadorPosicaoRepository.desativarTodas();
		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(clube, StatusJogador.ATIVO);
		
		/*if (ValidatorUtil.isEmpty(escalacao)) {
			escalacao = gerarEscalacaoInicial(clube, jogadores);
		} else {
			atualizarEscalacao(clube, escalacao, jogadores);
		}*/
		
		List<EscalacaoJogadorPosicao> escalacao = gerarEscalacaoInicial(clube, jogadores);
		
		//
		clube.setForcaGeralAtual(new Double(escalacao.stream()
				.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
				.mapToDouble(e -> e.getJogador().getForcaGeral()).average().getAsDouble()).intValue());
		//

		//escalacaoJogadorPosicaoRepository.saveAll(escalacao);
		escalacoes.addAll(escalacao);
	}

	/*private void atualizarEscalacao(Clube clube, List<EscalacaoJogadorPosicao> escalacao, List<Jogador> jogadores) {
		//TODO: setar null quando não houver jogador pra ser escalado
		//TODO: inverter (dir/esq) MEI e ATA
		
		Map<EscalacaoPosicao, EscalacaoJogadorPosicao> escalacaoMap = escalacao.stream()
				.collect(Collectors.toMap(EscalacaoJogadorPosicao::getEscalacaoPosicao, Function.identity()));
		
		Comparator<Jogador> comparator = JogadorFactory.getComparator();
		
		List<Jogador> jogPos = null;
		
		EscalacaoJogadorPosicao ejp = null;
		
		//Gol
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());

		ejp = escalacaoMap.get(EscalacaoPosicao.P_1);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_12);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		//Zag
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_3);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_4);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_13);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Lat
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_2);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_6);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_16);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Vol
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_5);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_8);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_15);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Mei
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_7);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_10);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_17);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
		
		//Ata
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(comparator).collect(Collectors.toList());
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_9);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 0) {
			ejp.setJogador(jogPos.get(0));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_11);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 1) {
			ejp.setJogador(jogPos.get(1));
		}
		
		ejp = escalacaoMap.get(EscalacaoPosicao.P_19);
		if (!ValidatorUtil.isEmpty(ejp) && jogPos.size() > 2) {
			ejp.setJogador(jogPos.get(2));
		}
	}*/
	
	private List<EscalacaoJogadorPosicao> gerarEscalacaoInicial(Clube clube, List<Jogador> jogadores) {
		
		Comparator<Jogador> comparator = JogadorFactory.getComparator();
		
		List <EscalacaoJogadorPosicao> escalacao = new ArrayList<EscalacaoJogadorPosicao>();
		
		List<Jogador> jogPos = null;
		
		List<EscalacaoPosicao> posicoesVazias = new ArrayList<EscalacaoPosicao>();
		Map<Posicao, List<Jogador>> jogadoresSuplentes = new HashMap<Posicao, List<Jogador>>();
		
		//Gol
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());

		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_1, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_1);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_12, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_12);
		}
		
		if (jogPos.size() > 2) {
			jogadoresSuplentes.put(Posicao.GOLEIRO, jogPos.subList(2, jogPos.size()));
		}
		
		//Zag
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_3, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_3);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_4, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_4);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_13, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_13);
		}
		
		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.ZAGUEIRO, jogPos.subList(3, jogPos.size()));
		}
		
		//Lateral
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_2, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_2);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_6, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_6);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_16, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_16);
		}

		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.LATERAL, jogPos.subList(3, jogPos.size()));
		}
		
		//Vol
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_5, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_5);
		}
		
		if (jogPos.size() > 1) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_8, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_8);
		}
		
		if (jogPos.size() > 2) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_15, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_15);
		}
		
		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.VOLANTE, jogPos.subList(3, jogPos.size()));
		}
		
		//Meia
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_7, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_7);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_10, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_10);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_17, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_17);
		}

		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.MEIA, jogPos.subList(3, jogPos.size()));
		}
		
		//Ata
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(comparator)
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_9, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_9);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_11, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_11);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, EscalacaoPosicao.P_19, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_19);
		}

		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.ATACANTE, jogPos.subList(3, jogPos.size()));
		}
		
		Collections.sort(posicoesVazias);
		
		List<Jogador> possiveisJogadores = null;
		for (EscalacaoPosicao ep : posicoesVazias) {
			boolean naoPreenchido = true;
			int i = 1;
			
			while (naoPreenchido && i < ep.getOrdemPosicaoParaEscalar().length) {
				possiveisJogadores = jogadoresSuplentes.get(ep.getOrdemPosicaoParaEscalar()[i]);
				if (!ValidatorUtil.isEmpty(possiveisJogadores)) {
					escalacao.add(new EscalacaoJogadorPosicao(clube, ep, possiveisJogadores.get(0), true));
					possiveisJogadores.remove(0);
					naoPreenchido = false;
				}
				i++;
			}
			
			if (naoPreenchido) throw new RuntimeException("Erro ao escalar clube");
		}
		
		return escalacao;
	}
}
