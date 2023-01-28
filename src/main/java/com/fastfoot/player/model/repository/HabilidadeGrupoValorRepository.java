package com.fastfoot.player.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastfoot.player.model.entity.HabilidadeGrupoValor;

@Repository
public interface HabilidadeGrupoValorRepository extends JpaRepository<HabilidadeGrupoValor, Long> {

}
