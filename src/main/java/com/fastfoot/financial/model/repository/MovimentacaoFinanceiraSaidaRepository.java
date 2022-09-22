package com.fastfoot.financial.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraSaida;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface MovimentacaoFinanceiraSaidaRepository extends JpaRepository<MovimentacaoFinanceiraSaida, Long> {
	
	public List<MovimentacaoFinanceiraSaida> findByClube(Clube clube);
	
	public List<MovimentacaoFinanceiraSaida> findBySemana(Semana semana);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceiraSaida mov WHERE mov.semana.temporada = :temporada ")
	public List<MovimentacaoFinanceiraSaida> findByTemporada(@Param("temporada") Temporada temporada);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceiraSaida mov WHERE mov.semana.temporada = :temporada AND mov.clube = :clube ")
	public List<MovimentacaoFinanceiraSaida> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);
}
