package com.fastfoot.financial.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.service.util.FormatadorUtil;

@Entity
public class ClubeSaldoSemana {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacaoFinanceiraSequence")
	@SequenceGenerator(name = "movimentacaoFinanceiraSequence", sequenceName = "movimentacao_financeira_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	private Double saldo;

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

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "ClubeSaldoSemana [clube=" + clube + ", semana=" + semana + ", saldo=" + FormatadorUtil.formatarDecimal(saldo) + "]";
	}

}
