package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.ClubeRanking;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface ClubeRankingRepository extends JpaRepository<ClubeRanking, Integer>{

	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.posicaoGeral BETWEEN :menor AND :maior AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByRankingNacionalBetween(@Param("ano") Integer ano, @Param("menor") Integer menor, @Param("maior") Integer maior);
	
	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.clube.liga = :liga AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByLigaAndAno(@Param("liga") Liga liga, @Param("ano") Integer ano); 

	public List<ClubeRanking> findByTemporada(Temporada temporada);
	
	@Query("SELECT cr FROM ClubeRanking cr WHERE cr.clube.liga = :liga AND cr.temporada = :temporada ORDER BY cr.posicaoGeral ")
	public List<ClubeRanking> findByLigaAndTemporada(@Param("liga") Liga liga, @Param("temporada") Temporada temporada);

	@Query("SELECT DISTINCT cr.ano FROM ClubeRanking cr ORDER BY cr.ano DESC ")
	public List<Integer> findAnosClubeRanking();
	
	public List<ClubeRanking> findByClube(Clube clube);
}