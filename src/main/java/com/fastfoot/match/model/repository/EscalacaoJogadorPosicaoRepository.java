package com.fastfoot.match.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.match.model.entity.EscalacaoJogadorPosicao;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface EscalacaoJogadorPosicaoRepository extends JpaRepository<EscalacaoJogadorPosicao, Long>{

	public List<EscalacaoJogadorPosicao> findByClubeAndAtivo(Clube clube, Boolean ativo);
	
	public List<EscalacaoJogadorPosicao> findByJogadorAndAtivo(Jogador jogador, Boolean ativo);
	
	@Query(" SELECT DISTINCT e FROM EscalacaoJogadorPosicao e INNER JOIN FETCH e.jogador j INNER JOIN FETCH j.habilidadesValor h WHERE e.clube = :clube AND e.ativo = :ativo")
	public List<EscalacaoJogadorPosicao> findByClubeAndAtivoFetchJogadorHabilidades(@Param("clube") Clube clube, @Param("ativo") Boolean ativo);
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" UPDATE escalacao_jogador_posicao " +
			" SET ativo = false "
	)
	public void desativarTodas();
	
	//###	/INSERT, UPDATE E DELETE	###

}
