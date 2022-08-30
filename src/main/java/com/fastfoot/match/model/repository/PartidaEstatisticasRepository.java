package com.fastfoot.match.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.match.model.entity.PartidaEstatisticas;

@Repository
public interface PartidaEstatisticasRepository extends JpaRepository<PartidaEstatisticas, Long> {

}
