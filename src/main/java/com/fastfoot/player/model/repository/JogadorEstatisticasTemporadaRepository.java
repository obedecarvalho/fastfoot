package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.player.model.entity.Jogador;
import com.fastfoot.player.model.entity.JogadorEstatisticasTemporada;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface JogadorEstatisticasTemporadaRepository extends JpaRepository<JogadorEstatisticasTemporada, Long>{

	public List<JogadorEstatisticasTemporada> findByJogadorAndAmistoso(Jogador jogador, Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByTemporadaAndAmistoso(Temporada temporada, Boolean amistoso);
	
	public List<JogadorEstatisticasTemporada> findByTemporadaAndJogadorAndAmistoso(Temporada temporada, Jogador jogador,
			Boolean amistoso);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" insert into public.jogador_estatisticas_temporada" +
			" (id, amistoso, assistencias, defesas_disputa_penalt, faltas, finalizacoes_defendidas," +
			"  finalizacoes_fora, goleiro_finalizacoes_defendidas, gols_disputa_penalt, gols_marcados," +
			"  gols_perdidos_disputa_penalt, gols_sofridos, gols_sofridos_disputa_penalt, numero_jogos," +
			"  numero_jogos_titular, numero_rodada_disputa_penalt, id_clube, id_jogador, id_temporada)" +
			" select nextval('jogador_estatisticas_temporada_seq') as id, ?2 as amistoso," +
			" 	0 as assistencias, 0 as defesas_disputa_penalt, 0 as faltas," +
			" 	0 as finalizacoes_defendidas, 0 as finalizacoes_fora," +
			" 	0 as goleiro_finalizacoes_defendidas, 0 as gols_disputa_penalt," +
			" 	0 as gols_marcados, 0 as gols_perdidos_disputa_penalt," +
			" 	0 as gols_sofridos, 0 as gols_sofridos_disputa_penalt," +
			" 	0 as numero_jogos, 0 as numero_jogos_titular," +
			" 	0 as numero_rodada_disputa_penalt, j.id_clube as id_clube," +
			" 	j.id as id_jogador, ?1 as id_temporada" +
			" from jogador j" +
			" where status_jogador = 0;"
	)
	public void criarJogadorEstatisticasTemporada(Long idTemporada, Boolean amistoso);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador" +
			" set id_jogador_estatisticas_temporada_atual = jet.id" +
			" from jogador_estatisticas_temporada jet" +
			" where jet.id_temporada = ?1" +
			" 	and jet.amistoso = false" +
			" 	and jet.id_jogador = jogador.id;"
	)
	public void associarJogadorEstatistica(Long idTemporada);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador" +
			" set id_jogador_estatisticas_amistosos_temporada_atual = jet.id" +
			" from jogador_estatisticas_temporada jet" +
			" where jet.id_temporada = ?1" +
			" 	and jet.amistoso = true" +
			" 	and jet.id_jogador = jogador.id;"
	)
	public void associarJogadorEstatisticaAmistosos(Long idTemporada);

	@Query(nativeQuery = true, value =
			" select id_clube," +
			" 	sum(gols_marcados/cast(numero_jogos as numeric)) as gols_partida," +
			" 	sum((gols_marcados + finalizacoes_defendidas + finalizacoes_fora)" +
			" 		/cast(numero_jogos as numeric)) as finalizacoes_partidas," +
			" sum((gols_marcados + finalizacoes_defendidas)/cast(numero_jogos as numeric))" +
			" 	/sum((gols_marcados + finalizacoes_defendidas + finalizacoes_fora)" +
			" 	/cast(numero_jogos as numeric)) as probilidade_finalizacoes_no_gol," +
			" 	sum(gols_marcados/cast(numero_jogos as numeric))/sum(" +
			" 		(gols_marcados + finalizacoes_defendidas + finalizacoes_fora)" +
			" 		/cast(numero_jogos as numeric)) as probilidade_gols" +
			" from jogador_estatisticas_temporada" +
			" where id_temporada = ?1" +
			" 	and amistoso = false" +
			" 	and numero_jogos > 0" +
			" 	and (gols_marcados + finalizacoes_defendidas + finalizacoes_fora) > 0" +
			" group by id_clube"
	)
	public List<Map<String, Object>> findEstatisticasFinalizacoesPorClube(Long idTemporada);
	
	@Query(nativeQuery = true, value =
			" select id_clube," +
			" 	sum(cast(gols_sofridos as numeric))/sum(goleiro_finalizacoes_defendidas + gols_sofridos) as probabilidade_defesa" +
			" from jogador_estatisticas_temporada jet" +
			" where id_temporada = ?1" +
			" group by id_clube"
	)
	public List<Map<String, Object>> findEstatisticasDefesaPorClube(Long idTemporada);
}
