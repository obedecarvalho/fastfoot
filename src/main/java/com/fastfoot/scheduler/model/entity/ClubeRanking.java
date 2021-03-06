package com.fastfoot.scheduler.model.entity;

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
import com.fastfoot.scheduler.model.ClassificacaoContinentalFinal;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalFinal;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;

/**
 * Ranking do Clube ao fim do Campeonato Nacional
 * Ranking vai de 1 a 32 (maior ao menor)
 * 
 * @author obede
 *
 */
@Entity
@Table(indexes = { @Index(columnList = "id_temporada"), @Index(columnList = "ano") })
public class ClubeRanking {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeRankingSequence")	
	@SequenceGenerator(name = "clubeRankingSequence", sequenceName = "clube_ranking_seq", initialValue = 1000)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;

	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private Integer ano;//TODO: remover?? pode ser recuperado pela temporada
	
	private ClassificacaoNacionalFinal classificacaoNacional;
	
	private ClassificacaoCopaNacionalFinal classificacaoCopaNacional;
	
	private ClassificacaoContinentalFinal classificacaoContinental;

	private Integer posicaoGeral;

	public ClubeRanking() {
		super();
	}

	public ClubeRanking(Integer id, Clube clube, Integer ano, Integer posicaoGeral,
			ClassificacaoNacionalFinal classificacaoNacional, ClassificacaoCopaNacionalFinal classificacaoCopaNacional,
			ClassificacaoContinentalFinal classificacaoContinental) {
		super();
		this.id = id;
		this.clube = clube;
		this.ano = ano;
		this.posicaoGeral = posicaoGeral;
		this.classificacaoNacional = classificacaoNacional;
		this.classificacaoContinental = classificacaoContinental;
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public ClassificacaoNacionalFinal getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacionalFinal classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoCopaNacionalFinal getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacionalFinal classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public ClassificacaoContinentalFinal getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinentalFinal classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public Integer getPosicaoGeral() {
		return posicaoGeral;
	}

	public void setPosicaoGeral(Integer posicaoGeral) {
		this.posicaoGeral = posicaoGeral;
	}

	public boolean isCampeaoContinental() {
		return ClassificacaoContinentalFinal.C_CAMPEAO.equals(classificacaoContinental);
	}

	public boolean isCampeaoContinentalII() {
		return ClassificacaoContinentalFinal.CII_CAMPEAO.equals(classificacaoContinental);
	}
	
	public boolean isCampeaoContinentalIII() {
		return ClassificacaoContinentalFinal.CIII_CAMPEAO.equals(classificacaoContinental);
	}

	public boolean isCampeaoCopaNacional() {
		return ClassificacaoCopaNacionalFinal.CN_CAMPEAO.equals(classificacaoCopaNacional);
	}

	public boolean isCampeaoCopaNacionalII() {
		return ClassificacaoCopaNacionalFinal.CNII_CAMPEAO.equals(classificacaoCopaNacional);
	}

	public boolean isCampeaoNacional() {
		return ClassificacaoNacionalFinal.N_1.equals(classificacaoNacional);
	}

	public boolean isCampeaoNacionalII() {
		return ClassificacaoNacionalFinal.NII_1.equals(classificacaoNacional);
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	@Override
	public String toString() {
		return "ClubeRanking [" + clube.getNome() + ", pos=" + posicaoGeral + (getTemporada() != null ? (", ano=" + ano) : "") + ", rkNac="
				+ (classificacaoNacional != null ? classificacaoNacional.name() : "") + ", rkCN="
				+ (classificacaoCopaNacional != null ? classificacaoCopaNacional.name() : "") + ", rkCont="
				+ (classificacaoContinental != null ? classificacaoContinental.name() : "") + "]";
	}
	
}
