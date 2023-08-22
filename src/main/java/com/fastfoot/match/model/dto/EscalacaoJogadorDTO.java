package com.fastfoot.match.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fastfoot.match.model.EscalacaoPosicao;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.player.model.dto.JogadorDTO;
import com.fastfoot.scheduler.model.dto.ClubeDTO;

public class EscalacaoJogadorDTO {
	
	private JogadorDTO jogador;
	
	private ClubeDTO clube;
	
	private Integer escalacao;
	
	private String escalacaoDesc;

	public EscalacaoJogadorDTO() {

	}
	
	public EscalacaoJogadorDTO(JogadorDTO jogadorDTO, ClubeDTO clubeDTO, EscalacaoPosicao escalacaoPosicao) {
		this.clube = clubeDTO;
		this.jogador = jogadorDTO;
		this.escalacao = escalacaoPosicao.ordinal();
		this.escalacaoDesc = escalacaoPosicao.getDescricao();
	}

	public Integer getEscalacao() {
		return escalacao;
	}

	public void setEscalacao(Integer escalacao) {
		this.escalacao = escalacao;
	}

	public JogadorDTO getJogador() {
		return jogador;
	}

	public void setJogador(JogadorDTO jogador) {
		this.jogador = jogador;
	}

	public ClubeDTO getClube() {
		return clube;
	}

	public void setClube(ClubeDTO clube) {
		this.clube = clube;
	}

	public String getEscalacaoDesc() {
		return escalacaoDesc;
	}

	public void setEscalacaoDesc(String escalacaoDesc) {
		this.escalacaoDesc = escalacaoDesc;
	}

	public static List<EscalacaoJogadorDTO> convertToDTO(List<EscalacaoJogadorPosicao> escalacaoClube) {
		return escalacaoClube.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}

	public static EscalacaoJogadorDTO convertToDTO(EscalacaoJogadorPosicao escalacaoJogador) {
		EscalacaoJogadorDTO dto = new EscalacaoJogadorDTO();
		
		dto.setJogador(JogadorDTO.convertToDTO(escalacaoJogador.getJogador()));
		dto.setClube(ClubeDTO.convertToDTO(escalacaoJogador.getClube()));
		dto.setEscalacao(escalacaoJogador.getEscalacaoPosicao().ordinal());
		dto.setEscalacaoDesc(escalacaoJogador.getEscalacaoPosicao().getDescricao());

		return dto;
	}

}
