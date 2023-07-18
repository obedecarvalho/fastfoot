package com.fastfoot.club.model.dto;

import java.util.List;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;

public class ClubeRankingHistoricoDTO {
	
	private Clube clube;
	
	private List<ClubeRanking> clubeRankings;
	
	private Double posicaoGeralMedia;
	
	private Double posicaoInternacionalMedia;
	
	private Double posicaoNacionalMedia;

	public Clube getClube() {
		return clube;
	}

	public void setClube(Clube clube) {
		this.clube = clube;
	}

	public List<ClubeRanking> getClubeRankings() {
		return clubeRankings;
	}

	public void setClubeRankings(List<ClubeRanking> clubeRankings) {
		this.clubeRankings = clubeRankings;
	}

	public Double getPosicaoGeralMedia() {
		return posicaoGeralMedia;
	}

	public void setPosicaoGeralMedia(Double posicaoGeralMedia) {
		this.posicaoGeralMedia = posicaoGeralMedia;
	}

	public Double getPosicaoNacionalMedia() {
		return posicaoNacionalMedia;
	}

	public void setPosicaoNacionalMedia(Double posicaoNacionalMedia) {
		this.posicaoNacionalMedia = posicaoNacionalMedia;
	}

	public Double getPosicaoInternacionalMedia() {
		return posicaoInternacionalMedia;
	}

	public void setPosicaoInternacionalMedia(Double posicaoInternacionalMedia) {
		this.posicaoInternacionalMedia = posicaoInternacionalMedia;
	}

	@Override
	public String toString() {
		return "ClubeRankingHistoricoDTO [clube=" + clube.getNome() + "(" + clube.getNivelNacional() + ")"
				+ ", posicaoGeralMedia=" + posicaoGeralMedia + ", posicaoNacionalMedia=" + posicaoNacionalMedia + "]";
	}

}
