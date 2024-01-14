package com.fastfoot.club.model.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoContinentalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.entity.Temporada;

/**
 * Ranking do Clube ao fim do Campeonato Nacional
 * Ranking vai de 1 a 32 (maior ao menor)
 * 
 * @author obede
 *
 */
@Entity
//@Table(indexes = { @Index(columnList = "id_temporada"), @Index(columnList = "ano") })
public class ClubeRanking {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeRankingSequence")	
	@SequenceGenerator(name = "clubeRankingSequence", sequenceName = "clube_ranking_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;

	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	private Integer ano;//TODO: remover?? pode ser recuperado pela temporada
	
	private ClassificacaoNacional classificacaoNacional;
	
	@Convert(converter = ClassificacaoCopaNacionalAttributeConverter.class)
	private ClassificacaoCopaNacional classificacaoCopaNacional;
	
	@Convert(converter = ClassificacaoContinentalAttributeConverter.class)
	private ClassificacaoContinental classificacaoContinental;

	private Integer posicaoGeral;
	
	private Integer posicaoInternacional;

	public ClubeRanking() {
		super();
	}

	/*public ClubeRanking(Long id, Clube clube, Integer ano, Integer posicaoGeral,
			ClassificacaoNacional classificacaoNacional, ClassificacaoCopaNacional classificacaoCopaNacional,
			ClassificacaoContinental classificacaoContinental) {
		super();
		this.id = id;
		this.clube = clube;
		this.ano = ano;
		this.posicaoGeral = posicaoGeral;
		this.classificacaoNacional = classificacaoNacional;
		this.classificacaoContinental = classificacaoContinental;
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}*/
	
	public ClubeRanking(Clube clube, Integer ano, Integer posicaoGeral,
			ClassificacaoNacional classificacaoNacional, ClassificacaoCopaNacional classificacaoCopaNacional,
			ClassificacaoContinental classificacaoContinental) {
		super();
		this.clube = clube;
		this.ano = ano;
		this.posicaoGeral = posicaoGeral;
		this.classificacaoNacional = classificacaoNacional;
		this.classificacaoContinental = classificacaoContinental;
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public ClassificacaoNacional getClassificacaoNacional() {
		return classificacaoNacional;
	}

	public void setClassificacaoNacional(ClassificacaoNacional classificacaoNacional) {
		this.classificacaoNacional = classificacaoNacional;
	}

	public ClassificacaoCopaNacional getClassificacaoCopaNacional() {
		return classificacaoCopaNacional;
	}

	public void setClassificacaoCopaNacional(ClassificacaoCopaNacional classificacaoCopaNacional) {
		this.classificacaoCopaNacional = classificacaoCopaNacional;
	}

	public ClassificacaoContinental getClassificacaoContinental() {
		return classificacaoContinental;
	}

	public void setClassificacaoContinental(ClassificacaoContinental classificacaoContinental) {
		this.classificacaoContinental = classificacaoContinental;
	}

	public Integer getPosicaoGeral() {
		return posicaoGeral;
	}

	public void setPosicaoGeral(Integer posicaoGeral) {
		this.posicaoGeral = posicaoGeral;
	}

	@JsonIgnore
	public boolean isCampeaoContinental() {
		return ClassificacaoContinental.C_CAMPEAO.equals(classificacaoContinental);
	}

	@JsonIgnore
	public boolean isCampeaoContinentalII() {
		return ClassificacaoContinental.CII_CAMPEAO.equals(classificacaoContinental);
	}
	
	@JsonIgnore
	public boolean isCampeaoContinentalIII() {
		return ClassificacaoContinental.CIII_CAMPEAO.equals(classificacaoContinental);
	}

	@JsonIgnore
	public boolean isCampeaoCopaNacional() {
		return ClassificacaoCopaNacional.CN_CAMPEAO.equals(classificacaoCopaNacional);
	}

	@JsonIgnore
	public boolean isCampeaoCopaNacionalII() {
		return ClassificacaoCopaNacional.CNII_CAMPEAO.equals(classificacaoCopaNacional);
	}

	@JsonIgnore
	public boolean isCampeaoNacional() {
		return ClassificacaoNacional.N_1.equals(classificacaoNacional);
	}

	@JsonIgnore
	public boolean isCampeaoNacionalII() {
		return ClassificacaoNacional.NII_1.equals(classificacaoNacional);
	}

	@JsonIgnore
	public boolean isCampeao() {
		return isCampeaoContinental() || isCampeaoContinentalII() || isCampeaoContinentalIII()
				|| isCampeaoCopaNacional() || isCampeaoCopaNacionalII() || isCampeaoNacional() || isCampeaoNacionalII();
	}

	@JsonIgnore
	public boolean isCampeaoAlgumNivelContinental() {
		return isCampeaoContinental() || isCampeaoContinentalII() || isCampeaoContinentalIII();
	}

	@JsonIgnore
	public boolean isCampeaoAlgumNivelCopaNacional() {
		return isCampeaoCopaNacional() || isCampeaoCopaNacionalII();
	}

	@JsonIgnore
	public boolean isCampeaoAlgumNivelCampeonatoNacional() {
		return isCampeaoNacional() || isCampeaoNacionalII();
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public Integer getPosicaoInternacional() {
		return posicaoInternacional;
	}

	public void setPosicaoInternacional(Integer posicaoGlobal) {
		this.posicaoInternacional = posicaoGlobal;
	}

	@Override
	public String toString() {
		return "ClubeRanking [" + clube.getNome() + ", pos=" + posicaoGeral + ", posInt=" + posicaoInternacional + (getTemporada() != null ? (", ano=" + ano) : "") + ", rkNac="
				+ (classificacaoNacional != null ? classificacaoNacional.name() : "") + ", rkCN="
				+ (classificacaoCopaNacional != null ? classificacaoCopaNacional.name() : "") + ", rkCont="
				+ (classificacaoContinental != null ? classificacaoContinental.name() : "") + "]";
	}
	
}
