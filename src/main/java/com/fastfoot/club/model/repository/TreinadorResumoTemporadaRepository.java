package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.entity.TreinadorResumoTemporada;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface TreinadorResumoTemporadaRepository extends JpaRepository<TreinadorResumoTemporada, Long> {

	public List<TreinadorResumoTemporada> findByTreinador(Treinador treinador);

	public List<TreinadorResumoTemporada> findByTemporada(Temporada temporada);

	@Query("SELECT ttr FROM TreinadorResumoTemporada ttr WHERE ttr.treinador.jogo = :jogo ")
	public List<TreinadorResumoTemporada> findByJogo(@Param("jogo") Jogo jogo);

}
