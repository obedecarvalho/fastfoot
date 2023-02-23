package com.fastfoot.match.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.dto.EscalacaoClubeDTO;
import com.fastfoot.match.model.dto.EscalacaoJogadorDTO;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.match.model.repository.EscalacaoJogadorPosicaoRepository;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.dto.JogadorDTO;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.repository.JogadorRepository;
import com.fastfoot.scheduler.model.dto.ClubeDTO;

@Service
public class ConsultarEscalacaoClubeService {
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private EscalacaoJogadorPosicaoRepository escalacaoJogadorPosicaoRepository;
	
	private static final Comparator<EscalacaoJogadorDTO> COMPARATOR;
	
	static {

		COMPARATOR = new Comparator<EscalacaoJogadorDTO>() {
			
			@Override
			public int compare(EscalacaoJogadorDTO o1, EscalacaoJogadorDTO o2) {
				return o1.getEscalacao().compareTo(o2.getEscalacao());
			}
		};

	}

	public EscalacaoClubeDTO consultarEscalacaoClube(Clube c) {
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
}
