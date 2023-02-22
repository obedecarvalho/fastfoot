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
import com.fastfoot.match.model.entity.EscalacaoClube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoClubeRepository;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.Posicao;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.dto.JogadorDTO;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.factory.JogadorFactory;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.PartidaResultadoJogavel;
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
	
	@Autowired
	private EscalacaoClubeRepository escalacaoClubePartidaRepository;
	
	private static final Comparator<EscalacaoJogadorDTO> COMPARATOR;
	
	static {

		COMPARATOR = new Comparator<EscalacaoJogadorDTO>() {
			
			@Override
			public int compare(EscalacaoJogadorDTO o1, EscalacaoJogadorDTO o2) {
				return o1.getEscalacao().compareTo(o2.getEscalacao());
			}
		};

	}
	
	@Deprecated
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
		List<EscalacaoClube> escalacoes = new ArrayList<EscalacaoClube>();
		for (Clube c : clubes) {
			escalarClube(c, escalacoes);
		}
		escalacaoClubePartidaRepository.saveAll(escalacoes);
		escalacaoJogadorPosicaoRepository.saveAll(escalacoes.stream()
				.flatMap(e -> e.getListEscalacaoJogadorPosicao().stream()).collect(Collectors.toList()));
		clubeRepository.saveAll(clubes);
		return CompletableFuture.completedFuture(Boolean.TRUE);
	}
	
	private void escalarClube(Clube clube, List<EscalacaoClube> escalacoes) {

		List<Jogador> jogadores = jogadorRepository.findByClubeAndStatusJogador(clube, StatusJogador.ATIVO);
		
		//List<EscalacaoJogadorPosicao> escalacao = gerarEscalacaoInicial(clube, jogadores, null);
		EscalacaoClube escalacaoClube = gerarEscalacaoInicial(clube, jogadores, null);
		
		//
		clube.setForcaGeralAtual(new Double(escalacaoClube.getListEscalacaoJogadorPosicao().stream()
				.filter(e -> EscalacaoPosicao.getEscalacaoTitulares().contains(e.getEscalacaoPosicao()))
				.mapToDouble(e -> e.getJogador().getForcaGeral()).average().getAsDouble()).intValue());
		//

		escalacoes.add(escalacaoClube);
	}

	public EscalacaoClube gerarEscalacaoInicial(Clube clube, List<Jogador> jogadores, PartidaResultadoJogavel partida) {
		
		EscalacaoClube escalacaoClubePartida = new EscalacaoClube(clube, partida, true);
		
		//Comparator<Jogador> comparator = JogadorFactory.getComparator();
		
		List <EscalacaoJogadorPosicao> escalacao = new ArrayList<EscalacaoJogadorPosicao>();
		
		List<Jogador> jogPos = null;
		
		List<EscalacaoPosicao> posicoesVazias = new ArrayList<EscalacaoPosicao>();
		Map<Posicao, List<Jogador>> jogadoresSuplentes = new HashMap<Posicao, List<Jogador>>();
		
		//Gol
		jogPos = jogadores.stream().filter(j -> Posicao.GOLEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());

		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_GOL, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_GOL);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_1, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_1);
		}
		
		if (jogPos.size() > 2) {
			jogadoresSuplentes.put(Posicao.GOLEIRO, jogPos.subList(2, jogPos.size()));
		}
		
		//Zag
		jogPos = jogadores.stream().filter(j -> Posicao.ZAGUEIRO.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_ZD, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_ZD);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_ZE, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_ZE);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_2, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_2);
		}
		
		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.ZAGUEIRO, jogPos.subList(3, jogPos.size()));
		}
		
		//Lateral
		jogPos = jogadores.stream().filter(j -> Posicao.LATERAL.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_LD, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_LD);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_LE, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_LE);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_4, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_4);
		}

		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.LATERAL, jogPos.subList(3, jogPos.size()));
		}
		
		//Vol
		jogPos = jogadores.stream().filter(j -> Posicao.VOLANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_VD, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_VD);
		}
		
		if (jogPos.size() > 1) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_VE, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_VE);
		}
		
		if (jogPos.size() > 2) {
		escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_3, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_3);
		}
		
		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.VOLANTE, jogPos.subList(3, jogPos.size()));
		}
		
		//Meia
		jogPos = jogadores.stream().filter(j -> Posicao.MEIA.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_MD, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_MD);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_ME, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_ME);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_5, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_5);
		}

		if (jogPos.size() > 3) {
			jogadoresSuplentes.put(Posicao.MEIA, jogPos.subList(3, jogPos.size()));
		}
		
		//Ata
		jogPos = jogadores.stream().filter(j -> Posicao.ATACANTE.equals(j.getPosicao())).sorted(JogadorFactory.getComparator())
				.collect(Collectors.toList());
		if (jogPos.size() > 0) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_AD, jogPos.get(0), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_AD);
		}
		
		if (jogPos.size() > 1) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_AE, jogPos.get(1), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_AE);
		}
		
		if (jogPos.size() > 2) {
			escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, EscalacaoPosicao.P_RES_6, jogPos.get(2), true));
		} else {
			posicoesVazias.add(EscalacaoPosicao.P_RES_6);
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
					escalacao.add(new EscalacaoJogadorPosicao(clube, escalacaoClubePartida, ep, possiveisJogadores.get(0), true));
					possiveisJogadores.remove(0);
					naoPreenchido = false;
				}
				i++;
			}
			
			if (naoPreenchido) throw new RuntimeException("Erro ao escalar clube");
		}
		
		escalacaoClubePartida.setListEscalacaoJogadorPosicao(escalacao);
		
		return escalacaoClubePartida;
	}
}
