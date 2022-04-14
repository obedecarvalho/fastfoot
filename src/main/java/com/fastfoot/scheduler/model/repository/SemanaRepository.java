package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

public interface SemanaRepository extends JpaRepository<Semana, Long>{

	public List<Semana> findByTemporada(Temporada temporada);
	
	public Optional<Semana> findFirstByTemporadaAndNumero(Temporada temporada, Integer numero);
}
