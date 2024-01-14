package com.fastfoot.financial.model.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceiraAttributeConverter;
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
public class DemonstrativoFinanceiroTemporada {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demonstrativoFinanceiroTemporadaSequence")
	@SequenceGenerator(name = "demonstrativoFinanceiroTemporadaSequence", sequenceName = "demonstrativo_financeiro_temporada_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@Convert(converter = TipoMovimentacaoFinanceiraAttributeConverter.class)
	private TipoMovimentacaoFinanceira tipoMovimentacao;
	
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

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public TipoMovimentacaoFinanceira getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(TipoMovimentacaoFinanceira tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Double getValorMovimentacao() {
		return valorMovimentacao;
	}

	public void setValorMovimentacao(Double valorMovimentacao) {
		this.valorMovimentacao = valorMovimentacao;
	}

}
