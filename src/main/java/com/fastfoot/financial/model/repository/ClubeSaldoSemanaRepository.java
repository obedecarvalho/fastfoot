package com.fastfoot.financial.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.financial.model.entity.ClubeSaldoSemana;

@Repository
public interface ClubeSaldoSemanaRepository extends JpaRepository<ClubeSaldoSemana, Long> {

}
