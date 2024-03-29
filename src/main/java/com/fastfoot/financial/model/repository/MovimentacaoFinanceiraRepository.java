package com.fastfoot.financial.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long>{
	
	public List<MovimentacaoFinanceira> findByClube(Clube clube);
	
	public List<MovimentacaoFinanceira> findBySemana(Semana semana);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada ")
	public List<MovimentacaoFinanceira> findByTemporada(@Param("temporada") Temporada temporada);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada AND mov.clube = :clube ")
	public List<MovimentacaoFinanceira> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);
	
	//@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada.jogo = :jogo ")
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.clube.ligaJogo.jogo = :jogo ")
	public List<MovimentacaoFinanceira> findByJogo(@Param("jogo") Jogo jogo);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.clube IN :clubes ")
	public List<MovimentacaoFinanceira> findByClubes(@Param("clubes") List<Clube> clubes);
	
	//###	SELECT ESPECIFICOS	###
	
	@Query(nativeQuery = true, value =
			" select id_clube, sum(valor_movimentacao) as saldo" +
			" from movimentacao_financeira mf" +
			" inner join clube c on mf.id_clube = c.id" +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id" +
			" where lj.id_jogo = ?1 " +
			" group by id_clube"
	)
	public List<Map<String, Object>> findSaldoPorClubeByIdJogo(Long idJogo);
	
	@Query(nativeQuery = true, value =
			" select id_clube, sum(valor_movimentacao) as saldo" +
			" from movimentacao_financeira mf" +
			" inner join semana s on mf.id_semana = s.id " +
			" where s.id_temporada = ?1 " +
			" group by id_clube"
	)
	public List<Map<String, Object>> findSaldoPorClube(Long idTemporada);
	
	@Query(nativeQuery = true, value =
			" select id_clube, tipo_movimentacao, sum(valor_movimentacao) as valor_movimentacao, s.id_temporada as id_temporada " +
			" from movimentacao_financeira mf " +
			" inner join semana s on mf.id_semana = s.id " +
			" where s.id_temporada = ?1 " +
			" group by id_clube, tipo_movimentacao, s.id_temporada "
	)
	public List<Map<String, Object>> findAgrupadoPorTipoAndTemporada(Long idTemporada);

	/*@Query(nativeQuery = true, value =
			" select c.id as id_clube, mov.saldo, sal.salarios as salarios_projetado" +
			" from clube c" +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id" +
			" inner join (" +
			" 	select id_clube, sum(valor_movimentacao) as saldo" +
			" 	from movimentacao_financeira" +
			" 	group by id_clube" +
			" ) as mov on mov.id_clube = c.id" +
			" inner join (" +
			" 	select id_clube, sum(valor_transferencia) * ?1 as salarios" +
			" 	from jogador j" +
			" 	where j.status_jogador = 0" +//StatusJogador.ATIVO
			" 	group by id_clube" +
			" ) as sal on sal.id_clube = c.id" +
			" where lj.id_jogo = ?2"
	)
	public List<Map<String, Object>> findSaldoProjetadoPorClube(Double porcSalarioAnual, Long idJogo);*/
	
	@Query(nativeQuery = true, value =
			" select c.id as id_clube, mov.saldo, sal.salarios as salarios_projetado " +
			" from clube c " +
			" inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" inner join ( " +
			" 	select id_clube, sum(valor_movimentacao) as saldo " +
			" 	from movimentacao_financeira " +
			" 	group by id_clube " +
			" ) as mov on mov.id_clube = c.id " +
			" inner join ( " +
			" 	select c.id_clube, sum(salario * ?1) as salarios " +
			" 	from contrato c " +
			" 	inner join jogador j on j.id = c.id_jogador " +
			" 	where ativo " +
			" 		and j.status_jogador = 0 " +//StatusJogador.ATIVO
			" 	group by c.id_clube " +
			" ) as sal on sal.id_clube = c.id " +
			" where lj.id_jogo = ?2 "
	)
	public List<Map<String, Object>> findSaldoProjetadoPorClube(Integer numeroSemanas, Long idJogo);
	
	//###	/SELECT ESPECIFICOS	###

}
