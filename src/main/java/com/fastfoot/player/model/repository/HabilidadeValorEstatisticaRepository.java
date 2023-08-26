package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.player.model.entity.HabilidadeValorEstatistica;

@Repository
public interface HabilidadeValorEstatisticaRepository extends JpaRepository<HabilidadeValorEstatistica, Long> {
	
	//###	SELECT ESPECIFICOS	###
	
	@Query(nativeQuery = true, value = " SELECT hve.id_habilidade_valor, "
			+ " SUM(hve.quantidade_uso) AS quantidade_uso, SUM(hve.quantidade_uso_vencedor) AS quantidade_uso_vencedor, hve.amistoso "
			+ " FROM habilidade_valor_estatistica hve " + " INNER JOIN semana s ON hve.id_semana = s.id "
			+ " WHERE s.id_temporada = ?1 " + " GROUP BY hve.id_habilidade_valor, hve.amistoso ")
	public List<Map<String, Object>> findAgrupadoByTemporada(Long idTemporada);
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" delete from habilidade_valor_estatistica" +
			" where id_semana in (select id from semana s where s.id_temporada = ?1)"
	)
	public void deleteByIdTemporada(Long idTemporada);
	
	//###	/INSERT, UPDATE E DELETE	###
}
