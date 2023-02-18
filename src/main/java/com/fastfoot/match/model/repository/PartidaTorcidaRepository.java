package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaTorcida;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.PartidaResultado;

@Repository
public interface PartidaTorcidaRepository extends JpaRepository<PartidaTorcida, Long>{

	public List<PartidaTorcida> findByPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado);

	public List<PartidaTorcida> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);

	public List<PartidaTorcida> findByPartidaResultado(PartidaResultado partidaResultado);
}
