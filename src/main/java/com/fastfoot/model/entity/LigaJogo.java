package com.fastfoot.model.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;

@Entity
public class LigaJogo {//TODO: melhorar nome

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ligaJogoSequence")	
	@SequenceGenerator(name = "ligaJogoSequence", sequenceName = "liga_jogo_seq", allocationSize = 8)
	private Long id;
	
	private Liga liga;
	
	@ManyToOne
	@JoinColumn(name = "id_jogo")
	private Jogo jogo;
	
	private Long idClubeInicial;
	
	private Long idClubeFinal;
	
	@Transient
	private List<Clube> clubes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public Long getIdClubeInicial() {
		return idClubeInicial;
	}

	public void setIdClubeInicial(Long idClubeInicial) {
		this.idClubeInicial = idClubeInicial;
	}

	public Long getIdClubeFinal() {
		return idClubeFinal;
	}

	public void setIdClubeFinal(Long idClubeFinal) {
		this.idClubeFinal = idClubeFinal;
	}

	public List<Clube> getClubes() {
		return clubes;
	}

	public void setClubes(List<Clube> clubes) {
		this.clubes = clubes;
	}

}
