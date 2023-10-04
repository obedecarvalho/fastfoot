package com.fastfoot.match.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaEstatisticas;

@Repository
public interface PartidaEstatisticasRepository extends JpaRepository<PartidaEstatisticas, Long> {

	//###	SELECT ESPECIFICOS	###
	
	@Query(nativeQuery = true, value =
			" select id_clube, " +
			" 	sum(gols)/sum(num_jogos) as gols_partida, " +
			" 	sum(gols + finalizacacoes_defendidas + finalizacacoes_fora)/sum(num_jogos) as finalizacoes_partidas, " +
			" 	sum(gols + finalizacacoes_defendidas)/sum(num_jogos) as finalizacoes_no_gol, " +
			" 	sum(gols + finalizacacoes_defendidas)/greatest(sum(gols + finalizacacoes_defendidas + finalizacacoes_fora), 1) " +
			" 			as probilidade_finalizacoes_no_gol, " +
			" 	sum(gols)/greatest(sum(gols + finalizacacoes_defendidas + finalizacacoes_fora), 1) as probilidade_gols " +
			" from ( " +
			" 	select p.id_clube_mandante as id_clube, count(pe.id) as num_jogos, sum(p.gols_mandante) as gols,  " +
			" 		sum(pe.finalizacacoes_defendidas_mandante) as finalizacacoes_defendidas, " +
			" 		sum(pe.finalizacacoes_fora_mandante) as finalizacacoes_fora, " +
			" 		true as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada r on r.id = p.id_rodada " +
			" 	inner join semana s on s.id = r.id_semana " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_mandante " +
			" 	union all " +
			" 	select p.id_clube_visitante as id_clube, count(pe.id) as num_jogos, sum(p.gols_visitante) as gols,  " +
			" 		sum(pe.finalizacacoes_defendidas_visitante) as finalizacacoes_defendidas, " +
			" 		sum(pe.finalizacacoes_fora_visitante) as finalizacacoes_fora, " +
			" 		false as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada r on r.id = p.id_rodada " +
			" 	inner join semana s on s.id = r.id_semana " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_visitante " +
			" 	union all " +
			" 	select p.id_clube_mandante as id_clube, count(pe.id) as num_jogos, sum(p.gols_mandante) as gols,  " +
			" 		sum(pe.finalizacacoes_defendidas_mandante) as finalizacacoes_defendidas, " +
			" 		sum(pe.finalizacacoes_fora_mandante) as finalizacacoes_fora, " +
			" 		true as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_eliminatoria_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada_eliminatoria r on r.id = p.id_rodada_eliminatoria " +
			" 	inner join semana s on s.id = r.id_semana " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_mandante " +
			" 	union all " +
			" 	select p.id_clube_visitante as id_clube, count(pe.id) as num_jogos, sum(p.gols_visitante) as gols,  " +
			" 		sum(pe.finalizacacoes_defendidas_visitante) as finalizacacoes_defendidas, " +
			" 		sum(pe.finalizacacoes_fora_visitante) as finalizacacoes_fora, " +
			" 		false as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_eliminatoria_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada_eliminatoria r on r.id = p.id_rodada_eliminatoria " +
			" 	inner join semana s on s.id = r.id_semana " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_visitante " +
			" ) as tmp " +
			" group by id_clube "
	)
	public List<Map<String, Object>> findEstatisticasFinalizacoesClubePorTemporada(Long idTemporada);

	@Query(nativeQuery = true, value =
			" select id_clube, " +
			" 	sum(finalizacacoes_defendidas)/greatest(sum(gols_sofridos + finalizacacoes_defendidas), 1) as probabilidade_defesa " +
			" from ( " +
			" 	select p.id_clube_mandante as id_clube, " +
			" 		count(pe.id) as num_jogos, " +
			" 		sum(p.gols_visitante) as gols_sofridos, " +
			" 		sum(pe.finalizacacoes_defendidas_mandante) as finalizacacoes_defendidas, " +
			" 		true as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada r on r.id = p.id_rodada " +
			" 	inner join semana s on r.id_semana = s.id " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_mandante " +
			" 	union all " +
			" 	select p.id_clube_visitante as id_clube, " +
			" 		count(pe.id) as num_jogos, " +
			" 		sum(p.gols_mandante) as gols_sofridos, " +
			" 		sum(pe.finalizacacoes_defendidas_visitante) as finalizacacoes_defendidas, " +
			" 		false as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada r on r.id = p.id_rodada " +
			" 	inner join semana s on r.id_semana = s.id " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_visitante " +
			" 	union all " +
			" 	select p.id_clube_mandante as id_clube, " +
			" 		count(pe.id) as num_jogos, " +
			" 		sum(p.gols_visitante) as gols_sofridos, " +
			" 		sum(pe.finalizacacoes_defendidas_mandante) as finalizacacoes_defendidas, " +
			" 		true as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_eliminatoria_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada_eliminatoria r on r.id = p.id_rodada_eliminatoria " +
			" 	inner join semana s on r.id_semana = s.id " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_mandante " +
			" 	union all " +
			" 	select p.id_clube_visitante as id_clube, " +
			" 		count(pe.id) as num_jogos, " +
			" 		sum(p.gols_mandante) as gols_sofridos, " +
			" 		sum(pe.finalizacacoes_defendidas_visitante) as finalizacacoes_defendidas, " +
			" 		false as mandante " +
			" 	from partida_estatisticas pe " +
			" 	inner join partida_eliminatoria_resultado p on pe.id = p.id_partida_estatisticas " +
			" 	inner join rodada_eliminatoria r on r.id = p.id_rodada_eliminatoria " +
			" 	inner join semana s on r.id_semana = s.id " +
			" 	where s.id_temporada = ?1 " +
			" 	group by p.id_clube_visitante " +
			" ) as tmp " +
			" group by id_clube "
	)
	public List<Map<String, Object>> findEstatisticasDefesaClubePorTemporada(Long idTemporada);
	
	//###	/SELECT ESPECIFICOS	###
}
