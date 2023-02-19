package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.PartidaEliminatoriaResultado;
import com.fastfoot.scheduler.model.entity.RodadaEliminatoria;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface PartidaEliminatoriaResultadoRepository extends JpaRepository<PartidaEliminatoriaResultado, Long>{

	public List<PartidaEliminatoriaResultado> findByRodada(RodadaEliminatoria rodada);

	@Query("SELECT pe FROM PartidaEliminatoriaResultado pe WHERE pe.rodada.campeonatoMisto = :campeonatoMisto AND pe.rodada.numero = :numero ")
	public List<PartidaEliminatoriaResultado> findByCampeonatoMistoAndNumero(
			@Param("campeonatoMisto") CampeonatoMisto campeonatoMisto, @Param("numero") Integer numero);
	
	@Query("SELECT pe FROM PartidaEliminatoriaResultado pe WHERE pe.rodada.semana.temporada = :temporada AND (pe.clubeMandante = :clube OR pe.clubeVisitante = :clube) ")
	public List<PartidaEliminatoriaResultado> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);
	
	@Query("SELECT pe FROM PartidaEliminatoriaResultado pe WHERE pe.rodada.semana = :semana")
	public List<PartidaEliminatoriaResultado> findBySemana(@Param("semana") Semana semana);

	public List<PartidaEliminatoriaResultado> findByClubeMandanteAndPartidaJogada(Clube clube, Boolean partidaJogada);

	public List<PartidaEliminatoriaResultado> findByClubeVisitanteAndPartidaJogada(Clube clube, Boolean partidaJogada);
	
	@Query("SELECT pe FROM PartidaEliminatoriaResultado pe WHERE pe.rodada.campeonatoMisto = :campeonato")
	public List<PartidaEliminatoriaResultado> findByCampeonato(@Param("campeonato") CampeonatoMisto campeonato);
	
	@Query("SELECT pe FROM PartidaEliminatoriaResultado pe WHERE pe.rodada.campeonatoEliminatorio = :campeonato")
	public List<PartidaEliminatoriaResultado> findByCampeonato(@Param("campeonato") CampeonatoEliminatorio campeonato);

	@Query("SELECT p FROM PartidaEliminatoriaResultado p WHERE p.clubeMandante = :clube OR p.clubeVisitante = :clube")
	public List<PartidaEliminatoriaResultado> findByClube(@Param("clube") Clube clube);
}
