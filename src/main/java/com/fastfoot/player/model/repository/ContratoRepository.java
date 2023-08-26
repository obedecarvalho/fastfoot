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
import com.fastfoot.player.model.entity.Contrato;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	public List<Contrato> findByClube(Clube clube);

	public List<Contrato> findByJogador(Jogador jogador);

	public List<Contrato> findByAtivo(Boolean ativo);

	public List<Contrato> findByJogadorAndAtivo(Jogador jogador, Boolean ativo);

	public List<Contrato> findByClubeAndAtivo(Clube clube, Boolean ativo);

	@Query("SELECT c FROM Contrato c WHERE c.clube.liga = :liga AND c.ativo = :ativo AND c.clube.id BETWEEN :idClubeMin AND :idClubeMax ")
	public List<Contrato> findByLigaAndAtivo(@Param("liga") Liga liga, @Param("ativo") Boolean ativo,
			@Param("idClubeMin") Integer idClubeMin, @Param("idClubeMax") Integer idClubeMax);
	
	@Query("SELECT c"
			+ " FROM Contrato c"
			+ " WHERE c.clube.liga = :liga AND c.ativo = :ativo"
			+ " 	AND c.clube.id BETWEEN :idClubeMin AND :idClubeMax"
			+ " 	AND (c.semanaInicial.temporada.ano + c.numeroTemporadasDuracao - 1) < :anoAtual"
			//+ " 	AND c.jogador.statusJogador = :status"
			)
	public List<Contrato> findByLigaAndAtivoAndVencidos(@Param("liga") Liga liga, @Param("ativo") Boolean ativo,
			@Param("idClubeMin") Integer idClubeMin, @Param("idClubeMax") Integer idClubeMax,
			@Param("anoAtual") Integer anoAtual/*, @Param("status") StatusJogador status*/);
	
	@Query("SELECT c FROM Contrato c WHERE c.jogador IN :jogadores AND c.ativo = :ativo")
	public List<Contrato> findByJogadoresAndAtivo(@Param("jogadores") Collection<Jogador> jogadores, @Param("ativo") Boolean ativo);
	
	//###	SELECT ESPECIFICOS	###
	
	@Query(nativeQuery = true, value =
			" select id_clube, sum(salario) as total_salarios" +
			" from contrato c" +
			" where ativo = true" +
			" group by id_clube"
	)
	public List<Map<String, Object>> findValorTotalSalariosPorClube();
	
	//###	/SELECT ESPECIFICOS	###
	
	//###	INSERT, UPDATE E DELETE	###

	@Transactional
	@Modifying
	@Query("UPDATE Contrato c SET c.ativo = false WHERE c IN :contratos")
	public void desativar(@Param("contratos") Collection<Contrato> contratos);
	
	//###	/INSERT, UPDATE E DELETE	###
}
