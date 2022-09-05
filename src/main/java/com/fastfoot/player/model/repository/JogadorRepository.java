package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndAposentado(Clube clube, Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube AND j.aposentado = :aposentado ")
	public List<Jogador> findByClubeAndAposentadoFetchHabilidades(@Param("clube") Clube clube, @Param("aposentado") Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);
	
	public List<Jogador> findByAposentado(Boolean aposentado);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.aposentado = :aposentado ")
	public List<Jogador> findByAposentadoFetchHabilidades(@Param("aposentado") Boolean aposentado);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = " update jogador jog " + " set forca_geral = tmp.forca_geral " + " from ( "
			+ " 	select j.id, avg(hv.valor) as forca_geral " + " 	from jogador j "
			+ " 	inner join habilidade_valor hv on hv.id_jogador = j.id "
			+ " 	where hv.habilidade_tipo = 0 " //HabilidadeTipo.ESPECIFICA 
			+ " and not j.aposentado " + " 	group by j.id " + " ) tmp "
			+ " where jog.id = tmp.id "
	)
	public void calcularForcaGeral();
	
	/*@Query(nativeQuery = true, value =
			" SELECT j.id AS id_jogador, j.posicao, j.forca_geral, j.id_clube AS id_clube_jog, ejp.escalacao_posicao < 10 AS titular, dn.id is not null as disp_neg " +
			" FROM necessidade_contratacao_clube ncc " +
			" INNER JOIN clube c ON ncc.id_clube = c.id " +
			" INNER JOIN jogador j ON (j.posicao = ncc.posicao AND j.id_clube <> ncc.id_clube AND j.aposentado = false) " +
			" LEFT JOIN proposta_transferencia_jogador ptj " +
			"	ON (ptj.id_jogador = j.id AND ptj.id_clube_destino = ncc.id_clube AND ncc.id_temporada = ptj.id_temporada) " +
			" LEFT JOIN escalacao_jogador_posicao ejp ON ejp.id_jogador = j.id " +
			" LEFT JOIN disponivel_negociacao dn ON (dn.id_jogador = j.id AND dn.id_temporada = ncc.id_temporada) " +
			" WHERE ncc.id_clube = ?2 " +
			" 	AND ncc.posicao = ?3 " +
			" 	AND j.forca_geral >= c.forca_geral * ?4 " +
			" 	AND j.forca_geral < c.forca_geral * ?5 " +
			" 	AND ptj.id IS NULL " + //não ha propostras transferencia nessa temporada
			" 	AND ncc.id_temporada = ?1 "
			)
	public List<Map<String, Object>> findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(Long idTemporada, Integer idClube, Integer posicao, Double forcaMin, Double forcaMax);*/
	
	@Query(nativeQuery = true, value =
			" SELECT j.id AS id_jogador, j.posicao, j.forca_geral as forca_geral_jog, j.id_clube AS id_clube, " +
			" 	ejp.escalacao_posicao < 10 AS titular, dn.id is not null as disponivel_negociacao, " +
			" 	j.idade, j.valor_transferencia, c.forca_geral as forca_geral_clube " +
			" FROM necessidade_contratacao_clube ncc " +
			" INNER JOIN clube c ON ncc.id_clube = c.id " +
			" INNER JOIN jogador j ON (j.posicao = ncc.posicao AND j.id_clube <> ncc.id_clube AND j.aposentado = false) " +
			" LEFT JOIN proposta_transferencia_jogador ptj " +
			" 	ON (ptj.id_jogador = j.id AND ptj.id_clube_destino = ncc.id_clube AND ncc.id_temporada = ptj.id_temporada) " +//TODO: filtrar para permitir apenas uma tranferencia concluida por ano por jogador
			" LEFT JOIN escalacao_jogador_posicao ejp ON ejp.id_jogador = j.id " +
			" LEFT JOIN disponivel_negociacao dn ON (dn.id_jogador = j.id AND dn.id_temporada = ncc.id_temporada) " +
			" WHERE ncc.id = ?1 " +
			" 	AND j.forca_geral >= c.forca_geral * ?2 " +
			" 	AND j.forca_geral < c.forca_geral * ?3 " +
			" 	AND ptj.id IS NULL " //não ha propostras transferencia do clube nessa temporada
			)
	public List<Map<String, Object>> findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(Long idNecessidadeContratacao, Double forcaMin, Double forcaMax);//TODO: colocar em repository especifico
}
