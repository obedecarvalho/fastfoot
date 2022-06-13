package com.fastfoot.probability.model.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;

@Entity
public class ClubeProbabilidade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clubeProbabilidadeSequence")	
	@SequenceGenerator(name = "clubeProbabilidadeSequence", sequenceName = "clube_probabilidade_seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_campeonato")
	private Campeonato campeonato;
	
	@ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;
	
	@ManyToOne
	@JoinColumn(name = "id_clube")
	private Clube clube;
	
	/*@OneToMany(mappedBy = "clubeProbabilidade", fetch = FetchType.LAZY)
	private List<ClubeProbabilidadePosicao> probabilidadePosicao;*/

	private Double probabilidadeCampeao;
	
	private Double probabilidadeRebaixamento;
	
	private Double probabilidadeAcesso;

	private Double probabilidadeClassificacaoContinental;
	
	private Double probabilidadeClassificacaoCopaNacional;
	
	private Double probabilidadeClassificacaoCI;
	
	private Double probabilidadeClassificacaoCII;
	
	private Double probabilidadeClassificacaoCIII;
	
	private Double probabilidadeClassificacaoCNI;
	
	private Double probabilidadeClassificacaoCNII;

	@Transient
	private Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao;//Key: posicao
	
	@Transient
	private Map<Integer, ClubeRankingPosicaoProbabilidade> clubeProbabilidadePosicaoGeral;

	public ClubeProbabilidade() {
		probabilidadeCampeao = 0d;
		probabilidadeRebaixamento = 0d;
		probabilidadeAcesso = 0d;
		probabilidadeClassificacaoContinental = 0d;
		probabilidadeClassificacaoCopaNacional = 0d;
	}
	
	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Integer, ClubeProbabilidadePosicao> getClubeProbabilidadePosicao() {
		return clubeProbabilidadePosicao;
	}

	public void setClubeProbabilidadePosicao(Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao) {
		this.clubeProbabilidadePosicao = clubeProbabilidadePosicao;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Semana getSemana() {
		return semana;
	}

	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	/*public List<ClubeProbabilidadePosicao> getProbabilidadePosicao() {
		return probabilidadePosicao;
	}

	public void setProbabilidadePosicao(List<ClubeProbabilidadePosicao> probabilidadePosicao) {
		this.probabilidadePosicao = probabilidadePosicao;
	}*/
	
	public String getProbabilidadeCampeaoDescricao() {
		return getDescricaoProcentagem(probabilidadeCampeao);
	}

	public Double getProbabilidadeCampeao() {
		return probabilidadeCampeao;
	}

	public void setProbabilidadeCampeao(Double probabilidadeCampeao) {
		this.probabilidadeCampeao = probabilidadeCampeao;
	}

	public String getProbabilidadeRebaixamentoDescricao() {
		return getDescricaoProcentagem(probabilidadeRebaixamento);
	}
	
	public Double getProbabilidadeRebaixamento() {
		return probabilidadeRebaixamento;
	}

	public void setProbabilidadeRebaixamento(Double probabilidadeRebaixamento) {
		this.probabilidadeRebaixamento = probabilidadeRebaixamento;
	}

	public String getProbabilidadeAcessoDescricao() {
		return getDescricaoProcentagem(probabilidadeAcesso);
	}

	public Double getProbabilidadeAcesso() {
		return probabilidadeAcesso;
	}

	public void setProbabilidadeAcesso(Double probabilidadeAcesso) {
		this.probabilidadeAcesso = probabilidadeAcesso;
	}
	
	public String getProbabilidadeClassificacaoContinentalDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoContinental);
	}

	public Double getProbabilidadeClassificacaoContinental() {
		return probabilidadeClassificacaoContinental;
	}

	public void setProbabilidadeClassificacaoContinental(Double probabilidadeClassificacaoContinental) {
		this.probabilidadeClassificacaoContinental = probabilidadeClassificacaoContinental;
	}
	
	public String getProbabilidadeClassificacaoCopaNacionalDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoCopaNacional);
	}

	public Double getProbabilidadeClassificacaoCopaNacional() {
		return probabilidadeClassificacaoCopaNacional;
	}

	public void setProbabilidadeClassificacaoCopaNacional(Double probabilidadeClassificacaoCopaNacional) {
		this.probabilidadeClassificacaoCopaNacional = probabilidadeClassificacaoCopaNacional;
	}

	public Double getProbabilidadeClassificacaoCI() {
		return probabilidadeClassificacaoCI;
	}

	public void setProbabilidadeClassificacaoCI(Double probabilidadeClassificacaoCI) {
		this.probabilidadeClassificacaoCI = probabilidadeClassificacaoCI;
	}

	public Double getProbabilidadeClassificacaoCII() {
		return probabilidadeClassificacaoCII;
	}

	public void setProbabilidadeClassificacaoCII(Double probabilidadeClassificacaoCII) {
		this.probabilidadeClassificacaoCII = probabilidadeClassificacaoCII;
	}

	public Double getProbabilidadeClassificacaoCIII() {
		return probabilidadeClassificacaoCIII;
	}

	public void setProbabilidadeClassificacaoCIII(Double probabilidadeClassificacaoCIII) {
		this.probabilidadeClassificacaoCIII = probabilidadeClassificacaoCIII;
	}

	private String getDescricaoProcentagem(Double d) {
		if (d > 0 && d < 0.001d) {
			return "< 0,10%";
		}
		
		return String.format("%.2f%s", d*100, "%");
	}

	public Map<Integer, ClubeRankingPosicaoProbabilidade> getClubeProbabilidadePosicaoGeral() {
		return clubeProbabilidadePosicaoGeral;
	}

	public void setClubeProbabilidadePosicaoGeral(
			Map<Integer, ClubeRankingPosicaoProbabilidade> clubeProbabilidadePosicaoContinental) {
		this.clubeProbabilidadePosicaoGeral = clubeProbabilidadePosicaoContinental;
	}

	public Double getProbabilidadeClassificacaoCNI() {
		return probabilidadeClassificacaoCNI;
	}

	public void setProbabilidadeClassificacaoCNI(Double probabilidadeClassificacaoCNI) {
		this.probabilidadeClassificacaoCNI = probabilidadeClassificacaoCNI;
	}

	public Double getProbabilidadeClassificacaoCNII() {
		return probabilidadeClassificacaoCNII;
	}

	public void setProbabilidadeClassificacaoCNII(Double probabilidadeClassificacaoCNII) {
		this.probabilidadeClassificacaoCNII = probabilidadeClassificacaoCNII;
	}

	@Override
	public String toString() {
		return "ClubeProbabilidade [clube=" + clube.getNome() + ", pCampeao=" + probabilidadeCampeao
				+ ", pAcesso=" + probabilidadeAcesso + ", pRebaixamento="
				+ probabilidadeRebaixamento + ", pClassificacaoCI=" + probabilidadeClassificacaoCI
				+ ", pClassificacaoCII=" + probabilidadeClassificacaoCII
				+ ", pClassificacaoCIII=" + probabilidadeClassificacaoCIII 
				+ ", pClassificacaoCNI=" + probabilidadeClassificacaoCNI
				+ ", pClassificacaoCNII=" + probabilidadeClassificacaoCNII
				+ "]";
	}
}
