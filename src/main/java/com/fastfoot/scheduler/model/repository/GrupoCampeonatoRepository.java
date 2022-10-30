package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface GrupoCampeonatoRepository extends JpaRepository<GrupoCampeonato, Long>{

	public List<GrupoCampeonato> findByCampeonato(CampeonatoMisto campeonato);
	
	@Query("SELECT gc FROM GrupoCampeonato gc WHERE gc.campeonato.temporada = :temporada ")
	public List<GrupoCampeonato> findByTemporada(@Param("temporada") Temporada temporada);
}
