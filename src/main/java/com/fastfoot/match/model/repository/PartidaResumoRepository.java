package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaResumo;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

public interface PartidaResumoRepository extends JpaRepository<PartidaResumo, Long> {
	
	public List<PartidaResumo> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);
	
	public List<PartidaResumo> findByPartidaResultado(PartidaResultado partidaResultado);
}
