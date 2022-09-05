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
	
	//private Long idJogador;
	
	//private String jogadorNome;
	
	//private String jogadorPosicao;
	
	//private Integer jogadorForcaGeral;
	
	//private Integer escalacaoOrdinal;
	
	private Integer escalacao;
	
	private String escalacaoDesc;
	
	//private Integer idClube;

	/*public Long getIdJogador() {
		return idJogador;
	}

	public void setIdJogador(Long idJogador) {
		this.idJogador = idJogador;
	}

	public String getJogadorNome() {
		return jogadorNome;
	}

	public void setJogadorNome(String jogadorNome) {
		this.jogadorNome = jogadorNome;
	}

	public String getJogadorPosicao() {
		return jogadorPosicao;
	}

	public void setJogadorPosicao(String jogadorPosicao) {
		this.jogadorPosicao = jogadorPosicao;
	}*/

	/*public Integer getEscalacaoOrdinal() {
		return escalacaoOrdinal;
	}

	public void setEscalacaoOrdinal(Integer escalacaoOrdinal) {
		this.escalacaoOrdinal = escalacaoOrdinal;
	}*/

	/*public Integer getIdClube() {
		return idClube;
	}

	public void setIdClube(Integer idClube) {
		this.idClube = idClube;
	}*/
	
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

	/*public Integer getJogadorForcaGeral() {
		return jogadorForcaGeral;
	}

	public void setJogadorForcaGeral(Integer jogadorForcaGeral) {
		this.jogadorForcaGeral = jogadorForcaGeral;
	}*/

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
		/*dto.setIdJogador(escalacaoJogador.getJogador().getId());
		dto.setJogadorNome(escalacaoJogador.getJogador().getNome());
		dto.setJogadorPosicao(escalacaoJogador.getJogador().getPosicao().name());
		dto.setJogadorForcaGeral(escalacaoJogador.getJogador().getForcaGeral());*/
		//dto.setIdClube(escalacaoJogador.getJogador().getClube().getId());
		dto.setEscalacao(escalacaoJogador.getEscalacaoPosicao().ordinal());
		dto.setEscalacaoDesc(escalacaoJogador.getEscalacaoPosicao().getDescricao());

		return dto;
	}

}
