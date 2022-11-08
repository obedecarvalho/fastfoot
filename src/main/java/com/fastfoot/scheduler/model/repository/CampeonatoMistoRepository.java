package com.fastfoot.scheduler.model.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoMistoRepository extends JpaRepository<CampeonatoMisto, Long> {

	public List<CampeonatoMisto> findByTemporada(Temporada temporada);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update campeonato_misto" +
			" set rodada_atual = rodada_atual + 1" +
			" where id in (?1)"
	)
	public void incrementarRodadaAtual(Collection<Long> idsCampeonatos);
}
