package com.fastfoot.club.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.model.entity.TrajetoriaForcaClube;
import com.fastfoot.scheduler.model.entity.Semana;

@Repository
public interface TrajetoriaForcaClubeRepository extends JpaRepository<TrajetoriaForcaClube, Long> {

	public List<TrajetoriaForcaClube> findByClube(Clube clube);

	public List<TrajetoriaForcaClube> findBySemana(Semana semana);

}
