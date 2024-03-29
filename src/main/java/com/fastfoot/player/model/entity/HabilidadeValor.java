package com.fastfoot.player.model.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fastfoot.model.Constantes;
import com.fastfoot.player.model.Habilidade;
import com.fastfoot.player.model.HabilidadeAcao;
import com.fastfoot.player.model.HabilidadeAcaoJogavel;
import com.fastfoot.player.model.HabilidadeJogavel;
import com.fastfoot.player.model.HabilidadeTipo;
import com.fastfoot.player.model.HabilidadeValorJogavel;
import com.fastfoot.player.model.HabilidadeValorJogavelEstatistica;
import com.fastfoot.service.util.ValidatorUtil;

@Entity
@Table(indexes = { @Index(columnList = "id_jogador") })
public class HabilidadeValor implements HabilidadeValorJogavel {

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
	
	//private Double potencialDesenvolvimentoEfetivo;

	private HabilidadeTipo habilidadeTipo;
	
	private Double passoDesenvolvimento;

	/*
	 * TODO:
	 * 	* Usar em PartidaService similar a Habilidade.FORA
	 * 	* A probabilidade vai ser definida semelhante a JogadorFactoryImplDesenVariavel
	 * 
	 */
	private Double probabilidadeErro;

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

	public HabilidadeValor(Jogador jogador, Habilidade habilidade, Integer valor, Double valorDecimal,
			HabilidadeTipo habilidadeTipo, Double potencialDesenvolvimento, Double passoDesenvolvimento) {
		super();
		this.jogador = jogador;
		this.habilidade = habilidade;
		this.valor = valor;
		this.valorDecimal = valorDecimal;
		this.habilidadeTipo = habilidadeTipo;
		this.potencialDesenvolvimento = potencialDesenvolvimento;
		//this.potencialDesenvolvimentoEfetivo = potencialDesenvolvimentoEfetivo;
		this.passoDesenvolvimento = passoDesenvolvimento;	
	}

	public HabilidadeAcao getHabilidadeAcao() {
		return HabilidadeAcao.HABILIDADE_ACAO.get(getHabilidade());
	}
	
	@Override
	public HabilidadeAcaoJogavel getHabilidadeAcaoJogavel() {
		return getHabilidadeAcao();
	}

	@Override
	public Integer getValor() {
		return valor;
	}

	@Override
	public Integer getValorN() {
		if (valorN == null) {
			//valorN = (int) Math.pow(valor, Constantes.ROLETA_N_POWER);
			calcularValorN();
		}
		return valorN;
	}
	
	@Override
	public void calcularValorN() {

		/*int energia = Constantes.ENERGIA_MINIMA_JOGAR;
		
		if (!ValidatorUtil.isEmpty(getJogador()) && !ValidatorUtil.isEmpty(getJogador().getJogadorDetalhe())
				&& !ValidatorUtil.isEmpty(getJogador().getJogadorDetalhe().getJogadorEnergia())
				&& !ValidatorUtil.isEmpty(getJogador().getJogadorDetalhe().getJogadorEnergia().getEnergiaAtual())) {
			energia = getJogador().getJogadorDetalhe().getJogadorEnergia().getEnergiaAtual();
		}*/

		if (ValidatorUtil.isEmpty(getJogador())) {
			valorN = (int) Math.pow(valor, Constantes.ROLETA_N_POWER);
		} else {
			double energia = (getJogador().getJogadorEnergia().getEnergiaAtual() / 100.0);
			if (energia > 1.0) throw new RuntimeException("Erro: energia jogador maior que 100%. [idJogador:" + getJogador() + "]");
			valorN = Math.max((int) (Math.pow(valor, Constantes.ROLETA_N_POWER) * energia), 1);
		}
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

	@Override
	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Habilidade getHabilidade() {
		return habilidade;
	}
	
	@Override
	public HabilidadeJogavel getHabilidadeJogavel() {
		return getHabilidade();
	}

	public void setHabilidade(Habilidade habilidade) {
		this.habilidade = habilidade;
	}

	@Override
	public Double getPotencialDesenvolvimento() {
		return potencialDesenvolvimento;
	}

	public void setPotencialDesenvolvimento(Double potencialDesenvolvimento) {
		this.potencialDesenvolvimento = potencialDesenvolvimento;
	}

	public HabilidadeValorEstatistica getHabilidadeValorEstatistica() {
		return habilidadeValorEstatistica;
	}
	
	@Override
	public HabilidadeValorJogavelEstatistica getHabilidadeValorJogavelEstatistica() {
		return getHabilidadeValorEstatistica();
	}

	public void setHabilidadeValorEstatistica(HabilidadeValorEstatistica habilidadeValorEstatistica) {
		this.habilidadeValorEstatistica = habilidadeValorEstatistica;
	}
	
	public Boolean isHabilidadeEspecifica() {
		return HabilidadeTipo.ESPECIFICA.equals(habilidadeTipo);
	}
	
	public Boolean isHabilidadeComum() {
		return HabilidadeTipo.COMUM.equals(habilidadeTipo);
	}
	
	public Boolean isHabilidadeOutro() {
		return HabilidadeTipo.OUTRO.equals(habilidadeTipo);
	}

	public HabilidadeTipo getHabilidadeTipo() {
		return habilidadeTipo;
	}

	public void setHabilidadeTipo(HabilidadeTipo habilidadeTipo) {
		this.habilidadeTipo = habilidadeTipo;
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
		//return getValor() + getValorDecimal();
		return getValorDecimal();
	}

	public Double getProbabilidadeErro() {
		return probabilidadeErro;
	}

	public void setProbabilidadeErro(Double probabilidadeErro) {
		this.probabilidadeErro = probabilidadeErro;
	}

	/*public Double getPotencialDesenvolvimentoEfetivo() {
		return potencialDesenvolvimentoEfetivo;
	}

	public void setPotencialDesenvolvimentoEfetivo(Double potencialDesenvolvimentoEfetivo) {
		this.potencialDesenvolvimentoEfetivo = potencialDesenvolvimentoEfetivo;
	}*/
	
	@Override
	public Double getValorAsDouble() {
		return Double.valueOf(getValor());
	}

	@Override
	public Double getValorNAsDouble() {
		return Double.valueOf(getValorN());
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
