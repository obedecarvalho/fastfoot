package com.fastfoot.player.model.repository;

import java.util.Collection;
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

	public List<Jogador> findByStatusJogador(StatusJogador status);

	public List<Jogador> findByIdadeAndStatusJogador(Integer idade, StatusJogador status);	
	
	@Query(" SELECT j FROM Jogador j WHERE j.clube.liga = :liga AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaClubeAndStatusJogador(@Param("liga") Liga liga,
			@Param("status") StatusJogador status, @Param("idClubeMin") Integer idClubeMin,
			@Param("idClubeMax") Integer idClubeMax);

	@Query(" SELECT j FROM Jogador j WHERE j.idade BETWEEN :idadeMin AND :idadeMax ")
	public List<Jogador> findByIdadeBetween(@Param("idadeMin") Integer idadeMin, @Param("idadeMax") Integer idadeMax);

	//###	SELECT ESPECIFICOS	###

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
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	FETCH HABILIDADES_VALOR	###
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube IN :clubes AND j.statusJogador = :status ")
	public List<Jogador> findByClubesAndStatusJogadorFetchHabilidades(@Param("clubes") Collection<Clube> clubes, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidades(@Param("clube") Clube clube, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube.liga = :liga AND j.statusJogador = :status ")
	public List<Jogador> findByLigaClubeAndStatusJogadorFetchHabilidades(@Param("liga") Liga liga, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube.liga = :liga AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaClubeAndStatusJogadorFetchHabilidades(@Param("liga") Liga liga,
			@Param("status") StatusJogador status, @Param("idClubeMin") Integer idClubeMin,
			@Param("idClubeMax") Integer idClubeMax);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.statusJogador = :status ")
	public List<Jogador> findByStatusJogadorFetchHabilidades(@Param("status") StatusJogador status);
	
	//###	/FETCH HABILIDADES_VALOR	###
	
	//###	FETCH HABILIDADES_GRUPO_VALOR	###
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube.liga = :liga AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaClubeAndStatusJogadorFetchHabilidadesGrupo(@Param("liga") Liga liga,
			@Param("status") StatusJogador status, @Param("idClubeMin") Integer idClubeMin,
			@Param("idClubeMax") Integer idClubeMax);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube IN :clubes AND j.statusJogador = :status ")
	public List<Jogador> findByClubesAndStatusJogadorFetchHabilidadesGrupo(@Param("clubes") Collection<Clube> clubes, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidadesGrupo(@Param("clube") Clube clube, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.statusJogador = :status ")
	public List<Jogador> findByStatusJogadorFetchHabilidadesGrupo(@Param("status") StatusJogador status);
	
	//###	/FETCH HABILIDADES_GRUPO_VALOR	###
	
	//###	INSERT, UPDATE E DELETE	###

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
	
	//###	/INSERT, UPDATE E DELETE	###
}
