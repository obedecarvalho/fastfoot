package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticaSemana;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface JogadorEstatisticaSemanaRepository extends JpaRepository<JogadorEstatisticaSemana, Long> {

	public List<JogadorEstatisticaSemana> findByClube(Clube clube);
	
	public List<JogadorEstatisticaSemana> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticaSemana> findBySemana(Semana semana);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" delete from jogador_estatistica_semana" +
			" where id_semana in (select id from semana s where s.id_temporada = ?1)"
	)
	public void deleteByIdTemporada(Long idTemporada);
}
