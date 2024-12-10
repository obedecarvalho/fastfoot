package com.fastfoot.club.model.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.Treinador;
import com.fastfoot.model.entity.Jogo;

@Repository
public interface TreinadorRepository extends JpaRepository<Treinador, Long> {

	@Query("SELECT c.treinador FROM Clube c WHERE c = :clube AND c.treinador.ativo = :ativo")
	public List<Treinador> findByClubeAndAtivo(@Param("clube") Clube clube, @Param("ativo") Boolean ativo);
	
	public List<Treinador> findByJogoAndAtivo(Jogo jogo, Boolean ativo);

	//###	SELECT ESPECIFICOS	###

	@Query(nativeQuery = true, value =
			" select t.id, t.nome, t.id_jogo, t.ativo " +
			" from treinador t " +
			" left join clube c on t.id = c.id_treinador " +
			" where c.id is null " +
			" 	and t.id_jogo = ?1 " +
			" 	and t.ativo = ?2 "
	)
	public List<Map<String, Object>> findByClubeIsNull(Long idJogo, Boolean ativo);
	
	//###	/SELECT ESPECIFICOS	###
}
