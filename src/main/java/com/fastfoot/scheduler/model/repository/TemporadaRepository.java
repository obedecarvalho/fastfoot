package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Temporada;

/**
 * 
 * @author obede
 *
 * Link util: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
@Repository
public interface TemporadaRepository extends JpaRepository<Temporada, Long>{

	public Optional<Temporada> findFirstByJogoAndAtual(Jogo jogo, Boolean atual);

	public Optional<Temporada> findFirstByJogoAndAno(Jogo jogo, Integer ano);
	
	public Optional<Temporada> findFirstByJogoOrderByAnoDesc(Jogo jogo);
	
	public List<Temporada> findByJogo(Jogo jogo);
}
