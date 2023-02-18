package com.fastfoot.financial.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.financial.model.entity.ClubeSaldoSemana;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface ClubeSaldoSemanaRepository extends JpaRepository<ClubeSaldoSemana, Long> {
	
	public List<ClubeSaldoSemana> findByClube(Clube clube);
	
	public List<ClubeSaldoSemana> findBySemana(Semana semana);

}
