package com.fastfoot.financial.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.financial.model.entity.MovimentacaoFinanceiraSaida;

@Repository
public interface MovimentacaoFinanceiraSaidaRepository extends JpaRepository<MovimentacaoFinanceiraSaida, Long> {

}
