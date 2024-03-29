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
	
	//###	SELECT ESPECIFICOS	###
	
	@Query(nativeQuery = true, value =
			" select id_jogador, sum(energia) as energia " +
			" from jogador_energia je " +
			" inner join jogador j on j.id = je.id_jogador " +
			" where j.id_clube = ?1 " +
			" group by id_jogador "
	)
	public List<Map<String, Object>> findEnergiaJogadorByIdClube(Long idClube);

	@Query(nativeQuery = true, value =
			" select id_jogador, sum(energia) as energia " +
			" from jogador_energia je " +
			" inner join jogador j on j.id = je.id_jogador " +
			" inner join clube c on j.id_clube = c.id " +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" where lj.id_jogo = ?1 " +
			" group by id_jogador "
	)
	public List<Map<String, Object>> findEnergiaJogador(Long idJogo);
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	INSERT, UPDATE E DELETE	###
	
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
			" 	inner join clube c on j.id_clube = c.id " +
			" 	inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 	where j.status_jogador = 0 " + //StatusJogador.ATIVO
			" 		and j.idade between ?3 and ?4 " +
			" 		and lj.id_jogo = ?5 " +
			" 	group by id_jogador " +
			" ) as tmp " +
			" where tmp.energia < ?1 "
	)
	public void inserirRecuperacaoEnergiaJogadores(Integer energiaMax, Integer energiaRecuperar, Integer idadeMin, Integer idadeMax, Long idJogo);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" insert into jogador_energia (id, energia, id_jogador)  " +
			" select nextval('jogador_energia_seq'), ?1 as energia, j.id as id_jogador " +
			" from jogador j " +
			" inner join clube c on j.id_clube = c.id " +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" where j.status_jogador = 0 " +//StatusJogador.ATIVO
			" 	and lj.id_jogo = ?2 "
	)
	public void inserirEnergiaTodosJogadores(Integer energia, Long idJogo);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" delete from jogador_energia " +
			" using ( " +
			" 	select j.id " +
			" 	from jogador j " +
			" 	inner join clube c on j.id_clube = c.id " +
			" 	inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 	where lj.id_jogo = ?1 " +
			" ) as tmp " +
			" where tmp.id = jogador_energia.id_jogador "
	)
	public void deleteByIdJogo(Long idJogo);
	
	//###	/INSERT, UPDATE E DELETE	###
}
