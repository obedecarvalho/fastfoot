package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface HabilidadeValorEstatisticaGrupoRepository extends JpaRepository<HabilidadeValorEstatisticaGrupo, Long> {

	public List<HabilidadeValorEstatisticaGrupo> findByTemporadaAndAmistoso(Temporada temporada, Boolean amistoso);
	
	//###	SELECT ESPECIFICOS	###

	@Query(nativeQuery = true, value = " SELECT "
			+ " 	percentile_disc(0.25) within group (order by quantidade_uso) as qu_q3, "
			+ " 	percentile_disc(0.5) within group (order by quantidade_uso) as qu_q2, "
			+ " 	percentile_disc(0.75) within group (order by quantidade_uso) as qu_q1, "
			+ " 	percentile_disc(0.25) within group (order by quantidade_uso_vencedor) as quv_q3, "
			+ " 	percentile_disc(0.5) within group (order by quantidade_uso_vencedor) as quv_q2, "
			+ " 	percentile_disc(0.75) within group (order by quantidade_uso_vencedor) as quv_q1, "
			+ " 	percentile_disc(0.25) within group (order by quantidade_uso_vencedor::float/quantidade_uso) as pa_q3, "
			+ " 	percentile_disc(0.5) within group (order by quantidade_uso_vencedor::float/quantidade_uso) as pa_q2, "
			+ " 	percentile_disc(0.75) within group (order by quantidade_uso_vencedor::float/quantidade_uso) as pa_q1 "
			+ " FROM habilidade_valor_estatistica_grupo " 
			+ " WHERE id_temporada = ?1 "
			+ " 	AND amistoso = ?2 "
	)
	public List<Map<String, Object>> findPercentilByTemporada(Long idTemporada, Boolean amistoso);
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" INSERT INTO habilidade_valor_estatistica_grupo" +
			" (id, id_habilidade_valor, id_temporada, quantidade_uso, quantidade_uso_vencedor, amistoso)" +
			" SELECT NEXTVAL('habilidade_valor_estatistica_grupo_seq') AS id, id_habilidade_valor, s.id_temporada, " +
			" 	SUM(quantidade_uso) AS quantidade_uso, " +
			" 	SUM(quantidade_uso_vencedor) AS quantidade_uso_vencedor, amistoso" +
			" FROM habilidade_valor_estatistica hve" +
			" INNER JOIN semana s ON s.id = hve.id_semana" +
			" WHERE s.id_temporada = ?1 " +
			" GROUP BY s.id_temporada, id_habilidade_valor, amistoso;"
	)
	public void agruparHabilidadeValorEstatisticas(Long idTemporadaAgrupar);
	
	@Deprecated
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" INSERT INTO habilidade_valor_estatistica_grupo" +
			" (id, id_habilidade_valor, id_temporada, quantidade_uso, quantidade_uso_vencedor, amistoso)" +
			" SELECT NEXTVAL('habilidade_valor_estatistica_grupo_seq') AS id, id_habilidade_valor, s.id_temporada, " +
			" 	SUM(quantidade_uso) AS quantidade_uso, " +
			" 	SUM(quantidade_uso_vencedor) AS quantidade_uso_vencedor, amistoso" +
			" FROM habilidade_valor_estatistica hve" +
			" INNER JOIN semana s ON s.id = hve.id_semana" +
			" GROUP BY s.id_temporada, id_habilidade_valor, amistoso;"
	)
	public void agruparHabilidadeValorEstatisticas();
	
	//###	/INSERT, UPDATE E DELETE	###
}
