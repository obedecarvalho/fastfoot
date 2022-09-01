package com.fastfoot.scheduler.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.Semana;
import com.fastfoot.scheduler.model.entity.Temporada;

@Repository
public interface SemanaRepository extends JpaRepository<Semana, Long>{

	public List<Semana> findByTemporada(Temporada temporada);
	
	public Optional<Semana> findFirstByTemporadaAndNumero(Temporada temporada, Integer numero);
	
	@Query(" SELECT s FROM Semana s WHERE s.temporada.atual = true AND s.temporada.semanaAtual = s.numero ")
	public Optional<Semana> findSemanaAtual();
}
