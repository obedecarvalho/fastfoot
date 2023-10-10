package com.fastfoot.club.model.entity;

import java.util.Objects;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.fastfoot.scheduler.model.ClassificacaoContinental;
import com.fastfoot.scheduler.model.ClassificacaoContinentalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacional;
import com.fastfoot.scheduler.model.ClassificacaoCopaNacionalAttributeConverter;
import com.fastfoot.scheduler.model.ClassificacaoNacional;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.NivelCampeonatoAttributeConverter;
import com.fastfoot.scheduler.model.entity.Temporada;
import com.fastfoot.service.util.ValidatorUtil;

@Entity
public class TreinadorResumoTemporada {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treinadorResumoTemporadaSequence")	
	@SequenceGenerator(name = "treinadorResumoTemporadaSequence", sequenceName = "treinador_resumo_temporada_seq")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_treinador")
	private Treinador treinador;

	@ManyToOne
	@JoinColumn(name = "id_temporada")
	private Temporada temporada;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
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
	
	public TreinadorResumoTemporada() {
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

	public Treinador getTreinador() {
		return treinador;
	}

	public void setTreinador(Treinador treinador) {
		this.treinador = treinador;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
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
	
	public void incrementarJogos() {
		this.jogos++;
	}

	public void incrementarVitorias() {
		this.vitorias++;
	}

	public void incrementarEmpates() {
		this.empates++;
	}

	public void incrementarGolsPro(int nroGols) {
		this.golsPro += nroGols;
	}

	public void incrementarGolsContra(int nroGols) {
		this.golsContra += nroGols;
	}
	
	//@Transient
	public String getClassificacaoDescricao() {
		if (!ValidatorUtil.isEmpty(classificacaoNacional)) {
			return classificacaoNacional.name();
		} else if (!ValidatorUtil.isEmpty(classificacaoCopaNacional)) {
			return classificacaoCopaNacional.name();
		} else if (!ValidatorUtil.isEmpty(classificacaoContinental)) {
			return classificacaoContinental.name();
		}
		return null;
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
		TreinadorResumoTemporada other = (TreinadorResumoTemporada) obj;
		return Objects.equals(id, other.id);
	}

}
