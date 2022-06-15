package com.fastfoot.probability.model.entity;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.ClubeProbabilidadePosicao;
import com.fastfoot.probability.model.ClubeRankingPosicaoProbabilidade;
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

	private Double probabilidadeCampeao;
	
	private Double probabilidadeRebaixamento;
	
	private Double probabilidadeAcesso;

	private Double probabilidadeClassificacaoCI;
	
	private Double probabilidadeClassificacaoCII;
	
	private Double probabilidadeClassificacaoCIII;
	
	//
	private Integer qtdeClassificacaoCIII;
	
	private Integer qtdeClassificacaoCII;
	
	private Integer qtdeClassificacaoCI;
	
	private Integer qtdeClassificacaoCNI;
	
	private Integer qtdeAcesso;
	
	private Integer qtdeRebaixamento;
	
	private Integer qtdeCampeao;
	//
	
	private Double probabilidadeClassificacaoCNI;

	@Transient
	private Map<Integer, ClubeProbabilidadePosicao> clubeProbabilidadePosicao;//Key: posicao
	
	@Transient
	private Map<Integer, ClubeRankingPosicaoProbabilidade> clubeProbabilidadePosicaoGeral;

	public ClubeProbabilidade() {
		probabilidadeCampeao = 0d;
		probabilidadeRebaixamento = 0d;
		probabilidadeAcesso = 0d;
		probabilidadeClassificacaoCI = 0d;
		probabilidadeClassificacaoCII = 0d;
		probabilidadeClassificacaoCIII = 0d;
		probabilidadeClassificacaoCNI = 0d;
		qtdeClassificacaoCIII = 0;
		qtdeClassificacaoCI = 0;
		qtdeClassificacaoCII = 0;
		qtdeClassificacaoCNI = 0;
		qtdeCampeao = 0;
		qtdeAcesso = 0;
		qtdeRebaixamento = 0;
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

	public String getProbabilidadeClassificacaoCIDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoCI);
	}
	
	public Double getProbabilidadeClassificacaoCI() {
		return probabilidadeClassificacaoCI;
	}

	public void setProbabilidadeClassificacaoCI(Double probabilidadeClassificacaoCI) {
		this.probabilidadeClassificacaoCI = probabilidadeClassificacaoCI;
	}

	public String getProbabilidadeClassificacaoCIIDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoCII);
	}
	
	public Double getProbabilidadeClassificacaoCII() {
		return probabilidadeClassificacaoCII;
	}

	public void setProbabilidadeClassificacaoCII(Double probabilidadeClassificacaoCII) {
		this.probabilidadeClassificacaoCII = probabilidadeClassificacaoCII;
	}

	public String getProbabilidadeClassificacaoCIIIDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoCIII);
	}
	
	public Double getProbabilidadeClassificacaoCIII() {
		return probabilidadeClassificacaoCIII;
	}

	public void setProbabilidadeClassificacaoCIII(Double probabilidadeClassificacaoCIII) {
		this.probabilidadeClassificacaoCIII = probabilidadeClassificacaoCIII;
	}

	private String getDescricaoProcentagem(Double d) {
		if (d > 0 && d < 0.0001d) {
			return "< 0,01%";
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
	
	public String getProbabilidadeClassificacaoCNIDescricao() {
		return getDescricaoProcentagem(probabilidadeClassificacaoCNI);
	}

	public Double getProbabilidadeClassificacaoCNI() {
		return probabilidadeClassificacaoCNI;
	}

	public void setProbabilidadeClassificacaoCNI(Double probabilidadeClassificacaoCNI) {
		this.probabilidadeClassificacaoCNI = probabilidadeClassificacaoCNI;
	}

	@Override
	public String toString() {
		return "ClubeProbabilidade [clube=" + clube.getNome() + ", pCampeao=" + probabilidadeCampeao
				+ ", pAcesso=" + probabilidadeAcesso + ", pRebaixamento="
				+ probabilidadeRebaixamento + ", pClassificacaoCI=" + probabilidadeClassificacaoCI
				+ ", pClassificacaoCII=" + probabilidadeClassificacaoCII
				+ ", pClassificacaoCIII=" + probabilidadeClassificacaoCIII 
				+ ", pClassificacaoCNI=" + probabilidadeClassificacaoCNI
				+ "]";
	}

	public Integer getQtdeClassificacaoCIII() {
		return qtdeClassificacaoCIII;
	}

	public void setQtdeClassificacaoCIII(Integer qtdeClassificacaoCIII) {
		this.qtdeClassificacaoCIII = qtdeClassificacaoCIII;
	}

	public Integer getQtdeClassificacaoCII() {
		return qtdeClassificacaoCII;
	}

	public void setQtdeClassificacaoCII(Integer qtdeClassificacaoCII) {
		this.qtdeClassificacaoCII = qtdeClassificacaoCII;
	}

	public Integer getQtdeClassificacaoCI() {
		return qtdeClassificacaoCI;
	}

	public void setQtdeClassificacaoCI(Integer qtdeClassificacaoCI) {
		this.qtdeClassificacaoCI = qtdeClassificacaoCI;
	}

	public Integer getQtdeClassificacaoCNI() {
		return qtdeClassificacaoCNI;
	}

	public void setQtdeClassificacaoCNI(Integer qtdeClassificacaoCNI) {
		this.qtdeClassificacaoCNI = qtdeClassificacaoCNI;
	}

	public Integer getQtdeAcesso() {
		return qtdeAcesso;
	}

	public void setQtdeAcesso(Integer qtdeAcesso) {
		this.qtdeAcesso = qtdeAcesso;
	}

	public Integer getQtdeRebaixamento() {
		return qtdeRebaixamento;
	}

	public void setQtdeRebaixamento(Integer qtdeRebaixamento) {
		this.qtdeRebaixamento = qtdeRebaixamento;
	}

	public Integer getQtdeCampeao() {
		return qtdeCampeao;
	}

	public void setQtdeCampeao(Integer qtdeCampeao) {
		this.qtdeCampeao = qtdeCampeao;
	}
}
