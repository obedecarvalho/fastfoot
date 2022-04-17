package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.match.model.entity.PartidaEstatisticas;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

public interface PartidaEstatisticasRepository extends JpaRepository<PartidaEstatisticas, Long> {
	
	public List<PartidaEstatisticas> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);
	
	public List<PartidaEstatisticas> findByPartidaResultado(PartidaResultado partidaResultado);
}
