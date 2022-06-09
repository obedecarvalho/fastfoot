package com.fastfoot.probability.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.probability.model.entity.ClubeProbabilidadePosicao;

@Repository
public interface ClubeProbabilidadePosicaoRepository extends JpaRepository<ClubeProbabilidadePosicao, Long>{

}
