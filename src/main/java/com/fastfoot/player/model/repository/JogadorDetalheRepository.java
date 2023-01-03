package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorDetalhe;

@Repository
public interface JogadorDetalheRepository extends JpaRepository<JogadorDetalhe, Long> {

	@Query(" SELECT jd FROM JogadorDetalhe jd WHERE jd.jogador.statusJogador = :status ")
	public List<JogadorDetalhe> findByStatusJogador(@Param("status") StatusJogador status);
	
	public List<JogadorDetalhe> findByJogador(Jogador jogador);
	
	@Query(" SELECT jd FROM JogadorDetalhe jd WHERE jd.jogador.idade BETWEEN :idadeMin AND :idadeMax ")
	public List<JogadorDetalhe> findByIdadeBetween(@Param("idadeMin") Integer idadeMin, @Param("idadeMax") Integer idadeMax);
}
