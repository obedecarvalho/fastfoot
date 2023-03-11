package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoContinentalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.NivelCampeonatoAttributeConverter;
import com.fastfoot.scheduler.model.entity.Temporada;

@Entity
public class ClubeResumoTemporada {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeResumoTemporadaSequence")	
	@SequenceGenerator(name = "clubeResumoTemporadaSequence", sequenceName = "clube_resumo_temporada_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;

	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@Convert(converter = NivelCampeonatoAttributeConverter.class)
	private NivelCampeonato nivelCampeonato;
	
	private Integer jogos;
	
	private Integer vitorias;
	
	private Integer empates;
	
	private Integer golsPro;
	
	private Integer golsContra;
	
	//private Integer posicaoFinal;
	
	private ClassificacaoNacional classificacaoNacional;
	
	@Convert(converter = ClassificacaoCopaNacionalAttributeConverter.class)
	private ClassificacaoCopaNacional classificacaoCopaNacional;
	
	@Convert(converter = ClassificacaoContinentalAttributeConverter.class)
	private ClassificacaoContinental classificacaoContinental;
	
	public ClubeResumoTemporada() {
		this.jogos = 0;
		this.vitorias = 0;
		this.empates = 0;
		this.golsPro = 0;
		this.golsContra = 0;
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

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public NivelCampeonato getNivelCampeonato() {
		return nivelCampeonato;
	}

	public void setNivelCampeonato(NivelCampeonato nivelCampeonato) {
		this.nivelCampeonato = nivelCampeonato;
	}

	public Integer getJogos() {
		return jogos;
	}

	public void setJogos(Integer jogos) {
		this.jogos = jogos;
	}

	public Integer getVitorias() {
		return vitorias;
	}

	public void setVitorias(Integer vitorias) {
		this.vitorias = vitorias;
	}

	public Integer getEmpates() {
		return empates;
	}

	public void setEmpates(Integer empates) {
		this.empates = empates;
	}

	public Integer getGolsPro() {
		return golsPro;
	}

	public void setGolsPro(Integer golsPro) {
		this.golsPro = golsPro;
	}

	public Integer getGolsContra() {
		return golsContra;
	}

	public void setGolsContra(Integer golsContra) {
		this.golsContra = golsContra;
	}

	/*public Integer getPosicaoFinal() {
		return posicaoFinal;
	}

	public void setPosicaoFinal(Integer posicaoFinal) {
		this.posicaoFinal = posicaoFinal;
	}*/

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
		ClubeResumoTemporada other = (ClubeResumoTemporada) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ClubeResumoTemporada [clube=" + clube.getNome() + ", nivelCampeonato=" + nivelCampeonato + ", jogos=" + jogos
				+ ", vitorias=" + vitorias + ", empates=" + empates + ", golsPro=" + golsPro + ", golsContra="
				+ golsContra + "]";
	}
}
