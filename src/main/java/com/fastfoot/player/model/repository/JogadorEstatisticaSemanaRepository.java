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

	@Query(nativeQuery = true, value = 
			" SELECT id_jogador, id_clube, s.id_temporada, amistoso, " +
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
			" FROM jogador_estatistica_semana jes" +
			" INNER JOIN semana s ON jes.id_semana = s.id" +
			" WHERE s.id_temporada = ?1" +
			" GROUP BY id_jogador, id_clube, s.id_temporada, amistoso"
	)
	public List<Map<String, Object>> findAgrupadoByTemporada(Long idTemporada);
	
	@Query(nativeQuery = true, value = 
			" SELECT id_jogador, jes.id_clube, s.id_temporada, amistoso, " +
			" 	j.nome as nome_jogador," +
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
			" FROM jogador_estatistica_semana jes" +
			" INNER JOIN semana s ON jes.id_semana = s.id" +
			" INNER JOIN jogador j ON j.id = jes.id_jogador" +
			" WHERE s.id_temporada = ?1" +
			" 	AND jes.id_clube = ?2"+
			" GROUP BY id_jogador, jes.id_clube, s.id_temporada, amistoso, j.nome"
	)
	public List<Map<String, Object>> findAgrupadoByTemporadaAndClube(Long idTemporada, Integer idClube);
}
