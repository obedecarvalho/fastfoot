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
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{

	public List<Jogador> findByClubeAndStatusJogador(Clube clube, StatusJogador status);

	@Query("SELECT j FROM Jogador j WHERE j.clube.ligaJogo.jogo = :jogo AND j.statusJogador = :status")
	public List<Jogador> findByJogoAndStatusJogador(@Param("jogo") Jogo jogo, @Param("status") StatusJogador status);

	@Query("SELECT j FROM Jogador j WHERE j.clube.ligaJogo.jogo = :jogo AND j.idade = :idade AND j.statusJogador = :status")
	public List<Jogador> findByJogoAndIdadeAndStatusJogador(@Param("jogo") Jogo jogo, @Param("idade") Integer idade, @Param("status") StatusJogador status);
	
	@Query(" SELECT j FROM Jogador j WHERE j.clube.ligaJogo = :ligaJogo AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaJogoClubeAndStatusJogador(@Param("ligaJogo") LigaJogo ligaJogo,
			@Param("status") StatusJogador status, @Param("idClubeMin") Long idClubeMin,
			@Param("idClubeMax") Long idClubeMax);
	
	//###	FETCH HABILIDADES_VALOR	###
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube IN :clubes AND j.statusJogador = :status ")
	public List<Jogador> findByClubesAndStatusJogadorFetchHabilidades(@Param("clubes") Collection<Clube> clubes, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidades(@Param("clube") Clube clube, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube.ligaJogo = :ligaJogo AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaJogoClubeAndStatusJogadorFetchHabilidades(@Param("ligaJogo") LigaJogo ligaJogo,
			@Param("status") StatusJogador status, @Param("idClubeMin") Long idClubeMin,
			@Param("idClubeMax") Long idClubeMax);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j = :jogador ")
	public List<Jogador> findByJogadorFetchHabilidades(@Param("jogador") Jogador jogador);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesValor hv WHERE j.clube.ligaJogo.jogo = :jogo AND j.statusJogador = :status ")
	public List<Jogador> findByJogoAndStatusJogadorFetchHabilidades(@Param("jogo") Jogo jogo, @Param("status") StatusJogador status);
	
	//###	/FETCH HABILIDADES_VALOR	###
	
	//###	FETCH HABILIDADES_GRUPO_VALOR	###
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube.ligaJogo = :ligaJogo AND j.statusJogador = :status AND j.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Jogador> findByLigaJogoClubeAndStatusJogadorFetchHabilidadesGrupo(@Param("ligaJogo") LigaJogo ligaJogo,
			@Param("status") StatusJogador status, @Param("idClubeMin") Long idClubeMin,
			@Param("idClubeMax") Long idClubeMax);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube IN :clubes AND j.statusJogador = :status ")
	public List<Jogador> findByClubesAndStatusJogadorFetchHabilidadesGrupo(@Param("clubes") Collection<Clube> clubes, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube = :clube AND j.statusJogador = :status ")
	public List<Jogador> findByClubeAndStatusJogadorFetchHabilidadesGrupo(@Param("clube") Clube clube, @Param("status") StatusJogador status);
	
	@Query(" SELECT DISTINCT j FROM Jogador j JOIN FETCH j.habilidadesGrupoValor hv WHERE j.clube.ligaJogo.jogo = :jogo AND j.statusJogador = :status ")
	public List<Jogador> findByJogoAndStatusJogadorFetchHabilidadesGrupo(@Param("jogo") Jogo jogo, @Param("status") StatusJogador status);
	
	//###	/FETCH HABILIDADES_GRUPO_VALOR	###

	//###	SELECT ESPECIFICOS	###

	@Query(nativeQuery = true, value =
			" select id_clube, sum(valor_transferencia) as valor_transferencia" +
			" from jogador j" +
			" inner join clube c on j.id_clube = c.id" +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id" +
			" where status_jogador = 0" + //StatusJogador.ATIVO
			"	 and lj.id_jogo = ?1 " +
			" group by id_clube"
	)
	public List<Map<String, Object>> findValorTransferenciaPorClube(Long idJogo);
	
	@Deprecated
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
			" inner join clube c on j.id_clube = c.id" +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id" +
			" where j.status_jogador = 0" + //StatusJogador.ATIVO
			" 	and j.posicao not in (?1)" +
			" 	and j.idade not in (?2)" +
			" 	and lj.id_jogo = ?3" +
			" group by j.id_clube, j.posicao" +
			" order by j.id_clube, total"
	)
	public List<Map<String, Object>> findQtdeJogadorPorPosicaoPorClube(String posicaoExcluir, Integer idadeExcluir, Long idJogo);
	
	//###	/SELECT ESPECIFICOS	###
	
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
			" 	inner join clube c on j.id_clube = c.id " +
			" 	inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 	where hv.habilidade_tipo = 0 " +//HabilidadeTipo.ESPECIFICA 
			" 		and j.status_jogador = 0 " +//StatusJogador.ATIVO
			" 		and lj.id_jogo = ?1 " +
			" 	group by j.id " +
			" ) tmp " + 
			" where jog.id = tmp.id "
	)
	public void calcularForcaGeral(Long idJogo);
	
	@Deprecated
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
	
	@Deprecated
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador j " +
			" set forca_geral = ( " +
			" 	select avg(valor_decimal) " +
			" 	from habilidade_valor hv " +
			"	inner join jogador j2 on hv.id_jogador = j2.id " +
			"	inner join clube c on j2.id_clube = c.id " +
			"	inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 	where j.id = hv.id_jogador " +
			"		and lj.id_jogo = ?1 " +
			" 		and hv.habilidade_tipo = 0) " +	//HabilidadeTipo.ESPECIFICA
			" where j.status_jogador = 0 "	//StatusJogador.ATIVO
	)
	public void calcularForcaGeral2(Long idJogo);
	
	@Deprecated
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador " 
					+ " set idade = idade + 1 " 
					//+ " where status_jogador = 0 "
	)
	public void incrementarIdade();
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update jogador j " +
			" set idade = idade + 1 " +
			" from clube c " +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" where lj.id_jogo = ?1 and j.id_clube = c.id "
	)
	public void incrementarIdade(Long idJogo);
	
	//###	/INSERT, UPDATE E DELETE	###
}
