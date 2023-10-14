package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface JogadorEstatisticasTemporadaRepository extends JpaRepository<JogadorEstatisticasTemporada, Long>{
	
	public List<JogadorEstatisticasTemporada> findByTemporada(Temporada temporada);

	@Query(" SELECT jet FROM JogadorEstatisticasTemporada jet WHERE "
			+ " jet.jogador.clube.ligaJogo.jogo = :jogo AND jet.amistoso = :amistoso ")
	public List<JogadorEstatisticasTemporada> findByJogoAndAmistoso(@Param("jogo") Jogo jogo,
			@Param("amistoso") Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByClubeAndAmistoso(Clube clube, Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByTemporadaAndAmistoso(Temporada temporada, Boolean amistoso);

	public List<JogadorEstatisticasTemporada> findByTemporadaAndClube(Temporada temporada, Clube clube);
	
	@Query(" SELECT jet FROM JogadorEstatisticasTemporada jet WHERE "
			+ " jet.jogador.clube.ligaJogo = :ligaJogo AND jet.jogador.clube.id BETWEEN :idClubeMin AND :idClubeMax "
			+ " AND jet.jogador.idade BETWEEN :idadeMin AND :idadeMax ")
	public List<JogadorEstatisticasTemporada> findByLigaJogoClubeAndStatusJogadorAndIdadeBetween(@Param("ligaJogo") LigaJogo ligaJogo,
			@Param("idClubeMin") Long idClubeMin, @Param("idClubeMax") Long idClubeMax,
			@Param("idadeMin") Integer idadeMin, @Param("idadeMax") Integer idadeMax);
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" INSERT INTO jogador_estatisticas_temporada" +
			" (id, id_jogador, id_clube, id_temporada, amistoso,  assistencias, defesas_disputa_penalties, " +
			" 	faltas, finalizacoes_defendidas, finalizacoes_fora, goleiro_finalizacoes_defendidas, " +
			" 	gols_disputa_penalties, gols_marcados, gols_perdidos_disputa_penalties, gols_sofridos, " +
			" 	gols_sofridos_disputa_penalties, jogos, jogos_titular, " +
			" 	minutos_jogados, rodadas_disputa_penalties)" +
			" SELECT NEXTVAL('jogador_estatisticas_temporada_seq') AS id," +
			" 	id_jogador, id_clube, s.id_temporada, amistoso, " +
			" 	sum(assistencias) AS assistencias," +
			" 	sum(defesas_disputa_penalties) AS defesas_disputa_penalties," +
			" 	sum(faltas) AS faltas," +
			" 	sum(finalizacoes_defendidas) AS finalizacoes_defendidas," +
			" 	sum(finalizacoes_fora) AS finalizacoes_fora," +
			" 	sum(goleiro_finalizacoes_defendidas) AS goleiro_finalizacoes_defendidas," +
			" 	sum(gols_disputa_penalties) AS gols_disputa_penalties," +
			" 	sum(gols_marcados) AS gols_marcados," +
			" 	sum(gols_perdidos_disputa_penalties) AS gols_perdidos_disputa_penalties," +
			" 	sum(gols_sofridos) AS gols_sofridos," +
			" 	sum(gols_sofridos_disputa_penalties) AS gols_sofridos_disputa_penalties," +
			" 	sum(jogos) AS jogos," +
			" 	sum(jogos_titular) AS jogos_titular," +
			" 	sum(minutos_jogados) AS minutos_jogados," +
			" 	sum(rodadas_disputa_penalties) AS rodadas_disputa_penalties" +
			" FROM jogador_estatisticas_semana jes" +
			" INNER JOIN semana s ON jes.id_semana = s.id" +
			" WHERE s.id_temporada = ?1 " +
			" GROUP BY id_jogador, id_clube, s.id_temporada, amistoso"
	)
	public void agruparJogadorEstatisticasTemporada(Long idTemporada);
	
	//###	/INSERT, UPDATE E DELETE	###

}
