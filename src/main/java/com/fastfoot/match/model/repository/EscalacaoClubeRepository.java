package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoClube;

@Repository
public interface EscalacaoClubeRepository extends JpaRepository<EscalacaoClube, Long> {

	@Query(" SELECT DISTINCT ec FROM EscalacaoClube ec JOIN FETCH ec.listEscalacaoJogadorPosicao ep WHERE ec.clube = :clube AND ec.ativo = :ativo ")
	public List<EscalacaoClube> findByClubeAndAtivoFetchEscalacaoJogadorPosicao(@Param("clube") Clube clube,
			@Param("ativo") Boolean ativo);
	
	
	public List<EscalacaoClube> findByClube(Clube clube);
}
