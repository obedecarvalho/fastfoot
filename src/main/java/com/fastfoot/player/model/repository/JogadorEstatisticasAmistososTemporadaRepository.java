package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasAmistososTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface JogadorEstatisticasAmistososTemporadaRepository extends JpaRepository<JogadorEstatisticasAmistososTemporada, Long> {

	public List<JogadorEstatisticasAmistososTemporada> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticasAmistososTemporada> findByTemporada(Temporada temporada);
}
