package com.fastfoot.scheduler.model.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.PartidaResultado;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface PartidaResultadoRepository extends JpaRepository<PartidaResultado, Long>{

	public List<PartidaResultado> findByRodada(Rodada rodada);

	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.campeonato = :campeonato ")
	public List<PartidaResultado> findByCampeonato(@Param("campeonato") Campeonato campeonato);

	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.grupoCampeonato = :grupoCampeonato ")
	public List<PartidaResultado> findByGrupoCampeonato(@Param("grupoCampeonato") GrupoCampeonato grupoCampeonato);
	
	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.grupoCampeonato.campeonato = :campeonato ")
	public List<PartidaResultado> findByCampeonato(@Param("campeonato") CampeonatoMisto campeonato);

	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.semana.temporada = :temporada AND (p.clubeMandante = :clube OR p.clubeVisitante = :clube) ")
	public List<PartidaResultado> findByTemporadaAndClube(@Param("temporada") Temporada temporada, @Param("clube") Clube clube);

	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.semana.temporada = :temporada AND p.partidaJogada = :partidaJogada")
	public List<PartidaResultado> findByTemporadaAndPartidaJogada(@Param("temporada") Temporada temporada,
			@Param("partidaJogada") Boolean partidaJogada);
	
	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada.semana = :semana ")
	public List<PartidaResultado> findBySemana(@Param("semana") Semana semana);

	public List<PartidaResultado> findByClubeMandanteAndPartidaJogada(Clube clube, Boolean partidaJogada);

	public List<PartidaResultado> findByClubeVisitanteAndPartidaJogada(Clube clube, Boolean partidaJogada);
	
	@Query("SELECT p FROM PartidaResultado p WHERE p.clubeMandante = :clube OR p.clubeVisitante = :clube")
	public List<PartidaResultado> findByClube(@Param("clube") Clube clube);

	@Query("SELECT p FROM PartidaResultado p WHERE p.rodada IN :rodadas")
	public List<PartidaResultado> findByRodadas(@Param("rodadas") Collection<Rodada> rodadas);
}
