package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface RodadaAmistoraRepository extends JpaRepository<RodadaAmistosa, Long>{

	public List<RodadaAmistosa> findBySemana(Semana semana);
	
	@Query("SELECT r FROM RodadaAmistosa r WHERE r.semana.temporada = :temporada")
	public List<RodadaAmistosa> findByTemporada(@Param("temporada") Temporada temporada);
}
