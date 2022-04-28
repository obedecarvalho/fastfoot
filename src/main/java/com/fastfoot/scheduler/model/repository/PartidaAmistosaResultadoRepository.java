package com.fastfoot.scheduler.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.scheduler.model.entity.PartidaAmistosaResultado;
import com.fastfoot.scheduler.model.entity.RodadaAmistosa;

@Repository
public interface PartidaAmistosaResultadoRepository extends JpaRepository<PartidaAmistosaResultado, Long> {

	public List<PartidaAmistosaResultado> findByRodada(RodadaAmistosa rodada);

}
