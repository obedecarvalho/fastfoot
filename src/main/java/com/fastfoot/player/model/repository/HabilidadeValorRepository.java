package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.player.model.StatusJogador;
import com.fastfoot.player.model.entity.HabilidadeValor;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface HabilidadeValorRepository extends JpaRepository<HabilidadeValor, Long> {

	public List<HabilidadeValor> findByJogador(Jogador jogador);

	@Transactional
	@Modifying
	@Query( nativeQuery = true, value =
			" update habilidade_valor hvx " +
			" set valor = tmp.valor_novo, valor_decimal = tmp.valor_decimal_novo " +
			" from ( " +
			" 	select " +
			" 		hv.id, " +
			" 		floor(hv.valor + hv.valor_decimal + hv.passo_desenvolvimento) as valor_novo, " +
			" 		hv.valor + hv.valor_decimal + hv.passo_desenvolvimento  " +
			" 			- floor(hv.valor + hv.valor_decimal + hv.passo_desenvolvimento) as valor_decimal_novo " +
			" 	from habilidade_valor hv " +
			" 	inner join jogador j on j.id = hv.id_jogador " +
			" 	inner join grupo_desenvolvimento_jogador gdj on gdj.id_jogador = j.id " +
			" 	where gdj.ativo and gdj.celula_desenvolvimento = ?1 " +
			" ) as tmp " +
			" where tmp.id = hvx.id; "
	)
	public void desenvolverHabilidadesByCelulaDesenvolvimento(Integer ordinalCelulaDesenvolvimento);

	@Transactional
	@Modifying
	@Query( nativeQuery = true, value =
			" update habilidade_valor hvx " +
			" set valor = tmp.valor_novo, valor_decimal = tmp.valor_decimal_novo " +
			" from ( " +
			" 	select " +
			" 		hv.id, " +
			" 		floor(hv.valor + hv.valor_decimal + hv.passo_desenvolvimento) as valor_novo, " +
			" 		hv.valor + hv.valor_decimal + hv.passo_desenvolvimento  " +
			" 			- floor(hv.valor + hv.valor_decimal + hv.passo_desenvolvimento) as valor_decimal_novo " +
			" 	from habilidade_valor hv " +
			" 	inner join jogador j on j.id = hv.id_jogador " +
			//" 	where j.aposentado = false " +
			" 	where j.status_jogador = 0 " + //StatusJogador.ATIVO
			" ) as tmp " +
			" where tmp.id = hvx.id; "
	)
	public void desenvolverTodasHabilidades();
}
