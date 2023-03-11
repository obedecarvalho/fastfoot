package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEnergia;

@Repository
public interface JogadorEnergiaRepository extends JpaRepository<JogadorEnergia, Long> {

	public Optional<JogadorEnergia> findFirstByJogadorOrderByIdDesc(Jogador jogador);
	
	@Query("SELECT je FROM JogadorEnergia je WHERE je.jogador.clube = :clube ORDER BY je.id DESC ")
	public List<JogadorEnergia> findByClube(Clube clube);
	
	/*@Query("SELECT je FROM JogadorEnergia je WHERE je.jogador.clube = :clube AND je.temporada = :temporada ORDER BY je.id DESC ")
	public List<JogadorEnergia> findByClubeAndTemporada(Clube clube, Temporada temporada);*/
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" insert into jogador_energia (id, energia, id_jogador)  " +
			" select nextval('jogador_energia_seq'), ?1 as energia, id as id_jogador " +
			" from jogador " +
			" where status_jogador = 0 " //StatusJogador.ATIVO
	)
	public void inserirEnergiaTodosJogadores(Integer energia);
	
	@Query(nativeQuery = true, value =
			" select id_jogador, sum(energia) as energia " +
			" from jogador_energia je " +
			" inner join jogador j on j.id = je.id_jogador " +
			" where j.id_clube = ?1 " +
			" group by id_jogador "
	)
	public List<Map<String, Object>> findEnergiaJogadorByIdClube(Integer idClube);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" insert into jogador_energia (id, energia, id_jogador) " +
			" select  " +
			" 	nextval('jogador_energia_seq'), " +
			" 	case  " +
			" 		when (?1 - tmp.energia) <= ?2 then (?1 - tmp.energia) " +
			" 		else ?2 " +
			" 	end as energia_recuperar, " +
			" 	tmp.id_jogador " +
			" from ( " +
			" 	select id_jogador, sum(energia) as energia " +
			" 	from jogador_energia je " +
			" 	inner join jogador j on j.id = je.id_jogador " +
			" 	where j.status_jogador = 0 " + //StatusJogador.ATIVO
			" 	group by id_jogador " +
			" ) as tmp " +
			" where tmp.energia < ?1 "
	)
	public void inserirRecuperacaoEnergiaTodosJogadores(Integer energiaMax, Integer energiaRecuperar);
}
