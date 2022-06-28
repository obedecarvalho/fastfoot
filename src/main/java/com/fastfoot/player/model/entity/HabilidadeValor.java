package com.fastfoot.player.model.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class HabilidadeValor implements ElementoRoleta {
	//https://vladmihalcea.com/manytoone-jpa-hibernate/

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeValorSequence")
	@SequenceGenerator(name = "habilidadeValorSequence", sequenceName = "habilidade_valor_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	private Habilidade habilidade;

	private Integer valor;
	
	private Double valorDecimal;
	
	private Double potencialDesenvolvimento;
	
	private Double potencialDesenvolvimentoEfetivo;

	//private Integer quantidadeUso;
	
	//private Integer quantidadeUsoVencedor;
	
	private Boolean habilidadeEspecifica = false;
	
	private Double passoDesenvolvimento;

	@Transient
	private Integer valorN;

	@Transient
	private HabilidadeValorEstatistica habilidadeValorEstatistica;
	
	public HabilidadeValor() {

	}
	
	public HabilidadeValor(Habilidade habilidade, Integer valor) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
	}

	/*public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		//this.quantidadeUso = 0;
		//this.quantidadeUsoVencedor = 0;
	}*/
	
	/*public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento,
			Boolean especifica) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		// this.quantidadeUso = 0;
		// this.quantidadeUsoVencedor = 0;
		this.habilidadeEspecifica = especifica;
	}*/

	/*public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento,
			Boolean especifica, Double passoDesenvolvimentoAno) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		// this.quantidadeUso = 0;
		// this.quantidadeUsoVencedor = 0;
		this.habilidadeEspecifica = especifica;
		this.passoDesenvolvimento = passoDesenvolvimentoAno;
	}*/

	/*public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento,
			Boolean especifica, Double passoDesenvolvimento, Double valorDecimal, Double potencialDesenvolvimentoEfetivo) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		// this.quantidadeUso = 0;
		// this.quantidadeUsoVencedor = 0;
		this.habilidadeEspecifica = especifica;
		this.passoDesenvolvimento = passoDesenvolvimento;
		this.valorDecimal = valorDecimal;
		this.potencialDesenvolvimentoEfetivo = potencialDesenvolvimentoEfetivo;
	}*/
	
	public HabilidadeValor(Jogador jogador, Habilidade habilidade, Integer valor, Double valorDecimal,
			Boolean especifica, Double potencialDesenvolvimento, Double potencialDesenvolvimentoEfetivo,
			Double passoDesenvolvimento) {
		super();
		this.jogador = jogador;
		this.habilidade = habilidade;
		this.valor = valor;
		this.valorDecimal = valorDecimal;
		this.habilidadeEspecifica = especifica;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		this.potencialDesenvolvimentoEfetivo = potencialDesenvolvimentoEfetivo;
		this.passoDesenvolvimento = passoDesenvolvimento;
		// this.quantidadeUso = 0;
		// this.quantidadeUsoVencedor = 0;	
	}

	public HabilidadeAcao getHabilidadeAcao() {
		return HabilidadeAcao.HABILIDADE_ACAO.get(getHabilidade());
	}

	@Override
	public Integer getValor() {
		return valor;
	}

	@Override
	public Integer getValorN() {
		if (valorN == null) {
			valorN = (int) Math.pow(valor, Constantes.ROLETA_N_POWER);
		}
		return valorN;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	public Double getPotencialDesenvolvimento() {
		return potencialDesenvolvimento;
	}

	public void setPotencialDesenvolvimento(Double potencialDesenvolvimento) {
		this.potencialDesenvolvimento = potencialDesenvolvimento;
	}
	
	/*public Integer getQuantidadeUso() {
		return quantidadeUso;
	}

	public void setQuantidadeUso(Integer quantidadeUso) {
		this.quantidadeUso = quantidadeUso;
	}

	public void incrementarQuantidadeUso() {
		this.quantidadeUso++;
	}*/

	/*public Integer getQuantidadeUsoVencedor() {
		return quantidadeUsoVencedor;
	}

	public void setQuantidadeUsoVencedor(Integer quantidadeUsoVencedor) {
		this.quantidadeUsoVencedor = quantidadeUsoVencedor;
	}

	public void incrementarQuantidadeUsoVencedor() {
		this.quantidadeUsoVencedor++;
	}*/

	public HabilidadeValorEstatistica getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}

	public void setHabilidadeValorEstatistica(HabilidadeValorEstatistica habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica = habilidadeValorEstatistica;
	}

	public Boolean getHabilidadeEspecifica() {
		return habilidadeEspecifica;
	}

	public void setHabilidadeEspecifica(Boolean habilidadeEspecifica) {
		this.habilidadeEspecifica = habilidadeEspecifica;
	}

	public Double getPassoDesenvolvimento() {
		return passoDesenvolvimento;
	}

	public void setPassoDesenvolvimento(Double passoDesenvolvimento) {
		this.passoDesenvolvimento = passoDesenvolvimento;
	}

	public Double getValorDecimal() {
		return valorDecimal;
	}

	public void setValorDecimal(Double valorDecimal) {
		this.valorDecimal = valorDecimal;
	}

	public Double getValorTotal() {
		return getValor() + getValorDecimal();
	}

	public Double getPotencialDesenvolvimentoEfetivo() {
		return potencialDesenvolvimentoEfetivo;
	}

	public void setPotencialDesenvolvimentoEfetivo(Double potencialDesenvolvimentoEfetivo) {
		this.potencialDesenvolvimentoEfetivo = potencialDesenvolvimentoEfetivo;
	}

	@Override
	public String toString() {
		return "HabilidadeValor [hab=" + getHabilidade().name() + ", valor=" + valor + "]";
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
		HabilidadeValor other = (HabilidadeValor) obj;
		return Objects.equals(id, other.id);
	}

}
