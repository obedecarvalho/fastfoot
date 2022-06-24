package com.fastfoot.player.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;

@Repository
public interface JogadorEstatisticasTemporadaRepository extends JpaRepository<JogadorEstatisticasTemporada, Long>{

}
