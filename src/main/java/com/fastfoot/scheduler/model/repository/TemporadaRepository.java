package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.scheduler.model.entity.Temporada;

/**
 * 
 * @author obede
 *
 * Link util: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface TemporadaRepository extends JpaRepository<Temporada, Long>{

	public List<Temporada> findByAtual(Boolean atual);
	
	public Optional<Temporada> findFirstByAtual(Boolean atual);
}
