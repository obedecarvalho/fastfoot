package com.fastfoot.probability.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.probability.model.entity.CampeonatoClubeProbabilidade;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface CampeonatoClubeProbabilidadeRepository extends JpaRepository<CampeonatoClubeProbabilidade, Long> {

	public List<CampeonatoClubeProbabilidade> findByCampeonato(Campeonato campeonato);
	
	public List<CampeonatoClubeProbabilidade> findByClube(Clube clube);
	
	public List<CampeonatoClubeProbabilidade> findBySemana(Semana semana);
	
	public List<CampeonatoClubeProbabilidade> findByCampeonatoAndSemana(Campeonato campeonato, Semana semana);
}
