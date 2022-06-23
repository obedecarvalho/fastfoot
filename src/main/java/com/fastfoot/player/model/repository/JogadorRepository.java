package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndAposentado(Clube clube, Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube AND j.aposentado = :aposentado ")
	public List<Jogador> findByClubeAndAposentadoFetchHabilidades(@Param("clube") Clube clube, @Param("aposentado") Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);
	
	public List<Jogador> findByAposentado(Boolean aposentado);
}
