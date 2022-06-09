package com.fastfoot.probability.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.model.entity.Clube;
import com.fastfoot.probability.model.entity.ClubeProbabilidade;
import com.fastfoot.scheduler.model.entity.Campeonato;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface ClubeProbabilidadeRepository extends JpaRepository<ClubeProbabilidade, Long> {

	public List<ClubeProbabilidade> findByCampeonato(Campeonato campeonato);
	
	public List<ClubeProbabilidade> findByClube(Clube clube);
	
	public List<ClubeProbabilidade> findBySemana(Semana semana);
	
	public List<ClubeProbabilidade> findByCampeonatoAndSemana(Campeonato campeonato, Semana semana);
}
