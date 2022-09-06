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
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndStatusJogador(Clube clube, StatusJogador status);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidades(@Param("clube") Clube clube, @Param("status") StatusJogador status);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);

	public List<Jogador> findByStatusJogador(StatusJogador status);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.statusJogador = :status ")
	public List<Jogador> findByStatusJogadorFetchHabilidades(@Param("status") StatusJogador status);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = " update jogador jog " + " set forca_geral = tmp.forca_geral " + " from ( "
			+ " 	select j.id, avg(hv.valor) as forca_geral " + " 	from jogador j "
			+ " 	inner join habilidade_valor hv on hv.id_jogador = j.id "
			+ " 	where hv.habilidade_tipo = 0 " //HabilidadeTipo.ESPECIFICA 
			+ " 		and j.status_jogador = 0 " //StatusJogador.ATIVO
			+ " 	group by j.id " + " ) tmp "
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
			//" INNER JOIN jogador j ON (j.posicao = ncc.posicao AND j.id_clube <> ncc.id_clube AND j.aposentado = false) " +
			" INNER JOIN jogador j ON (j.posicao = ncc.posicao AND j.id_clube <> ncc.id_clube AND j.status_jogador = 0) " + //StatusJogador.ATIVO
			" LEFT JOIN proposta_transferencia_jogador ptj " +
			" 	ON (ptj.id_jogador = j.id AND ptj.id_clube_destino = ncc.id_clube AND ncc.id_temporada = ptj.id_temporada) " +
			" LEFT JOIN proposta_transferencia_jogador ptj2 " +
			" 	ON (ptj2.id_jogador = j.id AND ncc.id_temporada = ptj2.id_temporada AND ptj2.proposta_aceita = true) " +//TODO: filtrar para permitir apenas uma tranferencia concluida por ano por jogador
			" LEFT JOIN escalacao_jogador_posicao ejp ON ejp.id_jogador = j.id " +
			" LEFT JOIN disponivel_negociacao dn ON (dn.id_jogador = j.id AND dn.id_temporada = ncc.id_temporada) " +
			" WHERE ncc.id = ?1 " +
			" 	AND j.forca_geral >= c.forca_geral * ?2 " +
			" 	AND j.forca_geral < c.forca_geral * ?3 " +
			" 	AND j.forca_geral_potencial_efetiva BETWEEN c.forca_geral * 0.95 AND c.forca_geral * 1.05 " +
			" 	AND ptj.id IS NULL " + //não ha propostras transferencia do clube nessa temporada
			" 	AND ptj2.id IS NULL "
			)
	public List<Map<String, Object>> findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(Long idNecessidadeContratacao, Double forcaMin, Double forcaMax);//TODO: colocar em repository especifico
}
