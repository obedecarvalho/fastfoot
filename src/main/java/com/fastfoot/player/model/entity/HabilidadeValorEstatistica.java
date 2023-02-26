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

import com.fastfoot.scheduler.model.entity.Semana;

@Entity
@Table(indexes = { @Index(columnList = "id_semana, id_habilidade_valor") })
public class HabilidadeValorEstatistica {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidadeValorEstatisticaSequence")
	@SequenceGenerator(name = "habilidadeValorEstatisticaSequence", sequenceName = "habilidade_valor_estatistica_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_habilidade_valor")
	private HabilidadeValor habilidadeValor;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;

	private Integer quantidadeUso;
	
	private Integer quantidadeUsoVencedor;
	
	private Boolean amistoso;
	
	/*@ManyToOne
	@JoinColumn(name = "id_partida_resultado")
	private PartidaResultado partidaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_amistosa_resultado")
	private PartidaAmistosaResultado partidaAmistosaResultado;
	
	@ManyToOne
	@JoinColumn(name = "id_partida_eliminatoria_resultado")
	private PartidaEliminatoriaResultado partidaEliminatoriaResultado;*/
	
	public HabilidadeValorEstatistica() {
		quantidadeUso = 0;
		quantidadeUsoVencedor = 0;
	}

	public HabilidadeValorEstatistica(HabilidadeValor habilidadeValor) {
		this.quantidadeUso = 0;
		this.quantidadeUsoVencedor = 0;
		this.habilidadeValor = habilidadeValor;
	}
	
	public HabilidadeValorEstatistica(HabilidadeValor habilidadeValor, Semana semana, Boolean amistoso/*, PartidaResultadoJogavel partidaResultado*/) {
		quantidadeUso = 0;
		quantidadeUsoVencedor = 0;
		this.habilidadeValor = habilidadeValor;
		this.semana = semana;
		this.amistoso = amistoso;
		
		/*if (partidaResultado instanceof PartidaResultado) {
			setPartidaResultado((PartidaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaAmistosaResultado) {
			setPartidaAmistosaResultado((PartidaAmistosaResultado) partidaResultado);
		} else if (partidaResultado instanceof PartidaEliminatoriaResultado) {
			setPartidaEliminatoriaResultado((PartidaEliminatoriaResultado) partidaResultado);
		}*/
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HabilidadeValor getHabilidadeValor() {
		return habilidadeValor;
	}

	public void setHabilidadeValor(HabilidadeValor habilidadeValor) {
		this.habilidadeValor = habilidadeValor;
	}

	public Integer getQuantidadeUso() {
		return quantidadeUso;
	}

	public void setQuantidadeUso(Integer quantidadeUso) {
		this.quantidadeUso = quantidadeUso;
	}

	public Integer getQuantidadeUsoVencedor() {
		return quantidadeUsoVencedor;
	}

	public void setQuantidadeUsoVencedor(Integer quantidadeUsoVencedor) {
		this.quantidadeUsoVencedor = quantidadeUsoVencedor;
	}
	
	public void incrementarQuantidadeUso() {
		this.quantidadeUso++;
	}

	public void incrementarQuantidadeUsoVencedor() {
		this.quantidadeUsoVencedor++;
	}

	/*public PartidaResultado getPartidaResultado() {
		return partidaResultado;
	}

	public void setPartidaResultado(PartidaResultado partidaResultado) {
		this.partidaResultado = partidaResultado;
	}

	public PartidaAmistosaResultado getPartidaAmistosaResultado() {
		return partidaAmistosaResultado;
	}

	public void setPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado) {
		this.partidaAmistosaResultado = partidaAmistosaResultado;
	}

	public PartidaEliminatoriaResultado getPartidaEliminatoriaResultado() {
		return partidaEliminatoriaResultado;
	}

	public void setPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado) {
		this.partidaEliminatoriaResultado = partidaEliminatoriaResultado;
	}*/

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public Boolean getAmistoso() {
		return amistoso;
	}

	public void setAmistoso(Boolean amistoso) {
		this.amistoso = amistoso;
	}

	@Override
	public String toString() {
		return "HabilidadeValorEstatistica [habilidadeValor=" + habilidadeValor + ", qtdeUso=" + quantidadeUso
				+ ", qtdeUsoVencedor=" + quantidadeUsoVencedor + "]";
	}

}
