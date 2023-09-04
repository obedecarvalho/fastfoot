package com.fastfoot.bets.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Repository
public interface PartidaProbabilidadeResultadoRepository extends JpaRepository<PartidaProbabilidadeResultado, Long> {

	public List<PartidaProbabilidadeResultado> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);
	
	public List<PartidaProbabilidadeResultado> findByPartidaResultado(PartidaResultado partidaResultado);
}
