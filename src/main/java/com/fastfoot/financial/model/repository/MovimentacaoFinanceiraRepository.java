package com.fastfoot.financial.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long>{
	
	public List<MovimentacaoFinanceira> findByClube(Clube clube);
	
	public List<MovimentacaoFinanceira> findBySemana(Semana semana);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada ")
	public List<MovimentacaoFinanceira> findByTemporada(@Param("temporada") Temporada temporada);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada AND mov.clube = :clube ")
	public List<MovimentacaoFinanceira> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);

}
