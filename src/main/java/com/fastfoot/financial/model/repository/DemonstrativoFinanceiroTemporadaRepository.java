package com.fastfoot.financial.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.financial.model.entity.DemonstrativoFinanceiroTemporada;

@Repository
public interface DemonstrativoFinanceiroTemporadaRepository extends JpaRepository<DemonstrativoFinanceiroTemporada, Long> {

}
