package com.fastfoot.transfer.model;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

/*@Entity
@Table(indexes = { @Index(columnList = "id_clube") })*/
public class AdequacaoJogadorDTO {
	
	/*@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adequacaoJogadorSequence")
	@SequenceGenerator(name = "adequacaoJogadorSequence", sequenceName = "adequacao_jogador_seq")
	private Long id;*/

	/*@ManyToOne
	@JoinColumn(name = "id_clube")*/
	private Clube clube;
	
	/*@ManyToOne
	@JoinColumn(name = "id_jogador")*/
	private Jogador jogador;
	
	private NivelAdequacao nivelAdequacao;
	
	/*@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;*/
	
	public AdequacaoJogadorDTO() {

	}
	
	public AdequacaoJogadorDTO(Clube clube, Jogador jogador, NivelAdequacao nivelAdequacao) {
		this.clube = clube;
		this.jogador = jogador;
		this.nivelAdequacao = nivelAdequacao;
	}

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public NivelAdequacao getNivelAdequacao() {
		return nivelAdequacao;
	}

	public void setNivelAdequacao(NivelAdequacao nivelAdequacao) {
		this.nivelAdequacao = nivelAdequacao;
	}

}
