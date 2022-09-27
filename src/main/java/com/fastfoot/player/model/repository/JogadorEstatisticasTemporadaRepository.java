package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface JogadorEstatisticasTemporadaRepository extends JpaRepository<JogadorEstatisticasTemporada, Long>{

	public List<JogadorEstatisticasTemporada> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticasTemporada> findByTemporada(Temporada temporada);
	
	public List<JogadorEstatisticasTemporada> findByTemporadaAndJogador(Temporada temporada, Jogador jogador);

}
