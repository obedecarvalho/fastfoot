package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface HabilidadeValorEstatisticaRepository extends JpaRepository<HabilidadeValorEstatistica, Long> {
	
	public List<HabilidadeValorEstatistica> findByHabilidadeValor(HabilidadeValor habilidadeValor);
	
	@Query(" SELECT hve FROM HabilidadeValorEstatistica hve WHERE hve.semana.temporada = :temporada ")
	public List<HabilidadeValorEstatistica> findByTemporada(@Param("temporada") Temporada temporada);
	
	public List<HabilidadeValorEstatistica> findBySemana(Semana semana);
	
	@Query(nativeQuery = true, value = " SELECT hve.id_habilidade_valor, "
			+ " SUM(hve.quantidade_uso) AS quantidade_uso, SUM(hve.quantidade_uso_vencedor) AS quantidade_uso_vencedor "
			+ " FROM habilidade_valor_estatistica hve " + " INNER JOIN semana s ON hve.id_semana = s.id "
			+ " WHERE s.id_temporada = ?1 " + " GROUP BY id_habilidade_valor ")
	public List<Map<String, Object>> findAgrupadoByTemporada(Long idTemporada);
	
}
