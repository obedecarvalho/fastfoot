package com.fastfoot.financial.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraEntrada;

@Repository
public interface MovimentacaoFinanceiraEntradaRepository extends JpaRepository<MovimentacaoFinanceiraEntrada, Long> {

}
