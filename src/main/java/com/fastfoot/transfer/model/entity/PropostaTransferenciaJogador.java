package com.fastfoot.transfer.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
//@Table(indexes = { @Index(columnList = "id_jogador"), @Index(columnList = "id_clube_origem"), @Index(columnList = "id_clube_destino") })
public class PropostaTransferenciaJogador implements ElementoRoleta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propostaTransferenciaJogadorSequence")
	@SequenceGenerator(name = "propostaTransferenciaJogadorSequence", sequenceName = "proposta_transferencia_jogador_seq")
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
	
	@Transient
	private Integer peso;
	
	@Transient
	private Integer pesoN;

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

	@Override
	public Integer getValor() {
		if (peso == null) {
			peso = Constantes.PESO_DIFERENCA_JOGADOR_CLUBE_TRANSFERENCIA
					/ ((getClubeDestino().getForcaGeral() - getJogador().getForcaGeral()) + 1);
		}
		return peso;
	}

	@Override
	public Integer getValorN() {
		if (pesoN == null) {
			pesoN = new Double(Constantes.PESO_DIFERENCA_JOGADOR_CLUBE_TRANSFERENCIA
					/ (Math.pow((getClubeDestino().getForcaGeral() - getJogador().getForcaGeral()), 2) + 1)).intValue();
		}
		return pesoN;
	}
	
	@Override
	public Double getValorAsDouble() {
		return new Double(getValor());
	}

	@Override
	public Double getValorNAsDouble() {
		return new Double(getValorN());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropostaTransferenciaJogador other = (PropostaTransferenciaJogador) obj;
		return Objects.equals(id, other.id);
	}

	/*public AdequacaoElencoPosicao getAdequacaoElencoPosicao() {
		return adequacaoElencoPosicao;
	}

	public void setAdequacaoElencoPosicao(AdequacaoElencoPosicao adequacaoElencoPosicao) {
		this.adequacaoElencoPosicao = adequacaoElencoPosicao;
	}*/

}
