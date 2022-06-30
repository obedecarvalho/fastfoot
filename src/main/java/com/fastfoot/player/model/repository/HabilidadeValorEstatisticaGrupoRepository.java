package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.player.model.entity.HabilidadeValorEstatisticaGrupo;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface HabilidadeValorEstatisticaGrupoRepository extends JpaRepository<HabilidadeValorEstatisticaGrupo, Long> {

	public List<HabilidadeValorEstatistica> findByHabilidadeValor(HabilidadeValor habilidadeValor);

	public List<HabilidadeValorEstatisticaGrupo> findByTemporada(Temporada temporada);

	@Query(nativeQuery = true, value = " SELECT "
			+ " 	percentile_disc(0.25) within group (order by quantidade_uso) as qu_q3, "
			+ " 	percentile_disc(0.5) within group (order by quantidade_uso) as qu_q2, "
			+ " 	percentile_disc(0.75) within group (order by quantidade_uso) as qu_q1, "
			+ " 	percentile_disc(0.25) within group (order by quantidade_uso_vencedor) as quv_q3, "
			+ " 	percentile_disc(0.5) within group (order by quantidade_uso_vencedor) as quv_q2, "
			+ " 	percentile_disc(0.75) within group (order by quantidade_uso_vencedor) as quv_q1, "
			+ " 	percentile_disc(0.25) within group (order by porc_acerto) as pa_q3, "
			+ " 	percentile_disc(0.5) within group (order by porc_acerto) as pa_q2, "
			+ " 	percentile_disc(0.75) within group (order by porc_acerto) as pa_q1 "
			+ " FROM habilidade_valor_estatistica_grupo " + " WHERE id_temporada = ?1 ")
	public List<Map<String, Object>> findPercentilByTemporada(Long idTemporada);
}
