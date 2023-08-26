package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface JogadorEstatisticasTemporadaRepository extends JpaRepository<JogadorEstatisticasTemporada, Long>{
	
	public List<JogadorEstatisticasTemporada> findByTemporada(Temporada temporada);

	public List<JogadorEstatisticasTemporada> findByAmistoso(Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByClubeAndAmistoso(Clube clube, Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByTemporadaAndAmistoso(Temporada temporada, Boolean amistoso);

	public List<JogadorEstatisticasTemporada> findByTemporadaAndClube(Temporada temporada, Clube clube);
	
	@Query(" SELECT jet FROM JogadorEstatisticasTemporada jet WHERE "
			+ " jet.jogador.clube.liga = :liga AND jet.jogador.clube.id BETWEEN :idClubeMin AND :idClubeMax "
			+ " AND jet.jogador.idade BETWEEN :idadeMin AND :idadeMax ")
	public List<JogadorEstatisticasTemporada> findByLigaClubeAndStatusJogadorAndIdadeBetween(@Param("liga") Liga liga,
			@Param("idClubeMin") Integer idClubeMin, @Param("idClubeMax") Integer idClubeMax,
			@Param("idadeMin") Integer idadeMin, @Param("idadeMax") Integer idadeMax);
	
	//###	INSERT, UPDATE E DELETE	###

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" INSERT INTO jogador_estatisticas_temporada" +
			" (id, id_jogador, id_clube, id_temporada, amistoso,  assistencias, defesas_disputa_penalt, " +
			" 	faltas, finalizacoes_defendidas, finalizacoes_fora, goleiro_finalizacoes_defendidas, " +
			" 	gols_disputa_penalt, gols_marcados, gols_perdidos_disputa_penalt, gols_sofridos, " +
			" 	gols_sofridos_disputa_penalt, numero_jogos, numero_jogos_titular, " +
			" 	numero_minutos_jogados, numero_rodada_disputa_penalt)" +
			" SELECT NEXTVAL('jogador_estatisticas_temporada_seq') AS id," +
			" 	id_jogador, id_clube, s.id_temporada, amistoso, " +
			" 	sum(assistencias) AS assistencias," +
			" 	sum(defesas_disputa_penalt) AS defesas_disputa_penalt," +
			" 	sum(faltas) AS faltas," +
			" 	sum(finalizacoes_defendidas) AS finalizacoes_defendidas," +
			" 	sum(finalizacoes_fora) AS finalizacoes_fora," +
			" 	sum(goleiro_finalizacoes_defendidas) AS goleiro_finalizacoes_defendidas," +
			" 	sum(gols_disputa_penalt) AS gols_disputa_penalt," +
			" 	sum(gols_marcados) AS gols_marcados," +
			" 	sum(gols_perdidos_disputa_penalt) AS gols_perdidos_disputa_penalt," +
			" 	sum(gols_sofridos) AS gols_sofridos," +
			" 	sum(gols_sofridos_disputa_penalt) AS gols_sofridos_disputa_penalt," +
			" 	sum(numero_jogos) AS numero_jogos," +
			" 	sum(numero_jogos_titular) AS numero_jogos_titular," +
			" 	sum(numero_minutos_jogados) AS numero_minutos_jogados," +
			" 	sum(numero_rodada_disputa_penalt) AS numero_rodada_disputa_penalt" +
			" FROM jogador_estatisticas_semana jes" +
			" INNER JOIN semana s ON jes.id_semana = s.id" +
			" GROUP BY id_jogador, id_clube, s.id_temporada, amistoso"
	)
	public void agruparJogadorEstatisticasTemporada();
	
	//###	/INSERT, UPDATE E DELETE	###

}
