package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface RodadaAmistoraRepository extends JpaRepository<RodadaAmistosa, Long>{

	public List<RodadaAmistosa> findBySemana(Semana semana);
}
