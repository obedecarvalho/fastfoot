package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.club.model.entity.TreinadorTituloRanking;
import com.fastfoot.model.entity.Jogo;

@Repository
public interface TreinadorTituloRankingRepository extends JpaRepository<TreinadorTituloRanking, Long> {

	public List<TreinadorTituloRanking> findByTreinador(Treinador treinador);

	@Query("SELECT ttr FROM TreinadorTituloRanking ttr WHERE ttr.treinador.jogo = :jogo ")
	public List<TreinadorTituloRanking> findByJogo(@Param("jogo") Jogo jogo);

}
