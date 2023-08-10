package com.fastfoot.player.model.entity;

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
import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.HabilidadeGrupoAcao;
import com.fastfoot.service.util.ElementoRoleta;
import com.fastfoot.service.util.ValidatorUtil;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class HabilidadeGrupoValor implements ElementoRoleta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeGrupoValorSequence")
	@SequenceGenerator(name = "habilidadeGrupoValorSequence", sequenceName = "habilidade_grupo_valor_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_jogador")
	private Jogador jogador;
	
	private HabilidadeGrupo habilidadeGrupo;

	private Integer valor;
	
	private Double valorTotal;

	@Transient
	private Integer valorN;

	@Transient
	private HabilidadeGrupoValorEstatistica habilidadeGrupoValorEstatistica;

	public HabilidadeGrupoValor() {

	}

	public HabilidadeGrupoValor(HabilidadeGrupo habilidadeGrupo, Integer valor) {
		this.habilidadeGrupo = habilidadeGrupo;
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

	public HabilidadeGrupo getHabilidadeGrupo() {
		return habilidadeGrupo;
	}

	public void setHabilidadeGrupo(HabilidadeGrupo habilidadeGrupo) {
		this.habilidadeGrupo = habilidadeGrupo;
	}

	@Override
	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public Integer getValorN() {
		if (valorN == null) {
			calcularValorN();
		}
		return valorN;
	}

	@Override
	public Double getValorAsDouble() {
		return valorTotal;
	}

	@Override
	public Double getValorNAsDouble() {
		if (valorN == null) {
			calcularValorN();
		}
		return new Double(valorN);
	}

	public HabilidadeGrupoAcao getHabilidadeGrupoAcao() {
		return HabilidadeGrupoAcao.HABILIDADE_GRUPO_ACAO.get(getHabilidadeGrupo());
	}
	
	public void calcularValorN() {

		if (ValidatorUtil.isEmpty(getJogador())) {
			valorN = (int) Math.pow(valor, Constantes.ROLETA_N_POWER);
		} else {
			double energia = (getJogador().getJogadorEnergia().getEnergiaAtual() / 100.0);
			if (energia > 1.0) throw new RuntimeException("Erro: energia jogador maior que 100%. [idJogador:" + getJogador() + "]");
			valorN = Math.max((int) (Math.pow(valor, Constantes.ROLETA_N_POWER) * energia), 1);
		}
	}

	public HabilidadeGrupoValorEstatistica getHabilidadeGrupoValorEstatistica() {
		return habilidadeGrupoValorEstatistica;
	}

	public void setHabilidadeGrupoValorEstatistica(HabilidadeGrupoValorEstatistica habilidadeGrupoValorEstatistica) {
		this.habilidadeGrupoValorEstatistica = habilidadeGrupoValorEstatistica;
	}

	@Override
	public String toString() {
		return "HabilidadeGrupoValor [grupo=" + habilidadeGrupo + ", valor=" + valor + "]";
	}

}
