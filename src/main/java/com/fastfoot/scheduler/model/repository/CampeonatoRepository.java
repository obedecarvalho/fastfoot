package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato, Long>{

	public List<Campeonato> findByTemporada(Temporada temporada);
}
