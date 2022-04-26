package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.CampeonatoMisto;
import com.fastfoot.scheduler.model.entity.GrupoCampeonato;

@Repository
public interface GrupoCampeonatoRepository extends JpaRepository<GrupoCampeonato, Long>{

	public List<GrupoCampeonato> findByCampeonato(CampeonatoMisto campeonato);
}
