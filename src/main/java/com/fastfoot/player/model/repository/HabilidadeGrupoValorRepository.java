package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.player.model.HabilidadeGrupo;
import com.fastfoot.player.model.entity.HabilidadeGrupoValor;
import com.fastfoot.player.model.entity.Jogador;

@Repository
public interface HabilidadeGrupoValorRepository extends JpaRepository<HabilidadeGrupoValor, Long> {

	public List<HabilidadeGrupoValor> findByJogador(Jogador jogador);

	public List<HabilidadeGrupoValor> findByHabilidadeGrupo(HabilidadeGrupo habilidadeGrupo);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update habilidade_grupo_valor " +
			" set valor = tmp.valor " +
			" from ( " +
			" 	select id_jogador as id_jog, avg(valor_decimal) as valor " +
			" 	from habilidade_valor " +
			" 	where habilidade in (?2) " +
			" 	group by id_jogador " +
			" ) tmp " +
			" where id_jogador = tmp.id_jog " +
			" 	and habilidade_grupo = ?1 "
	)
	public void calcular(int habilidadeGrupoOrdinal, int[] habilidadeOrdinal);

}
