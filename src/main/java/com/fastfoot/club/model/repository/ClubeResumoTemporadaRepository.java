package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeResumoTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface ClubeResumoTemporadaRepository extends JpaRepository<ClubeResumoTemporada, Long> {

	public List<ClubeResumoTemporada> findByClube(Clube clube);

	public List<ClubeResumoTemporada> findByTemporada(Temporada temporada);

}
