package com.fastfoot.scheduler.model.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato, Long>{

	public List<Campeonato> findByTemporada(Temporada temporada);
	
	public List<Campeonato> findByLiga(Liga liga);
	
	public Optional<Campeonato> findFirstByTemporadaAndLiga(Temporada temporada, Liga liga);
	
	public List<CampeonatoEliminatorio> findByNivelCampeonato(NivelCampeonato nivelCampeonato);
	
	public Optional<Campeonato> findFirstByTemporadaAndLigaAndNivelCampeonato(Temporada temporada, Liga liga,
			NivelCampeonato nivelCampeonato);
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update campeonato" +
			" set rodada_atual = rodada_atual + 1" +
			" where id in (?1)"
	)
	public void incrementarRodadaAtual(Collection<Long> idsCampeonatos);
	
	//###	/INSERT, UPDATE E DELETE	###
}
