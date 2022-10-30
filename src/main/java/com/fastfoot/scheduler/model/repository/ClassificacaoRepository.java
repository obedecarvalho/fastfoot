package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Classificacao;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {

	public List<Classificacao> findByCampeonato(Campeonato campeonato);

	public List<Classificacao> findByCampeonatoOrderByPosicao(Campeonato campeonato);
	
	public List<Classificacao> findByClube(Clube clube);
	
	public List<Classificacao> findByGrupoCampeonato(GrupoCampeonato grupoCampeonato);

	@Query("SELECT c FROM Classificacao c WHERE c.grupoCampeonato.campeonato = :campeonatoMisto ORDER BY c.posicao ")
	public List<Classificacao> findByCampeonatoMistoOrderByPosicao(@Param("campeonatoMisto") CampeonatoMisto campeonatoMisto);
	
	@Query("SELECT c FROM Classificacao c WHERE c.grupoCampeonato.campeonato.temporada = :temporada ")
	public List<Classificacao> findByTemporadaGrupoCampeonato(@Param("temporada") Temporada temporada);
	
	@Query("SELECT c FROM Classificacao c WHERE c.campeonato.temporada = :temporada ")
	public List<Classificacao> findByTemporadaCampeonato(@Param("temporada") Temporada temporada);
}
