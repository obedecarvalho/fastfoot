package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasSemana;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface JogadorEstatisticasSemanaRepository extends JpaRepository<JogadorEstatisticasSemana, Long> {

	public List<JogadorEstatisticasSemana> findByClube(Clube clube);
	
	public List<JogadorEstatisticasSemana> findByJogador(Jogador jogador);
	
	public List<JogadorEstatisticasSemana> findBySemana(Semana semana);
		
	//###	SELECT ESPECIFICOS	###

	@Query(nativeQuery = true, value = 
			" SELECT id_jogador, jes.id_clube, s.id_temporada, amistoso, " +
			" 	j.nome as nome_jogador, j.posicao," +
			" 	c.nome as nome_clube," +
			" 	c.logo as logo_clube," +
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
			" INNER JOIN jogador j ON j.id = jes.id_jogador" +
			" INNER JOIN clube c ON c.id = jes.id_clube" +//TODO: clube da estatistica ou clube do jogador?
			" WHERE s.id_temporada = ?1" +
			" 	AND jes.amistoso = ?2" +
			" GROUP BY id_jogador, jes.id_clube, s.id_temporada, amistoso, j.nome, c.nome, j.posicao, c.logo"
	)
	public List<Map<String, Object>> findAgrupadoByTemporada(Long idTemporada, Boolean amistoso);
	
	@Query(nativeQuery = true, value = 
			" SELECT id_jogador, jes.id_clube, s.id_temporada, amistoso, " +
			" 	j.nome as nome_jogador, j.posicao," +
			" 	c.nome as nome_clube," +
			" 	c.logo as logo_clube," +
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
			" INNER JOIN jogador j ON j.id = jes.id_jogador" +
			" INNER JOIN clube c ON c.id = jes.id_clube" +//TODO: clube da estatistica ou clube do jogador?
			" WHERE s.id_temporada = ?1" +
			" 	AND jes.id_clube = ?2" +
			" 	AND jes.amistoso = ?3" +
			" GROUP BY id_jogador, jes.id_clube, s.id_temporada, amistoso, j.nome, c.nome, j.posicao, c.logo"
	)
	public List<Map<String, Object>> findAgrupadoByTemporadaAndClube(Long idTemporada, Long idClube, Boolean amistoso);
	
	@Query(nativeQuery = true, value = 
			" SELECT id_jogador, jes.id_clube, s.id_temporada, amistoso, " +
			" 	j.nome as nome_jogador, j.posicao," +
			" 	c.nome as nome_clube," +
			" 	c.logo as logo_clube," +
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
			" INNER JOIN jogador j ON j.id = jes.id_jogador" +
			" INNER JOIN clube c ON c.id = jes.id_clube" +//TODO: clube da estatistica ou clube do jogador?
			" INNER JOIN (" +
			"	 SELECT jes.id" +
			"	 FROM jogador_estatisticas_semana jes" +
			"	 INNER JOIN partida_resultado pr ON jes.id_partida_resultado = pr.id" +
			"	 INNER JOIN rodada r ON r.id = pr.id_rodada" +
			"	 LEFT JOIN grupo_campeonato gc ON gc.id = r.id_grupo_campeonato" +
			"	 WHERE r.id_campeonato = ?1" +
			"	 	OR gc.id_campeonato_misto = ?1" +
			"	 UNION" +
			"	 SELECT jes.id" +
			"	 FROM jogador_estatisticas_semana jes" +
			"	 INNER JOIN partida_eliminatoria_resultado per ON jes.id_partida_eliminatoria_resultado = per.id" +
			"	 INNER JOIN rodada_eliminatoria re ON re.id = per.id_rodada_eliminatoria" +
			"	 WHERE re.id_campeonato_eliminatorio = ?1" +
			"	 	OR re.id_campeonato_misto = ?1" +
			" ) AS tmp on jes.id = tmp.id " +
			" GROUP BY id_jogador, jes.id_clube, s.id_temporada, amistoso, j.nome, c.nome, j.posicao, c.logo"
	)
	public List<Map<String, Object>> findAgrupadoByIdCampeonato(Long idCampeonato);
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" delete from jogador_estatisticas_semana" +
			" where id_semana in (select id from semana s where s.id_temporada = ?1)"
	)
	public void deleteByIdTemporada(Long idTemporada);
	
	//###	/INSERT, UPDATE E DELETE	###
}
