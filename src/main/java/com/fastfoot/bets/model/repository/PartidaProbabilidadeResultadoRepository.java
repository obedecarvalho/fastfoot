package com.fastfoot.bets.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.bets.model.entity.PartidaProbabilidadeResultado;

@Repository
public interface PartidaProbabilidadeResultadoRepository extends JpaRepository<PartidaProbabilidadeResultado, Long> {

}
