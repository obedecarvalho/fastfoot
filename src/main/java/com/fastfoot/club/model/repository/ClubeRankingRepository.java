package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface ClubeRankingRepository extends JpaRepository<ClubeRanking, Long>{

	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByLigaJogoAndAno(@Param("ligaJogo") LigaJogo liga, @Param("ano") Integer ano);

	public List<ClubeRanking> findByTemporada(Temporada temporada);
	
	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.temporada = :temporada ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByLigaJogoAndTemporada(@Param("ligaJogo") LigaJogo ligaJogo, @Param("temporada") Temporada temporada);
	
	public List<ClubeRanking> findByClube(Clube clube);
	
	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.temporada.jogo = :jogo ")
	public List<ClubeRanking> findByJogo(@Param("jogo") Jogo jogo);
}
