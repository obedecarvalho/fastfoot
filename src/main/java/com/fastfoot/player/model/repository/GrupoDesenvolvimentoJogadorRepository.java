package com.fastfoot.player.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;

@Deprecated
public interface GrupoDesenvolvimentoJogadorRepository {

	public List<GrupoDesenvolvimentoJogador> findByCelulaDesenvolvimentoAndAtivo(CelulaDesenvolvimento celulaDesenvolvimento, Boolean ativo);

	@Query(" SELECT gdj FROM GrupoDesenvolvimentoJogador gdj WHERE gdj.jogador.idade = :idadeJogador AND gdj.ativo = :ativo ")
	public List<GrupoDesenvolvimentoJogador> findByAtivoAndIdadeJogador(@Param(value = "ativo") Boolean ativo, @Param(value = "idadeJogador") Integer idadeJogador);

	@Query(" SELECT DISTINCT gdj FROM GrupoDesenvolvimentoJogador gdj INNER JOIN FETCH gdj.jogador j INNER JOIN FETCH j.habilidades "
			+ " WHERE gdj.celulaDesenvolvimento = :celulaDesenvolvimento AND gdj.ativo = :ativo ")
	public List<GrupoDesenvolvimentoJogador> findByCelulaDesenvolvimentoAndAtivoFetchJogadorHabilidades(
			@Param(value = "celulaDesenvolvimento") CelulaDesenvolvimento celulaDesenvolvimento,
			@Param(value = "ativo") Boolean ativo);

}
