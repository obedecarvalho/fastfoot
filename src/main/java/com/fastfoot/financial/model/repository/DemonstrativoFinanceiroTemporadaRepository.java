package com.fastfoot.financial.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.DemonstrativoFinanceiroTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface DemonstrativoFinanceiroTemporadaRepository extends JpaRepository<DemonstrativoFinanceiroTemporada, Long> {

	public List<DemonstrativoFinanceiroTemporada> findByClube(Clube clube);
	
	public List<DemonstrativoFinanceiroTemporada> findByTemporada(Temporada temporada);
	
	public List<DemonstrativoFinanceiroTemporada> findByTemporadaAndClube(Temporada temporada, Clube clube);
}
