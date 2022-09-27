package com.fastfoot.player.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.CelulaDesenvolvimento;
import com.fastfoot.player.model.entity.GrupoDesenvolvimentoJogador;

@Repository
public interface GrupoDesenvolvimentoJogadorRepository extends JpaRepository<GrupoDesenvolvimentoJogador, Long>{

	//public List<GrupoDesenvolvimentoJogador> findByJogador(Jogador jogador);

	//public List<GrupoDesenvolvimentoJogador> findByCelulaDesenvolvimento(CelulaDesenvolvimento celulaDesenvolvimento);
	
	public List<GrupoDesenvolvimentoJogador> findByCelulaDesenvolvimentoAndAtivo(CelulaDesenvolvimento celulaDesenvolvimento, Boolean ativo);

	@Query(" SELECT gdj FROM GrupoDesenvolvimentoJogador gdj WHERE gdj.jogador.idade = :idadeJogador AND gdj.ativo = :ativo ")
	public List<GrupoDesenvolvimentoJogador> findByAtivoAndIdadeJogador(@Param(value = "ativo") Boolean ativo, @Param(value = "idadeJogador") Integer idadeJogador);
	
	@Query(" SELECT DISTINCT gdj FROM GrupoDesenvolvimentoJogador gdj INNER JOIN FETCH gdj.jogador j INNER JOIN FETCH j.habilidades "
			+ " WHERE gdj.celulaDesenvolvimento = :celulaDesenvolvimento AND gdj.ativo = :ativo ")
	public List<GrupoDesenvolvimentoJogador> findByCelulaDesenvolvimentoAndAtivoFetchJogadorHabilidades(
			@Param(value = "celulaDesenvolvimento") CelulaDesenvolvimento celulaDesenvolvimento,
			@Param(value = "ativo") Boolean ativo);
	
	
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
	public List<Map<String, Object>> findQtdeJogadorPorPosicaoPorClube(Integer posicaoExcluir, Integer idadeExcluir);
}
