package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndAposentado(Clube clube, Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube AND j.aposentado = :aposentado ")
	public List<Jogador> findByClubeAndAposentadoFetchHabilidades(@Param("clube") Clube clube, @Param("aposentado") Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);
	
	public List<Jogador> findByAposentado(Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.aposentado = :aposentado ")
	public List<Jogador> findByAposentadoFetchHabilidades(@Param("aposentado") Boolean aposentado);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = " update jogador jog " + " set forca_geral = tmp.forca_geral " + " from ( "
			+ " 	select j.id, avg(hv.valor) as forca_geral " + " 	from jogador j "
			+ " 	inner join habilidade_valor hv on hv.id_jogador = j.id "
			+ " 	where hv.habilidade_especifica and not j.aposentado " + " 	group by j.id " + " ) tmp "
			+ " where jog.id = tmp.id "
	)
	public void calcularForcaGeral();
}
