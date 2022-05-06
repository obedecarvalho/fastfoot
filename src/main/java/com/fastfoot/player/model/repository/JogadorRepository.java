package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClube(Clube clube);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube ")
	public List<Jogador> findByClubeFetchHabilidades(@Param("clube") Clube clube);
}
