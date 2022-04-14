package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Temporada;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long>{

	public List<Campeonato> findByTemporada(Temporada temporada);
}
