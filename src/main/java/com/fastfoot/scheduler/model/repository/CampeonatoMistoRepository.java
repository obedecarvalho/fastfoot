package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoMistoRepository extends JpaRepository<CampeonatoMisto, Long> {

	public List<CampeonatoMisto> findByTemporada(Temporada temporada);
}
