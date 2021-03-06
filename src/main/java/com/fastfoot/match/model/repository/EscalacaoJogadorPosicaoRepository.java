package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface EscalacaoJogadorPosicaoRepository extends JpaRepository<EscalacaoJogadorPosicao, Long>{

	public List<EscalacaoJogadorPosicao> findByClube(Clube clube);
	
	public List<EscalacaoJogadorPosicao> findByJogador(Jogador jogador);
	
	@Query(" SELECT DISTINCT e FROM EscalacaoJogadorPosicao e INNER JOIN FETCH e.jogador j INNER JOIN FETCH j.habilidades h WHERE e.clube = :clube")
	public List<EscalacaoJogadorPosicao> findByClubeFetchJogadorHabilidades(@Param("clube") Clube clube);
}
