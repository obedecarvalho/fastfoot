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
import com.fastfoot.model.Liga;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndStatusJogador(Clube clube, StatusJogador status);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidades(@Param("clube") Clube clube, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube.liga = :liga AND j.statusJogador = :status ")
	public List<Jogador> findByLigaClubeAndStatusJogadorFetchHabilidades(@Param("liga") Liga liga, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.clube.liga = :liga AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaClubeAndStatusJogadorFetchHabilidades(@Param("liga") Liga liga,
			@Param("status") StatusJogador status, @Param("idClubeMin") Integer idClubeMin,
			@Param("idClubeMax") Integer idClubeMax);
	
	@Query(" SELECT j FROM Jogador j WHERE j.clube.liga = :liga AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaClubeAndStatusJogador(@Param("liga") Liga liga,
			@Param("status") StatusJogador status, @Param("idClubeMin") Integer idClubeMin,
			@Param("idClubeMax") Integer idClubeMax);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);

	public List<Jogador> findByStatusJogador(StatusJogador status);

	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidades hv WHERE j.statusJogador = :status ")
	public List<Jogador> findByStatusJogadorFetchHabilidades(@Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupo hv WHERE j.statusJogador = :status ")
	public List<Jogador> findByStatusJogadorFetchHabilidadesGrupo(@Param("status") StatusJogador status);
	
	@Query(" SELECT j FROM Jogador j WHERE j.idade BETWEEN :idadeMin AND :idadeMax ")
	public List<Jogador> findByIdadeBetween(@Param("idadeMin") Integer idadeMin, @Param("idadeMax") Integer idadeMax);
	
	public List<Jogador> findByIdadeAndStatusJogador(Integer idade, StatusJogador status);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador jog " + 
			" set forca_geral = tmp.forca_geral " + 
			" from ( " +
			" 	select j.id, avg(hv.valor_decimal) as forca_geral " +
			" 	from jogador j " +
			" 	inner join habilidade_valor hv on hv.id_jogador = j.id " +
			" 	where hv.habilidade_tipo = 0 " +//HabilidadeTipo.ESPECIFICA 
			" 		and j.status_jogador = 0 " +//StatusJogador.ATIVO
			" 	group by j.id " +
			" ) tmp " + 
			" where jog.id = tmp.id "
	)
	public void calcularForcaGeral();
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador j " +
			" set forca_geral = ( " +
			" 	select avg(valor_decimal) " +
			" 	from habilidade_valor hv " +
			" 	where j.id = hv.id_jogador " +
			" 		and hv.habilidade_tipo = 0) " +	//HabilidadeTipo.ESPECIFICA
			" where j.status_jogador = 0 "	//StatusJogador.ATIVO
	)
	public void calcularForcaGeral2();
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador " 
					+ " set idade = idade + 1 " 
					//+ " where status_jogador = 0 "
	)
	public void incrementarIdade();

	@Query(nativeQuery = true, value =
			" SELECT j.id AS id_jogador, j.posicao, j.forca_geral as forca_geral_jog, j.id_clube AS id_clube, " +
			" 	dn.id is not null as disponivel_negociacao, " +
			" 	j.idade, j.valor_transferencia, c.forca_geral as forca_geral_clube " +
			" FROM necessidade_contratacao_clube ncc " +
			" INNER JOIN clube c ON ncc.id_clube = c.id " +
			" INNER JOIN jogador j ON (j.posicao = ncc.posicao AND j.id_clube <> ncc.id_clube AND j.status_jogador = 0) " + //StatusJogador.ATIVO
			//não pode haver outras propostas do clube na mesma temporada para o jogador
			" LEFT JOIN proposta_transferencia_jogador ptj " +
			" 	ON (ptj.id_jogador = j.id AND ptj.id_clube_destino = ncc.id_clube AND ncc.id_temporada = ptj.id_temporada) " +
			//filtrar para permitir apenas uma tranferencia concluida por ano por jogador
			" LEFT JOIN proposta_transferencia_jogador ptj2 " +
			" 	ON (ptj2.id_jogador = j.id AND ncc.id_temporada = ptj2.id_temporada AND ptj2.proposta_aceita = true) " +
			" LEFT JOIN disponivel_negociacao dn ON (dn.id_jogador = j.id AND dn.id_temporada = ncc.id_temporada) " +
			" WHERE ncc.id = ?1 " +
			" 	AND j.forca_geral >= c.forca_geral * ?2 " +
			" 	AND j.forca_geral < c.forca_geral * ?3 " +
			" 	AND j.forca_geral_potencial_efetiva BETWEEN c.forca_geral * ?4 AND c.forca_geral * ?5 " +
			" 	AND ptj.id IS NULL " +
			" 	AND ptj2.id IS NULL " +
			" 	AND dn.tipo_negociacao = 0 " //TipoNegociacao.COMPRA_VENDA
	)
	public List<Map<String, Object>> findByTemporadaAndClubeAndPosicaoAndVariacaoForcaMinMax(
			Long idNecessidadeContratacao, Double forcaMin, Double forcaMax, Double limDiffForcaMin,
			Double limDiffForcaMax);// TODO: colocar em repository especifico

	@Query(nativeQuery = true, value =
			" select id_clube, sum(valor_transferencia) as valor_transferencia" +
			" from jogador j" +
			" where status_jogador = 0" + //StatusJogador.ATIVO
			" group by id_clube"
	)
	public List<Map<String, Object>> findValorTransferenciaPorClube();
	
	@Query(nativeQuery = true, value =
			" select j.id_clube, j.posicao, count(*) as total" +
			" from jogador j" +
			" where j.status_jogador = 0" + //StatusJogador.ATIVO
			" group by j.id_clube, j.posicao" +
			" order by j.id_clube, total"
	)
	public List<Map<String, Object>> findQtdeJogadorPorPosicaoPorClube();

	@Query(nativeQuery = true, value =
			" select j.id_clube, j.posicao, count(*) as total" +
			" from jogador j" +
			" where j.status_jogador = 0" + //StatusJogador.ATIVO
			" 	and j.posicao not in (?1)" +
			" 	and j.idade not in (?2)" +
			" group by j.id_clube, j.posicao" +
			" order by j.id_clube, total"
	)
	public List<Map<String, Object>> findQtdeJogadorPorPosicaoPorClube(String posicaoExcluir, Integer idadeExcluir);
}
