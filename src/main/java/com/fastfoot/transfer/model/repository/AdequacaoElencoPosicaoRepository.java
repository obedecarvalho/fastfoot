package com.fastfoot.transfer.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.transfer.model.entity.AdequacaoElencoPosicao;

@Deprecated
@Repository
public interface AdequacaoElencoPosicaoRepository extends JpaRepository<AdequacaoElencoPosicao, Long> {
	
	public List<AdequacaoElencoPosicao> findByClube(Clube clube);
	
	public List<AdequacaoElencoPosicao> findByAdequacaoBetween(Double adequacaoMin, Double adequacaoMax);
	
	public List<AdequacaoElencoPosicao> findByClubeAndAdequacaoBetween(Clube clube, Double adequacaoMin, Double adequacaoMax);

	@Query(nativeQuery = true, value = 
			" SELECT j.id as id_jogador, j.posicao, j.forca_geral, j.id_clube as id_clube_jog " +
			" FROM adequacao_elenco_posicao aep " +
			" INNER JOIN clube c ON aep.id_clube = c.id " +
			" INNER JOIN jogador j ON (j.id_clube <> c.id AND aep.posicao = j.posicao AND j.aposentado = false) " +
			" LEFT JOIN escalacao_jogador_posicao ejp ON ejp.id_jogador = j.id  " +
			" left join proposta_transferencia_jogador ptj on ptj.id_jogador = j.id " +//TODO: usar para evitar negociar novamente na mesma temporada
			" WHERE true " +
			//" 	AND aep.adequacao >= ?5 AND aep.adequacao < ?6 " +
			" 	AND j.forca_geral BETWEEN c.forca_geral * ?3 AND c.forca_geral * ?4 " +
			//" 	AND (ejp.id IS NULL OR ejp.escalacao_posicao > 10) " + //> EscalacaoPosicao.P_11
			" 	AND aep.id_clube = ?1 " +
			" 	and ptj.id is null " +//TODO
			" 	AND j.posicao = ?2 ")
	public List<Map<String, Object>> findByClubeAndAdequacaoMinMaxAndVariacaoForcaMinMax(Integer idClube, Integer posicao, Double forcaMin, Double forcaMax/*, Double adequacaoMin, Double adequacaoMax*/);
}
