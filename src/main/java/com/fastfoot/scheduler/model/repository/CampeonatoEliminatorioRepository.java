package com.fastfoot.scheduler.model.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastfoot.model.entity.LigaJogo;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoEliminatorioRepository extends JpaRepository<CampeonatoEliminatorio, Long>{

	public List<CampeonatoEliminatorio> findByTemporada(Temporada temporada);

	public Optional<CampeonatoEliminatorio> findFirstByTemporadaAndLigaJogoAndNivelCampeonato(Temporada temporada,
			LigaJogo ligaJogo, NivelCampeonato nivelCampeonato);

	public List<CampeonatoEliminatorio> findByTemporadaAndLigaJogo(Temporada temporada, LigaJogo ligaJogo);
	
	//###	INSERT, UPDATE E DELETE	###
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = 
			" update campeonato_eliminatorio" +
			" set rodada_atual = rodada_atual + 1" +
			" where id in (?1)"
	)
	public void incrementarRodadaAtual(Collection<Long> idsCampeonatos);
	
	//###	/INSERT, UPDATE E DELETE	###
}
