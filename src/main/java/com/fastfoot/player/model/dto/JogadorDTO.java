package com.fastfoot.player.model.dto;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

public class JogadorDTO {
	
	private String nome;

	private String clubeNome;
	
	private String posicao;
	
	private Integer numero;
	
	private Integer idade;
	
	private Integer forcaGeral;
	
	//Ação Fim
	
	private Integer valorPasse;

	private Integer valorFinalizacao;

	private Integer valorCruzamento;

	private Integer valorArmacao;

	private Integer valorCabeceio;

	//Reação

	private Integer valorMarcacao;

	private Integer valorDesarme;

	private Integer valorInterceptacao;

	//Ação Meio

	private Integer valorVelocidade;

	private Integer valorDrible;

	private Integer valorForca;
	
	//Ação Inicio

	private Integer valorPosicionamento;

	private Integer valorDominio;
	
	//Goleiro
	
	private Integer valorReflexo;
	
	private Integer valorJogoAereo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClubeNome() {
		return clubeNome;
	}

	public void setClubeNome(String clubeNome) {
		this.clubeNome = clubeNome;
	}

	public String getPosicao() {
		return posicao;
	}

	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getValorPasse() {
		return valorPasse;
	}

	public void setValorPasse(Integer valorPasse) {
		this.valorPasse = valorPasse;
	}

	public Integer getValorFinalizacao() {
		return valorFinalizacao;
	}

	public void setValorFinalizacao(Integer valorFinalizacao) {
		this.valorFinalizacao = valorFinalizacao;
	}

	public Integer getValorCruzamento() {
		return valorCruzamento;
	}

	public void setValorCruzamento(Integer valorCruzamento) {
		this.valorCruzamento = valorCruzamento;
	}

	public Integer getValorArmacao() {
		return valorArmacao;
	}

	public void setValorArmacao(Integer valorArmacao) {
		this.valorArmacao = valorArmacao;
	}

	public Integer getValorCabeceio() {
		return valorCabeceio;
	}

	public void setValorCabeceio(Integer valorCabeceio) {
		this.valorCabeceio = valorCabeceio;
	}

	public Integer getValorMarcacao() {
		return valorMarcacao;
	}

	public void setValorMarcacao(Integer valorMarcacao) {
		this.valorMarcacao = valorMarcacao;
	}

	public Integer getValorDesarme() {
		return valorDesarme;
	}

	public void setValorDesarme(Integer valorDesarme) {
		this.valorDesarme = valorDesarme;
	}

	public Integer getValorInterceptacao() {
		return valorInterceptacao;
	}

	public void setValorInterceptacao(Integer valorInterceptacao) {
		this.valorInterceptacao = valorInterceptacao;
	}

	public Integer getValorVelocidade() {
		return valorVelocidade;
	}

	public void setValorVelocidade(Integer valorVelocidade) {
		this.valorVelocidade = valorVelocidade;
	}

	public Integer getValorDrible() {
		return valorDrible;
	}

	public void setValorDrible(Integer valorDrible) {
		this.valorDrible = valorDrible;
	}

	public Integer getValorForca() {
		return valorForca;
	}

	public void setValorForca(Integer valorForca) {
		this.valorForca = valorForca;
	}

	public Integer getValorPosicionamento() {
		return valorPosicionamento;
	}

	public void setValorPosicionamento(Integer valorPosicionamento) {
		this.valorPosicionamento = valorPosicionamento;
	}

	public Integer getValorDominio() {
		return valorDominio;
	}

	public void setValorDominio(Integer valorDominio) {
		this.valorDominio = valorDominio;
	}

	public Integer getValorReflexo() {
		return valorReflexo;
	}

	public void setValorReflexo(Integer valorReflexo) {
		this.valorReflexo = valorReflexo;
	}

	public Integer getValorJogoAereo() {
		return valorJogoAereo;
	}

	public void setValorJogoAereo(Integer valorJogoAereo) {
		this.valorJogoAereo = valorJogoAereo;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Integer getForcaGeral() {
		return forcaGeral;
	}

	public void setForcaGeral(Integer forcaGeral) {
		this.forcaGeral = forcaGeral;
	}

	public static List<JogadorDTO> convertToDTO(List<Jogador> jogadores) {
		return jogadores.stream().map(s -> convertToDTO(s)).collect(Collectors.toList());
	}
	
	public static JogadorDTO convertToDTO(Jogador jogador){
		JogadorDTO dto = new JogadorDTO();
		dto.setNome(jogador.getNome());
		dto.setClubeNome(jogador.getClube().getNome());
		dto.setPosicao(jogador.getPosicao().name());
		dto.setNumero(jogador.getNumero());
		dto.setIdade(jogador.getIdade());
		dto.setForcaGeral(jogador.getForcaGeral());
		
		Map<Habilidade, HabilidadeValor> habs = jogador.getHabilidades().stream()
				.collect(Collectors.toMap(HabilidadeValor::getHabilidade, Function.identity()));

		dto.setValorPasse(habs.get(Habilidade.PASSE).getValor());
		dto.setValorFinalizacao(habs.get(Habilidade.FINALIZACAO).getValor());
		dto.setValorCruzamento(habs.get(Habilidade.CRUZAMENTO).getValor());
		dto.setValorArmacao(habs.get(Habilidade.ARMACAO).getValor());
		
		dto.setValorCabeceio(habs.get(Habilidade.CABECEIO).getValor());
		
		dto.setValorMarcacao(habs.get(Habilidade.MARCACAO).getValor());
		dto.setValorDesarme(habs.get(Habilidade.DESARME).getValor());
		dto.setValorInterceptacao(habs.get(Habilidade.INTERCEPTACAO).getValor());
		
		dto.setValorVelocidade(habs.get(Habilidade.VELOCIDADE).getValor());
		dto.setValorDrible(habs.get(Habilidade.DRIBLE).getValor());
		dto.setValorForca(habs.get(Habilidade.FORCA).getValor());
		
		dto.setValorPosicionamento(habs.get(Habilidade.POSICIONAMENTO).getValor());
		dto.setValorDominio(habs.get(Habilidade.DOMINIO).getValor());
		
		dto.setValorReflexo(habs.get(Habilidade.REFLEXO).getValor());
		dto.setValorJogoAereo(habs.get(Habilidade.JOGO_AEREO).getValor());

		return dto;
	}
}
