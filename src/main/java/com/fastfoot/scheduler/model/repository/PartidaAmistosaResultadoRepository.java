package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface PartidaAmistosaResultadoRepository extends JpaRepository<PartidaAmistosaResultado, Long> {

	public List<PartidaAmistosaResultado> findByRodada(RodadaAmistosa rodada);

	@Query("SELECT par FROM PartidaAmistosaResultado par WHERE par.rodada.semana = :semana")
	public List<PartidaAmistosaResultado> findBySemana(@Param("semana") Semana semana);

	@Query("SELECT par FROM PartidaAmistosaResultado par WHERE par.rodada.semana.temporada = :temporada")
	public List<PartidaAmistosaResultado> findByTemporada(@Param("temporada") Temporada temporada);

	public List<PartidaAmistosaResultado> findByClubeMandanteAndPartidaJogada(Clube clube, Boolean partidaJogada);

	public List<PartidaAmistosaResultado> findByClubeVisitanteAndPartidaJogada(Clube clube, Boolean partidaJogada);
	
	@Query("SELECT pa FROM PartidaAmistosaResultado pa WHERE pa.rodada.semana.temporada = :temporada AND (pa.clubeMandante = :clube OR pa.clubeVisitante = :clube) ")
	public List<PartidaAmistosaResultado> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);
}
