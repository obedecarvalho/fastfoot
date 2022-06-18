package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.ClassificacaoNacionalFinal;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Integer>{

	public List<Clube> findByLiga(Liga liga);
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.liga = :liga AND cr.posicaoGeral BETWEEN :menor AND :maior AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<Clube> findByLigaAndAnoAndPosicaoGeralBetween(@Param("liga") Liga liga, @Param("ano") Integer ano, @Param("menor") Integer menor, @Param("maior") Integer maior);
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.liga = :liga AND cr.classificacaoNacional IN :classNac AND cr.ano = :ano ORDER BY cr.classificacaoNacional ")
	public List<Clube> findByLigaAndAnoAndClassificacaoNacionalBetween(@Param("liga") Liga liga, @Param("ano") Integer ano, @Param("classNac") ClassificacaoNacionalFinal[] classNac);
}
