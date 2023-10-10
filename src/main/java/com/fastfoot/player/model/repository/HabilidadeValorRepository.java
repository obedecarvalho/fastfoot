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
	
	//###	INSERT, UPDATE E DELETE	###

	@Deprecated
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
			" 	select j.id " +
			" 	from jogador j " +
			" 	inner join clube c on j.id_clube = c.id " +
			" 	inner join liga_jogo lj on c.id_liga_jogo = lj.id " +
			" 	where status_jogador = 0 " + //StatusJogador.ATIVO
			" 		and lj.id_jogo = ?1 " +
			" ) "
	)
	public void desenvolverTodasHabilidades(Long idJogo);
	
	@Deprecated
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
			" 	where j.idade = ?1 " +
			"		AND j.modo_desenvolvimento_jogador = ?4 " +
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
			" 	INNER JOIN clube c on j.id_clube = c.id "+
			" 	INNER JOIN liga_jogo lj on c.id_liga_jogo = lj.id "+
			" 	WHERE j.idade = ?1 "+
			" 		AND j.modo_desenvolvimento_jogador = ?4 "+
			" 		AND lj.id_jogo = ?5 "+
			" ) "
	)
	public void atualizarPassoDesenvolvimento(Integer idade, Integer qtdeDesenvolvimentoAno, Double ajusteForca, Integer modoDesenvolvimentoJogador, Long idJogo);
	
	//###	/INSERT, UPDATE E DELETE	###
}
