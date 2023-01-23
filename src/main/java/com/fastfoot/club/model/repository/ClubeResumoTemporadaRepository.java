package com.fastfoot.club.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.club.model.entity.ClubeResumoTemporada;

@Repository
public interface ClubeResumoTemporadaRepository extends JpaRepository<ClubeResumoTemporada, Long> {

}