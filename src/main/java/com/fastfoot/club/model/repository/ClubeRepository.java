package com.fastfoot.club.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.ClassificacaoNacional;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long>{
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.posicaoGeral BETWEEN :menor AND :maior AND cr.ano = :ano ORDER BY cr.posicaoGeral ")
	public List<Clube> findByLigaJogoAndAnoAndPosicaoGeralBetween(@Param("ligaJogo") LigaJogo liga, @Param("ano") Integer ano,
			@Param("menor") Integer menor, @Param("maior") Integer maior);
	
	@Query("SELECT cr.clube FROM ClubeRanking cr WHERE cr.clube.ligaJogo = :ligaJogo AND cr.classificacaoNacional IN :classNac AND cr.ano = :ano ORDER BY cr.classificacaoNacional ")
	public List<Clube> findByLigaJogoAndAnoAndClassificacaoNacionalBetween(@Param("ligaJogo") LigaJogo liga,
			@Param("ano") Integer ano, @Param("classNac") ClassificacaoNacional[] classNac);
	
	@Query("SELECT max(c.id) FROM Clube c")
	public Optional<Long> findLastId();
	
	public List<Clube> findByLigaJogo(LigaJogo ligaJogo);
	
	@Query("SELECT c FROM Clube c WHERE c.ligaJogo.jogo = :jogo")
	public List<Clube> findByJogo(@Param("jogo") Jogo jogo);
}
