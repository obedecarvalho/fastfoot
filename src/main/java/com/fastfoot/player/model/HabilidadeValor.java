package com.fastfoot.player.model;

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
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.service.util.ElementoRoleta;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class HabilidadeValor implements ElementoRoleta {//TODO: mover para ./entity/
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
	
	private Integer potencialDesenvolvimento;

	private Integer quantidadeUso;//TODO: ??
	
	private Integer quantidadeUsoVencedor;//TODO: ??
	
	private Boolean habilidadeEspecifica = false;

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

	public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		this.quantidadeUso = 0;
		this.quantidadeUsoVencedor = 0;
	}
	
	public HabilidadeValor(Habilidade habilidade, Integer valor, Jogador jogador, Integer potencialDesenvolvimento, Boolean especifica) {
		super();
		this.habilidade = habilidade;
		this.valor = valor;
		this.jogador = jogador;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		this.quantidadeUso = 0;
		this.quantidadeUsoVencedor = 0;
		this.habilidadeEspecifica = especifica;
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

	public Integer getPotencialDesenvolvimento() {
		return potencialDesenvolvimento;
	}

	public void setPotencialDesenvolvimento(Integer potencialDesenvolvimento) {
		this.potencialDesenvolvimento = potencialDesenvolvimento;
	}
	
	public Integer getQuantidadeUso() {
		return quantidadeUso;
	}

	public void setQuantidadeUso(Integer quantidadeUso) {
		this.quantidadeUso = quantidadeUso;
	}

	public void incrementarQuantidadeUso() {
		this.quantidadeUso++;
	}

	public Integer getQuantidadeUsoVencedor() {
		return quantidadeUsoVencedor;
	}

	public void setQuantidadeUsoVencedor(Integer quantidadeUsoVencedor) {
		this.quantidadeUsoVencedor = quantidadeUsoVencedor;
	}

	public void incrementarQuantidadeUsoVencedor() {
		this.quantidadeUsoVencedor++;
	}

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
