package com.fastfoot.financial.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceiraSaida;
import com.fastfoot.scheduler.model.entity.Semana;

@Entity
public class MovimentacaoFinanceiraSaida {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacaoFinanceiraSaidaSequence")
	@SequenceGenerator(name = "movimentacaoFinanceiraSaidaSequence", sequenceName = "movimentacao_financeira_saida_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	/*@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;*/
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	private TipoMovimentacaoFinanceiraSaida tipoMovimentacao;
	
	private Double valorMovimentacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	/*public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}*/

	public TipoMovimentacaoFinanceiraSaida getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(TipoMovimentacaoFinanceiraSaida tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public Double getValorMovimentacao() {
		return valorMovimentacao;
	}

	public void setValorMovimentacao(Double valorMovimentacao) {
		this.valorMovimentacao = valorMovimentacao;
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
		MovimentacaoFinanceiraSaida other = (MovimentacaoFinanceiraSaida) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "MovimentacaoFinanceiraSaida [clube=" + clube + ", semana=" + semana + ", tipoMovimentacao="
				+ tipoMovimentacao + ", valorMovimentacao=" + valorMovimentacao + "]";
	}

}
