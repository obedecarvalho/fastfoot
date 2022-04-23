package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.ClubeRanking;
import com.fastfoot.scheduler.model.entity.Temporada;

public interface ClubeRankingRepository extends JpaRepository<ClubeRanking, Integer>{

	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.posicaoGeral BETWEEN :menor AND :maior AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByRankingNacionalBetween(@Param("ano") Integer ano, @Param("menor") Integer menor, @Param("maior") Integer maior);
	
	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.clube.liga = :liga AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByLigaAndAno(@Param("liga") Liga liga, @Param("ano") Integer ano); 

	public List<ClubeRanking> findByTemporada(Temporada temporada);

}
