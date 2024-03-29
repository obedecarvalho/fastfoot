package com.fastfoot.match.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaTorcida;

@Repository
public interface PartidaTorcidaRepository extends JpaRepository<PartidaTorcida, Long>{

	/*public List<PartidaTorcida> findByPartidaAmistosaResultado(PartidaAmistosaResultado partidaAmistosaResultado);

	public List<PartidaTorcida> findByPartidaEliminatoriaResultado(PartidaEliminatoriaResultado partidaEliminatoriaResultado);

	public List<PartidaTorcida> findByPartidaResultado(PartidaResultado partidaResultado);*/
}
