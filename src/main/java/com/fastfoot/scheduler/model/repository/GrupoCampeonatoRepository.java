package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

public interface GrupoCampeonatoRepository extends JpaRepository<GrupoCampeonato, Long>{

	public List<GrupoCampeonato> findByCampeonato(CampeonatoMisto campeonato);
}
