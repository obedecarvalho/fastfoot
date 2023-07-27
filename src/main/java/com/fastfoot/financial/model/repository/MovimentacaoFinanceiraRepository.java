package com.fastfoot.financial.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.TipoMovimentacaoFinanceira;
import com.fastfoot.financial.model.entity.MovimentacaoFinanceira;
import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Long>{
	
	public List<MovimentacaoFinanceira> findByClube(Clube clube);
	
	public List<MovimentacaoFinanceira> findBySemana(Semana semana);
	
	public List<MovimentacaoFinanceira> findByTipoMovimentacao(TipoMovimentacaoFinanceira tipoMovimentacao);
	
	public List<MovimentacaoFinanceira> findByTipoMovimentacaoIn(List<TipoMovimentacaoFinanceira> tipoMovimentacao);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada ")
	public List<MovimentacaoFinanceira> findByTemporada(@Param("temporada") Temporada temporada);
	
	@Query(" SELECT mov FROM MovimentacaoFinanceira mov WHERE mov.semana.temporada = :temporada AND mov.clube = :clube ")
	public List<MovimentacaoFinanceira> findByTemporadaAndClube(@Param("temporada") Temporada temporada,
			@Param("clube") Clube clube);
	
	@Query(nativeQuery = true, value =
			" select id_clube, sum(valor_movimentacao) as saldo" +
			" from movimentacao_financeira mf" +
			" group by id_clube"
	)
	public List<Map<String, Object>> findSaldoPorClube();
	
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

	@Query(nativeQuery = true, value =
			" select c.id as id_clube, mov.saldo, sal.salarios as salarios_projetado" +
			" from clube c" +
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
			" ) as sal on sal.id_clube = c.id"
	)
	public List<Map<String, Object>> findSaldoProjetadoPorClube(Double porcSalarioAnual);//TODO: usar salario contrato

}
