package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Rodada;
import com.fastfoot.scheduler.model.entity.Semana;

/**
 * 
 * @author obede
 *
 * Link util: 
 * 	https://www.baeldung.com/spring-data-jpa-query
 * 	https://www.devmedia.com.br/tipos-de-heranca-no-hibernate/28641
 */
@Repository
public interface RodadaRepository extends JpaRepository<Rodada, Long>{

	@Query("SELECT r FROM Rodada r WHERE r.campeonato = :campeonato AND r.numero = :numero ")
	public List<Rodada> findByCampeonatoAndNumero(@Param("campeonato") Campeonato campeonato, @Param("numero") Integer numero);
	
	@Query("SELECT r FROM Rodada r WHERE r.grupoCampeonato IN :gruposCampeonato AND r.numero = :numero ")
	public List<Rodada> findByGruposCampeonatoAndNumero(@Param("gruposCampeonato") List<GrupoCampeonato> grupoCampeonato, @Param("numero") Integer numero);
	
	public List<Rodada> findBySemana(Semana semana);

	public List<Rodada> findByCampeonato(Campeonato campeonato);

	public List<Rodada> findByGrupoCampeonato(GrupoCampeonato grupoCampeonato);
}
