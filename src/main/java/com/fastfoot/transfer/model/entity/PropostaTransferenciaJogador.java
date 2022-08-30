package com.fastfoot.transfer.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador"), @Index(columnList = "id_clube_origem"), @Index(columnList = "id_clube_destino") })
public class PropostaTransferenciaJogador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transferenciaJogadorSequence")
	@SequenceGenerator(name = "transferenciaJogadorSequence", sequenceName = "transferencia_jogador_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube_origem")
	private Clube clubeOrigem;
	
	@ManyToOne
	@JoinColumn(name = "id_clube_destino")
	private Clube clubeDestino;
	
	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@ManyToOne
	@JoinColumn(name = "id_semana_transferencia")
	private Semana semanaTransferencia;
	
	/*@ManyToOne
	@JoinColumn(name = "id_adequacao_elenco_posicao")
	private AdequacaoElencoPosicao adequacaoElencoPosicao;*/
	
	@ManyToOne
	@JoinColumn(name = "id_necessidade_contratacao_clube")
	private NecessidadeContratacaoClube necessidadeContratacaoClube;

	private Double valorTransferencia;
	
	private Boolean propostaAceita;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clube getClubeOrigem() {
		return clubeOrigem;
	}

	public void setClubeOrigem(Clube clubeOrigem) {
		this.clubeOrigem = clubeOrigem;
	}

	public Clube getClubeDestino() {
		return clubeDestino;
	}

	public void setClubeDestino(Clube clubeDestino) {
		this.clubeDestino = clubeDestino;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Semana getSemanaTransferencia() {
		return semanaTransferencia;
	}

	public void setSemanaTransferencia(Semana semanaTransferencia) {
		this.semanaTransferencia = semanaTransferencia;
	}

	public Double getValorTransferencia() {
		return valorTransferencia;
	}

	public void setValorTransferencia(Double valorTransferencia) {
		this.valorTransferencia = valorTransferencia;
	}

	public Boolean getPropostaAceita() {
		return propostaAceita;
	}

	public void setPropostaAceita(Boolean propostaAceita) {
		this.propostaAceita = propostaAceita;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public NecessidadeContratacaoClube getNecessidadeContratacaoClube() {
		return necessidadeContratacaoClube;
	}

	public void setNecessidadeContratacaoClube(NecessidadeContratacaoClube necessidadeContratacaoClube) {
		this.necessidadeContratacaoClube = necessidadeContratacaoClube;
	}

	/*public AdequacaoElencoPosicao getAdequacaoElencoPosicao() {
		return adequacaoElencoPosicao;
	}

	public void setAdequacaoElencoPosicao(AdequacaoElencoPosicao adequacaoElencoPosicao) {
		this.adequacaoElencoPosicao = adequacaoElencoPosicao;
	}*/

}
