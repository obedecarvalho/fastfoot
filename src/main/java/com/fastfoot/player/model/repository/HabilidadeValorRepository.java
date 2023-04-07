package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
			" 		floor(hv.valor_decimal + hv.passo_desenvolvimento) as valor_novo, " +
			" 		(hv.valor_decimal + hv.passo_desenvolvimento) as valor_decimal_novo " +
			" 	from habilidade_valor hv " +
			" 	inner join jogador j on j.id = hv.id_jogador " +
			" 	where j.status_jogador = 0 " + //StatusJogador.ATIVO
			" ) as tmp " +
			" where tmp.id = hvx.id; "
	)
	public void desenvolverTodasHabilidades();
	
	@Transactional
	@Modifying
	@Query( nativeQuery = true, value =
			" update habilidade_valor " +
			" set valor = floor(valor_decimal + passo_desenvolvimento), " +
			" 	valor_decimal = valor_decimal + passo_desenvolvimento " +
			" where id_jogador in ( " +
			" 	select id " +
			" 	from jogador " +
			" 	where status_jogador = 0 " + //StatusJogador.ATIVO
			" ) "
	)
	public void desenvolverTodasHabilidades2();
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update habilidade_valor hv " +
			" set passo_desenvolvimento = tmp.novo_passo " +
			" from ( " +
			" 	select hv.id, " +
			" 		((hv.potencial_desenvolvimento * ?3) - (hv.valor_decimal))/?2 as novo_passo " +
			" 	from habilidade_valor hv " +
			" 	inner join jogador j on j.id = hv.id_jogador " +
			" 	inner join jogador_detalhe jd on jd.id = j.id_jogador_detalhe " +
			" 	where j.idade = ?1 " +
			"		AND jd.modo_desenvolvimento_jogador = ?4 " +
			" ) as tmp " +
			" where hv.id = tmp.id "
	)
	public void atualizarPassoDesenvolvimento(Integer idade, Integer qtdeDesenvolvimentoAno, Double ajusteForca, Integer modoDesenvolvimentoJogador);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" UPDATE habilidade_valor "+
			" SET passo_desenvolvimento = ((potencial_desenvolvimento * ?3) - (valor_decimal))/?2 "+
			" WHERE id_jogador IN ( "+
			" 	SELECT j.id "+
			" 	FROM jogador j "+
			" 	INNER JOIN jogador_detalhe jd ON jd.id = j.id_jogador_detalhe "+
			" 	WHERE j.idade = ?1 "+
			" 		AND jd.modo_desenvolvimento_jogador = ?4 "+
			" ) "
	)
	public void atualizarPassoDesenvolvimento2(Integer idade, Integer qtdeDesenvolvimentoAno, Double ajusteForca, Integer modoDesenvolvimentoJogador);
}
