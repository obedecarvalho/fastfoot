package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.Liga;
import com.fastfoot.scheduler.model.NivelCampeonato;
import com.fastfoot.scheduler.model.entity.CampeonatoEliminatorio;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface CampeonatoEliminatorioRepository extends JpaRepository<CampeonatoEliminatorio, Long>{

	public List<CampeonatoEliminatorio> findByTemporada(Temporada temporada);

	public List<CampeonatoEliminatorio> findByNivelCampeonato(NivelCampeonato nivelCampeonato);

	public List<CampeonatoEliminatorio> findByLiga(Liga liga);

	public List<CampeonatoEliminatorio> findByTemporadaAndLigaAndNivelCampeonato(Temporada temporada, Liga liga, NivelCampeonato nivelCampeonato);

	public Optional<CampeonatoEliminatorio> findFirstByTemporadaAndLigaAndNivelCampeonato(Temporada temporada, Liga liga, NivelCampeonato nivelCampeonato);

	public List<CampeonatoEliminatorio> findByTemporadaAndLiga(Temporada temporada, Liga liga);
}
